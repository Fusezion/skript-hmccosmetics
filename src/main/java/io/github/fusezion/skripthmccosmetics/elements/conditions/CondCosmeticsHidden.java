package io.github.fusezion.skripthmccosmetics.elements.conditions;

import ch.njol.skript.Skript;
import ch.njol.skript.conditions.base.PropertyCondition;
import com.hibiscusmc.hmccosmetics.api.HMCCosmeticsAPI;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class CondCosmeticsHidden extends PropertyCondition<Player> {

	static {
		Skript.registerCondition(CondCosmeticsHidden.class, ConditionType.PROPERTY,
				"cosmetics of %players% are hidden",
				"cosmetics of %players% (aren't|are not) hidden");
	}

	@Override
	public boolean check(Player value) {
		CosmeticUser user = HMCCosmeticsAPI.getUser(value.getUniqueId());
		return user != null && user.isHidden();
	}

	@Override
	protected String getPropertyName() {
		return "cosmetics hidden condition";
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (isNegated())
			return "cosmetics of " + getExpr().toString(event, debug) + " aren't hidden";
		return "cosmetics of " + getExpr().toString(event, debug) + " are hidden";
	}
}
