
@echo off

:: Kill background processes and stop docker-compose on exit
setlocal
set BACKEND_PID=

:: Build only the parent POM and common module
call mvnw clean install -N
call mvnw clean install -pl common

:: Start backend (Spring Boot) in background
start /B mvnw spring-boot:run -pl backend

:: Sleep for 4 seconds to let the backend initialize
timeout /T 4 /NOBREAK

:: Start frontend (JavaFX)
call mvnw javafx:run -pl frontend

:: Trap Ctrl+C to stop background processes and docker-compose
:: Killing processes
trap "kill %BACKEND_PID% && docker-compose down" EXIT
