package io.github.fusezion.skripthmccosmetics.elements.expressions;

import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.expressions.base.PropertyExpression;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.hibiscusmc.hmccosmetics.api.HMCCosmeticsAPI;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExprPlayerCosmetics extends PropertyExpression<Player, Cosmetic> {

	static {
		register(ExprPlayerCosmetics.class, Cosmetic.class, "cosmetics", "players");
	}

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		setExpr((Expression<? extends Player>) expressions[0]);
		return true;
	}

	@Override
	protected Cosmetic[] get(Event event, Player[] source) {
		List<Cosmetic> cosmetics = new ArrayList<>();
		for (Player player : source) {
			CosmeticUser user = HMCCosmeticsAPI.getUser(player.getUniqueId());
			if (user == null) continue;
			cosmetics.addAll(user.getCosmetics());
		}
		if (cosmetics.isEmpty()) return new Cosmetic[0];
		return cosmetics.toArray(Cosmetic[]::new);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return (mode == ChangeMode.DELETE || mode == ChangeMode.RESET) ? CollectionUtils.array() : null;
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		if (acceptChange(mode) == null) return;
		for (Player player : getExpr().getArray(event)) {
			CosmeticUser user = HMCCosmeticsAPI.getUser(player.getUniqueId());
			if (user == null || user.getCosmetics().isEmpty()) continue;
			user.removeCosmetics();
		}
	}

	@Override
	public boolean isSingle() {
		return false;
	}

	@Override
	public Class<? extends Cosmetic> getReturnType() {
		return Cosmetic.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "cosmetics of " + getExpr().toString(event, debug);
	}
}
