package io.github.fusezion.skripthmccosmetics.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import com.hibiscusmc.hmccosmetics.api.HMCCosmeticsAPI;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import org.bukkit.entity.Player;

public class CondPlayerIsInWardrobe extends PropertyCondition<Player> {

	static {
		register(CondPlayerIsInWardrobe.class, "in a wardrobe", "players");
	}

	@Override
	public boolean check(Player player) {
		CosmeticUser user = HMCCosmeticsAPI.getUser(player.getUniqueId());
		return user != null && user.isInWardrobe();
	}

	@Override
	protected String getPropertyName() {
		return "in a wardrobe";
	}
}
