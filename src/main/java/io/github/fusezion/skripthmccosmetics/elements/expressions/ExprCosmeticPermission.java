package io.github.fusezion.skripthmccosmetics.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import org.jspecify.annotations.Nullable;

public class ExprCosmeticPermission extends SimplePropertyExpression<Cosmetic, String> {

	static {
		register(ExprCosmeticPermission.class, String.class, "cosmetic permission[s]", "cosmetics");
	}

	@Override
	public @Nullable String convert(Cosmetic cosmetic) {
		return cosmetic.getPermission();
	}

	@Override
	protected String getPropertyName() {
		return "cosmetic permission";
	}

	@Override
	public Class<? extends String> getReturnType() {
		return String.class;
	}

}
