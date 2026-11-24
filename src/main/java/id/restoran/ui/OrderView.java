package id.restoran.ui;

import static id.restoran.ui.Dom.el;

import id.restoran.App;
import id.restoran.model.MenuItem;
import id.restoran.model.OrderLine;
import id.restoran.model.Ticket;
import id.restoran.service.Cart;
import id.restoran.service.TicketStore;
import id.restoran.util.Rupiah;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;
import org.teavm.jso.dom.html.HTMLSelectElement;
import org.teavm.jso.dom.html.HTMLTextAreaElement;

/**
 * Halaman menu + keranjang pesanan. Kasir memilih menu lalu menerbitkan tiket dapur.
 */
public class OrderView extends MenuView {
    private static final int TABLES = 20;
    private static final String TAKE_AWAY = "Bawa Pulang";

    private final Cart cart = new Cart();
    private HTMLElement cartLines;
    private HTMLElement cartSummary;
    private HTMLElement submit;
    private HTMLElement counter;

    @Override
    public String label() {
        return "Menu & Pesan";
    }

    @Override
    protected HTMLElement actionFor(MenuItem item) {
        return Dom.button("+ Tambah", "btn btn-sm", () -> {
            cart.add(item);
            renderCart();
        });
    }

    @Override
    protected void decorate(HTMLElement layout) {
        layout.getClassList().add("with-cart");
        HTMLElement aside = el("aside", "cart");

        HTMLElement head = el("div", "cart-head");
        head.appendChild(el("h2", null, "🧾 Pesanan Baru"));
        counter = el("span", "cart-count");
        head.appendChild(counter);
        aside.appendChild(head);

        HTMLElement form = el("div", "cart-form");
        HTMLInputElement name = (HTMLInputElement) el("input", "field");
        name.setPlaceholder("Nama pelanggan");
        name.setValue(cart.customer());
        name.onInput(evt -> cart.setCustomer(name.getValue()));
        form.appendChild(labeled("Pelanggan", name));

        HTMLSelectElement table = (HTMLSelectElement) el("select", "field");
        for (int i = 1; i <= TABLES; i++) {
            table.appendChild(option(Integer.toString(i), "Meja " + i));
        }
        table.appendChild(option(TAKE_AWAY, "🥡 " + TAKE_AWAY));
        table.setValue(cart.table());
        table.addEventListener("change", evt -> cart.setTable(table.getValue()));
        form.appendChild(labeled("Meja", table));

        HTMLTextAreaElement notes = (HTMLTextAreaElement) el("textarea", "field");
        notes.setPlaceholder("Catatan: tidak pedas, tanpa bawang, dll.");
        notes.setValue(cart.notes());
        notes.onInput(evt -> cart.setNotes(notes.getValue()));
        HTMLElement notesField = labeled("Catatan", notes);
        notesField.getClassList().add("full");
        form.appendChild(notesField);
        aside.appendChild(form);

        cartLines = el("div", "cart-lines");
        aside.appendChild(cartLines);
        cartSummary = el("div", "cart-summary");
        aside.appendChild(cartSummary);

        submit = Dom.button("Kirim Tiket ke Dapur", "btn btn-block", this::submit);
        aside.appendChild(submit);

        layout.appendChild(aside);
        renderCart();
    }

    private HTMLElement labeled(String label, HTMLElement input) {
        HTMLElement wrap = el("label", "field-wrap");
        wrap.appendChild(el("span", "field-label", label));
        wrap.appendChild(input);
        return wrap;
    }

    private HTMLElement option(String value, String text) {
        HTMLElement o = el("option", null, text);
        o.setAttribute("value", value);
        return o;
    }

    private void renderCart() {
        Dom.clear(cartLines);
        counter.setInnerText(cart.itemCount() + " item");
        if (cart.isEmpty()) {
            cartLines.appendChild(el("div", "cart-empty", "Belum ada menu dipilih. Klik “+ Tambah” pada menu."));
        }
        for (OrderLine line : cart.lines()) {
            HTMLElement row = el("div", "cart-line");
            HTMLElement info = el("div", "cart-line-info");
            info.appendChild(el("div", "cart-line-name", line.item().emoji() + " " + line.item().name()));
            info.appendChild(el("div", "cart-line-price", Rupiah.format(line.item().price())));
            row.appendChild(info);

            HTMLElement qty = el("div", "qty");
            qty.appendChild(Dom.button("−", "qty-btn", () -> {
                cart.change(line.item(), -1);
                renderCart();
            }));
            qty.appendChild(el("span", "qty-val", Integer.toString(line.quantity())));
            qty.appendChild(Dom.button("+", "qty-btn", () -> {
                cart.change(line.item(), 1);
                renderCart();
            }));
            row.appendChild(qty);
            row.appendChild(el("div", "cart-line-sub", Rupiah.format(line.subtotal())));
            cartLines.appendChild(row);
        }

        Dom.clear(cartSummary);
        cartSummary.appendChild(summaryRow("Subtotal", Rupiah.format(cart.subtotal()), false));
        cartSummary.appendChild(summaryRow("Pajak " + Ticket.TAX_PERCENT + "%", Rupiah.format(cart.tax()), false));
        cartSummary.appendChild(summaryRow("Total", Rupiah.format(cart.total()), true));
        if (cart.isEmpty()) {
            submit.setAttribute("disabled", "disabled");
        } else {
            submit.removeAttribute("disabled");
        }
    }

    private HTMLElement summaryRow(String label, String value, boolean strong) {
        HTMLElement row = el("div", strong ? "sum-row total" : "sum-row");
        row.appendChild(el("span", null, label));
        row.appendChild(el("span", null, value));
        return row;
    }

    private void submit() {
        if (cart.isEmpty()) {
            return;
        }
        String customer = cart.customer().trim().isEmpty() ? "Tamu" : cart.customer().trim();
        String table = TAKE_AWAY.equals(cart.table()) ? TAKE_AWAY : "Meja " + cart.table();
        Ticket ticket = TicketStore.get().create(customer, table, cart.notes().trim(), cart.lines());
        cart.clear();
        Toast.show("✅ Tiket " + ticket.code() + " untuk " + customer + " dikirim ke dapur");
        App.refresh();
    }
}
