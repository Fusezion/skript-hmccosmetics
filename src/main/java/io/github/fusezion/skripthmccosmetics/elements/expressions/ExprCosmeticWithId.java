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
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class ExprCosmeticWithId extends SimpleExpression<Cosmetic> {

	static {
		Skript.registerExpression(ExprCosmeticWithId.class, Cosmetic.class, ExpressionType.PATTERN_MATCHES_EVERYTHING,
				"(%*-cosmeticslot%|cosmetic) [with id] %string%");
	}

	private Expression<CosmeticSlot> cosmeticSlot;
	private Expression<String> cosmeticId;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.cosmeticSlot = (Expression<CosmeticSlot>) expressions[0];
		this.cosmeticId = (Expression<String>) expressions[1];
		return true;
	}

	@Override
	protected Cosmetic @Nullable [] get(Event event) {
		String identifier = this.cosmeticId.getSingle(event);
		if (identifier == null) return new Cosmetic[0];
		Cosmetic cosmetic = HMCCosmeticsAPI.getCosmetic(identifier);
		if (cosmetic == null) return new Cosmetic[0];

		if (cosmeticSlot != null) {
			CosmeticSlot cosmeticSlot = this.cosmeticSlot.getSingle(event);
			if (!cosmetic.getSlot().equals(cosmeticSlot)) return new Cosmetic[0];
		}
		return new Cosmetic[]{cosmetic};
	}

	@Override
	public boolean isSingle() {
		return true;
	}

	@Override
	public Class<? extends Cosmetic> getReturnType() {
		return Cosmetic.class;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (cosmeticSlot != null)
			return this.cosmeticSlot.toString(event, debug) + " with id " + this.cosmeticId.toString(event, debug);
		return "cosmetic with id " + this.cosmeticId.toString(event, debug);
	}

}
