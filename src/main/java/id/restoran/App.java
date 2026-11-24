package id.restoran;

import static id.restoran.ui.Dom.el;

import id.restoran.ui.Dom;
import id.restoran.service.TicketStore;
import id.restoran.ui.HomeView;
import id.restoran.ui.KitchenBoardView;
import id.restoran.ui.OrderView;
import id.restoran.ui.Styles;
import id.restoran.ui.View;
import java.util.ArrayList;
import java.util.List;
import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Titik masuk aplikasi. TeaVM memanggil {@link #main(String[])} saat halaman dimuat.
 */
public final class App {
    private static final List<View> VIEWS = new ArrayList<>();
    private static final List<HTMLElement> TABS = new ArrayList<>();
    private static HTMLElement nav;
    private static HTMLElement main;
    private static View current;

    private App() {
    }

    public static void main(String[] args) {
        Styles.install();
        registerViews();
        buildShell();
        Window.current().onEvent("hashchange", evt -> showFromHash());
        TicketStore.get().addListener(App::updateBadges);
        // Sinkron antar-tab: kasir dan dapur bisa membuka tab berbeda di browser yang sama.
        Window.current().onEvent("storage", evt -> {
            TicketStore.get().load();
            TicketStore.get().notifyListeners();
            refresh();
        });
        // Perbarui waktu tunggu di papan dapur setiap 30 detik.
        Window.setInterval(() -> {
            if (current != null && "dapur".equals(current.key())) {
                refresh();
            }
        }, 30000);
        showFromHash();
        updateBadges();
    }

    private static void registerViews() {
        VIEWS.add(new HomeView());
        VIEWS.add(new OrderView());
        VIEWS.add(new KitchenBoardView());
    }

    private static void buildShell() {
        HTMLElement body = Dom.doc().getBody();

        HTMLElement header = el("header", "topbar");
        HTMLElement brand = el("div", "brand");
        brand.appendChild(el("span", "brand-logo", "🍽️"));
        HTMLElement brandText = el("div", null);
        brandText.appendChild(el("div", "brand-name", "Restoran Tiga Rasa"));
        brandText.appendChild(el("div", "brand-tag", "Indonesia · Jepang · Arab"));
        brand.appendChild(brandText);
        header.appendChild(brand);

        nav = el("nav", "tabs");
        for (View v : VIEWS) {
            HTMLElement link = el("a", "tab", v.label());
            link.setAttribute("href", "#" + v.key());
            link.setAttribute("data-key", v.key());
            TABS.add(link);
            nav.appendChild(link);
        }
        header.appendChild(nav);
        body.appendChild(header);

        main = el("main", "content");
        body.appendChild(main);

        HTMLElement footer = el("footer", "footer",
                "Dibuat dengan Java ☕ + TeaVM · Tanpa database, data disimpan di browser");
        body.appendChild(footer);
    }

    private static void showFromHash() {
        String hash = Window.current().getLocation().getHash();
        String key = hash == null || hash.length() <= 1 ? VIEWS.get(0).key() : hash.substring(1);
        View target = VIEWS.get(0);
        for (View v : VIEWS) {
            if (v.key().equals(key)) {
                target = v;
            }
        }
        show(target);
    }

    /** Tampilkan view tertentu. */
    public static void show(View view) {
        current = view;
        for (HTMLElement tab : TABS) {
            Dom.toggleClass(tab, "active", view.key().equals(tab.getAttribute("data-key")));
        }
        refresh();
    }

    /** Render ulang view yang sedang aktif. */
    public static void refresh() {
        Dom.clear(main);
        current.render(main);
    }

    /** Perbarui label dan badge di navigasi. */
    public static void updateBadges() {
        for (int i = 0; i < TABS.size(); i++) {
            HTMLElement tab = TABS.get(i);
            View v = VIEWS.get(i);
            tab.setInnerText(v.label());
            String badge = v.badge();
            if (badge != null) {
                tab.appendChild(el("span", "badge", badge));
            }
        }
    }

    /** Pindah ke tab berdasarkan key (mengubah hash URL). */
    public static void navigate(String key) {
        Window.current().getLocation().setHash(key);
    }
}
