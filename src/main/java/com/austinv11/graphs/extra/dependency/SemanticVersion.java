package com.austinv11.graphs.extra.dependency;

import javax.annotation.Nonnull;
import java.util.Arrays;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * This represents a "semantic version" of a dependency.
 *
 * @see <a href="https://semver.org/">Semver website</a>
 * @see #NONE
 */
public class SemanticVersion implements Comparable<SemanticVersion> {

    /**
     * Represents a non-existent version.
     */
    public static final SemanticVersion NONE = new SemanticVersion("0.0.0");

    private static final Pattern SANITIZE_PATTERN = Pattern.compile("[^\\d.]");

    private final int major, minor, patch;
    private final int[] others;

    public SemanticVersion(String versionString) {
        String sanitized = SANITIZE_PATTERN.matcher(versionString).replaceAll("");
        String[] split = sanitized.split(".");

        if (split.length > 0) {
            major = Integer.parseInt(split[0]);
        } else {
            major = 0;
        }

        if (split.length > 1) {
            minor = Integer.parseInt(split[1]);
        } else {
            minor = 0;
        }

        if (split.length > 2) {
            patch = Integer.parseInt(split[2]);
        } else {
            patch = 0;
        }

        if (split.length > 3) {
            others = new int[split.length-3];
            for (int i = 0; i < split.length-3; i++) {
                others[i] = Integer.parseInt(split[i+3]);
            }
        } else {
            others = new int[0];
        }
    }

    /**
     * Checks if this version is the magic "none" (i.e. no version present).
     *
     * @return If this instance is not an actual version.
     *
     * @see #NONE
     */
    public boolean isNone() {
        return NONE == this;
    }

    /**
     * Checks if this version is higher (more recent) than another.
     *
     * @param other The version to compare the current one to.
     * @return True if the current version is more recent than the other one.
     */
    public boolean isHigherThan(@Nonnull SemanticVersion other) {
        return this.compareTo(other) > 0;
    }

    /**
     * Checks if this version is lower (older) than another.
     *
     * @param other The version to compare the current one to.
     * @return True if the current version is older than the other one.
     */
    public boolean isLowerThan(@Nonnull SemanticVersion other) {
        return this.compareTo(other) < 0;
    }

    /**
     * Checks if this version is equivalent to another.
     *
     * @param other The version to compare the current one to.
     * @return True if the current version is equivalent than the other one.
     */
    public boolean matches(@Nonnull SemanticVersion other) {
        return this.compareTo(other) == 0;
    }

    /**
     * Gets the major version.
     *
     * @return The major version.
     */
    public int getMajor() {
        return major;
    }

    /**
     * Gets the minor version.
     *
     * @return The minor version.
     */
    public int getMinor() {
        return minor;
    }

    /**
     * Gets the patch version.
     *
     * @return The patch version.
     */
    public int getPatch() {
        return patch;
    }

    /**
     * Gets any additional version points if present.
     *
     * @return The additional version points or an empty array if none.
     */
    @Nonnull
    public int[] getOthers() {
        return others;
    }

    @Override
    public int compareTo(@Nonnull SemanticVersion o) {
        int currResult = Integer.compare(major, o.major);
        if (currResult == 0) {
            currResult = Integer.compare(minor, o.minor);
            if (currResult == 0) {
                currResult = Integer.compare(patch, o.patch);
                if (currResult == 0) {
                    for (int i = 0; i < others.length; i++) {
                        if (i >= o.others.length || currResult != 0)
                            break;

                        currResult = Integer.compare(others[i], o.others[i]);
                    }
                }
            }
        }
        return currResult;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof SemanticVersion)) {
            return false;
        }
        SemanticVersion that = (SemanticVersion) o;
        return major == that.major &&
                minor == that.minor &&
                patch == that.patch &&
                Arrays.equals(others, that.others);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(major, minor, patch);
        result = 31 * result + Arrays.hashCode(others);
        return result;
    }

    @Override
    public String toString() {
        StringJoiner joiner = new StringJoiner(".")
                .add(Integer.toString(major))
                .add(Integer.toString(minor))
                .add(Integer.toString(patch));
        for (int v : others) {
            joiner.add(Integer.toString(v));
        }
        return joiner.toString();
    }
}
