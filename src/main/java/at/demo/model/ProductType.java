package at.demo.model;

import java.util.stream.Stream;

/**
 * Enumeration of product types.
 *
 */
public enum ProductType {
    // COFFIN
    COFFIN("coffin"),
    // URN
    URN("urn"),
    // FLOWER
    FLOWER("flower"),
    // JEWELRY
    JEWELRY("jewelry"),
    // DEATH NOTICE PARTE
    PARTE("parte"),
    // MEMORIAL PICTURES
    PICTURE("picture"),
    // MEMENTOS
    MEMENTO("memento");

    private String name;

    ProductType(String name){ this.name = name;}

    public String getName() {
        return name;
    }

    public static ProductType of(String name) {
        return Stream.of(ProductType.values())
                .filter(g -> g.getName().equals(name))
                .findFirst()
                .orElseThrow(IllegalArgumentException::new);
    }
}
