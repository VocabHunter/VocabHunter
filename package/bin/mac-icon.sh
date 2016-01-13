#!/bin/bash

set -e

SOURCE_FILE=$1
DESTINATION_DIR=$2
NAME=$3

SCRATCH="$DESTINATION_DIR/$NAME".iconset

mkdir -p "$SCRATCH"
sips -z 16 16     "$SOURCE_FILE" --out "$SCRATCH"/icon_16x16.png >/dev/null
sips -z 32 32     "$SOURCE_FILE" --out "$SCRATCH"/icon_16x16@2x.png >/dev/null
sips -z 32 32     "$SOURCE_FILE" --out "$SCRATCH"/icon_32x32.png >/dev/null
sips -z 64 64     "$SOURCE_FILE" --out "$SCRATCH"/icon_32x32@2x.png >/dev/null
sips -z 128 128   "$SOURCE_FILE" --out "$SCRATCH"/icon_128x128.png >/dev/null
sips -z 256 256   "$SOURCE_FILE" --out "$SCRATCH"/icon_128x128@2x.png >/dev/null
sips -z 256 256   "$SOURCE_FILE" --out "$SCRATCH"/icon_256x256.png >/dev/null
sips -z 512 512   "$SOURCE_FILE" --out "$SCRATCH"/icon_256x256@2x.png >/dev/null
sips -z 512 512   "$SOURCE_FILE" --out "$SCRATCH"/icon_512x512.png >/dev/null
cp "$SOURCE_FILE" "$SCRATCH"/icon_512x512@2x.png
iconutil -c icns "$SCRATCH"
rm -R "$SCRATCH"