package dev.aangepast.residents.inventories;

import dev.aangepast.residents.Main;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class classInventory {

    public static void openClassInventory(Player player){
        Inventory inv = Bukkit.createInventory(player, InventoryType.HOPPER, "Verander vaardigheid");

        ItemStack stick = new ItemStack(Material.STICK, 1);
        ItemMeta stickMeta = stick.getItemMeta();
        stickMeta.setDisplayName(ChatColor.DARK_AQUA + "Maak werkloos");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Klik om deze inwoner werkloos te maken.");
        stickMeta.setLore(lore);
        stick.setItemMeta(stickMeta);
        inv.setItem(1,stick);

        ItemStack axe = new ItemStack(Material.IRON_AXE, 1);
        ItemMeta axeMeta = axe.getItemMeta();
        axeMeta.setDisplayName(ChatColor.RED + "Maak slager");
        List<String> loreAxe = new ArrayList<>();
        loreAxe.add(ChatColor.GRAY + "Klik om deze inwoner een slager te maken.");
        axeMeta.addItemFlags(ItemFlag.HIDE_ATTRIBUTES);
        axeMeta.setLore(loreAxe);
        axe.setItemMeta(axeMeta);
        inv.setItem(3,axe);

        player.openInventory(inv);

    }

}
