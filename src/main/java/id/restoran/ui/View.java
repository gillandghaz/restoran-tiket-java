package id.restoran.ui;

import org.teavm.jso.dom.html.HTMLElement;

/**
 * Satu halaman/tab di aplikasi.
 */
public interface View {
    /** Kunci unik tab, dipakai juga sebagai hash URL (#menu, #tiket, ...). */
    String key();

    /** Label yang tampil di navigasi. */
    String label();

    /** Bangun ulang isi halaman ke dalam container. */
    void render(HTMLElement container);

    /** Angka kecil di samping label navigasi; null jika tidak ada. */
    default String badge() {
        return null;
    }
}
