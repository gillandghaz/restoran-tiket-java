package id.restoran.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Tiket pesanan yang dikirim ke dapur.
 */
public final class Ticket {
    /** Pajak restoran (PB1) 10%. */
    public static final int TAX_PERCENT = 10;

    private final int number;
    private final String customer;
    private final String table;
    private final String notes;
    private final List<OrderLine> lines;
    private final double createdAt;
    private TicketStatus status;
    private double updatedAt;

    public Ticket(int number, String customer, String table, String notes, List<OrderLine> lines,
            TicketStatus status, double createdAt, double updatedAt) {
        this.number = number;
        this.customer = customer;
        this.table = table;
        this.notes = notes;
        this.lines = new ArrayList<>(lines);
        this.status = status;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
    }

    public int number() {
        return number;
    }

    /** Kode tiket, mis. T-0007. */
    public String code() {
        String n = Integer.toString(number);
        while (n.length() < 4) {
            n = "0" + n;
        }
        return "T-" + n;
    }

    public String customer() {
        return customer;
    }

    public String table() {
        return table;
    }

    public String notes() {
        return notes;
    }

    public List<OrderLine> lines() {
        return lines;
    }

    public TicketStatus status() {
        return status;
    }

    public double createdAt() {
        return createdAt;
    }

    public double updatedAt() {
        return updatedAt;
    }

    public void setStatus(TicketStatus status, double now) {
        this.status = status;
        this.updatedAt = now;
    }

    public int itemCount() {
        int count = 0;
        for (OrderLine line : lines) {
            count += line.quantity();
        }
        return count;
    }

    public long subtotal() {
        long sum = 0;
        for (OrderLine line : lines) {
            sum += line.subtotal();
        }
        return sum;
    }

    public long tax() {
        return subtotal() * TAX_PERCENT / 100;
    }

    public long total() {
        return subtotal() + tax();
    }
}
