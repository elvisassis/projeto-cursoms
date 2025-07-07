FROM maven:3.9.7-eclipse-temurin-21 as build
WORKDIR /app
COPY . .
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jdk
WORKDIR /app
COPY --from=build ./app/target/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=docker"]
