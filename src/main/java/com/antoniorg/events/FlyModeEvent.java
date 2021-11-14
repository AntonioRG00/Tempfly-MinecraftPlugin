package com.antoniorg.events;

import java.util.Objects;
import java.util.Timer;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import lombok.Getter;
import lombok.Setter;

public class FlyModeEvent extends Event implements Cancellable {
	
	private static final HandlerList HANDLERS = new HandlerList();
	private @Getter @Setter boolean cancelled = false;
	private @Getter @Setter Timer timer;
	private @Getter Player player;
	private @Getter long tiempoFly;
	private @Getter @Setter boolean isBeignCancelled = false;
	
	public FlyModeEvent(Player player, long tiempoFly) {
		this.player = player;
		this.tiempoFly = tiempoFly;
		this.timer = new Timer();
	}
	
    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

	@Override
	public int hashCode() {
		return Objects.hash(player);
	}

	@Override
	public boolean equals(Object obj) {
		if(!(obj instanceof FlyModeEvent))
			return false;
		
		return this.player.getUniqueId().equals(((FlyModeEvent)obj).getPlayer().getUniqueId());
	}
}