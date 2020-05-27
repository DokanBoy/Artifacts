package ru.shkolakola.artifacts.artifact;

import com.gmail.filoghost.holographicdisplays.api.Hologram;
import com.gmail.filoghost.holographicdisplays.api.HologramsAPI;
import org.bukkit.Location;
import org.bukkit.Material;
import ru.shkolakola.artifacts.Core;

/**
 * @author Alexey Zakharov
 * @date 23.05.2020
 */
public class Artifact {

    private final Location location;
    private final String element;
    private final Material material;

    public Artifact(Location location, String element) {
        if (!element.equalsIgnoreCase("HELMET")
                && !element.equalsIgnoreCase("CHESTPLATE")
                && !element.equalsIgnoreCase("LEGGINGS")
                && !element.equalsIgnoreCase("BOOTS"))
            throw new IllegalArgumentException("Illegal element type. Accept only: HELMET, CHESTPLATE, LEGGINGS, BOOTS");

        this.location = location;
        this.element = element.toUpperCase();
        this.material = Material.getMaterial("LEATHER_" + element.toUpperCase());

        Location hologramLocation = location.clone();
        hologramLocation.setX(((int) getLocation().getX()) + 0.5);
        hologramLocation.setY(((int) getLocation().getY()) + 2.0);
        hologramLocation.setZ(((int) getLocation().getZ()) + 0.5);

        Hologram hologram = HologramsAPI.createHologram(Core.getInstance(), hologramLocation);
        hologram.appendTextLine("§eПолучи артефакт");
        hologram.appendTextLine("§7ПКМ по раздатчику");
        hologram.getVisibilityManager().setVisibleByDefault(true);
    }

    public Location getLocation() {
        return location;
    }

    public String getElement() {
        return element;
    }

    public Material getMaterial() {
        return material;
    }

    @Override
    public String toString() {
        return "Artifact{" + "location=" + location.toString() +
                ", element=" + element +
                ", material=" + material.name() +
                '}';
    }

    @Override
    public int hashCode() {
        return location.hashCode();
    }

}
