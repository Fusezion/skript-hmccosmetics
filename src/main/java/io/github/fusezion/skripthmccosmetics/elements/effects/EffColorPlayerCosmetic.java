package io.github.fusezion.skripthmccosmetics.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import com.hibiscusmc.hmccosmetics.api.HMCCosmeticsAPI;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import com.hibiscusmc.hmccosmetics.cosmetic.CosmeticSlot;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffColorPlayerCosmetic extends Effect {

	static {
		Skript.registerEffect(EffColorPlayerCosmetic.class,
				"dye %*cosmeticslot% of %players% %color%");
	}

	private Expression<CosmeticSlot> cosmeticSlot;
	private Expression<Player> players;
	private Expression<Color> color;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.cosmeticSlot = (Expression<CosmeticSlot>) expressions[0];
		this.players = (Expression<Player>) expressions[1];
		this.color = (Expression<Color>) expressions[2];
		return true;
	}

	@Override
	protected void execute(Event event) {
		CosmeticSlot cosmeticSlot = this.cosmeticSlot.getSingle(event);
		Color color = this.color.getSingle(event);
		if (color == null || cosmeticSlot == null) return;
		for (Player player : this.players.getArray(event)) {
			CosmeticUser user = HMCCosmeticsAPI.getUser(player.getUniqueId());
			if (user == null || !user.hasCosmeticInSlot(cosmeticSlot)) continue;
			Cosmetic cosmetic = user.getCosmetic(cosmeticSlot);
			assert cosmetic != null;
			user.addCosmetic(cosmetic, color.asBukkitColor());
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "dye " + this.cosmeticSlot.toString(event, debug) + " of " + this.players.toString(event, debug) + " " + this.color.toString(event, debug);
	}
}
