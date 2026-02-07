package io.github.fusezion.skripthmccosmetics.elements.events;

import ch.njol.skript.Skript;
import ch.njol.skript.lang.Literal;
import ch.njol.skript.lang.SkriptEvent;
import ch.njol.skript.lang.SkriptParser.ParseResult;
import ch.njol.skript.registrations.EventValues;
import com.hibiscusmc.hmccosmetics.api.events.PlayerWardrobeLeaveEvent;
import com.hibiscusmc.hmccosmetics.config.section.Wardrobe;
import com.hibiscusmc.hmccosmetics.user.manager.UserWardrobeManager;
import org.bukkit.event.Event;
import org.jetbrains.annotations.Nullable;

public class EvtWardrobeExit extends SkriptEvent {

	static {
		Skript.registerEvent("Cosmetics - Wardrobe Exit", EvtWardrobeExit.class, PlayerWardrobeLeaveEvent.class,
				"[player] wardrobe exit",
				"exit of wardrobe %string%");

		EventValues.registerEventValue(PlayerWardrobeLeaveEvent.class, Wardrobe.class, event -> {
			UserWardrobeManager wardrobeManager = event.getUser().getWardrobeManager();
			if (wardrobeManager != null) return wardrobeManager.getWardrobe();
			return null;
		});
	}

	private Literal<String> wardrobe;

	@Override
	public boolean init(Literal<?>[] args, int matchedPattern, ParseResult parseResult) {
		this.wardrobe = matchedPattern == 0 ? null : (Literal<String>) args[0];
		return true;
	}

	@Override
	public boolean check(Event event) {
		if (!(event instanceof PlayerWardrobeLeaveEvent wardrobeExitEvent)) return false;
		if (this.wardrobe != null) {
			UserWardrobeManager wardrobeManager = wardrobeExitEvent.getUser().getWardrobeManager();
			if (wardrobeManager == null) return false;
			Wardrobe wardrobe = wardrobeManager.getWardrobe();
			return wardrobe != null && wardrobe.getId().equals(this.wardrobe.getSingle());
		}
		return true;
	}

	@Override
	public String toString(@Nullable Event event, boolean debug) {
		if (this.wardrobe != null)
			return "exit of wardrobe " + this.wardrobe.toString(event, debug);
		return "wardrobe exit";
	}
}
