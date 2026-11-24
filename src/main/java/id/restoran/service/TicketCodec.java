package id.restoran.service;

import id.restoran.data.MenuCatalog;
import id.restoran.model.MenuItem;
import id.restoran.model.OrderLine;
import id.restoran.model.Ticket;
import id.restoran.model.TicketStatus;
import java.util.ArrayList;
import java.util.List;
import org.teavm.jso.browser.Window;

/**
 * Serialisasi tiket ke teks sederhana untuk disimpan di localStorage.
 *
 * <pre>
 * v1
 * next=12
 * nomor|status|createdAt|updatedAt|pelanggan|meja|catatan|ID01*2,JP03*1
 * </pre>
 * Setiap field teks di-encode dengan encodeURIComponent agar aman dari pemisah.
 */
public final class TicketCodec {
    private static final String VERSION = "v1";

    private TicketCodec() {
    }

    public static String encode(List<Ticket> tickets, int nextNumber) {
        StringBuilder sb = new StringBuilder();
        sb.append(VERSION).append('\n');
        sb.append("next=").append(nextNumber).append('\n');
        for (Ticket t : tickets) {
            sb.append(t.number()).append('|')
                    .append(t.status().name()).append('|')
                    .append((long) t.createdAt()).append('|')
                    .append((long) t.updatedAt()).append('|')
                    .append(enc(t.customer())).append('|')
                    .append(enc(t.table())).append('|')
                    .append(enc(t.notes())).append('|');
            for (int i = 0; i < t.lines().size(); i++) {
                OrderLine line = t.lines().get(i);
                if (i > 0) {
                    sb.append(',');
                }
                sb.append(line.item().id()).append('*').append(line.quantity());
            }
            sb.append('\n');
        }
        return sb.toString();
    }

    /** Hasil decode: daftar tiket dan nomor berikutnya. */
    public static final class Result {
        public final List<Ticket> tickets = new ArrayList<>();
        public int nextNumber = 1;
    }

    public static Result decode(String text) {
        Result result = new Result();
        if (text == null || !text.startsWith(VERSION)) {
            return result;
        }
        String[] rows = text.split("\n");
        for (int r = 1; r < rows.length; r++) {
            String row = rows[r];
            if (row.isEmpty()) {
                continue;
            }
            if (row.startsWith("next=")) {
                result.nextNumber = Integer.parseInt(row.substring(5));
                continue;
            }
            try {
                Ticket t = parseTicket(row);
                if (t != null) {
                    result.tickets.add(t);
                    result.nextNumber = Math.max(result.nextNumber, t.number() + 1);
                }
            } catch (RuntimeException e) {
                // Baris rusak dilewati agar aplikasi tetap berjalan.
            }
        }
        return result;
    }

    private static Ticket parseTicket(String row) {
        String[] f = row.split("\\|", -1);
        if (f.length < 8) {
            return null;
        }
        List<OrderLine> lines = new ArrayList<>();
        if (!f[7].isEmpty()) {
            for (String part : f[7].split(",")) {
                String[] kv = part.split("\\*");
                MenuItem item = MenuCatalog.byId(kv[0]);
                if (item != null) {
                    lines.add(new OrderLine(item, Integer.parseInt(kv[1])));
                }
            }
        }
        return new Ticket(Integer.parseInt(f[0]), dec(f[4]), dec(f[5]), dec(f[6]), lines,
                TicketStatus.valueOf(f[1]), Double.parseDouble(f[2]), Double.parseDouble(f[3]));
    }

    private static String enc(String s) {
        return Window.encodeURIComponent(s == null ? "" : s);
    }

    private static String dec(String s) {
        return Window.decodeURIComponent(s);
    }
}
