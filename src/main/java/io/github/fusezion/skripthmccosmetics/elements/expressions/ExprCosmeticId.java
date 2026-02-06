package io.github.fusezion.skripthmccosmetics.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import org.jspecify.annotations.Nullable;

public class ExprCosmeticId extends SimplePropertyExpression<Cosmetic, String> {

	static {
		register(ExprCosmeticId.class, String.class, "cosmetic id[s]", "cosmetics");
	}

	@Override
	public @Nullable String convert(Cosmetic cosmetic) {
		return cosmetic.getId();
	}

	@Override
	protected String getPropertyName() {
		return "cosmetic id";
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

}
