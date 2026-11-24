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
        style.setInnerText(BASE + MENU + ORDER + BOARD);
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

    private static final String MENU = """
            .menu-layout { display: grid; grid-template-columns: 1fr; gap: 24px; align-items: start; }
            .search { flex: 1; max-width: 360px; padding: 10px 14px; border-radius: 999px;
              border: 1px solid var(--line); background: #fff; outline: none; }
            .search:focus { border-color: var(--brand); box-shadow: 0 0 0 3px rgba(180,68,29,.15); }
            .chips { display: flex; flex-wrap: wrap; gap: 8px; margin-bottom: 10px; }
            .chip { border: 1px solid var(--line); background: #fff; color: var(--ink); padding: 7px 14px;
              border-radius: 999px; font-weight: 600; font-size: 14px; }
            .chip:hover { border-color: var(--brand); }
            .chip.active { background: var(--ink); color: #fff; border-color: var(--ink); }
            .chips-sm .chip { padding: 4px 11px; font-size: 13px; font-weight: 500; }
            .chips-sm .chip.active { background: var(--accent); border-color: var(--accent); }
            .menu-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(230px, 1fr)); gap: 16px;
              margin-top: 14px; }
            .menu-card { background: var(--surface); border: 1px solid var(--line); border-radius: var(--radius);
              padding: 16px; display: flex; flex-direction: column; box-shadow: var(--shadow);
              border-top: 4px solid var(--accent); transition: transform .12s; }
            .menu-card:hover { transform: translateY(-2px); }
            .menu-card.cuisine-id { border-top-color: #c0392b; }
            .menu-card.cuisine-jp { border-top-color: #d6457a; }
            .menu-card.cuisine-ar { border-top-color: #1f8a70; }
            .menu-card-head { display: flex; justify-content: space-between; align-items: center; }
            .menu-emoji { font-size: 38px; }
            .pill { font-size: 12px; padding: 3px 9px; border-radius: 999px; background: #f4ece3; color: var(--muted); }
            .menu-name { margin: 10px 0 2px; font-size: 17px; }
            .menu-cat { font-size: 12px; text-transform: uppercase; letter-spacing: .6px; color: var(--accent);
              font-weight: 700; }
            .menu-desc { flex: 1; font-size: 13.5px; color: var(--muted); margin: 8px 0 12px; }
            .menu-card-foot { display: flex; justify-content: space-between; align-items: center; gap: 8px; }
            .price { color: var(--brand-dark); font-size: 16px; }
            """;

    private static final String ORDER = """
            .menu-layout.with-cart { grid-template-columns: minmax(0, 1fr) 370px; }
            @media (max-width: 960px) { .menu-layout.with-cart { grid-template-columns: 1fr; } }
            .cart { position: sticky; top: 90px; background: var(--surface); border: 1px solid var(--line);
              border-radius: var(--radius); padding: 18px; box-shadow: var(--shadow);
              max-height: calc(100vh - 110px); overflow-y: auto; }
            .cart-head { display: flex; justify-content: space-between; align-items: center; margin-bottom: 10px; }
            .cart-head h2 { margin: 0; font-size: 20px; }
            .cart-count { background: var(--accent); color: #fff; border-radius: 999px; padding: 3px 10px;
              font-size: 13px; font-weight: 700; }
            .cart-form { display: grid; grid-template-columns: 1fr 1fr; gap: 10px; margin-bottom: 12px; }
            .field-wrap { display: flex; flex-direction: column; gap: 4px; }
            .field-wrap.full { grid-column: 1 / -1; }
            .field-label { font-size: 12px; font-weight: 700; color: var(--muted); text-transform: uppercase; }
            .field { width: 100%; padding: 9px 11px; border: 1px solid var(--line); border-radius: 9px;
              background: #fffdfa; outline: none; }
            .field:focus { border-color: var(--brand); }
            textarea.field { resize: vertical; min-height: 54px; }
            .cart-lines { border-top: 1px dashed var(--line); border-bottom: 1px dashed var(--line);
              padding: 8px 0; margin-bottom: 10px; }
            .cart-empty { color: var(--muted); font-size: 14px; text-align: center; padding: 16px 4px; }
            .cart-line { display: grid; grid-template-columns: 1fr auto 86px; align-items: center; gap: 8px;
              padding: 7px 0; }
            .cart-line-name { font-weight: 600; font-size: 14px; }
            .cart-line-price { font-size: 12px; color: var(--muted); }
            .cart-line-sub { text-align: right; font-weight: 600; font-size: 14px; }
            .qty { display: flex; align-items: center; gap: 6px; }
            .qty-btn { width: 26px; height: 26px; border-radius: 50%; border: 1px solid var(--line); background: #fff;
              font-weight: 700; line-height: 1; padding: 0; }
            .qty-btn:hover { border-color: var(--brand); color: var(--brand); }
            .qty-val { min-width: 18px; text-align: center; font-weight: 700; }
            .cart-summary { margin-bottom: 12px; }
            .sum-row { display: flex; justify-content: space-between; font-size: 14px; padding: 3px 0; color: var(--muted); }
            .sum-row.total { font-size: 18px; font-weight: 800; color: var(--ink); padding-top: 6px; }
            .btn-block { width: 100%; padding: 12px; font-size: 15px; }
            """;

    private static final String BOARD = """
            .title-actions { display: flex; gap: 8px; }
            .board { display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 16px; align-items: start; }
            @media (max-width: 900px) { .board { grid-template-columns: 1fr; } }
            .column { background: #f3ebe1; border-radius: var(--radius); padding: 12px; min-height: 200px;
              border-top: 5px solid var(--muted); }
            .column.st-wait { border-top-color: var(--warn); }
            .column.st-cook { border-top-color: var(--danger); }
            .column.st-ready { border-top-color: var(--ok); }
            .column-head { display: flex; justify-content: space-between; align-items: center; font-weight: 800;
              margin: 2px 4px 12px; }
            .column-count { background: #fff; border-radius: 999px; padding: 1px 10px; font-size: 13px; }
            .column-empty { text-align: center; color: var(--muted); font-size: 14px; padding: 26px 0; }
            .ticket { background: var(--surface); border-radius: 12px; padding: 12px 14px; margin-bottom: 12px;
              box-shadow: var(--shadow); border-left: 4px solid transparent; }
            .ticket.late { border-left-color: var(--danger); }
            .ticket-head { display: flex; justify-content: space-between; align-items: center; }
            .ticket-code { font-family: ui-monospace, Consolas, monospace; font-size: 17px; }
            .ticket-table { background: var(--ink); color: #fff; border-radius: 8px; padding: 2px 9px;
              font-size: 13px; font-weight: 700; }
            .ticket-meta { display: flex; justify-content: space-between; gap: 6px; font-size: 13px;
              color: var(--muted); margin: 6px 0; flex-wrap: wrap; }
            .wait.late { color: var(--danger); font-weight: 700; }
            .ticket-items { list-style: none; padding: 0; margin: 8px 0; border-top: 1px dashed var(--line); }
            .ticket-items li { display: flex; gap: 8px; padding: 5px 0; font-size: 14.5px; border-bottom: 1px dashed var(--line); }
            .ticket-qty { font-weight: 800; color: var(--brand); min-width: 26px; }
            .ticket-cuisine { margin-left: auto; }
            .ticket-notes { background: #fff6df; border-radius: 8px; padding: 6px 9px; font-size: 13px; margin: 6px 0; }
            .ticket-foot { display: flex; justify-content: space-between; align-items: center; gap: 8px;
              flex-wrap: wrap; margin-top: 8px; }
            .ticket-total { font-weight: 700; font-size: 14px; }
            .ticket-btns { display: flex; gap: 6px; }
            .history { margin-top: 28px; }
            .history h3 { margin: 0 0 10px; }
            .history-table { width: 100%; border-collapse: collapse; background: var(--surface);
              border-radius: var(--radius); overflow: hidden; box-shadow: var(--shadow); font-size: 14px; }
            .history-table th, .history-table td { text-align: left; padding: 9px 12px; border-bottom: 1px solid var(--line); }
            .history-table th { background: #f3ebe1; font-size: 12px; text-transform: uppercase; color: var(--muted); }
            .mono { font-family: ui-monospace, Consolas, monospace; font-weight: 700; }
            .status-pill { border-radius: 999px; padding: 2px 10px; font-size: 12.5px; font-weight: 700; }
            .status-pill.st-done { background: #e2f3e9; color: var(--ok); }
            .status-pill.st-cancel { background: #f8e1df; color: var(--danger); }
            .status-pill.st-wait { background: #fbead7; color: var(--warn); }
            .status-pill.st-cook { background: #f8e1df; color: var(--danger); }
            .status-pill.st-ready { background: #e2f3e9; color: var(--ok); }
            """;
}
