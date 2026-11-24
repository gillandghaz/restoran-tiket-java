package id.restoran.ui;

import static id.restoran.ui.Dom.el;

import id.restoran.data.MenuCatalog;
import id.restoran.model.Category;
import id.restoran.model.Cuisine;
import id.restoran.model.MenuItem;
import id.restoran.util.Rupiah;
import java.util.List;
import org.teavm.jso.dom.html.HTMLElement;
import org.teavm.jso.dom.html.HTMLInputElement;

/**
 * Halaman daftar menu dengan filter masakan, kategori, dan pencarian.
 */
public class MenuView implements View {
    private Cuisine cuisine;
    private Category category;
    private String query = "";
    private HTMLElement grid;
    private HTMLElement cuisineChips;
    private HTMLElement categoryChips;

    @Override
    public String key() {
        return "menu";
    }

    @Override
    public String label() {
        return "Menu";
    }

    @Override
    public void render(HTMLElement container) {
        HTMLElement layout = el("div", "menu-layout");
        HTMLElement left = el("section", "menu-main");

        HTMLElement title = el("div", "section-title");
        title.appendChild(el("h2", null, "Daftar Menu"));
        HTMLInputElement search = (HTMLInputElement) el("input", "search");
        search.setType("search");
        search.setPlaceholder("Cari rendang, ramen, shawarma...");
        search.setValue(query);
        search.onInput(evt -> {
            query = search.getValue();
            renderGrid();
        });
        title.appendChild(search);
        left.appendChild(title);

        cuisineChips = el("div", "chips");
        categoryChips = el("div", "chips chips-sm");
        left.appendChild(cuisineChips);
        left.appendChild(categoryChips);
        renderChips();

        grid = el("div", "menu-grid");
        left.appendChild(grid);
        renderGrid();

        layout.appendChild(left);
        decorate(layout);
        container.appendChild(layout);
    }

    /** Titik ekstensi: subclass/fitur lain dapat menambah panel di samping menu. */
    protected void decorate(HTMLElement layout) {
    }

    /** Titik ekstensi: tombol aksi pada tiap kartu menu. */
    protected HTMLElement actionFor(MenuItem item) {
        return null;
    }

    private void renderChips() {
        Dom.clear(cuisineChips);
        cuisineChips.appendChild(chip("🍽️ Semua", cuisine == null, () -> cuisine = null));
        for (Cuisine c : Cuisine.values()) {
            cuisineChips.appendChild(chip(c.icon() + " " + c.label(), cuisine == c, () -> cuisine = c));
        }
        Dom.clear(categoryChips);
        categoryChips.appendChild(chip("Semua kategori", category == null, () -> category = null));
        for (Category c : Category.values()) {
            categoryChips.appendChild(chip(c.label(), category == c, () -> category = c));
        }
    }

    private HTMLElement chip(String text, boolean active, Runnable select) {
        return Dom.button(text, active ? "chip active" : "chip", () -> {
            select.run();
            renderChips();
            renderGrid();
        });
    }

    private void renderGrid() {
        Dom.clear(grid);
        List<MenuItem> items = MenuCatalog.filter(cuisine, category, query);
        if (items.isEmpty()) {
            grid.appendChild(el("div", "empty", "Menu tidak ditemukan."));
            return;
        }
        for (MenuItem item : items) {
            grid.appendChild(card(item));
        }
    }

    private HTMLElement card(MenuItem item) {
        HTMLElement card = el("article", "menu-card " + item.cuisine().cssClass());
        HTMLElement head = el("div", "menu-card-head");
        head.appendChild(el("span", "menu-emoji", item.emoji()));
        head.appendChild(el("span", "pill", item.cuisine().icon() + " " + item.cuisine().label()));
        card.appendChild(head);

        HTMLElement name = el("h3", "menu-name", item.name());
        if (item.spicy()) {
            name.appendChild(el("span", "spicy", " 🌶️"));
        }
        card.appendChild(name);
        card.appendChild(el("div", "menu-cat", item.category().label()));
        card.appendChild(el("p", "menu-desc", item.description()));

        HTMLElement foot = el("div", "menu-card-foot");
        foot.appendChild(el("strong", "price", Rupiah.format(item.price())));
        HTMLElement action = actionFor(item);
        if (action != null) {
            foot.appendChild(action);
        }
        card.appendChild(foot);
        return card;
    }
}
