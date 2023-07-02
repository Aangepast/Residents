package dev.aangepast.residents.listener;

import dev.aangepast.residents.Main;
import dev.aangepast.residents.components.Resident;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class inventoryClose implements Listener {

    private Main plugin;

    public inventoryClose(Main plugin){
        this.plugin = plugin;
    }


    @EventHandler
    public void onInv(InventoryCloseEvent e){
        if(e.getView().getTitle().equals("Resident inventory")){

            Player player = (Player) e.getPlayer();
            Resident resident = plugin.inventoryManager.getInteracting().get(player);

            HashMap<Integer, ItemStack> newInventory = new HashMap<>();

            for(int i = 0; i<27;i++){
                if(e.getView().getTopInventory().getItem(i) == null){
                    continue;
                }
                newInventory.put(i, e.getView().getTopInventory().getItem(i));
            }

            resident.setInventory(newInventory);
            plugin.inventoryManager.getInteracting().remove(player);
            plugin.workersManager.saveResident(resident, plugin);

        }
    }

}
