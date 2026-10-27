#!/bin/sh
set -eu
cd "$(dirname "$0")"
sh run.sh SelfCheck
sh run.sh BoundaryTests
