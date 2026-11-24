#!/usr/bin/env bash
# Build script untuk Vercel: memasang JDK portabel, mengompilasi Java -> JavaScript
# lewat TeaVM, lalu menyalin hasilnya ke folder `dist` (static output).
set -euo pipefail

# Image build Vercel tidak punya locale en_US.UTF-8; pakai locale C agar log bersih.
export LANG=C LC_ALL=C

ROOT="$(cd "$(dirname "$0")" && pwd)"
JDK_DIR="$ROOT/.jdk"

if ! command -v java >/dev/null 2>&1 || ! java -version 2>&1 | grep -qE 'version "(2[1-9]|[3-9][0-9])'; then
  if [ ! -x "$JDK_DIR/bin/java" ]; then
    echo ">> Mengunduh Eclipse Temurin JDK 21..."
    mkdir -p "$JDK_DIR"
    curl -fsSL "https://api.adoptium.net/v3/binary/latest/21/ga/linux/x64/jdk/hotspot/normal/eclipse" \
      | tar -xz -C "$JDK_DIR" --strip-components=1
  fi
  export JAVA_HOME="$JDK_DIR"
  export PATH="$JAVA_HOME/bin:$PATH"
fi

java -version
chmod +x "$ROOT/mvnw"
"$ROOT/mvnw" -B -q -f "$ROOT/pom.xml" clean package

rm -rf "$ROOT/dist"
mkdir -p "$ROOT/dist"
cp -r "$ROOT/src/main/webapp/." "$ROOT/dist/"
cp -r "$ROOT/target/generated/js/." "$ROOT/dist/"
echo ">> Build selesai: $(ls -1 "$ROOT/dist" | tr '\n' ' ')"
