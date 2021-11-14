package com.antoniorg.comandos;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.antoniorg.events.FlyModeEvent;
import com.antoniorg.utils.EffectsUtils;
import com.antoniorg.utils.GlobalUtils;
import com.antoniorg.utils.ListenerUtils;

import net.md_5.bungee.api.ChatColor;

public class ComandoTempfly implements CommandExecutor {

	public static List<FlyModeEvent> flyUsers = new ArrayList<>();

	@Override
	public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
		try {
			if (!checkFilters(sender, command, label, args))
				return true;
			
			Player playerReceiver = Bukkit.getPlayer(args[0]);
			
			if (args[1].equalsIgnoreCase("stop")) {
				if(ComandoTempfly.isFlyingByUniqueID(playerReceiver.getUniqueId())) {
					ComandoTempfly.disableFlyEventByUniqueID(playerReceiver.getUniqueId());
					sender.sendMessage(ChatColor.GREEN + "Se le ha avisado al jugador de que su tempfly será removido");
				} else {
					sender.sendMessage(ChatColor.RED + "El jugador actualmente no tiene tempfly");
				}
				
			} else if(ComandoTempfly.strIsNumber(args[1])) {
				if(ComandoTempfly.isFlyingByUniqueID(playerReceiver.getUniqueId())) {
					sender.sendMessage(ChatColor.YELLOW + "El usuario ya tenía tempfly, se reemplaza el tiempo");
					ComandoTempfly.flyUsers.stream().filter(x -> x.getPlayer().getUniqueId().equals(playerReceiver.getUniqueId())).forEach(x -> x.getTimer().cancel());
					ComandoTempfly.flyUsers.removeIf(x -> x.getPlayer().getUniqueId().equals(playerReceiver.getUniqueId()));
				}
				
				long segundosFly = Long.parseLong(args[1]);
				FlyModeEvent flyModeEvent = new FlyModeEvent(playerReceiver, segundosFly * 1000);
				Bukkit.getServer().getPluginManager().callEvent(flyModeEvent);
				ComandoTempfly.flyUsers.add(flyModeEvent);
				EffectsUtils.doFlyEnableEffects(playerReceiver);
				
				playerReceiver.setAllowFlight(true);
				playerReceiver.sendMessage(ChatColor.GREEN + "Fly concedido por " + GlobalUtils.secondsToTimeUnit(segundosFly));
				
			} else {
				sender.sendMessage(ChatColor.RED + "Parece que no ha introducido un comando correcto");
				sender.sendMessage(ChatColor.RED + "/tempfly help");
			}
		} catch(Exception e) {
			sender.sendMessage(ChatColor.RED + "Parece que no ha introducido un comando correcto");
			sender.sendMessage(ChatColor.RED + "/tempfly help");
		}

		return true;
	}
	
	public static boolean strIsNumber(String number) {
		try {
			Long.parseLong(number);
			return true;
		} catch(NumberFormatException e) {
			return false;
		}
	}
	
	public static Optional<FlyModeEvent> getFlyModeEventByUniqueID(UUID id) {
		return ComandoTempfly.flyUsers.stream().filter(x -> x.getPlayer().getUniqueId().equals(id)).findFirst();
	}
	
	public static boolean isFlyingByUniqueID(UUID id) {
		Optional<FlyModeEvent> optFlyMode = ComandoTempfly.flyUsers.stream().filter(x -> x.getPlayer().getUniqueId().equals(id)).findFirst();
		
		return optFlyMode.isPresent();
	}
	
	public static void disableFlyEventByUniqueID(UUID id) {
		Optional<FlyModeEvent> optFlyMode = ComandoTempfly.flyUsers.stream().filter(x -> x.getPlayer().getUniqueId().equals(id)).findFirst();

		try {
			FlyModeEvent flyModeEvent = optFlyMode.get();
			ListenerUtils.lastSecondsOfFly(flyModeEvent);
		} catch (NoSuchElementException e) {
			// Significa que el se ha vuelto a cancelar
		}
	}

	public boolean checkFilters(CommandSender sender, Command command, String label, String[] args) throws Exception{
		Player playerReceiver = Bukkit.getPlayer(args[0]);

		if (playerReceiver == null) {
			sender.sendMessage(ChatColor.RED + "El jugador no está conectado");
			return false;
		} else if (args.length <= 1) {
			sender.sendMessage(ChatColor.RED + "Hay que especificar los segundos");
			return false;
		}

		if (args[1].equalsIgnoreCase("stop")) {
			return true;
		}

		long segundosFly = 0;
		try {
			segundosFly = Long.parseLong(args[1]);
		} catch (NumberFormatException e) {
			sender.sendMessage(ChatColor.RED + "No has introducido un número correcto");
			return false;
		}

		if (segundosFly < 10) {
			sender.sendMessage(ChatColor.RED + "Como mínimo hay que especificar 10s");
			return false;
		}

		return true;
	}
}