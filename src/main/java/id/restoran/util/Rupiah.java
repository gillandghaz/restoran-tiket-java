package id.restoran.util;

/**
 * Format angka ke mata uang Rupiah, mis. 45000 -&gt; "Rp 45.000".
 */
public final class Rupiah {
    private Rupiah() {
    }

    public static String format(long amount) {
        boolean negative = amount < 0;
        String digits = Long.toString(Math.abs(amount));
        StringBuilder sb = new StringBuilder();
        int count = 0;
        for (int i = digits.length() - 1; i >= 0; i--) {
            sb.append(digits.charAt(i));
            if (++count % 3 == 0 && i > 0) {
                sb.append('.');
            }
        }
        return (negative ? "-Rp " : "Rp ") + sb.reverse();
    }
}
