package io.github.fusezion.skripthmccosmetics.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import com.hibiscusmc.hmccosmetics.api.HMCCosmeticsAPI;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser.HiddenReason;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffHideShowCosmetics extends Effect {

	static {
		Skript.registerEffect(EffHideShowCosmetics.class,
				"[:force] show cosmetics of %players% [caused by %-hiddenreason%]",
				"hide cosmetics of %players% [caused by %-hiddenreason%]");
	}

	private Expression<Player> players;
	private Expression<HiddenReason> hiddenReason;
	private boolean isHide, useForce;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.players = (Expression<Player>) expressions[0];
		this.hiddenReason = (Expression<HiddenReason>) expressions[1];
		this.isHide = matchedPattern == 1;
		this.useForce = parseResult.hasTag("force");
		return true;
	}

	@Override
	protected void execute(Event event) {
		HiddenReason reason = this.hiddenReason == null ? HiddenReason.PLUGIN : this.hiddenReason.getOptionalSingle(event).orElse(HiddenReason.PLUGIN);
		for (Player player : this.players.getArray(event)) {
			CosmeticUser user = HMCCosmeticsAPI.getUser(player.getUniqueId());
			if (user == null) continue;
			if (isHide) {
				user.hideCosmetics(reason);
			} else {
				if (useForce) {
					user.clearHiddenReasons();
					user.silentlyAddHideFlag(reason);
				}
				user.showCosmetics(reason);
			}
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		String string;
		if (this.isHide) {
			string = "hide cosmetics of " + this.players.toString(event, debug);
		} else {
			string = (useForce ? "force " : "") + "show cosmetics of " + this.players.toString(event, debug);
		}
		return string + (this.hiddenReason == null ? "" : " caused by" + this.hiddenReason.toString(event, debug));
	}

}
