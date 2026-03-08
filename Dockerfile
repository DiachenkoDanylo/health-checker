# Stage 1: Build
FROM maven:3.9-eclipse-temurin-25-alpine AS build
WORKDIR /app

# copy pom.xml and fetching dependencies
COPY pom.xml .
RUN mvn dependency:go-offline -B

# copy codebase and packing jar
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Extract layers
FROM eclipse-temurin:25-jdk AS extracted
WORKDIR /build
COPY --from=build /app/target/*.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 3: Runtime
FROM eclipse-temurin:25-jdk
WORKDIR /application
VOLUME /tmp

# copying layers
COPY --from=extracted /build/dependencies/ ./
COPY --from=extracted /build/spring-boot-loader/ ./
COPY --from=extracted /build/snapshot-dependencies/ ./
COPY --from=extracted /build/application/ ./

# run with docker and profiles
ENTRYPOINT ["java", "-Dspring.profiles.active=prod,h2", "org.springframework.boot.loader.launch.JarLauncher"]