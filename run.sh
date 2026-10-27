#!/bin/sh
set -eu
cd "$(dirname "$0")"
entry="${1:-Main}"
if [ "$#" -gt 0 ]; then shift; fi
case "$entry" in Main|SelfCheck|BoundaryTests) ;; *) echo "Unknown entry: $entry" >&2; exit 2;; esac
mkdir -p out
find src test -name '*.java' > out/sources.txt
javac --release 17 -encoding UTF-8 -d out @out/sources.txt
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp out "ru.edu.pr02.$entry" "$@"
