package io.github.fusezion.skripthmccosmetics.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import com.hibiscusmc.hmccosmetics.api.events.PlayerCosmeticRemoveEvent;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import com.hibiscusmc.hmccosmetics.cosmetic.CosmeticSlot;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EvtCosmeticUnequip extends SkriptEvent {

	static {
		Skript.registerEvent("Player Cosmetic Remove", EvtCosmeticUnequip.class, PlayerCosmeticRemoveEvent.class,
				"[player] (%*-cosmeticslot%|cosmetic) unequip",
				"[player] (%*-cosmeticslot%|cosmetic) remove");

		EventValues.registerEventValue(PlayerCosmeticRemoveEvent.class, Cosmetic.class, PlayerCosmeticRemoveEvent::getCosmetic);
	}

	private Literal<CosmeticSlot> cosmeticSlot;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		this.cosmeticSlot = (Literal<CosmeticSlot>) args[0];
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (!(event instanceof PlayerCosmeticRemoveEvent cosmeticRemoveEvent)) return false;
		if (this.cosmeticSlot != null && !cosmeticRemoveEvent.getCosmetic().getSlot().equals(this.cosmeticSlot.getSingle())) return false;
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (cosmeticSlot == null)
			return "player cosmetic remove";
		return "player " + this.cosmeticSlot.toString(event, debug) + " remove";
	}

}
