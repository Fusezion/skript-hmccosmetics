package io.github.fusezion.skripthmccosmetics.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import com.hibiscusmc.hmccosmetics.api.events.PlayerCosmeticShowEvent;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EvtPlayerCosmeticShow extends SkriptEvent {

	static {
		Skript.registerEvent("Player Cosmetic Show", EvtPlayerCosmeticShow.class,
				PlayerCosmeticShowEvent.class, "[player] cosmetics show");
	}

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		return true;
	}

	@Override
	public boolean check(Event event) {
		return event instanceof PlayerCosmeticShowEvent;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		return "player cosmetic show";
	}

}
