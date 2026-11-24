package id.restoran.ui;

import org.teavm.jso.dom.html.HTMLDocument;
import org.teavm.jso.dom.html.HTMLElement;

/**
 * Helper kecil untuk membangun elemen DOM langsung dari Java.
 */
public final class Dom {
    private Dom() {
    }

    public static HTMLDocument doc() {
        return HTMLDocument.current();
    }

    public static HTMLElement el(String tag) {
        return doc().createElement(tag);
    }

    public static HTMLElement el(String tag, String className) {
        HTMLElement e = el(tag);
        if (className != null && !className.isEmpty()) {
            e.setClassName(className);
        }
        return e;
    }

    public static HTMLElement el(String tag, String className, String text) {
        HTMLElement e = el(tag, className);
        if (text != null) {
            e.setInnerText(text);
        }
        return e;
    }

    public static HTMLElement byId(String id) {
        return doc().getElementById(id);
    }

    public static HTMLElement button(String text, String className, Runnable onClick) {
        HTMLElement b = el("button", className, text);
        b.setAttribute("type", "button");
        b.onClick(evt -> onClick.run());
        return b;
    }

    public static void clear(HTMLElement e) {
        while (e.getFirstChild() != null) {
            e.removeChild(e.getFirstChild());
        }
    }

    public static void toggleClass(HTMLElement e, String cls, boolean on) {
        if (on) {
            e.getClassList().add(cls);
        } else {
            e.getClassList().remove(cls);
        }
    }
}
