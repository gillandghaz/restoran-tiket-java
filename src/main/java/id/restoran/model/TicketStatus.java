package id.restoran.model;

/**
 * Alur status tiket dapur.
 */
public enum TicketStatus {
    MENUNGGU("Menunggu", "⏳", "st-wait"),
    DIMASAK("Dimasak", "🔥", "st-cook"),
    SIAP("Siap Saji", "🛎️", "st-ready"),
    SELESAI("Selesai", "✅", "st-done"),
    DIBATALKAN("Dibatalkan", "✖️", "st-cancel");

    private final String label;
    private final String icon;
    private final String cssClass;

    TicketStatus(String label, String icon, String cssClass) {
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

    /** Status berikutnya pada alur normal, atau null jika sudah final. */
    public TicketStatus next() {
        switch (this) {
            case MENUNGGU:
                return DIMASAK;
            case DIMASAK:
                return SIAP;
            case SIAP:
                return SELESAI;
            default:
                return null;
        }
    }

    public boolean isActive() {
        return this == MENUNGGU || this == DIMASAK || this == SIAP;
    }
}
