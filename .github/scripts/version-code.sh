#!/usr/bin/env bash
set -euo pipefail

VERSION="${1:-}"
FORM_FACTOR="${2:-phone}"

if ! [[ "$VERSION" =~ ^([0-9]+)\.([0-9]+)\.([0-9]+)$ ]]; then
  echo "::error::Version '$VERSION' is not MAJOR.MINOR.PATCH" >&2
  exit 1
fi

MAJOR=$((10#${BASH_REMATCH[1]}))
MINOR=$((10#${BASH_REMATCH[2]}))
PATCH=$((10#${BASH_REMATCH[3]}))

if [ "$MAJOR" -gt 999 ] || [ "$MINOR" -gt 99 ] || [ "$PATCH" -gt 99 ]; then
  echo "::error::Version '$VERSION' exceeds 999.99.99, so its version code would overlap another field or form factor" >&2
  exit 1
fi

case "$FORM_FACTOR" in
  phone) OFFSET=0 ;;
  tv) OFFSET=10000000 ;;
  *)
    echo "::error::Form factor '$FORM_FACTOR' is not phone or tv" >&2
    exit 1
    ;;
esac

echo $((OFFSET + MAJOR * 10000 + MINOR * 100 + PATCH))
