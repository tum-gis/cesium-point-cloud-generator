
package de.tum.gis.tiles3d.model;

public enum Refine {

    ADD("add"),
    REPLACE("replace");
    
    private final String value;

    Refine(String v) {
        value = v;
    }

    public String value() {
        return value;
    }

    public static Refine fromValue(String v) {
        for (Refine c: Refine.values()) {
            if (c.value.equals(v)) {
                return c;
            }
        }
        throw new IllegalArgumentException(v);
    }

}
