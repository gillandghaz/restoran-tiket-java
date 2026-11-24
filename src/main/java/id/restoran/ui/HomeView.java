package id.restoran.ui;

import static id.restoran.ui.Dom.el;

import org.teavm.jso.dom.html.HTMLElement;

/**
 * Halaman sambutan.
 */
public class HomeView implements View {
    @Override
    public String key() {
        return "beranda";
    }

    @Override
    public String label() {
        return "Beranda";
    }

    @Override
    public void render(HTMLElement container) {
        HTMLElement hero = el("section", "hero");
        hero.appendChild(el("h1", "hero-title", "Selamat datang di Restoran Tiga Rasa"));
        hero.appendChild(el("p", "hero-sub",
                "Masakan khas Indonesia, Jepang, dan Arab dalam satu dapur. "
                        + "Kelola menu, pesanan, dan tiket dapur langsung dari browser."));

        HTMLElement grid = el("div", "feature-grid");
        grid.appendChild(card("🍛", "Nusantara", "Rendang, sate, nasi goreng, dan cita rasa rempah Indonesia."));
        grid.appendChild(card("🍣", "Jepang", "Ramen, sushi, tempura, dan hidangan khas negeri sakura."));
        grid.appendChild(card("🥙", "Arab", "Nasi kebuli, mandi, shawarma, dan kehangatan Timur Tengah."));
        hero.appendChild(grid);

        HTMLElement note = el("p", "hero-note",
                "Aplikasi ini 100% ditulis dalam Java dan dikompilasi ke JavaScript dengan TeaVM. "
                        + "Tanpa database & tanpa server — data tersimpan di browser Anda.");
        hero.appendChild(note);
        container.appendChild(hero);
    }

    private HTMLElement card(String icon, String title, String desc) {
        HTMLElement c = el("div", "feature-card");
        c.appendChild(el("div", "feature-icon", icon));
        c.appendChild(el("h3", null, title));
        c.appendChild(el("p", null, desc));
        return c;
    }
}
