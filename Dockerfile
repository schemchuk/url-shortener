# Используем официальный образ JDK 17
FROM openjdk:17-jdk-slim

# Устанавливаем рабочую директорию внутри контейнера
WORKDIR /app

# Устанавливаем Maven
RUN apt-get update && apt-get install -y maven

# Копируем файл pom.xml и директорию с исходными кодами
COPY pom.xml /app/
COPY src /app/src

# Сборка приложения с помощью Maven
RUN mvn clean install -DskipTests

# Копируем собранный JAR файл
COPY target/url-shortener-0.0.1-SNAPSHOT.jar /app/url-shortener-0.0.1-SNAPSHOT.jar

# Порт, на котором приложение будет доступно
EXPOSE 8080

# Запуск Spring Boot приложения
ENTRYPOINT ["java", "-jar", "/app/url-shortener-0.0.1-SNAPSHOT.jar"]
