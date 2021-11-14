package com.antoniorg.listener;

import java.util.TimerTask;

import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.PlayerDeathEvent;

import com.antoniorg.comandos.ComandoTempfly;
import com.antoniorg.constantes.GlobalConst;
import com.antoniorg.events.FlyModeEvent;
import com.antoniorg.utils.GlobalUtils;
import com.antoniorg.utils.ListenerUtils;

import net.md_5.bungee.api.ChatColor;

public class MySpigotPluginListener implements Listener {

	@EventHandler
	public void onDamage(EntityDamageByEntityEvent event) {
		if (event.getDamager() instanceof Firework && ((Firework) event.getDamager()).hasMetadata("nodamage")) {
			event.setCancelled(true);
		} else if(event.getDamager() instanceof Player && event.getEntity() instanceof Player) {
			ComandoTempfly.disableFlyEventByUniqueID(event.getEntity().getUniqueId());
		}
	}
	
	@EventHandler
	public void onPlayerDeath(PlayerDeathEvent event) {
		if(ComandoTempfly.isFlyingByUniqueID(event.getEntity().getUniqueId())) {
			ComandoTempfly.flyUsers.stream().filter(x -> x.getPlayer().getUniqueId().equals(event.getEntity().getUniqueId())).forEach(x -> x.getTimer().cancel());
			ComandoTempfly.flyUsers.removeIf(x -> x.getPlayer().getUniqueId().equals(event.getEntity().getUniqueId()));
			event.getEntity().sendMessage(ChatColor.RED + "Fly desactivado por morir");
		}
	}

	@EventHandler
	public void onFlyModeEvent(FlyModeEvent event) {
		long[] schedulersTime = new long[GlobalConst.NUM_AVISOS_FLY];

		event.getTimer().schedule(new TimerTask() {
			public void run() {
				ListenerUtils.lastSecondsOfFly(event);
			}
		}, event.getTiempoFly() - GlobalConst.NUM_AVISOS_FLY_SEC * 1000);

		for (int i = 0; i < schedulersTime.length; i++) {
			schedulersTime[i] = (event.getTiempoFly() - GlobalConst.NUM_AVISOS_FLY_SEC * 1000)
					- (event.getTiempoFly() - GlobalConst.NUM_AVISOS_FLY_SEC * 1000) / (2 + i);
			final int numIteracion = i;

			event.getTimer().schedule(new TimerTask() {
				public void run() {
					long tiempoFly = ((event.getTiempoFly() - GlobalConst.NUM_AVISOS_FLY_SEC * 1000)
							/ (2 + numIteracion)) + GlobalConst.NUM_AVISOS_FLY_SEC * 1000;
					event.getPlayer().sendMessage(ChatColor.RED + "Te queda de fly: " + GlobalUtils.milisecondsToTimeUnit(tiempoFly));
				}
			}, schedulersTime[i]);
		}

	}
}
