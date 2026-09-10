#!/usr/bin/env bash
set -euo pipefail

THRESHOLD="${COVERAGE_THRESHOLD:-50}"
FILE="swagger-coverage-results.json"

TOTAL=$(jq '.conditionCounter.all' "$FILE")
COVERED=$(jq '.conditionCounter.covered' "$FILE")

echo "Условий: $TOTAL, покрыто: $COVERED, порог: ${THRESHOLD}%"

if (( COVERED * 100 < TOTAL * THRESHOLD )); then
  echo "❌ Quality Gate FAILED"
  exit 1
fi

echo "✅ Quality Gate PASSED"
