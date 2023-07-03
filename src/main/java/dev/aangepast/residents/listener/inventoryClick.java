package dev.aangepast.residents.listener;

import dev.aangepast.residents.Main;
import dev.aangepast.residents.components.WorkingClass;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class inventoryClick implements Listener {

    private Main plugin;

    public inventoryClick(Main plugin){
        this.plugin = plugin;
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if(e.getView().getTitle().equals("Verander vaardigheid")){
            e.setCancelled(true);
            Player player = (Player) e.getWhoClicked();
            if(plugin.inventoryManager.getInteracting().get(player) != null){
                switch(e.getRawSlot()){
                    case 1:
                        plugin.inventoryManager.getInteracting().get(player).setSkill(WorkingClass.CITIZEN);
                        plugin.inventoryManager.getInteracting().get(player).getNpc().setName(ChatColor.LIGHT_PURPLE + "Inwoner");
                        player.playSound(player.getLocation(), "entity.experience_orb.pickup",1,1);
                        player.closeInventory();
                        break;
                    case 3:
                        plugin.inventoryManager.getInteracting().get(player).setSkill(WorkingClass.BUTCHER);
                        plugin.inventoryManager.getInteracting().get(player).getNpc().setName(ChatColor.RED + "Slager");
                        player.playSound(player.getLocation(), "entity.experience_orb.pickup",1,1);
                        player.closeInventory();
                        break;
                }
            }
        }
    }

}
