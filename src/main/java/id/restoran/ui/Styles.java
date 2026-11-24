package id.restoran.ui;

import org.teavm.jso.dom.html.HTMLElement;

/**
 * Stylesheet aplikasi, ditulis sebagai Java text block lalu disuntikkan ke &lt;head&gt;.
 */
public final class Styles {
    private Styles() {
    }

    public static void install() {
        HTMLElement style = Dom.el("style");
        style.setInnerText(BASE);
        Dom.doc().getHead().appendChild(style);
    }

    private static final String BASE = """
            :root {
              --bg: #faf6f0; --surface: #ffffff; --ink: #2b2118; --muted: #7a6a5b;
              --line: #eadfd2; --brand: #b4441d; --brand-dark: #8a3014; --accent: #e8a33d;
              --ok: #2f8f5b; --warn: #d9822b; --info: #2f6f9f; --danger: #b8322a;
              --radius: 14px; --shadow: 0 6px 20px rgba(60, 35, 15, .08);
            }
            * { box-sizing: border-box; }
            body { margin: 0; font-family: "Segoe UI", system-ui, -apple-system, Roboto, sans-serif;
              background: var(--bg); color: var(--ink); min-height: 100vh; display: flex; flex-direction: column; }
            button { font: inherit; cursor: pointer; }
            input, select, textarea { font: inherit; }
            .topbar { position: sticky; top: 0; z-index: 10; display: flex; align-items: center;
              justify-content: space-between; gap: 16px; flex-wrap: wrap; padding: 12px 24px;
              background: linear-gradient(90deg, var(--brand-dark), var(--brand)); color: #fff;
              box-shadow: 0 2px 12px rgba(0,0,0,.15); }
            .brand { display: flex; align-items: center; gap: 12px; }
            .brand-logo { font-size: 30px; }
            .brand-name { font-weight: 700; font-size: 20px; letter-spacing: .3px; }
            .brand-tag { font-size: 12px; opacity: .85; }
            .tabs { display: flex; gap: 6px; flex-wrap: wrap; }
            .tab { color: #fff; text-decoration: none; padding: 8px 16px; border-radius: 999px;
              font-weight: 600; font-size: 14px; opacity: .85; transition: background .15s; }
            .tab:hover { background: rgba(255,255,255,.15); opacity: 1; }
            .tab.active { background: #fff; color: var(--brand-dark); opacity: 1; }
            .tab .badge { display: inline-block; min-width: 20px; margin-left: 6px; padding: 0 6px;
              border-radius: 999px; background: var(--accent); color: #fff; font-size: 12px; text-align: center; }
            .content { flex: 1; width: 100%; max-width: 1280px; margin: 0 auto; padding: 24px; }
            .footer { text-align: center; padding: 18px; font-size: 13px; color: var(--muted); }
            .hero { text-align: center; padding: 32px 8px; }
            .hero-title { font-size: 34px; margin: 0 0 8px; color: var(--brand-dark); }
            .hero-sub { font-size: 17px; color: var(--muted); max-width: 640px; margin: 0 auto 28px; }
            .hero-note { margin-top: 28px; font-size: 14px; color: var(--muted); }
            .feature-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(220px, 1fr)); gap: 18px; }
            .feature-card { background: var(--surface); border: 1px solid var(--line); border-radius: var(--radius);
              padding: 22px; box-shadow: var(--shadow); }
            .feature-card h3 { margin: 8px 0 6px; }
            .feature-card p { margin: 0; color: var(--muted); font-size: 14px; }
            .feature-icon { font-size: 40px; }
            .btn { border: none; border-radius: 10px; padding: 9px 16px; font-weight: 600;
              background: var(--brand); color: #fff; transition: transform .05s, background .15s; }
            .btn:hover { background: var(--brand-dark); }
            .btn:active { transform: scale(.97); }
            .btn:disabled { background: #cbbfb3; cursor: not-allowed; }
            .btn-ghost { background: transparent; color: var(--brand); border: 1px solid var(--line); }
            .btn-ghost:hover { background: #fff3ea; }
            .btn-sm { padding: 5px 10px; font-size: 13px; border-radius: 8px; }
            .section-title { display: flex; align-items: center; justify-content: space-between;
              gap: 12px; flex-wrap: wrap; margin: 0 0 16px; }
            .section-title h2 { margin: 0; }
            .empty { text-align: center; color: var(--muted); padding: 32px; border: 2px dashed var(--line);
              border-radius: var(--radius); }
            .toast { position: fixed; bottom: 24px; left: 50%; transform: translateX(-50%); background: var(--ink);
              color: #fff; padding: 12px 20px; border-radius: 10px; box-shadow: var(--shadow); z-index: 50;
              animation: fade 2.6s forwards; }
            @keyframes fade { 0% { opacity: 0; } 10% { opacity: 1; } 85% { opacity: 1; } 100% { opacity: 0; } }
            """;
}
