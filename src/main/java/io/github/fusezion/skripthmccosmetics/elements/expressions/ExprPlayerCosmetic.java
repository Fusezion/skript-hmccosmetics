package io.github.fusezion.skripthmccosmetics.elements.expressions;

import ch.njol.skript.Skript;
import ch.njol.skript.classes.Changer.ChangeMode;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.ExpressionType;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.util.SimpleExpression;
import ch.njol.util.Kleenean;
import ch.njol.util.coll.CollectionUtils;
import com.hibiscusmc.hmccosmetics.api.HMCCosmeticsAPI;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import com.hibiscusmc.hmccosmetics.cosmetic.CosmeticSlot;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

public class ExprPlayerCosmetic extends SimpleExpression<Cosmetic>{

	static {
		Skript.registerExpression(ExprPlayerCosmetic.class, Cosmetic.class, ExpressionType.PATTERN_MATCHES_EVERYTHING,
				"%*cosmeticslot% of %players%",
				"%players%'[s] %*cosmeticslot%");
	}

	private Expression<CosmeticSlot> cosmeticSlot;
	private Expression<Player> players;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.cosmeticSlot = (Expression<CosmeticSlot>) expressions[matchedPattern];
		this.players = (Expression<Player>) expressions[matchedPattern == 1 ? 0 : 1];
		return true;
	}

	@Override
	protected Cosmetic @Nullable [] get(Event event) {
		List<Cosmetic> cosmetics = new ArrayList<>();
		CosmeticSlot cosmeticSlot = this.cosmeticSlot.getSingle(event);
		if (cosmeticSlot == null) return new Cosmetic[0];
		for (Player player : this.players.getArray(event)) {
			CosmeticUser user = HMCCosmeticsAPI.getUser(player.getUniqueId());
			if (user == null || !user.hasCosmeticInSlot(cosmeticSlot)) continue;
			cosmetics.add(user.getCosmetic(cosmeticSlot));
		}
		return cosmetics.toArray(Cosmetic[]::new);
	}

	@Override
	public Class<?> @Nullable [] acceptChange(ChangeMode mode) {
		return switch (mode) {
			case SET -> CollectionUtils.array(Cosmetic.class);
			case RESET, DELETE -> CollectionUtils.array();
			default -> null;
		};
	}

	@Override
	public void change(Event event, Object @Nullable [] delta, ChangeMode mode) {
		if (acceptChange(mode) == null) return;
		CosmeticSlot cosmeticSlot = this.cosmeticSlot.getSingle(event);
		if (cosmeticSlot == null) return;
		switch (mode) {
			case RESET, DELETE ->  {
				for (Player player : this.players.getArray(event)) {
					CosmeticUser user = HMCCosmeticsAPI.getUser(player.getUniqueId());
					if (user == null || !user.hasCosmeticInSlot(cosmeticSlot)) continue;
					user.removeCosmeticSlot(cosmeticSlot);
				}
			}
			case SET -> {
				if (delta == null || !(delta[0] instanceof Cosmetic cosmetic)) return;
				for (Player player : this.players.getArray(event)) {
					CosmeticUser user = HMCCosmeticsAPI.getUser(player.getUniqueId());
					if (user == null) continue;
					user.addCosmetic(cosmetic);
				}
			}
		}
	}

	@Override
	public boolean isSingle() {
		return this.players.isSingle();
	}

	@Override
	public Class<? extends Cosmetic> getReturnType() {
		return Cosmetic.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return this.cosmeticSlot + " of " + this.players.toString(event, debug);
	}

}
