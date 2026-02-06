package io.github.fusezion.skripthmccosmetics.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import com.hibiscusmc.hmccosmetics.cosmetic.CosmeticSlot;
import org.jspecify.annotations.Nullable;

public class ExprCosmeticSlot extends SimplePropertyExpression<Cosmetic, CosmeticSlot> {

	static {
		register(ExprCosmeticSlot.class, CosmeticSlot.class, "cosmetic slot", "cosmetic");
	}

	@Override
	public @Nullable CosmeticSlot convert(Cosmetic from) {
		return from.getSlot();
	}

	@Override
	protected String getPropertyName() {
		return "cosmetic slot";
	}

	@Override
	public Class<? extends CosmeticSlot> getReturnType() {
		return CosmeticSlot.class;
	}
}
