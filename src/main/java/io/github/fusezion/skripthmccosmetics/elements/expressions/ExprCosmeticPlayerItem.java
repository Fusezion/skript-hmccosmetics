package io.github.fusezion.skripthmccosmetics.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import com.hibiscusmc.hmccosmetics.api.HMCCosmeticsAPI;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import com.hibiscusmc.hmccosmetics.cosmetic.CosmeticSlot;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.Nullable;

public class ExprCosmeticPlayerItem extends SimpleExpression<ItemStack> {

	static {
		Skript.registerExpression(ExprCosmeticPlayerItem.class, ItemStack.class, ExpressionType.COMBINED,
				"cosmetic item of %cosmeticslot/cosmetic% of %player%");
	}

	private Expression<Player> player;
	private Expression<?> cosmetic;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.cosmetic =  expressions[0];
		this.player = (Expression<Player>) expressions[1];
		return true;
	}

	@Override
	protected ItemStack @Nullable [] get(Event event) {
		Player player = this.player.getSingle(event);
		if (player == null) return new ItemStack[0];
		CosmeticUser user = HMCCosmeticsAPI.getUser(player.getUniqueId());
		if (user == null) return new ItemStack[0];
		Object uncheckedCosmetic = this.cosmetic.getSingle(event);
		Cosmetic cosmetic;
		if (uncheckedCosmetic instanceof Cosmetic) {
			cosmetic = (Cosmetic) uncheckedCosmetic;
		} else if (uncheckedCosmetic instanceof CosmeticSlot cosmeticSlot) {
			cosmetic = user.getCosmetic(cosmeticSlot);
		} else {
			return new ItemStack[0];
		}
		if (cosmetic == null) return new ItemStack[0];
		return new ItemStack[]{user.getUserCosmeticItem(cosmetic)};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends ItemStack> getReturnType() {
		return ItemStack.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "cosmetic item of " + this.cosmetic.toString(event, debug) + " of " + this.player.toString(event, debug);
	}

}
