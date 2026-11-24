package id.restoran.service;

import id.restoran.model.MenuItem;
import id.restoran.model.OrderLine;
import id.restoran.model.Ticket;
import java.util.ArrayList;
import java.util.List;

/**
 * Keranjang pesanan yang sedang disusun kasir.
 */
public final class Cart {
    private final List<OrderLine> lines = new ArrayList<>();
    private String customer = "";
    private String table = "1";
    private String notes = "";

    public void add(MenuItem item) {
        for (OrderLine line : lines) {
            if (line.item().id().equals(item.id())) {
                line.setQuantity(line.quantity() + 1);
                return;
            }
        }
        lines.add(new OrderLine(item, 1));
    }

    public void change(MenuItem item, int delta) {
        for (int i = 0; i < lines.size(); i++) {
            OrderLine line = lines.get(i);
            if (line.item().id().equals(item.id())) {
                int q = line.quantity() + delta;
                if (q <= 0) {
                    lines.remove(i);
                } else {
                    line.setQuantity(q);
                }
                return;
            }
        }
    }

    public List<OrderLine> lines() {
        return lines;
    }

    public boolean isEmpty() {
        return lines.isEmpty();
    }

    public int itemCount() {
        int c = 0;
        for (OrderLine line : lines) {
            c += line.quantity();
        }
        return c;
    }

    public long subtotal() {
        long s = 0;
        for (OrderLine line : lines) {
            s += line.subtotal();
        }
        return s;
    }

    public long tax() {
        return subtotal() * Ticket.TAX_PERCENT / 100;
    }

    public long total() {
        return subtotal() + tax();
    }

    public String customer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String table() {
        return table;
    }

    public void setTable(String table) {
        this.table = table;
    }

    public String notes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void clear() {
        lines.clear();
        customer = "";
        notes = "";
    }
}
