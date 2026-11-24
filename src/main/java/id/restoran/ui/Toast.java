package id.restoran.ui;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Notifikasi singkat di bagian bawah layar.
 */
public final class Toast {
    private Toast() {
    }

    private static HTMLElement last;

    public static void show(String message) {
        if (last != null && last.getParentNode() != null) {
            last.getParentNode().removeChild(last);
        }
        HTMLElement toast = Dom.el("div", "toast", message);
        Dom.doc().getBody().appendChild(toast);
        last = toast;
        Window.setTimeout(() -> {
            if (toast.getParentNode() != null) {
                toast.getParentNode().removeChild(toast);
            }
        }, 2700);
    }
}
