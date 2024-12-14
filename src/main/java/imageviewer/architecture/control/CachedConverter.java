package imageviewer.architecture.control;

import java.util.Optional;

public interface CachedConverter<Source, Target> {
    Optional<Target> convertAndCache(Source source);

    void clearCache();
}
