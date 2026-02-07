package io.github.fusezion.skripthmccosmetics.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.hibiscusmc.hmccosmetics.api.HMCCosmeticsAPI;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import com.hibiscusmc.hmccosmetics.cosmetic.CosmeticSlot;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.List;

public class EffUnequipPlayerCosmetic extends Effect {

	static {
		Skript.registerEffect(EffUnequipPlayerCosmetic.class,
				"unequip cosmetics of %players%",
				"unequip cosmetic[s] %cosmetics% from %players%",
				"unequip %*cosmeticslot% of %players%");
	}

	private int matchedPattern;
	private Expression<Player> players;
	private Expression<Cosmetic> cosmetics;
	private Expression<CosmeticSlot> cosmeticSlot;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.matchedPattern = matchedPattern;
		this.players = (Expression<Player>) expressions[expressions.length -1];
		if (matchedPattern == 1) {
			this.cosmetics = (Expression<Cosmetic>) expressions[0];
		} else if (matchedPattern == 2) {
			this.cosmeticSlot = (Expression<CosmeticSlot>) expressions[0];
		}
		return true;
	}

	@Override
	protected void execute(Event event) {
		List<CosmeticUser> users = this.players.stream(event).map(player -> HMCCosmeticsAPI.getUser(player.getUniqueId())).toList();
		if (matchedPattern == 0) {
			users.forEach(CosmeticUser::removeCosmetics);
		} else if (matchedPattern == 1) {
			Cosmetic[] cosmetics = this.cosmetics.getArray(event);
			if (cosmetics.length == 0) return;
			for (CosmeticUser user : users) {
				for (Cosmetic cosmetic : cosmetics) {
					//noinspection DataFlowIssue - We already know user has a cosmetic in the related slot
					if (user.hasCosmeticInSlot(cosmetic) && user.getCosmetic(cosmetic.getSlot()).equals(cosmetic)) {
						user.removeCosmeticSlot(cosmetic);
					}
				}
			}
		} else if (matchedPattern == 2) {
			CosmeticSlot cosmeticSlot = this.cosmeticSlot.getSingle(event);
			if (cosmeticSlot == null) return;
			users.stream()
					.filter(user -> user.hasCosmeticInSlot(cosmeticSlot))
					.forEach(user -> user.removeCosmeticSlot(cosmeticSlot));
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return switch (matchedPattern) {
			case 0 -> "unequip cosmetics of " + this.players.toString(event, debug);
			case 1 -> "unequip cosmetics " + this.cosmetics.toString(event, debug) + " from " + this.players.toString(event, debug);
			case 2 -> "uneqip " + this.cosmeticSlot.toString(event, debug) + " of " + this.players.toString(event, debug);
			default -> throw new IllegalStateException("Unknown pattern for EffUnequipPlayerCosmetic. Pattern: " + this.matchedPattern);
		};
	}

}
