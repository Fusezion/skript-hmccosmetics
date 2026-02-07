package io.github.fusezion.skripthmccosmetics.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import com.hibiscusmc.hmccosmetics.api.events.PlayerCosmeticHideEvent;
import com.hibiscusmc.hmccosmetics.user.CosmeticUser.HiddenReason;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EvtPlayerCosmeticHide extends SkriptEvent {

	static {
		Skript.registerEvent("Player Cosmetic Hide", EvtPlayerCosmeticHide.class,
				PlayerCosmeticHideEvent.class, "[player] cosmetics hide");

		EventValues.registerEventValue(PlayerCosmeticHideEvent.class, HiddenReason.class, PlayerCosmeticHideEvent::getReason);
	}

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		return true;
	}

	@Override
	public boolean check(Event event) {
		return event instanceof PlayerCosmeticHideEvent;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "player cosmetic hide";
	}

}
