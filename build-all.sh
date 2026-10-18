#!/usr/bin/env bash
set -euo pipefail
cd "$(dirname "$0")"
for m in hello-mod hud-clock block-counter mod-template; do
  echo "=== $m ==="
  ./common-build.sh "$PWD/$m" "$m.jar"
done
echo "All example mods compiled into dist/."
