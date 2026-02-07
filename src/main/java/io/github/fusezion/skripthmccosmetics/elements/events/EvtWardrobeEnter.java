package io.github.fusezion.skripthmccosmetics.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import com.hibiscusmc.hmccosmetics.api.events.PlayerWardrobeEnterEvent;
import com.hibiscusmc.hmccosmetics.config.section.Wardrobe;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EvtWardrobeEnter extends SkriptEvent {

	static {
		Skript.registerEvent("Cosmetics - Wardrobe Enter", EvtWardrobeEnter.class, PlayerWardrobeEnterEvent.class,
				"[player] wardrobe enter",
				"enter of wardrobe %string%");

		EventValues.registerEventValue(PlayerWardrobeEnterEvent.class, Wardrobe.class, PlayerWardrobeEnterEvent::getWardrobe);
	}

	private Literal<String> wardrobe;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		this.wardrobe = matchedPattern == 0 ? null : (Literal<String>) args[0];
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (!(event instanceof PlayerWardrobeEnterEvent wardrobeEnterEvent)) return false;
		if (this.wardrobe != null && !wardrobeEnterEvent.getWardrobe().getId().equals(this.wardrobe.getSingle())) return false;
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (this.wardrobe != null)
			return "enter of wardrobe " + this.wardrobe.toString(event, debug);
		return "wardrobe enter";
	}
}
