package id.restoran.ui;

import org.teavm.jso.browser.Window;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Notifikasi singkat di bagian bawah layar.
 */
public final class Toast {
    private Toast() {
    }

    public static void show(String message) {
        HTMLElement toast = Dom.el("div", "toast", message);
        Dom.doc().getBody().appendChild(toast);
        Window.setTimeout(() -> {
            if (toast.getParentNode() != null) {
                toast.getParentNode().removeChild(toast);
            }
        }, 2700);
    }
}
