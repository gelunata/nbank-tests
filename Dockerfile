FROM maven:3.9-eclipse-temurin-24

# Дефолтовые значния аргументов
ARG TEST_PROFILE=api
ARG APIBASEURL=http://192.168.1.53:4111
ARG UIBASEURL=http://localhost:3000

# Переменные окружения для контейнеров
ENV TEST_PROFILE=${TEST_PROFILE}
ENV APIBASEURL=${APIBASEURL}
ENV UIBASEURL=${UIBASEURL}

# работаем из папки /app
WORKDIR /app

# копируем помник
COPY pom.xml .

# закружаем зависимости
RUN mvn dependency:go-offline

# копирукм весь проект
COPY . .

# теперь внутри есть зависимости, есть весь проект и мы готовы запускать тесты

USER root

# mvn test -p api
# mvn -DskipTests=true surfire report:report
# лог выводился не в консоль, а в файл
CMD mkdir -p /app/logs && \
    { \
        echo ">>> Running tests with profile ${TEST_PROFILE}" ; \
        mvn test -P ${TEST_PROFILE} ; \
        TEST_EXIT_CODE=$PIPESTATUS ; \
        \
        echo ">>> Running surefire-report:report" ; \
        mvn -DskipTests=true surefire-report:report ; \
        \
        exit $TEST_EXIT_CODE ; \
    } 2>&1 | tee /app/logs/run.log ; \
    exit ${PIPESTATUS}