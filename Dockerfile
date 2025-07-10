FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080

ENV SPRING_DATA_MONGODB_URI="mongodb://mongo-db:27017/examen_db"
ENTRYPOINT ["java", "-jar", "app.jar"]