package dev.aangepast.residents.inventories;

import dev.aangepast.residents.components.Resident;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class residentInventory {

    public static void openInventory(Player player, Resident resident){

        Inventory inv = Bukkit.createInventory(player, 27, "Inwoner inventaris");

        for(int slot : resident.getInventory().keySet()){
            inv.setItem(slot, resident.getInventory().get(slot));
        }

        player.openInventory(inv);

    }

}
