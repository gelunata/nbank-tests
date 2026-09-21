#!/usr/bin/env bash
set -euo pipefail

THRESHOLD="${COVERAGE_THRESHOLD:-50}"
FILE="swagger-coverage-results.json"

TOTAL=$(jq '.conditionCounter.all' "$FILE")
COVERED=$(jq '.conditionCounter.covered' "$FILE")
UNCOVERED=$((TOTAL - COVERED))
PERCENT=$(awk "BEGIN {printf \"%.1f\", ($COVERED * 100) / ($TOTAL == 0 ? 1 : $TOTAL)}")

echo "=== API Coverage Summary ==="
echo "Всего условий: $TOTAL"
echo "Покрыто: $COVERED"
echo "Не покрыто: $UNCOVERED"
echo "Покрытие: ${PERCENT}%"
echo "Порог: ${THRESHOLD}%"

# Если порог не пройден — выводим список операций, где есть uncovered условия
if (( COVERED * 100 < TOTAL * THRESHOLD )); then
  echo ""
  echo "=== Операции с недостающим покрытием ==="
  jq -r '
    .operations | to_entries[] |
    select(.value.coveredConditionCount < .value.allConditionCount) |
    "\(.key) | State: \(.value.state) | Covered: \(.value.coveredConditionCount)/\(.value.allConditionCount)"
  ' "$FILE" || true

  echo ""
  echo "❌ Quality Gate FAILED"
  exit 1
else
  echo ""
  echo "✅ Quality Gate PASSED"
fi
