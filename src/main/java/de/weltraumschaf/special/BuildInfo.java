package de.weltraumschaf.special;

import lombok.NonNull;
import org.springframework.boot.info.BuildProperties;

/**
 * Provides application build information such as version
 */
public final class BuildInfo {
    private final BuildProperties buildProperties;

    public BuildInfo(@NonNull BuildProperties buildProperties) {
        super();
        this.buildProperties = buildProperties;
    }

    public String version() {
        return buildProperties.getVersion();
    }

    public String commitId() {
        final var id = buildProperties.get("git.commit.id.abbrev");
        return id == null ? "" : id;
    }

    @Override
    public String toString() {
        return String.format("%s (%s)", version(), commitId());
    }
}
