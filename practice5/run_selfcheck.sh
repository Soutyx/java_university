#!/bin/sh
set -eu
cd "$(dirname "$0")"
mkdir -p out
javac --release 17 -encoding UTF-8 -d out src/ru/edu/pr02/*.java
java -Dfile.encoding=UTF-8 -Dstdout.encoding=UTF-8 -Dstderr.encoding=UTF-8 -cp out ru.edu.pr02.SelfCheck
