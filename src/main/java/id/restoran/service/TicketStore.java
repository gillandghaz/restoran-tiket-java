package id.restoran.service;

import id.restoran.model.OrderLine;
import id.restoran.model.Ticket;
import id.restoran.model.TicketStatus;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.teavm.jso.browser.Storage;

/**
 * Penyimpanan tiket di browser. Tanpa database: data dipersist ke localStorage
 * sehingga tetap ada setelah halaman dimuat ulang.
 */
public final class TicketStore {
    public static final String STORAGE_KEY = "restoran-tiga-rasa.tickets";
    private static final TicketStore INSTANCE = new TicketStore();

    private final List<Ticket> tickets = new ArrayList<>();
    private final List<Runnable> listeners = new ArrayList<>();
    private int nextNumber = 1;

    private TicketStore() {
        load();
    }

    public static TicketStore get() {
        return INSTANCE;
    }

    public List<Ticket> all() {
        return Collections.unmodifiableList(tickets);
    }

    public void addListener(Runnable listener) {
        listeners.add(listener);
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

    /** Majukan tiket ke status berikutnya (Menunggu → Dimasak → Siap → Selesai). */
    public void advance(Ticket ticket) {
        TicketStatus next = ticket.status().next();
        if (next != null) {
            ticket.setStatus(next, System.currentTimeMillis());
            changed();
        }
    }

    public void cancel(Ticket ticket) {
        if (ticket.status().isActive()) {
            ticket.setStatus(TicketStatus.DIBATALKAN, System.currentTimeMillis());
            changed();
        }
    }

    /** Hapus semua tiket dan mulai penomoran dari awal. */
    public void reset() {
        tickets.clear();
        nextNumber = 1;
        changed();
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

    /** Muat ulang dari localStorage (mis. ketika tab lain mengubah data). */
    public void load() {
        TicketCodec.Result r = TicketCodec.decode(storage() == null ? null : storage().getItem(STORAGE_KEY));
        tickets.clear();
        tickets.addAll(r.tickets);
        nextNumber = r.nextNumber;
    }

    public void notifyListeners() {
        for (Runnable l : listeners) {
            l.run();
        }
    }

    private void changed() {
        Storage s = storage();
        if (s != null) {
            s.setItem(STORAGE_KEY, TicketCodec.encode(tickets, nextNumber));
        }
        notifyListeners();
    }

    private static Storage storage() {
        return Storage.getLocalStorage();
    }
}
