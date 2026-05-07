#!/bin/bash
export MSYS_NO_PATHCONV=1

# настройка
IMAGE_NAME=nbank-tests
TEST_PROFILE=${1:-api} # аргумент запуска
TIMESTAMP=$(date +"%Y%m%d_%H%M")

WIN_PWD=$(pwd -W)
TEST_OUTPUT_DIR="$WIN_PWD/test-output/$TIMESTAMP"

# собираем Docker образ
echo ">>> Сборка тестов запущена"
docker build -t $IMAGE_NAME .

mkdir -p "$TEST_OUTPUT_DIR/logs"
mkdir -p "$TEST_OUTPUT_DIR/results"
mkdir -p "$TEST_OUTPUT_DIR/report"

# запуск Docker контейнера
echo ">>> Тесты запущены"
docker run --rm \
  -v "$TEST_OUTPUT_DIR/logs:/app/logs" \
  -v "$TEST_OUTPUT_DIR/results:/app/target/surefire-reports" \
  -v "$TEST_OUTPUT_DIR/report:/app/target/site" \
  -e TEST_PROFILE="$TEST_PROFILE" \
  -e APIBASEURL=http://192.168.1.53:4111 \
  -e UIBASEURL=http://localhost:3000 \
$IMAGE_NAME

# Вывод итогов
echo ">>> Тесты завершены"
echo "Лог файл: $TEST_OUTPUT_DIR/logs/run.log"
echo "Результаты тестов: $TEST_OUTPUT_DIR/results"
echo "Репорт: $TEST_OUTPUT_DIR/report"