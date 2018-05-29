package com.austinv11.graphs.extra.dependency;

import com.austinv11.graphs.Vertex;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BooleanSupplier;
import java.util.function.Function;

/**
 * Represents a dependency vertex in a dependency graph.
 */
public class Dependency<D extends Dependency.Info> implements Vertex<D> {

    private final D info;
    private final BooleanSupplier installation;
    private final Function<Info, Collection<Dependency<D>>> dependencyDetection;

    public Dependency(D info, BooleanSupplier installation,
                      Function<Info, Collection<Dependency<D>>> dependencyDetection) {
        this.info = info;
        this.installation = installation;
        this.dependencyDetection = dependencyDetection;
    }

    @Nonnull
    @Override
    public D get() {
        return info;
    }

    /**
     * Installs this dependency.
     *
     * @return True if successful, false if otherwise.
     */
    public boolean install() {
        return installation.getAsBoolean();
    }

    /**
     * Retrieves the <b>DIRECT</b> dependencies of the current dependency or an empty collection if there
     * are none.
     *
     * @return The direct dependencies of this dependency, or an empty collection if none.
     */
    @Nonnull
    public Collection<Dependency<D>> getDirectDependencies() {
        return dependencyDetection.apply(info);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Dependency)) {
            return false;
        }
        Dependency<?> that = (Dependency<?>) o;
        return Objects.equals(info, that.info);
    }

    @Override
    public int hashCode() {
        return Objects.hash(info);
    }

    /**
     * Represents metadata for a dependency.
     *
     * @see com.austinv11.graphs.extra.dependency.SemanticVersion
     * @see com.austinv11.graphs.extra.dependency.SemanticVersion#NONE
     */
    public static class Info {

        private final String name;
        private final SemanticVersion version;
        private final @Nullable String description;

        public Info(String name, SemanticVersion version, @Nullable String description) {
            this.name = name;
            this.version = version;
            this.description = description;
        }

        public String getName() {
            return name;
        }

        /**
         * @see com.austinv11.graphs.extra.dependency.SemanticVersion
         * @see com.austinv11.graphs.extra.dependency.SemanticVersion#NONE
         */
        public SemanticVersion getVersion() {
            return version;
        }

        @Nullable
        public String getDescription() {
            return description;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (!(o instanceof Info)) {
                return false;
            }
            Info info = (Info) o;
            return Objects.equals(getName(), info.getName()) &&
                    Objects.equals(getVersion(), info.getVersion()) &&
                    Objects.equals(getDescription(), info.getDescription());
        }

        @Override
        public int hashCode() {
            return Objects.hash(getName(), getVersion(), getDescription());
        }
    }
}
