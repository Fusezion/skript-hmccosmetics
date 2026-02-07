package io.github.fusezion.skripthmccosmetics.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Condition;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.hibiscusmc.hmccosmetics.api.HMCCosmeticsAPI;
import com.hibiscusmc.hmccosmetics.cosmetic.CosmeticSlot;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class CondPlayerHasCosmeticInSlot extends Condition {

	static {
		Skript.registerCondition(CondPlayerHasCosmeticInSlot.class, ConditionType.COMBINED,
				"%players% (has|have) %*cosmeticslot%",
				"%players% (doesn't|does not) have %*cosmeticslot%");
	}

	private Expression<Player> players;
	private Expression<CosmeticSlot> cosmeticSlot;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.players = (Expression<Player>) expressions[0];
		this.cosmeticSlot = (Expression<CosmeticSlot>) expressions[1];
		setNegated(matchedPattern == 1);
		return true;
	}

	@Override
	public boolean check(Event event) {
		CosmeticSlot cosmeticSlot = this.cosmeticSlot.getSingle(event);
		if (cosmeticSlot == null) return false;
		return players.check(event, (player) -> {
			CosmeticUser user = HMCCosmeticsAPI.getUser(player.getUniqueId());
			return user != null && user.hasCosmeticInSlot(cosmeticSlot);
		}, isNegated());
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (isNegated())
			return this.players.toString(event, debug) + " doesn't have " + this.cosmeticSlot.toString(event, debug);
		return this.players.toString(event, debug) + " has " + this.cosmeticSlot.toString(event, debug);
	}
}
