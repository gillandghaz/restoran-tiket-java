package id.restoran.ui;

import static id.restoran.ui.Dom.el;

import id.restoran.model.Cuisine;
import id.restoran.model.Ticket;
import id.restoran.service.Report;
import id.restoran.service.TicketStore;
import id.restoran.util.Clock;
import id.restoran.util.Rupiah;
import java.util.List;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Laporan penjualan: ringkasan omzet, omzet per masakan, menu terlaris, dan daftar struk.
 */
public class ReportView implements View {
    private static final int TOP = 5;

    @Override
    public String key() {
        return "laporan";
    }

    @Override
    public String label() {
        return "Laporan";
    }

    @Override
    public void render(HTMLElement container) {
        List<Ticket> tickets = TicketStore.get().all();
        Report r = Report.of(tickets);

        HTMLElement title = el("div", "section-title");
        title.appendChild(el("h2", null, "📊 Laporan Penjualan"));
        title.appendChild(el("span", "muted", "Omzet dihitung dari tiket berstatus Selesai"));
        container.appendChild(title);

        HTMLElement stats = el("div", "stats");
        stats.appendChild(stat("Omzet", Rupiah.format(r.revenue), "termasuk pajak " + Rupiah.format(r.taxCollected)));
        stats.appendChild(stat("Tiket Selesai", Integer.toString(r.doneTickets), "dari " + r.totalTickets + " tiket"));
        stats.appendChild(stat("Tiket Aktif", Integer.toString(r.activeTickets), "sedang diproses dapur"));
        stats.appendChild(stat("Rata-rata / Tiket", Rupiah.format(r.averageTicket()), r.cancelledTickets + " dibatalkan"));
        stats.appendChild(stat("Porsi Dipesan", Integer.toString(r.portionsSold), "tidak termasuk batal"));
        container.appendChild(stats);

        HTMLElement grid = el("div", "report-grid");
        grid.appendChild(cuisinePanel(r));
        grid.appendChild(bestSellerPanel(r));
        container.appendChild(grid);

        container.appendChild(ticketList(tickets));
    }

    private HTMLElement stat(String label, String value, String hint) {
        HTMLElement s = el("div", "stat");
        s.appendChild(el("div", "stat-label", label));
        s.appendChild(el("div", "stat-value", value));
        s.appendChild(el("div", "stat-hint", hint));
        return s;
    }

    private HTMLElement cuisinePanel(Report r) {
        HTMLElement panel = el("section", "panel");
        panel.appendChild(el("h3", null, "Omzet per Masakan"));
        long max = 1;
        for (long v : r.revenueByCuisine.values()) {
            max = Math.max(max, v);
        }
        for (Cuisine c : Cuisine.values()) {
            long v = r.revenueByCuisine.get(c);
            HTMLElement row = el("div", "bar-row");
            HTMLElement head = el("div", "bar-head");
            head.appendChild(el("span", null, c.icon() + " " + c.label()));
            head.appendChild(el("strong", null, Rupiah.format(v)));
            row.appendChild(head);
            HTMLElement track = el("div", "bar-track");
            HTMLElement fill = el("div", "bar-fill " + c.cssClass());
            fill.setAttribute("style", "width:" + (v * 100 / max) + "%");
            track.appendChild(fill);
            row.appendChild(track);
            panel.appendChild(row);
        }
        return panel;
    }

    private HTMLElement bestSellerPanel(Report r) {
        HTMLElement panel = el("section", "panel");
        panel.appendChild(el("h3", null, "🏆 Menu Terlaris"));
        if (r.bestSellers.isEmpty()) {
            panel.appendChild(el("div", "empty", "Belum ada pesanan."));
            return panel;
        }
        HTMLElement list = el("ol", "rank");
        for (int i = 0; i < Math.min(TOP, r.bestSellers.size()); i++) {
            Report.Seller s = r.bestSellers.get(i);
            HTMLElement li = el("li", null);
            li.appendChild(el("span", "rank-name", s.item.emoji() + " " + s.item.name()));
            li.appendChild(el("span", "rank-qty", s.quantity + " porsi"));
            list.appendChild(li);
        }
        panel.appendChild(list);
        return panel;
    }

    private HTMLElement ticketList(List<Ticket> tickets) {
        HTMLElement section = el("section", "history");
        section.appendChild(el("h3", null, "🧾 Semua Tiket & Struk"));
        if (tickets.isEmpty()) {
            section.appendChild(el("div", "empty", "Belum ada tiket. Buat pesanan dari halaman Menu & Pesan."));
            return section;
        }
        HTMLElement table = el("table", "history-table");
        HTMLElement head = el("tr", null);
        for (String h : new String[] {"Tiket", "Waktu", "Pelanggan", "Meja", "Total", "Status", ""}) {
            head.appendChild(el("th", null, h));
        }
        table.appendChild(head);
        for (int i = tickets.size() - 1; i >= 0; i--) {
            Ticket t = tickets.get(i);
            HTMLElement row = el("tr", null);
            row.appendChild(el("td", "mono", t.code()));
            row.appendChild(el("td", null, Clock.dateTime(t.createdAt())));
            row.appendChild(el("td", null, t.customer()));
            row.appendChild(el("td", null, t.table()));
            row.appendChild(el("td", null, Rupiah.format(t.total())));
            HTMLElement st = el("td", null);
            st.appendChild(el("span", "status-pill " + t.status().cssClass(), t.status().icon() + " " + t.status().label()));
            row.appendChild(st);
            HTMLElement act = el("td", null);
            act.appendChild(Dom.button("Lihat Struk", "btn btn-sm btn-ghost", () -> Receipt.open(t)));
            row.appendChild(act);
            table.appendChild(row);
        }
        section.appendChild(table);
        return section;
    }
}
