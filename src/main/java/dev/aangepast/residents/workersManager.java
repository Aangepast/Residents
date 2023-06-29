package dev.aangepast.residents;

import dev.aangepast.residents.components.Resident;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class workersManager {

    public List<Resident> residents = new ArrayList<>();

    public List<Resident> getResidents() {
        return residents;
    }

    public void setResidents(List<Resident> residents) {
        this.residents = residents;
    }

    public void saveResident(Resident resident, Main plugin){
        File file = new File(plugin.getDataFolder() + "/residents.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(resident.getID()+".type", resident.getSkill().toString());
        config.set(resident.getID()+".npc", resident.getNpc().getUniqueId().toString());
        config.set(resident.getID()+".x", resident.getSpawnLocation().getBlockX());
        config.set(resident.getID()+".y", resident.getSpawnLocation().getBlockY());
        config.set(resident.getID()+".z", resident.getSpawnLocation().getBlockZ());
        config.set(resident.getID()+".world", resident.getSpawnLocation().getWorld().getName());
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeResident(Resident resident, Main plugin){
        File file = new File(plugin.getDataFolder() + "/residents.yml");
        FileConfiguration config = YamlConfiguration.loadConfiguration(file);
        config.set(resident.getID()+".type", null);
        config.set(resident.getID()+".npc", null);
        config.set(resident.getID()+".x", null);
        config.set(resident.getID()+".y", null);
        config.set(resident.getID()+".z", null);
        config.set(resident.getID()+".world", null);
        config.set(resident.getID()+"", null);
        this.getResidents().remove(resident);
        try {
            config.save(file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
