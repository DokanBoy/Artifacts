package ru.shkolakola.artifacts.artifact;

import com.google.common.collect.ImmutableList;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.abstractcoder.benioapi.item.ItemBuilder;
import ru.shkolakola.artifacts.Core;
import ru.shkolakola.artifacts.repository.ArtifactRepository;
import ru.shkolakola.artifacts.repository.JsonPlayerRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

/**
 * @author Alexey Zakharov
 * @date 23.05.2020
 */
public class ArtifactManager {

    private final ImmutableList<Artifact> ARTIFACTS_REGISTRY;
    private final Map<UUID, List<String>> playerArtifactMap;

    public ArtifactManager(ArtifactRepository artifactRepository, JsonPlayerRepository playerRepository) {
        this.ARTIFACTS_REGISTRY = ImmutableList.copyOf(artifactRepository.loadAll());
        this.playerArtifactMap = playerRepository.getPlayerListMap();
    }

    private static ItemStack getColoredArmor(Color color, Material material) {
        return ItemBuilder.fromMaterial(material)
                .withItemMeta(LeatherArmorMeta.class)
                .customModifying(leatherArmorMeta -> {
                    leatherArmorMeta.setColor(color);
                    leatherArmorMeta.setDisplayName("§fАртефакт");
                })
                .and().build();
    }

    private static void setColoredArmor(Player player, ItemStack coloredArmor, String element) {
        switch (element) {
            case "HELMET":
                player.getInventory().setHelmet(coloredArmor);
                break;
            case "CHESTPLATE":
                player.getInventory().setChestplate(coloredArmor);
                break;
            case "LEGGINGS":
                player.getInventory().setLeggings(coloredArmor);
                break;
            case "BOOTS":
                player.getInventory().setBoots(coloredArmor);
                break;
        }
    }

    public void giveArtifact(Player player, Artifact artifact) {
        setColoredArmor(player, getColoredArmor(Core.getArtifactsColor(), artifact.getMaterial()),
                artifact.getElement());

        List<String> artifactList = new ArrayList<>();
        if (playerArtifactMap.containsKey(player.getUniqueId())) {
            artifactList.addAll(playerArtifactMap.get(player.getUniqueId()));
            playerArtifactMap.remove(player.getUniqueId());
        }
        artifactList.add(artifact.getElement());
        playerArtifactMap.put(player.getUniqueId(), artifactList);
    }

    public void giveReceivedArtifacts(Player player) {
        if (!playerArtifactMap.containsKey(player.getUniqueId())) return;
        ARTIFACTS_REGISTRY.forEach(artifact -> {
            if (playerArtifactMap.get(player.getUniqueId()).contains(artifact.getElement()))
                setColoredArmor(player, getColoredArmor(Core.getArtifactsColor(), artifact.getMaterial()),
                        artifact.getElement());
        });
    }

    public List<Artifact> getArtifacts() {
        return ARTIFACTS_REGISTRY;
    }

    public Map<UUID, List<String>> getPlayerArtifactMap() {
        return playerArtifactMap;
    }

}
