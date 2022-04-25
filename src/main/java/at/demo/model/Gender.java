package at.demo.model;

import java.util.stream.Stream;

/**
 * Enumeration of available genders.
 *
 */
public enum Gender {
    MALE("male"),
    FEMALE("female"),
    OTHER("other"),
    UNKNOWN("unknown");

    private String name;

    Gender(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static Gender of(String name) {
        return Stream.of(Gender.values())
                .filter(g -> g.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
