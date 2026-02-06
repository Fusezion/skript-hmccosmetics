package io.github.fusezion.skripthmccosmetics.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import com.hibiscusmc.hmccosmetics.api.events.PlayerCosmeticEquipEvent;
import com.hibiscusmc.hmccosmetics.api.events.PlayerCosmeticHideEvent;
import com.hibiscusmc.hmccosmetics.api.events.PlayerCosmeticShowEvent;
import com.hibiscusmc.hmccosmetics.cosmetic.CosmeticSlot;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser.HiddenReason;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EvtPlayerCosmeticsToggle extends SkriptEvent {

	static {
		Skript.registerEvent("Player Cosmetic Hide", EvtPlayerCosmeticsToggle.class,
				new Class[]{ PlayerCosmeticHideEvent.class, PlayerCosmeticShowEvent.class },
				"[player] cosmetic toggle", "[player] cosmetics hide", "[player] cosmetics show");

		EventValues.registerEventValue(PlayerCosmeticHideEvent.class, HiddenReason.class, PlayerCosmeticHideEvent::getReason);
	}

	private int matchedPattern;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		this.matchedPattern = matchedPattern;
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (!(event instanceof PlayerCosmeticHideEvent) && !(event instanceof  PlayerCosmeticShowEvent)) return false;
		if (event instanceof PlayerCosmeticShowEvent && this.matchedPattern == 1) return false;
		if (event instanceof PlayerCosmeticHideEvent && this.matchedPattern == 2) return false;
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return switch (this.matchedPattern) {
			case 0 -> "player cosmetic toggle";
			case 1 -> "player cosmetic hide";
			case 2 -> "player cosmetic show";
			default -> throw new IllegalStateException("Unhandled EvtPlayerCosmeticsToggle matchedPattern: " + this.matchedPattern);
		};
	}

}
