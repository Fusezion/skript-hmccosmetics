package io.github.fusezion.skripthmccosmetics.elements.conditions;

import ch.njol.skript.conditions.base.PropertyCondition;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;

public class CondCosmeticIsDyeable extends PropertyCondition<Cosmetic> {

	static {
		register(CondCosmeticIsDyeable.class, "dyeable", "cosmetics");
	}

	@Override
	public boolean check(Cosmetic cosmetic) {
		return cosmetic.isDyeable();
	}

	@Override
	protected String getPropertyName() {
		return "dyeable";
	}
}
