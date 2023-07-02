package dev.aangepast.residents.listener;

import dev.aangepast.residents.Main;
import dev.aangepast.residents.components.Resident;
import dev.aangepast.residents.inventories.residentInventory;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class onRightClick implements Listener {

    private Main plugin;

    public onRightClick(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onRightClickNPC(NPCRightClickEvent e){
        if(e.getNPC().getName().contains("Butcher") || e.getNPC().getName().contains("Resident")){

            e.setCancelled(true);

            NPC npc = e.getNPC();

            for(Resident resident : plugin.workersManager.getResidents()){
                if(resident.getNpc().equals(npc)){

                    if(plugin.inventoryManager.getInteracting().containsValue(resident)){
                        e.getClicker().sendMessage(ChatColor.RED + "This villager is already interacting with someone else.");
                        return;
                    }

                    plugin.inventoryManager.getInteracting().put(e.getClicker(), resident);
                    residentInventory.openInventory(e.getClicker(), resident);
                    break;
                }
            }

        }
    }

}
