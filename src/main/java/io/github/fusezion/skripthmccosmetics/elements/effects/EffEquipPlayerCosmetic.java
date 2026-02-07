package io.github.fusezion.skripthmccosmetics.elements.effects;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Effect;
import ch.njol.skript.lang.Expression;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.lang.SyntaxStringBuilder;
import ch.njol.skript.util.Color;
import ch.njol.util.Kleenean;
import com.hibiscusmc.hmccosmetics.api.HMCCosmeticsAPI;
import com.hibiscusmc.hmccosmetics.cosmetic.Cosmetic;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EffEquipPlayerCosmetic extends Effect {

	static {
		Skript.registerEffect(EffEquipPlayerCosmetic.class,
				"equip %players% with cosmetic[s] %cosmetics% [(dyed|colo[u]red) %-color%]");
	}

	private Expression<Player> players;
	private Expression<Cosmetic> cosmetics;
	private Expression<Color> color;

	@Override
	public boolean init(Expression<?>[] expressions, int matchedPattern, Kleenean isDelayed, ParseResult parseResult) {
		this.players = (Expression<Player>) expressions[0];
		this.cosmetics = (Expression<Cosmetic>) expressions[1];
		this.color = (Expression<Color>) expressions[2];
		return true;
	}

	@Override
	protected void execute(Event event) {
		Cosmetic[] cosmetics = this.cosmetics.getArray(event);
		Color color = this.color == null ? null : this.color.getSingle(event);
		org.bukkit.Color bukkitColor = color == null ? null : color.asBukkitColor();
		if (cosmetics.length == 0) return;
		for (Player player : this.players.getArray(event)) {
			CosmeticUser user = HMCCosmeticsAPI.getUser(player.getUniqueId());
			if (user == null) continue;
			for (Cosmetic cosmetic : cosmetics) {
				user.addCosmetic(cosmetic, bukkitColor);
			}
		}
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		SyntaxStringBuilder ssb = new SyntaxStringBuilder(event, debug);
		ssb.append("equip", players, "with cosmetics", cosmetics);
		if (this.color != null) ssb.append("dyed", color);
		return ssb.toString();
	}
}
