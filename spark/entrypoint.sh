#!/usr/bin/env bash

set -euo pipefail
[ -n "${DEBUG:-}" ] && set -x

cd /opt/spark

tail -f /dev/null
exec $@