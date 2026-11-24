# Restoran Tiga Rasa — Pengelola Tiket (Java di Browser)

Website pengelola tiket/pesanan restoran dengan menu khas **Indonesia**, **Jepang**, dan **Arab**.
Seluruh UI dan logika ditulis dalam **Java**, lalu dikompilasi menjadi JavaScript oleh
[TeaVM](https://teavm.org) sehingga berjalan langsung di browser — **tanpa database dan tanpa server**.
Data pesanan disimpan di `localStorage` browser.

## Fitur

| Fitur | Keterangan |
| --- | --- |
| Menu | Daftar masakan tiga negara, filter per masakan & pencarian |
| Pesanan | Keranjang, nomor meja, nama pelanggan, catatan → terbit tiket |
| Tiket Dapur | Papan status: Menunggu → Dimasak → Siap → Selesai / Batal |
| Laporan | Ringkasan omzet, menu terlaris, dan struk tiket |

## Struktur

```
src/main/java/id/restoran/   # semua kode Java
src/main/webapp/index.html   # cangkang HTML minimal yang memuat hasil kompilasi
build.sh                     # dipakai Vercel: pasang JDK 21, jalankan Maven + TeaVM
vercel.json                  # konfigurasi build & output (dist/)
```

## Menjalankan lokal

Butuh JDK 21+.

```bash
./mvnw clean package
# hasil: target/generated/js/teavm/classes.js
bash build.sh            # menyusun folder dist/ siap disajikan
cd dist && python -m http.server 8080
```

## Deploy ke Vercel

Import repo ini di Vercel — `vercel.json` sudah mengatur `buildCommand: bash build.sh`
dan `outputDirectory: dist`. Build script mengunduh JDK Temurin 21 secara otomatis
karena image build Vercel tidak menyertakan Java.
