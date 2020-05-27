package ru.shkolakola.artifacts.repository;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.abstractcoder.benioapi.config.HoconConfig;
import ru.shkolakola.artifacts.artifact.Artifact;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Alexey Zakharov
 * @date 23.05.2020
 */
public class HoconArtifactRepository implements ArtifactRepository {

    private final HoconConfig config;
    private final ObjectMapper mapper;

    public HoconArtifactRepository(HoconConfig hoconConfig, ObjectMapper objectMapper) {
        this.config = hoconConfig;
        this.mapper = objectMapper;
    }

    @Override
    public List<Artifact> loadAll() {
        List<Artifact> artifacts = new ArrayList<>();
        String artifactsJson = config.getHandle().getList("artifacts").render(HoconConfig.JSON_WRITE_DEFAULT);

        try {
            artifacts.addAll(mapper.readValue(artifactsJson, new TypeReference<List<Artifact>>() {}));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return artifacts;
    }

}
