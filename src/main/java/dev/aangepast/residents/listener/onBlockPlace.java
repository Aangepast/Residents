package dev.aangepast.residents.listener;

import dev.aangepast.residents.Main;
import dev.aangepast.residents.components.Resident;
import dev.aangepast.residents.components.WorkingClass;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;

import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.HashMap;
import java.util.Objects;

public class onBlockPlace implements Listener {

    private Main plugin;

    public onBlockPlace(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onBedPlace(BlockPlaceEvent e){
        if(e.getItemInHand().getType().equals(Material.RED_BED)){
            if(Objects.equals(e.getItemInHand().getItemMeta().getDisplayName(), ChatColor.AQUA + "Inwoners bed")){

                Location bedLocation = e.getBlockPlaced().getLocation();
                spawnResident(bedLocation, plugin);

                }

            }
        }

    public void spawnResident(Location location, Main plugin){
        NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, ChatColor.LIGHT_PURPLE + "Inwoner");
        npc.setAlwaysUseNameHologram(false);
        npc.setProtected(false);
        npc.spawn(location);
        Resident resident = new Resident("Inwoner", WorkingClass.CITIZEN, npc, plugin.useID(), location);
        resident.setInventory(new HashMap<>());
        plugin.workersManager.getResidents().add(resident);
        plugin.workersManager.saveResident(resident, plugin);
    }

}
