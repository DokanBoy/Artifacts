package ru.shkolakola.artifacts;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.bukkit.Bukkit;
import org.bukkit.Color;
import org.bukkit.plugin.java.annotation.dependency.Dependency;
import org.bukkit.plugin.java.annotation.dependency.DependsOn;
import org.bukkit.plugin.java.annotation.plugin.Plugin;
import org.bukkit.plugin.java.annotation.plugin.Website;
import org.bukkit.plugin.java.annotation.plugin.author.Author;
import ru.abstractcoder.benioapi.BenioPlugin;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.shkolakola.artifacts.artifact.ArtifactManager;
import ru.shkolakola.artifacts.repository.ArtifactRepository;
import ru.shkolakola.artifacts.repository.HoconArtifactRepository;
import ru.shkolakola.artifacts.repository.JsonPlayerRepository;

/**
 * @author Alexey Zakharov
 * @date 21.05.2020
 */
@Plugin(name = "Artifacts", version = "1.0-Final")
@Author(value = "DokanBoy")
@Website(value = "shkolakola.ru")
@DependsOn({@Dependency("BenioApi"), @Dependency("Vault"), @Dependency("HolographicDisplays")})
public class Core extends BenioPlugin {

    private static Core instance;

    private static ArtifactManager artifactManager;
    private static VaultServiceProvider vaultServiceProvider;
    private static ObjectMapper objectMapper;
    private static HoconConfig config;
    private static JsonPlayerRepository playerRepository;

    public static ArtifactManager getArtifactManager() {
        return artifactManager;
    }

    public static VaultServiceProvider getVaultServiceProvider() {
        return vaultServiceProvider;
    }

    public static Core getInstance() {
        return instance;
    }

    public static Color getArtifactsColor() {
        Color color = Color.ORANGE;
        String colorJson = config.getHandle().getValue("color").render(HoconConfig.JSON_WRITE_DEFAULT);

        try {
            color = objectMapper.readValue(colorJson, new TypeReference<Color>() {
            });
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return color;
    }

    @Override
    protected void onPluginEnable() throws Throwable {
        saveResource("config.conf", false);
        instance = this;

        vaultServiceProvider = new VaultServiceProvider();

        objectMapper = benioApiInstance.objectMapperService().createAdaptedObjectMapper();
        benioApiInstance.objectMapperService().adaptObjectMapper(objectMapper);

        config = benioApiInstance.configBuilder().baseConfig().buildHocon();

        ArtifactRepository artifactRepository = new HoconArtifactRepository(config, objectMapper);
        playerRepository = new JsonPlayerRepository(objectMapper);
        artifactManager = new ArtifactManager(artifactRepository, playerRepository);

        registerListener(new PlayerListener());

        Bukkit.getScheduler().scheduleSyncRepeatingTask(this, new Runnable() {
            @Override
            public void run() {
                playerRepository.save();
            }
        }, 20 * 60, 20 * 60);
    }

    @Override
    protected void onPluginDisable() throws Throwable {
        playerRepository.save();
    }

}
