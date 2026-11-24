package id.restoran.ui;

import static id.restoran.ui.Dom.el;

import id.restoran.App;
import id.restoran.model.OrderLine;
import id.restoran.model.Ticket;
import id.restoran.model.TicketStatus;
import id.restoran.service.TicketStore;
import id.restoran.util.Clock;
import id.restoran.util.Rupiah;
import java.util.ArrayList;
import java.util.List;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Papan tiket dapur ala kanban: Menunggu → Dimasak → Siap Saji, plus riwayat.
 */
public class KitchenBoardView implements View {
    /** Tiket aktif lebih lama dari ini (menit) ditandai terlambat. */
    private static final int LATE_MINUTES = 15;
    private static final int HISTORY_LIMIT = 12;

    private final TicketStore store = TicketStore.get();

    @Override
    public String key() {
        return "dapur";
    }

    @Override
    public String label() {
        return "Tiket Dapur";
    }

    @Override
    public String badge() {
        int active = store.activeCount();
        return active == 0 ? null : Integer.toString(active);
    }

    @Override
    public void render(HTMLElement container) {
        HTMLElement title = el("div", "section-title");
        title.appendChild(el("h2", null, "👨‍🍳 Papan Tiket Dapur"));
        HTMLElement actions = el("div", "title-actions");
        actions.appendChild(Dom.button("+ Pesanan Baru", "btn btn-sm", () -> App.navigate("menu")));
        actions.appendChild(Dom.button("Kosongkan Data", "btn btn-sm btn-ghost", () -> {
            if (Window.confirm("Hapus semua tiket? Data di browser ini akan dikosongkan.")) {
                store.reset();
                App.refresh();
            }
        }));
        title.appendChild(actions);
        container.appendChild(title);

        HTMLElement board = el("div", "board");
        board.appendChild(column(TicketStatus.MENUNGGU));
        board.appendChild(column(TicketStatus.DIMASAK));
        board.appendChild(column(TicketStatus.SIAP));
        container.appendChild(board);

        container.appendChild(history());
    }

    private HTMLElement column(TicketStatus status) {
        List<Ticket> tickets = store.byStatus(status);
        HTMLElement col = el("section", "column " + status.cssClass());
        HTMLElement head = el("div", "column-head");
        head.appendChild(el("span", null, status.icon() + " " + status.label()));
        head.appendChild(el("span", "column-count", Integer.toString(tickets.size())));
        col.appendChild(head);
        if (tickets.isEmpty()) {
            col.appendChild(el("div", "column-empty", "Tidak ada tiket"));
        }
        for (Ticket t : tickets) {
            col.appendChild(ticketCard(t));
        }
        return col;
    }

    private HTMLElement ticketCard(Ticket t) {
        long waited = Clock.minutesSince(t.createdAt());
        boolean late = t.status().isActive() && waited >= LATE_MINUTES;
        HTMLElement card = el("article", late ? "ticket late" : "ticket");

        HTMLElement head = el("div", "ticket-head");
        head.appendChild(el("strong", "ticket-code", t.code()));
        head.appendChild(el("span", "ticket-table", t.table()));
        card.appendChild(head);

        HTMLElement meta = el("div", "ticket-meta");
        meta.appendChild(el("span", null, "👤 " + t.customer()));
        meta.appendChild(el("span", late ? "wait late" : "wait",
                "🕒 " + Clock.time(t.createdAt()) + " · " + (waited <= 0 ? "baru saja" : waited + " mnt")));
        card.appendChild(meta);

        HTMLElement list = el("ul", "ticket-items");
        for (OrderLine line : t.lines()) {
            HTMLElement li = el("li", null);
            li.appendChild(el("span", "ticket-qty", line.quantity() + "×"));
            li.appendChild(el("span", null, line.item().name()));
            li.appendChild(el("span", "ticket-cuisine", line.item().cuisine().icon()));
            list.appendChild(li);
        }
        card.appendChild(list);

        if (!t.notes().isEmpty()) {
            card.appendChild(el("div", "ticket-notes", "📝 " + t.notes()));
        }

        HTMLElement foot = el("div", "ticket-foot");
        foot.appendChild(el("span", "ticket-total", Rupiah.format(t.total())));
        HTMLElement btns = el("div", "ticket-btns");
        if (t.status().isActive()) {
            btns.appendChild(Dom.button("Batal", "btn btn-sm btn-ghost", () -> {
                if (Window.confirm("Batalkan tiket " + t.code() + "?")) {
                    store.cancel(t);
                    Toast.show("Tiket " + t.code() + " dibatalkan");
                    App.refresh();
                }
            }));
            btns.appendChild(Dom.button(nextLabel(t.status()), "btn btn-sm", () -> {
                store.advance(t);
                Toast.show(t.code() + " → " + t.status().label());
                App.refresh();
            }));
        }
        foot.appendChild(btns);
        card.appendChild(foot);
        return card;
    }

    private String nextLabel(TicketStatus status) {
        switch (status) {
            case MENUNGGU:
                return "🔥 Mulai Masak";
            case DIMASAK:
                return "🛎️ Siap Saji";
            default:
                return "✅ Sudah Disajikan";
        }
    }

    private HTMLElement history() {
        List<Ticket> done = new ArrayList<>();
        List<Ticket> all = store.all();
        for (int i = all.size() - 1; i >= 0 && done.size() < HISTORY_LIMIT; i--) {
            if (!all.get(i).status().isActive()) {
                done.add(all.get(i));
            }
        }
        HTMLElement section = el("section", "history");
        section.appendChild(el("h3", null, "Riwayat Terakhir"));
        if (done.isEmpty()) {
            section.appendChild(el("div", "empty", "Belum ada tiket yang selesai atau dibatalkan."));
            return section;
        }
        HTMLElement table = el("table", "history-table");
        HTMLElement head = el("tr", null);
        for (String h : new String[] {"Tiket", "Pelanggan", "Meja", "Item", "Total", "Status", "Diperbarui"}) {
            head.appendChild(el("th", null, h));
        }
        table.appendChild(head);
        for (Ticket t : done) {
            HTMLElement row = el("tr", null);
            row.appendChild(el("td", "mono", t.code()));
            row.appendChild(el("td", null, t.customer()));
            row.appendChild(el("td", null, t.table()));
            row.appendChild(el("td", null, Integer.toString(t.itemCount())));
            row.appendChild(el("td", null, Rupiah.format(t.total())));
            HTMLElement st = el("td", null);
            st.appendChild(el("span", "status-pill " + t.status().cssClass(), t.status().icon() + " " + t.status().label()));
            row.appendChild(st);
            row.appendChild(el("td", null, Clock.time(t.updatedAt())));
            table.appendChild(row);
        }
        section.appendChild(table);
        return section;
    }
}
