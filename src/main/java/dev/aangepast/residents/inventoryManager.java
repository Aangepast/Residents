package dev.aangepast.residents;

import dev.aangepast.residents.components.Resident;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class inventoryManager {

    private HashMap<Player, Resident> interacting;

    public HashMap<Player, Resident> getInteracting() {
        return interacting;
    }

    public void setInteracting(HashMap<Player, Resident> interacting) {
        this.interacting = interacting;
    }
}
