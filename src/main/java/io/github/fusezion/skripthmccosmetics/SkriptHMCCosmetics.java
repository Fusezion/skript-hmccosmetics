package io.github.fusezion.skripthmccosmetics;

import ch.njol.skript.Skript;
import ch.njol.skript.SkriptAddon;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public final class SkriptHMCCosmetics extends JavaPlugin {

	private static SkriptHMCCosmetics instance;

	@Override
	public void onEnable() {
		instance = this;
		SkriptAddon addon = Skript.registerAddon(this);
		addon.setLanguageFileDirectory("lang");
		try {
			addon.loadClasses("io.github.fusezion.skripthmccosmetics", "elements");
		} catch (Exception exception) {
			getLogger().severe("skript-hmccosmetics encountered an issue while enabling, shtting down");
			exception.printStackTrace();
			Bukkit.getPluginManager().disablePlugin(this);
		}
	}

}
