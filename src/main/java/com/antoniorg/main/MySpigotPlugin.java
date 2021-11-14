package com.antoniorg.main;

import org.bukkit.plugin.java.JavaPlugin;

import com.antoniorg.comandos.ComandoTempfly;
import com.antoniorg.listener.MySpigotPluginListener;

public class MySpigotPlugin extends JavaPlugin{
	
	public static MySpigotPlugin getInstance() {
		return MySpigotPlugin.getPlugin(MySpigotPlugin.class);
	}

    @Override
    public void onEnable() {
    	this.getCommand("tempfly").setExecutor(new ComandoTempfly());
    	this.getServer().getPluginManager().registerEvents(new MySpigotPluginListener(), this);
    }
    
    @Override
    public void onDisable() {

    }
}
