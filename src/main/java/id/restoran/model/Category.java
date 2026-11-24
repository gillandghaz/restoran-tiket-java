package id.restoran.model;

/**
 * Kategori hidangan.
 */
public enum Category {
    PEMBUKA("Pembuka"),
    UTAMA("Hidangan Utama"),
    PENUTUP("Penutup"),
    MINUMAN("Minuman");

    private final String label;

    Category(String label) {
        this.label = label;
    }

    public String label() {
        return label;
    }
}
