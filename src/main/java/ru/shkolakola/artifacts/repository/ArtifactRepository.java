package ru.shkolakola.artifacts.repository;

import ru.shkolakola.artifacts.artifact.Artifact;

import java.util.List;

/**
 * @author Alexey Zakharov
 * @date 23.05.2020
 */
public interface ArtifactRepository {

    List<Artifact> loadAll();

}
