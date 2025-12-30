# Stage 1: Build the application
FROM gradle:8.5-jdk21 AS build
WORKDIR /app

# Copy gradle files first to cache dependencies
COPY build.gradle settings.gradle ./
COPY src ./src

# Build the application (bootJar creates the executable jar)
RUN gradle clean bootJar -x test

# Stage 2: Run the application
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app

# Copy the built jar from the previous stage
COPY --from=build /app/build/libs/*.jar app.jar

EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]