package dev.aangepast.residents.commands;

import dev.aangepast.residents.Main;
import dev.aangepast.residents.components.Resident;
import dev.aangepast.residents.components.WorkingClass;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;

public class residentCommand implements CommandExecutor {

    private Main plugin;

    public residentCommand(Main plugin){
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if(!(sender instanceof Player)){return true;}

        Player player = (Player) sender;

        if(!(args.length > 0)){
            player.sendMessage(ChatColor.RED + "Missing worker type. Syntax: /resident spawn [type]");
            return true;
        }

        if(args[0].equalsIgnoreCase("spawn")) {
            if (args[1].equalsIgnoreCase("Butcher")) {
                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, ChatColor.RED + "Butcher");
                npc.setAlwaysUseNameHologram(false);
                npc.setProtected(false);
                npc.spawn(player.getLocation());
                Resident resident = new Resident("Butcher", WorkingClass.BUTCHER, npc, plugin.useID(), player.getLocation());
                plugin.workersManager.getResidents().add(resident);
                player.sendMessage(ChatColor.GREEN + "Spawned a resident at your location.");
                plugin.workersManager.saveResident(resident, plugin);

                LivingEntity entity = (LivingEntity) resident.getNpc().getEntity();
                entity.getEquipment().setItem(EquipmentSlot.HAND, new ItemStack(Material.IRON_AXE, 1));

            } else if (args[1].equalsIgnoreCase("resident")) {
                NPC npc = CitizensAPI.getNPCRegistry().createNPC(EntityType.VILLAGER, ChatColor.LIGHT_PURPLE + "Resident");
                npc.setAlwaysUseNameHologram(false);
                npc.setProtected(false);
                npc.spawn(player.getLocation());
                Resident resident = new Resident("Resident", WorkingClass.CITIZEN, npc, plugin.useID(), player.getLocation());
                plugin.workersManager.getResidents().add(resident);
                player.sendMessage(ChatColor.GREEN + "Spawned citizen at your location");
                plugin.workersManager.saveResident(resident, plugin);
            }
        } else if (args[0].equalsIgnoreCase("remove")) {
            double distance = 100.0;
            Resident closestResident = null;

            for (Resident resident : plugin.workersManager.getResidents()){

                if(distance > resident.getNpc().getStoredLocation().distance(player.getLocation())){
                    distance = resident.getNpc().getStoredLocation().distance(player.getLocation());
                    closestResident = resident;
                }
            }

            if(closestResident == null){
                player.sendMessage(ChatColor.RED + "No Resident found in range of you to despawn.");
                return true;
            }

            closestResident.getNpc().destroy();
            plugin.workersManager.removeResident(closestResident, plugin);
            player.sendMessage(ChatColor.GREEN + "Successfully deleted the nearest resident.");


        } else {
            player.sendMessage(ChatColor.RED + "Incorrect command. Try again. /resident spawn [type]");
        }
        return true;
    }
}
