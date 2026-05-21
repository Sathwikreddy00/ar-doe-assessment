# ============================================================
# Stage 1: BUILD
# Uses a full Gradle + JDK image to compile and package the app
# ============================================================
FROM gradle:jdk25-alpine AS builder

WORKDIR /app

# Copy dependency files first (improves Docker layer caching)
# If only source changes, Gradle dependencies won't re-download
COPY build.gradle settings.gradle ./
COPY gradle ./gradle
COPY gradlew ./

# Download dependencies before copying source (cache optimization)
RUN ./gradlew dependencies --no-daemon

# Now copy the actual source code
COPY src ./src

# Build the app, skip tests (tests run in CI pipeline separately)
RUN ./gradlew bootJar --no-daemon -x test

# ============================================================
# Stage 2: RUNTIME
# Uses a slim JRE-only image — no build tools, smaller and safer
# ============================================================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Create a non-root user for security best practices
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copy only the built JAR from the builder stage
COPY --from=builder /app/build/libs/*.jar app.jar

# Set ownership to non-root user
RUN chown appuser:appgroup app.jar

# Switch to non-root user
USER appuser

# Expose the port the app runs on (matches server.port in application.yaml)
EXPOSE 9090

# Health check — Docker will poll this to know if the container is healthy
HEALTHCHECK --interval=30s --timeout=5s --start-period=30s --retries=3 \
  CMD wget -qO- http://localhost:9090/actuator/health || exit 1

# Run the app
ENTRYPOINT ["java", "-jar", "app.jar"]
