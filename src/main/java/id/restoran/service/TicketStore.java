package id.restoran.service;

import id.restoran.model.OrderLine;
import id.restoran.model.Ticket;
import id.restoran.model.TicketStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Penyimpanan tiket di memori browser.
 */
public final class TicketStore {
    private static final TicketStore INSTANCE = new TicketStore();

    private final List<Ticket> tickets = new ArrayList<>();
    private int nextNumber = 1;

    private TicketStore() {
    }

    public static TicketStore get() {
        return INSTANCE;
    }

    public List<Ticket> all() {
        return Collections.unmodifiableList(tickets);
    }

    /** Terbitkan tiket baru dari isi keranjang. */
    public Ticket create(String customer, String table, String notes, List<OrderLine> lines) {
        double now = System.currentTimeMillis();
        List<OrderLine> copy = new ArrayList<>();
        for (OrderLine line : lines) {
            copy.add(new OrderLine(line.item(), line.quantity()));
        }
        Ticket t = new Ticket(nextNumber++, customer, table, notes, copy, TicketStatus.MENUNGGU, now, now);
        tickets.add(t);
        changed();
        return t;
    }

    public List<Ticket> byStatus(TicketStatus status) {
        List<Ticket> result = new ArrayList<>();
        for (Ticket t : tickets) {
            if (t.status() == status) {
                result.add(t);
            }
        }
        return result;
    }

    public int activeCount() {
        int c = 0;
        for (Ticket t : tickets) {
            if (t.status().isActive()) {
                c++;
            }
        }
        return c;
    }

    /** Dipanggil setiap ada perubahan data. */
    void changed() {
    }
}
