package id.restoran.data;

import static id.restoran.model.Category.MINUMAN;
import static id.restoran.model.Category.PEMBUKA;
import static id.restoran.model.Category.PENUTUP;
import static id.restoran.model.Category.UTAMA;
import static id.restoran.model.Cuisine.ARAB;
import static id.restoran.model.Cuisine.INDONESIA;
import static id.restoran.model.Cuisine.JEPANG;

import id.restoran.model.Category;
import id.restoran.model.Cuisine;
import id.restoran.model.MenuItem;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Daftar menu restoran. Data statis di dalam kode — tidak memakai database.
 */
public final class MenuCatalog {
    private static final List<MenuItem> ITEMS = new ArrayList<>();

    static {
        // ---------- Indonesia ----------
        add("ID01", "Rendang Sapi", INDONESIA, UTAMA, 55000,
                "Daging sapi dimasak perlahan dengan santan dan rempah Minang.", "🥩", true);
        add("ID02", "Nasi Goreng Kampung", INDONESIA, UTAMA, 35000,
                "Nasi goreng terasi, telur mata sapi, kerupuk, dan acar.", "🍳", true);
        add("ID03", "Sate Ayam Madura", INDONESIA, UTAMA, 38000,
                "10 tusuk sate ayam dengan bumbu kacang dan lontong.", "🍢", false);
        add("ID04", "Soto Betawi", INDONESIA, UTAMA, 42000,
                "Kuah santan susu dengan daging sapi, kentang, dan emping.", "🍲", false);
        add("ID05", "Gado-Gado", INDONESIA, PEMBUKA, 28000,
                "Sayuran rebus, tahu, tempe, dan telur dengan saus kacang.", "🥗", false);
        add("ID06", "Ayam Betutu", INDONESIA, UTAMA, 52000,
                "Ayam khas Bali berbumbu base genep, dibakar dalam daun pisang.", "🍗", true);
        add("ID07", "Klepon", INDONESIA, PENUTUP, 18000,
                "Bola ketan pandan isi gula merah dengan taburan kelapa.", "🟢", false);
        add("ID08", "Es Cendol", INDONESIA, MINUMAN, 20000,
                "Cendol hijau, santan, dan gula aren dingin.", "🥤", false);
        add("ID09", "Es Teh Manis", INDONESIA, MINUMAN, 10000,
                "Teh melati manis dengan es batu.", "🧋", false);

        // ---------- Jepang ----------
        add("JP01", "Tonkotsu Ramen", JEPANG, UTAMA, 68000,
                "Kuah kaldu tulang babi kental, chashu, ajitama, dan nori.", "🍜", false);
        add("JP02", "Sushi Moriawase", JEPANG, UTAMA, 85000,
                "Aneka 8 potong nigiri: salmon, tuna, ebi, dan tamago.", "🍣", false);
        add("JP03", "Chicken Katsu Curry", JEPANG, UTAMA, 58000,
                "Ayam goreng tepung panko dengan kari Jepang dan nasi.", "🍛", false);
        add("JP04", "Ebi Tempura", JEPANG, PEMBUKA, 45000,
                "Udang goreng tepung renyah dengan saus tentsuyu.", "🍤", false);
        add("JP05", "Takoyaki", JEPANG, PEMBUKA, 32000,
                "6 bola gurita dengan saus takoyaki, mayones, dan katsuobushi.", "🐙", false);
        add("JP06", "Gyudon", JEPANG, UTAMA, 55000,
                "Irisan daging sapi manis gurih di atas nasi hangat.", "🍚", false);
        add("JP07", "Spicy Miso Udon", JEPANG, UTAMA, 52000,
                "Udon tebal dengan kuah miso pedas dan ayam.", "🍲", true);
        add("JP08", "Matcha Mochi", JEPANG, PENUTUP, 25000,
                "Mochi lembut isi krim matcha.", "🍡", false);
        add("JP09", "Ocha Dingin", JEPANG, MINUMAN, 12000,
                "Teh hijau Jepang tanpa gula.", "🍵", false);

        // ---------- Arab ----------
        add("AR01", "Nasi Kebuli Kambing", ARAB, UTAMA, 65000,
                "Nasi berempah dengan daging kambing empuk dan kismis.", "🍖", false);
        add("AR02", "Nasi Mandi Ayam", ARAB, UTAMA, 58000,
                "Nasi khas Yaman dengan ayam panggang asap dan sambal tomat.", "🍗", false);
        add("AR03", "Shawarma Daging", ARAB, UTAMA, 45000,
                "Roti pita isi daging sapi panggang, sayur, dan saus garlic.", "🌯", false);
        add("AR04", "Hummus & Roti Pita", ARAB, PEMBUKA, 30000,
                "Pasta kacang arab dengan minyak zaitun dan roti pita hangat.", "🫓", false);
        add("AR05", "Falafel", ARAB, PEMBUKA, 32000,
                "6 bola kacang arab goreng dengan saus tahini.", "🧆", false);
        add("AR06", "Kabsah Ayam Pedas", ARAB, UTAMA, 60000,
                "Nasi kabsah Saudi dengan ayam berbumbu pedas.", "🌶️", true);
        add("AR07", "Kunafa", ARAB, PENUTUP, 38000,
                "Pastry kataifi renyah isi keju dengan sirup mawar.", "🧁", false);
        add("AR08", "Teh Arab (Shai)", ARAB, MINUMAN, 15000,
                "Teh hitam dengan kapulaga dan mint.", "🫖", false);
        add("AR09", "Qahwa Arabiya", ARAB, MINUMAN, 22000,
                "Kopi Arab ringan dengan kapulaga, disajikan dengan kurma.", "☕", false);
    }

    private MenuCatalog() {
    }

    private static void add(String id, String name, Cuisine cuisine, Category category, long price,
            String desc, String emoji, boolean spicy) {
        ITEMS.add(new MenuItem(id, name, cuisine, category, price, desc, emoji, spicy));
    }

    public static List<MenuItem> all() {
        return Collections.unmodifiableList(ITEMS);
    }

    public static MenuItem byId(String id) {
        for (MenuItem item : ITEMS) {
            if (item.id().equals(id)) {
                return item;
            }
        }
        return null;
    }

    /** Filter menu berdasarkan masakan (null = semua) dan kata kunci. */
    public static List<MenuItem> filter(Cuisine cuisine, Category category, String query) {
        List<MenuItem> result = new ArrayList<>();
        for (MenuItem item : ITEMS) {
            if ((cuisine == null || item.cuisine() == cuisine)
                    && (category == null || item.category() == category)
                    && item.matches(query)) {
                result.add(item);
            }
        }
        return result;
    }
}
