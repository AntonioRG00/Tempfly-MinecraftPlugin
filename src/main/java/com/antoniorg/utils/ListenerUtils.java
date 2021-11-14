package com.antoniorg.utils;

import java.util.Timer;
import java.util.TimerTask;

import org.bukkit.Particle;
import org.bukkit.Sound;

import com.antoniorg.comandos.ComandoTempfly;
import com.antoniorg.constantes.GlobalConst;
import com.antoniorg.events.FlyModeEvent;

import net.md_5.bungee.api.ChatColor;

public abstract class ListenerUtils {
	
	public static void lastSecondsOfFly(FlyModeEvent event) {
		if(event.isBeignCancelled()) {
			return;
		}
		
		try {
			event.getTimer().cancel();				
			event.setTimer(new Timer());
		} catch(Exception e) {
			//do nothing
		} finally {			
			event.setBeignCancelled(true);
		}
		
		event.getTimer().schedule(new TimerTask() {
			public void run() {
				event.getPlayer().sendMessage(ChatColor.RED + "Peligro! Fly removido");
				event.getPlayer().setAllowFlight(false);
				ComandoTempfly.flyUsers.removeIf(x -> x.getPlayer().getUniqueId().equals(event.getPlayer().getUniqueId()));
			}
		}, GlobalConst.NUM_AVISOS_FLY_SEC * 1000);
		
		for(int i=0 ; i<GlobalConst.NUM_AVISOS_FLY_SEC ; i++) {
    		final int numIteracion = i;
    		event.getTimer().schedule(new TimerTask() {
    			public void run() {
    				event.getPlayer().sendMessage(ChatColor.RED + "Peligro! Te quedan " + (GlobalConst.NUM_AVISOS_FLY_SEC-numIteracion) + "s de fly");
    				event.getPlayer().spawnParticle(Particle.PORTAL, event.getPlayer().getLocation(), 100);
    				event.getPlayer().playSound(event.getPlayer().getLocation(), Sound.AMBIENT_CAVE, 0.4f, 0.4f);
    			}
    		}, i*1000);
		}
	}
}
