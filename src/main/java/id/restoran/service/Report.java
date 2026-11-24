package id.restoran.service;

import id.restoran.model.Cuisine;
import id.restoran.model.MenuItem;
import id.restoran.model.OrderLine;
import id.restoran.model.Ticket;
import id.restoran.model.TicketStatus;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Rekap penjualan yang dihitung dari tiket yang ada.
 * Omzet hanya menghitung tiket berstatus Selesai.
 */
public final class Report {
    /** Jumlah porsi terjual per item menu. */
    public static final class Seller {
        public final MenuItem item;
        public int quantity;
        public long revenue;

        Seller(MenuItem item) {
            this.item = item;
        }
    }

    public int totalTickets;
    public int activeTickets;
    public int doneTickets;
    public int cancelledTickets;
    public long revenue;
    public long taxCollected;
    public int portionsSold;
    public final Map<Cuisine, Long> revenueByCuisine = new LinkedHashMap<>();
    public final List<Seller> bestSellers = new ArrayList<>();

    public static Report of(List<Ticket> tickets) {
        Report r = new Report();
        for (Cuisine c : Cuisine.values()) {
            r.revenueByCuisine.put(c, 0L);
        }
        Map<String, Seller> sellers = new LinkedHashMap<>();
        for (Ticket t : tickets) {
            r.totalTickets++;
            if (t.status().isActive()) {
                r.activeTickets++;
            } else if (t.status() == TicketStatus.DIBATALKAN) {
                r.cancelledTickets++;
                continue;
            }
            if (t.status() == TicketStatus.SELESAI) {
                r.doneTickets++;
                r.revenue += t.total();
                r.taxCollected += t.tax();
            }
            for (OrderLine line : t.lines()) {
                MenuItem item = line.item();
                Seller s = sellers.get(item.id());
                if (s == null) {
                    s = new Seller(item);
                    sellers.put(item.id(), s);
                }
                s.quantity += line.quantity();
                s.revenue += line.subtotal();
                r.portionsSold += line.quantity();
                if (t.status() == TicketStatus.SELESAI) {
                    r.revenueByCuisine.put(item.cuisine(), r.revenueByCuisine.get(item.cuisine()) + line.subtotal());
                }
            }
        }
        r.bestSellers.addAll(sellers.values());
        r.bestSellers.sort((a, b) -> a.quantity != b.quantity
                ? Integer.compare(b.quantity, a.quantity)
                : Long.compare(b.revenue, a.revenue));
        return r;
    }

    public long averageTicket() {
        return doneTickets == 0 ? 0 : revenue / doneTickets;
    }
}
