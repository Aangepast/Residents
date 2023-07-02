package dev.aangepast.residents.components;

import net.citizensnpcs.api.npc.NPC;
import org.bukkit.Location;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public class Resident {

    private String name;
    private boolean goingHome = false;
    private WorkingClass skill;
    private NPC npc;
    private int ID;
    Location spawnLocation;
    private HashMap<Integer, ItemStack> inventory;

    public Resident(String name, WorkingClass workingClass, NPC npc, int id, Location spawnLocation){
        this.name = name;
        this.skill = workingClass;
        this.npc = npc;
        this.ID = id;
        this.spawnLocation = spawnLocation;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public WorkingClass getSkill() {
        return skill;
    }

    public void setSkill(WorkingClass skill) {
        this.skill = skill;
    }

    public NPC getNpc() {
        return npc;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public Location getSpawnLocation() {
        return spawnLocation;
    }

    public void setSpawnLocation(Location spawnLocation) {
        this.spawnLocation = spawnLocation;
    }

    public boolean isGoingHome() {
        return goingHome;
    }

    public void setGoingHome(boolean goingHome) {
        this.goingHome = goingHome;
    }

    public HashMap<Integer, ItemStack> getInventory() {
        return inventory;
    }

    public void setInventory(HashMap<Integer, ItemStack> inventory) {
        this.inventory = inventory;
    }
}
