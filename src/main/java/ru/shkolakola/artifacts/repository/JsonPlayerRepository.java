package ru.shkolakola.artifacts.repository;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import ru.abstractcoder.benioapi.jackson.util.JsonFileRepository;
import ru.shkolakola.artifacts.Core;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * @author Alexey Zakharov
 * @date 23.05.2020
 */
public class JsonPlayerRepository extends JsonFileRepository {

    private static final Map<UUID, List<String>> playerListMap = new HashMap<>();

    public JsonPlayerRepository(ObjectMapper objectMapper) {
        super(Core.getInstance().getDataPath().resolve("players.json"), objectMapper);

        initialize();
    }

    @Override
    protected void load(InputStream inputStream) throws IOException {
        String json = new BufferedReader(new InputStreamReader(inputStream))
                .lines().collect(Collectors.joining("\n"));

        playerListMap.putAll(objectMapper.readValue(json, new TypeReference<Map<UUID, List<String>>>() {}));
    }

    @Override
    protected void save(OutputStream outputStream) throws IOException {
        String json = objectMapper.writeValueAsString(playerListMap);

        outputStream.write(json.getBytes());
    }

    public Map<UUID, List<String>> getPlayerListMap() {
        return playerListMap;
    }

}
