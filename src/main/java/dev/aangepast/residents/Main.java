package dev.aangepast.residents;

import dev.aangepast.residents.commands.residentCommand;
import dev.aangepast.residents.components.Resident;
import dev.aangepast.residents.components.WorkingClass;
import dev.aangepast.residents.listener.entityDeathEvent;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public workersManager workersManager;
    public World world;
    public int currentResidentID;
    public Random random;

    @Override
    public void onEnable() {
        // Plugin startup logic

        this.workersManager = new workersManager();
        workersManager.setResidents(new ArrayList<>());
        this.random = new Random();
        String worldName = getConfig().getString("world");

        if(worldName == null){
            worldName = "world";
        }

        this.world = Bukkit.getWorld(worldName);

        saveDefaultConfig();
        this.currentResidentID = getConfig().getInt("residentID");

        Bukkit.getPluginCommand("resident").setExecutor(new residentCommand(this));
        Bukkit.getPluginManager().registerEvents(new entityDeathEvent(this),this);

        loadResidents(this);
        runResidents(this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic

        for(Resident resident : workersManager.getResidents()){
            resetResidentStatus(resident);
            workersManager.saveResident(resident, this);
        }

    }

    public void runResidents(Main plugin){
        Bukkit.getScheduler().runTaskTimer(this, new Runnable() {
            @Override
            public void run() {
                for(Resident resident : workersManager.getResidents()){

                    if(resident.getNpc() == null){
                        deleteResident(resident, plugin);
                        continue;
                    }

                    if(resident.isGoingHome()){
                        setResidentStatus(resident, "Going home");
                        resident.getNpc().getNavigator().setTarget(resident.getSpawnLocation());
                        continue;
                    }

                    if(resident.getNpc().getNavigator().isNavigating()){
                        continue;
                    }

                    if(!isDay(resident.getSpawnLocation().getWorld())){
                        if(resident.getNpc().getStoredLocation().distance(resident.getSpawnLocation()) > 3.0){
                            sendResidentToHome(resident,plugin);
                            setResidentStatus(resident, "Going home");
                            continue;
                        }
                        setResidentStatus(resident, "Sleeping");
                        continue;
                    }

                    if(resident.getSkill().equals(WorkingClass.BUTCHER)){

                        if(getChance(70)){

                            killNearestAnimal(resident, plugin);
                        }

                    }

                    if(getChance(97)){
                        getLogger().info("Going home");
                        setResidentStatus(resident, "Going home");
                        sendResidentToHome(resident, plugin);
                    }

                    if(getChance(50)){
                        Location npcLocation = resident.getNpc().getStoredLocation();

                        if(npcLocation == null){
                            continue;
                        }

                        int x = random.nextInt(-7,7);
                        int z = random.nextInt(-7, 7);
                        int y = random.nextInt(-3, 3);
                        npcLocation.add(x,y,z);
                        resident.getNpc().getNavigator().setTarget(npcLocation);
                        setResidentStatus(resident, "Walking");
                        continue;
                    }
                    setResidentStatus(resident, "Idling");
                }
            }
        }, 30, 30);
    }

    public int useID(){
        currentResidentID++;
        getConfig().set("residentID", currentResidentID);
        saveConfig();
        return this.currentResidentID;
    }

    public boolean getChance(int minimalChance) {
        Random random = new Random();
        return random.nextInt(99) + 1 >= minimalChance;
    }

    public void sendResidentToHome(Resident resident, Main plugin){
        resident.getNpc().getNavigator().setTarget(resident.getSpawnLocation());
        resident.setGoingHome(true);

        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                resident.setGoingHome(false);
            }
        },20*9);

    }

    public void loadResidents(Main plugin){
        Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
            @Override
            public void run() {
                File file = new File(getDataFolder() + "/residents.yml");
                FileConfiguration config = YamlConfiguration.loadConfiguration(file);

                for (String key : config.getKeys(false)) {
                    UUID uuid = UUID.fromString(config.getString(key + ".npc"));
                    String skillRaw = config.getString(key + ".type");
                    WorkingClass skill = WorkingClass.CITIZEN;
                    String name = ChatColor.GREEN + "Resident";
                    World world = Bukkit.getWorld(config.getString(key + ".world"));

                    if (world == null) {
                        plugin.getLogger().warning("WORLD OF RESIDENTS IS NOT FOUND, DISABLING PLUGIN...");
                        Bukkit.getPluginManager().disablePlugin(plugin);
                    }

                    Location spawnLocation = new Location(world, config.getInt(key + "x"), config.getInt(key + "y"), config.getInt(key + "z"));

                    if (skillRaw != null) {
                        if (skillRaw.equals("Butcher")) {
                            skill = WorkingClass.BUTCHER;
                            name = ChatColor.RED + ChatColor.BOLD.toString() + "Butcher";
                        }
                    }

                    Resident resident = new Resident(name, skill, CitizensAPI.getNPCRegistry().getByUniqueId(uuid), Integer.parseInt(key), spawnLocation);
                    plugin.workersManager.getResidents().add(resident);
                }
            }
        }, 20);
    }

    public void killNearestAnimal(Resident resident, Main plugin){

        NPC npc = resident.getNpc();
        List<Entity> entities = npc.getEntity().getNearbyEntities(npc.getStoredLocation().getX(), npc.getStoredLocation().getY(), npc.getStoredLocation().getZ());

        double distance = 100.0;
        Entity entityToKill = null;
        for(Entity entity : entities){
            if(entity.getType().equals(EntityType.COW) || entity.getType().equals(EntityType.PIG) || entity.getType().equals(EntityType.SHEEP) || entity.getType().equals(EntityType.CHICKEN)){
                if(entity.getCustomName() == null){

                    // Get the nearest animal
                    if(entity.getLocation().distance(resident.getNpc().getStoredLocation()) < distance){
                        distance = entity.getLocation().distance(resident.getNpc().getStoredLocation());
                        entityToKill = entity;
                    }
                }
            }
        }

        // Kill nearest animal
        if(entityToKill != null && distance < 10.0){
            final Entity finalEntityToKill = entityToKill;
            Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    setResidentStatus(resident, "Searching for animal");
                    killEntity(resident, finalEntityToKill, plugin);
                }
            }, 20*3);
        }

    }


    public boolean waitForButcher(Resident resident, Entity entityToKill){

        if(resident.getNpc().getNavigator().isNavigating()){
            Bukkit.getScheduler().runTaskLater(this, new Runnable() {
                @Override
                public void run() {
                    waitForButcher(resident, entityToKill);
                }
            }, 10);
        }
        return true;
    }

    public void deleteResident(Resident resident, Main plugin){
        Bukkit.getScheduler().runTaskLater(this, new Runnable() {
            @Override
            public void run() {
                if(resident.getNpc() != null){
                    resident.getNpc().destroy();
                }
                plugin.workersManager.removeResident(resident, plugin);
            }
        }, 1);
    }

    public void setResidentStatus(Resident resident, String status){
        String[] name = resident.getNpc().getName().split(" ");
        if(resident.getSkill().equals(WorkingClass.CITIZEN)){
            resident.getNpc().setName(ChatColor.LIGHT_PURPLE + name[0] + ChatColor.GRAY + " " + status);
        } else if (resident.getSkill().equals(WorkingClass.BUTCHER)){
            resident.getNpc().setName(ChatColor.RED + name[0] + ChatColor.GRAY + " " + status);
        }

    }

    public void resetResidentStatus(Resident resident){
        String[] name = resident.getNpc().getName().split(" ");
        if(name.length > 0){

            if(resident.getSkill().equals(WorkingClass.CITIZEN)){
                resident.getNpc().setName(ChatColor.LIGHT_PURPLE + name[0]);
            } else if (resident.getSkill().equals(WorkingClass.BUTCHER)){
                resident.getNpc().setName(ChatColor.RED + name[0]);
            }
        }
    }

    public void killEntity(Resident resident, Entity entity, Main plugin){
        resident.getNpc().getNavigator().setTarget(entity.getLocation());

        if(waitForButcher(resident, entity)){

            setResidentStatus(resident, "Killing animal");

            Bukkit.getScheduler().runTaskLater(plugin, new Runnable() {
                @Override
                public void run() {
                    EntityDamageEvent event = new EntityDamageEvent(entity, EntityDamageEvent.DamageCause.LIGHTNING, 100);
                    entity.setLastDamageCause(event);
                    entity.remove();
                    World world = entity.getWorld();
                    ItemStack redstoneBlock = new ItemStack(Material.REDSTONE_BLOCK, 1);
                    world.spawnParticle(Particle.ITEM_CRACK, entity.getLocation(), 20, redstoneBlock);
                    world.playSound(entity.getLocation(), "entity.player.attack.strong",1,1);
                }
            }, 20*5);
        }
    }

    public boolean isDay(World world) {
        if (world.getTime() < 13000) {
            return true;
        } else return world.getTime() < 13000;
    }


}
