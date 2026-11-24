package id.restoran.ui;

import static id.restoran.ui.Dom.el;

import id.restoran.model.OrderLine;
import id.restoran.model.Ticket;
import id.restoran.util.Clock;
import id.restoran.util.Rupiah;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Struk pembayaran dalam jendela modal, siap dicetak.
 */
public final class Receipt {
    private Receipt() {
    }

    public static void open(Ticket t) {
        HTMLElement overlay = el("div", "modal-overlay");
        HTMLElement modal = el("div", "modal");

        HTMLElement paper = el("div", "receipt");
        paper.appendChild(el("div", "rc-center rc-title", "RESTORAN TIGA RASA"));
        paper.appendChild(el("div", "rc-center rc-small", "Indonesia · Jepang · Arab"));
        paper.appendChild(el("div", "rc-center rc-small", "Jl. Rempah Nusantara No. 3"));
        paper.appendChild(el("div", "rc-sep"));
        paper.appendChild(row("No. Tiket", t.code()));
        paper.appendChild(row("Tanggal", Clock.dateTime(t.createdAt())));
        paper.appendChild(row("Pelanggan", t.customer()));
        paper.appendChild(row("Meja", t.table()));
        paper.appendChild(row("Status", t.status().label()));
        paper.appendChild(el("div", "rc-sep"));
        for (OrderLine line : t.lines()) {
            paper.appendChild(el("div", "rc-item", line.item().name()));
            paper.appendChild(row("  " + line.quantity() + " x " + Rupiah.format(line.item().price()),
                    Rupiah.format(line.subtotal())));
        }
        paper.appendChild(el("div", "rc-sep"));
        paper.appendChild(row("Subtotal", Rupiah.format(t.subtotal())));
        paper.appendChild(row("Pajak " + Ticket.TAX_PERCENT + "%", Rupiah.format(t.tax())));
        HTMLElement total = row("TOTAL", Rupiah.format(t.total()));
        total.getClassList().add("rc-total");
        paper.appendChild(total);
        if (!t.notes().isEmpty()) {
            paper.appendChild(el("div", "rc-sep"));
            paper.appendChild(el("div", "rc-small", "Catatan: " + t.notes()));
        }
        paper.appendChild(el("div", "rc-sep"));
        paper.appendChild(el("div", "rc-center rc-small", "Terima kasih · Arigatou · Syukran"));
        modal.appendChild(paper);

        HTMLElement actions = el("div", "modal-actions");
        actions.appendChild(Dom.button("Tutup", "btn btn-ghost", () -> close(overlay)));
        actions.appendChild(Dom.button("🖨️ Cetak Struk", "btn", () -> Window.current().print()));
        modal.appendChild(actions);

        overlay.appendChild(modal);
        overlay.onClick(evt -> {
            if ("modal-overlay".equals(((HTMLElement) evt.getTarget()).getClassName())) {
                close(overlay);
            }
        });
        Dom.doc().getBody().appendChild(overlay);
        Dom.doc().getBody().getClassList().add("printing-receipt");
    }

    private static void close(HTMLElement overlay) {
        Dom.doc().getBody().getClassList().remove("printing-receipt");
        if (overlay.getParentNode() != null) {
            overlay.getParentNode().removeChild(overlay);
        }
    }

    private static HTMLElement row(String left, String right) {
        HTMLElement r = el("div", "rc-row");
        r.appendChild(el("span", null, left));
        r.appendChild(el("span", null, right));
        return r;
    }
}
