package id.restoran.model;

/**
 * Satu item di daftar menu.
 */
public final class MenuItem {
    private final String id;
    private final String name;
    private final Cuisine cuisine;
    private final Category category;
    private final long price;
    private final String description;
    private final String emoji;
    private final boolean spicy;

    public MenuItem(String id, String name, Cuisine cuisine, Category category, long price,
            String description, String emoji, boolean spicy) {
        this.id = id;
        this.name = name;
        this.cuisine = cuisine;
        this.category = category;
        this.price = price;
        this.description = description;
        this.emoji = emoji;
        this.spicy = spicy;
    }

    public String id() {
        return id;
    }

    public String name() {
        return name;
    }

    public Cuisine cuisine() {
        return cuisine;
    }

    public Category category() {
        return category;
    }

    public long price() {
        return price;
    }

    public String description() {
        return description;
    }

    public String emoji() {
        return emoji;
    }

    public boolean spicy() {
        return spicy;
    }

    /** Cocokkan dengan kata kunci pencarian (nama atau deskripsi). */
    public boolean matches(String query) {
        if (query == null || query.isBlank()) {
            return true;
        }
        String q = query.trim().toLowerCase();
        return name.toLowerCase().contains(q) || description.toLowerCase().contains(q);
    }
}
