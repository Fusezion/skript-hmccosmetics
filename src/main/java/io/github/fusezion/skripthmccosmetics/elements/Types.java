package io.github.fusezion.skripthmccosmetics.elements;

import ch.njol.skript.classes.ClassInfo;
import ch.njol.skript.classes.EnumClassInfo;
import ch.njol.skript.classes.Parser;
import ch.njol.skript.classes.Serializer;
import ch.njol.skript.expressions.base.EventValueExpression;
import ch.njol.skript.lang.ParseContext;
import ch.njol.skript.registrations.Classes;
import ch.njol.skript.registrations.EventValues;
import ch.njol.yggdrasil.Fields;
import com.hibiscusmc.hmccosmetics.api.HMCCosmeticsAPI;
import com.hibiscusmc.hmccosmetics.api.events.PlayerEvent;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import com.hibiscusmc.hmccosmetics.cosmetic.CosmeticSlot;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser.HiddenReason;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.Nullable;

import java.io.StreamCorruptedException;
import java.util.Locale;
import java.util.UUID;

public class Types {

	static {
		Classes.registerClass(new ClassInfo<>(Cosmetic.class, "cosmetic")
				.user("cosmetics?")
				.name("Cosmetic")
				.description("Represents a cosmetic object within the HMCCosmetics plugin.")
				.examples("""
						set {_cosmetic} to cosmetic with id "example"
						set {_cosmetic} to helmet cosmetic with id "example"
						set {_cosmetic} to backpack cosmetic of player
						set backpack cosmetic of player to cosmetic "starter_backpack"
						""")
				.since("INSERT VERSION")
				.supplier(() -> HMCCosmeticsAPI.getAllCosmetics().iterator())
				.defaultExpression(new EventValueExpression<>(Cosmetic.class))
				.usage(HMCCosmeticsAPI.getAllCosmetics().stream().map(Cosmetic::getId).toArray(String[]::new))
				.serializer(new Serializer<>() {
					@Override
					public Fields serialize(Cosmetic cosmetic) {
						Fields fields = new Fields();
						fields.putObject("cosmetic_id", cosmetic.getId());
						return null;
					}

					@Override
					protected Cosmetic deserialize(Fields fields) throws StreamCorruptedException {
						if (!fields.hasField("cosmetic_id"))
							throw new StreamCorruptedException("No cosmetic_id was found");
						String cosmetic_id = fields.getObject("cosmetic_id", String.class);
						if (cosmetic_id == null)
							throw new StreamCorruptedException("cosmetic_id was found, but could not be retrieved");
						return HMCCosmeticsAPI.getCosmetic(cosmetic_id);
					}

					@Override
					public void deserialize(Cosmetic cosmetic, Fields f) {
						throw new UnsupportedOperationException("This method should never be called");
					}

					@Override
					public boolean mustSyncDeserialization() {
						return true;
					}

					@Override
					protected boolean canBeInstantiated() {
						return false;
					}
				})
				.parser(new Parser<>() {

					@Override
					public boolean canParse(ParseContext context) {
						return context == ParseContext.COMMAND;
					}

					@Override
					public @Nullable Cosmetic parse(String input, ParseContext context) {
						if (context != ParseContext.COMMAND) return null;
						return HMCCosmeticsAPI.getCosmetic(input);
					}

					@Override
					public String toString(Cosmetic cosmetic, int flags) {
						return "cosmetic with id '" + cosmetic.getId() + "'";
					}

					@Override
					public String toVariableNameString(Cosmetic cosmetic) {
						return cosmetic.getId();
					}
				})
		);

		Classes.registerClass(new ClassInfo<>(CosmeticSlot.class, "cosmeticslot")
				.user("cosmetic ?slots?")
				.name("Cosmetic Slot")
				.description("Represents a slot a cosmetic can be used within")
				.since("INSERT VERSION")
				.supplier(() -> CosmeticSlot.values().values().iterator())
				.defaultExpression(new EventValueExpression<>(CosmeticSlot.class))
				.usage(
						CosmeticSlot.values().values()
								.stream()
								.map(slot -> slot.getName().toLowerCase(Locale.ROOT).replace("_", " "))
								.toArray(String[]::new)
				)
				.parser(new Parser<>() {

					@Override
					public @Nullable CosmeticSlot parse(String input, ParseContext context) {
						input = input.toUpperCase(Locale.ROOT).replace(' ', '_');
						input = input.replace("_COSMETIC", "");
						if (!CosmeticSlot.contains(input)) return null;
						return CosmeticSlot.valueOf(input);
					}

					@Override
					public String toString(CosmeticSlot cosmeticSlot, int flags) {
						return cosmeticSlot.getName().toLowerCase(Locale.ROOT).replace('_', ' ') + " cosmetic";
					}

					@Override
					public String toVariableNameString(CosmeticSlot cosmeticSlot) {
						return cosmeticSlot.getName().toLowerCase(Locale.ROOT).replace('_', ' ') + " cosmetic";
					}
				})
		);

		Classes.registerClass(new EnumClassInfo<>(HiddenReason.class, "hiddenreason", "hiddenreasons")
				.user("hidden ?reasons?")
				.name("Cosmetic Hidden Reason")
				.description("Represents a reason for why a player's cosmetics have been hidden")
				.since("INSERT VERSION")
		);
	}

	static {
		EventValues.registerEventValue(PlayerEvent.class, Player.class, event -> Bukkit.getPlayer(event.getUniqueId()));
		EventValues.registerEventValue(PlayerEvent.class, UUID.class, PlayerEvent::getUniqueId);
	}

}
