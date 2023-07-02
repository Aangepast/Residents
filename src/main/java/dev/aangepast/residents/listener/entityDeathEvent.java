package dev.aangepast.residents.listener;

import dev.aangepast.residents.Main;
import dev.aangepast.residents.components.Resident;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
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
        } else if (e.getEntity().getType().equals(EntityType.COW) || e.getEntity().getType().equals(EntityType.PIG) || e.getEntity().getType().equals(EntityType.SHEEP) || e.getEntity().getType().equals(EntityType.CHICKEN)){
            if(e.getEntity().getLastDamageCause() != null){
                if(e.getEntity().getLastDamageCause().getCause().equals(EntityDamageEvent.DamageCause.LIGHTNING)){
                    switch(e.getEntity().getType()){
                        case PIG:
                            e.getDrops().add(new ItemStack(Material.PORKCHOP, 2));
                            break;
                        case COW:
                            e.getDrops().add(new ItemStack(Material.BEEF, 2));
                            break;
                        case SHEEP:
                            e.getDrops().add(new ItemStack(Material.MUTTON, 2));
                            e.getDrops().add(new ItemStack(Material.WHITE_WOOL, 1));
                            break;
                        case CHICKEN:
                            e.getDrops().add(new ItemStack(Material.CHICKEN, 1));
                            e.getDrops().add(new ItemStack(Material.FEATHER, 1));
                            break;
                    }
                }
            }
        }
    }

}
