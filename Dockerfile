# ---- Stage 1: Build backend ----
FROM gradle:8.14.3-alpine AS backend-builder
WORKDIR /home/gradle

# Copy project config
COPY settings.gradle.kts ./
COPY gradle gradle
COPY backend backend

WORKDIR /home/gradle/backend

# Build the jar (skip tests for faster builds)
RUN gradle bootJar -x test

# Rename and move the jar for convenience
RUN mv build/libs/*.jar app.jar


# ---- Stage 3: Run the app ----
FROM eclipse-temurin:21-alpine

# Create a non-root user
RUN addgroup -g 1000 app && \
    adduser -G app -D -u 1000 -h /app app

USER app
WORKDIR /app

# Copy the jar from the builder stage
COPY --from=backend-builder --chown=1000:1000 /home/gradle/backend/app.jar .

# Expose Spring Boot default port
EXPOSE 8080

# Start the Spring Boot app
CMD ["java", "-jar", "app.jar"]