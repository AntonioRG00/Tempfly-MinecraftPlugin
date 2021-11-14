package com.antoniorg.utils;

import org.bukkit.Color;
import org.bukkit.FireworkEffect;
import org.bukkit.FireworkEffect.Builder;
import org.bukkit.FireworkEffect.Type;
import org.bukkit.Sound;
import org.bukkit.entity.Firework;
import org.bukkit.entity.Player;
import org.bukkit.inventory.meta.FireworkMeta;
import org.bukkit.metadata.FixedMetadataValue;

import com.antoniorg.main.MySpigotPlugin;

public abstract class EffectsUtils {

    public static void playFireWorkSound(Player p) {
    	p.playSound(p.getEyeLocation(), Sound.valueOf("ENTITY_FIREWORK_ROCKET_LAUNCH"), 1.0f, 1.0f);
    }
    
    public static void doFlyEnableEffects(Player p) {
    	Builder builder = FireworkEffect.builder();
    	builder.withColor(Color.GREEN);
    	builder.withFade(Color.WHITE);
    	builder.with(Type.BALL_LARGE);
    	builder.withFlicker();
    	FireworkEffect firework = builder.build();
    	
    	Firework fw = (Firework) p.getWorld().spawn(p.getLocation(), Firework.class);
    	FireworkMeta fwm = fw.getFireworkMeta();
    	fwm.addEffect(firework);
    	fw.setMetadata("nodamage", new FixedMetadataValue(MySpigotPlugin.getInstance(), true));
    
    	fw.setFireworkMeta(fwm);
    	fw.detonate();
    }
}
