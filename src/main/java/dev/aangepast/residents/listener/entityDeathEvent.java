package dev.aangepast.residents.listener;

import dev.aangepast.residents.Main;
import dev.aangepast.residents.components.Resident;
import org.bukkit.ChatColor;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;

public class entityDeathEvent implements Listener {

    private Main plugin;

    public entityDeathEvent(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void npcDeath(EntityDeathEvent e){
        if(e.getEntity().getType().equals(EntityType.VILLAGER) && e.getEntity().getCustomName() != null){
            Resident killed = null;
            for (Resident resident : plugin.workersManager.getResidents()){
                if(!resident.getNpc().isSpawned()){
                    for(ItemStack item : resident.getInventory().values()){
                        resident.getNpc().getStoredLocation().getWorld().dropItemNaturally(resident.getNpc().getStoredLocation(), item);
                    }
                    plugin.workersManager.removeResident(resident, plugin);
                    resident.getNpc().destroy();
                    killed = resident;
                    if(e.getEntity().getKiller() != null){
                        e.getEntity().getKiller().sendMessage(ChatColor.RED + "Je hebt een resident gedood.");
                        break;
                    }
                }
            }
            if(killed != null){
                plugin.workersManager.removeResident(killed,plugin);
            }
        }
    }

}
