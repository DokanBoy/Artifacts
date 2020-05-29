package ru.shkolakola.artifacts;

import com.typesafe.config.Config;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import ru.shkolakola.artifacts.artifact.Artifact;
import ru.shkolakola.artifacts.artifact.ArtifactManager;

/**
 * @author Alexey Zakharov
 * @date 21.05.2020
 */
public class PlayerListener implements Listener {

    private static final ArtifactManager artifactManager = Core.getArtifactManager();
    private static final Config config = Core.getConfiguration().getHandle();


    public PlayerListener() {
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        Block block = event.getClickedBlock();
        Player player = event.getPlayer();

        if (block == null
                || block.getType() != Material.DISPENSER
                || !event.getAction().name().contains("RIGHT")
                || !event.getHand().name().equals("HAND"))
            return;

        event.setCancelled(true);

        for (Artifact artifact : artifactManager.getArtifacts()) {
            if (!artifact.getLocation().equals(block.getLocation()))
                continue;

            if (artifactManager.getPlayerArtifactMap().containsKey(player.getUniqueId())
                    && artifactManager.getPlayerArtifactMap().get(player.getUniqueId()).contains(artifact.getElement())) {
                player.sendTitle(
                        ChatColor.translateAlternateColorCodes('&', config.getString("messages.already_picked_up")),
                        ChatColor.translateAlternateColorCodes('&', config.getString("messages.already_picked_up-sub")),
                        15, 20, 15);
                return;
            }

            artifactManager.giveArtifact(player, artifact);
            int leftArtifacts = artifactManager.getArtifacts().size() -
                    artifactManager.getPlayerArtifactMap().get(player.getUniqueId()).size();

            if (leftArtifacts != 0) {
                player.sendTitle(
                        ChatColor.translateAlternateColorCodes('&', config.getString("messages.picked_up")),
                        ChatColor.translateAlternateColorCodes('&', config.getString("messages.picked_up-sub"))
                                .replace("{left}", String.valueOf(leftArtifacts)),
                        15, 20, 15);
            } else {
                player.sendTitle(
                        ChatColor.translateAlternateColorCodes('&', config.getString("messages.last_artifact")),
                        ChatColor.translateAlternateColorCodes('&', config.getString("messages.last_artifact-sub")),
                        15, 20, 15);
                Core.getVaultServiceProvider().depositMoney(player, 20);
            }
            return;
        }
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        artifactManager.giveReceivedArtifacts(event.getPlayer());
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        artifactManager.giveReceivedArtifacts(event.getEntity());
    }

}
