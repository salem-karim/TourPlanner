#!/usr/bin/env bash

# Kill background processes on script exit
trap 'kill $(jobs -p); docker-compose down' EXIT

# Build only the parent POM and common module
./mvnw clean install -N
./mvnw clean install -pl common

# Start backend (Spring Boot) in background
./mvnw spring-boot:run -pl backend &
sleep 4

# Start frontend (JavaFX)
./mvnw javafx:run -pl frontend
