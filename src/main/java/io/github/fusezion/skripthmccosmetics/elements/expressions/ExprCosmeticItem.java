package io.github.fusezion.skripthmccosmetics.elements.expressions;

import ch.njol.skript.expressions.base.SimplePropertyExpression;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import org.bukkit.inventory.ItemStack;
import org.jspecify.annotations.Nullable;

public class ExprCosmeticItem extends SimplePropertyExpression<Cosmetic, ItemStack> {

	static {
		register(ExprCosmeticItem.class, ItemStack.class, "cosmetic item[s]", "cosmetics");
	}

	@Override
	public @Nullable ItemStack convert(Cosmetic cosmetic) {
		return cosmetic.getItem();
	}

	@Override
	protected String getPropertyName() {
		return "cosmetic item";
	}

	@Override
	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}

}
