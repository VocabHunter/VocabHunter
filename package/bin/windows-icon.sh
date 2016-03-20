#!/bin/bash

set -e

SOURCE_FILE=$1
DESTINATION_DIR=$2
NAME=$3

DESTINATION_FILE="$DESTINATION_DIR/$NAME".ico

mkdir -p "$DESTINATION_DIR"

convert "$SOURCE_FILE" \( -clone 0 -resize 16x16 \) \( -clone 0 -resize 24x24 \) \( -clone 0 -resize 32x32 \) \( -clone 0 -resize 40x40 \) \( -clone 0 -resize 48x48 \) \( -clone 0 -resize 64x64 \) \( -clone 0 -resize 256x256 \) -delete 0 "$DESTINATION_FILE"
