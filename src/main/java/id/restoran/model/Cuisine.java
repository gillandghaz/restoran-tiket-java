package id.restoran.model;

/**
 * Asal masakan.
 */
public enum Cuisine {
    INDONESIA("Indonesia", "🍛", "cuisine-id"),
    JEPANG("Jepang", "🍣", "cuisine-jp"),
    ARAB("Arab", "🥙", "cuisine-ar");

    private final String label;
    private final String icon;
    private final String cssClass;

    Cuisine(String label, String icon, String cssClass) {
        this.label = label;
        this.icon = icon;
        this.cssClass = cssClass;
    }

    public String label() {
        return label;
    }

    public String icon() {
        return icon;
    }

    public String cssClass() {
        return cssClass;
    }
}
