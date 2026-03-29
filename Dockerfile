# -----------
# Build Stage
# -----------
FROM eclipse-temurin:17-jdk-jammy AS build
WORKDIR /app

# Copy maven wrapper and pom.xml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./

# Convert Windows line endings (CRLF) to Unix line endings (LF) for the wrapper script, just in case
RUN sed -i 's/\r$//' mvnw

# Download dependencies
RUN ./mvnw dependency:go-offline

# Copy the actual source code
COPY src ./src

# Build the jar file
RUN ./mvnw package -DskipTests

# -----------
# Run Stage
# -----------
FROM eclipse-temurin:17-jre-jammy
WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/*.jar app.jar

# Expose port (Render automatically assigns PORT env variable, our properties handles this)
EXPOSE 8080

# Command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]
