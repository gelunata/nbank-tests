#!/usr/bin/env bash
set -euo pipefail

THRESHOLD="${COVERAGE_THRESHOLD:-50}"
FILE="swagger-coverage-results.json"

TOTAL=$(jq '[.[]] | length' "$FILE")
COVERED=$(jq '[.[] | select(.state == "FULL" or .state == "PARTY")] | length' "$FILE")
COVERAGE=$(echo "scale=2; $COVERED * 100 / $TOTAL" | bc -l)

echo "Покрытие API: ${COVERAGE}% (порог: ${THRESHOLD}%)"

if (( $(echo "$COVERAGE < $THRESHOLD" | bc -l) )); then
  echo "❌ Quality Gate FAILED"
  exit 1
fi
