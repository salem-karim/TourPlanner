#!/usr/bin/env bash

# Kill background processes on script exit
cleanup() {
  echo "Shutting down backend and docker..."
  jobs -p | xargs -r kill
  docker-compose down
}
trap cleanup EXIT

# Build only the parent POM and common module
./mvnw clean install -N
./mvnw clean install -pl common

# Start backend (Spring Boot) in background
./mvnw spring-boot:run -pl backend &
sleep 4

# Start frontend (JavaFX)
./mvnw javafx:run -pl frontend
