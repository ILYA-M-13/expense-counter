FROM openjdk:21
# Рабочая директория
WORKDIR /app
COPY .env ./
COPY target/ExpenseCounter-1.0-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "ExpenseCounter-1.0-SNAPSHOT.jar"]