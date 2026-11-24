package id.restoran.util;

import org.teavm.jso.core.JSDate;

/**
 * Format waktu sederhana berbasis Date milik browser.
 */
public final class Clock {
    private static final String[] MONTHS = {"Jan", "Feb", "Mar", "Apr", "Mei", "Jun", "Jul", "Agu", "Sep", "Okt",
        "Nov", "Des"};

    private Clock() {
    }

    public static String time(double millis) {
        JSDate d = JSDate.create(millis);
        return pad(d.getHours()) + ":" + pad(d.getMinutes());
    }

    public static String dateTime(double millis) {
        JSDate d = JSDate.create(millis);
        return d.getDate() + " " + MONTHS[d.getMonth()] + " " + d.getFullYear() + ", " + time(millis);
    }

    /** Selisih menit dari waktu tertentu hingga sekarang. */
    public static long minutesSince(double millis) {
        return (long) ((System.currentTimeMillis() - millis) / 60000);
    }

    private static String pad(int v) {
        return v < 10 ? "0" + v : Integer.toString(v);
    }
}
