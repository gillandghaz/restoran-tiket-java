package id.restoran.model;

/**
 * Satu baris pesanan: item menu dan jumlahnya.
 */
public final class OrderLine {
    private final MenuItem item;
    private int quantity;

    public OrderLine(MenuItem item, int quantity) {
        this.item = item;
        this.quantity = quantity;
    }

    public MenuItem item() {
        return item;
    }

    public int quantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public long subtotal() {
        return item.price() * quantity;
    }
}
