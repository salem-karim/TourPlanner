
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

echo Press any key to stop the backend and exit.
:: Wait for user input
pause >nul
:: Kill the backend process
taskkill /F /PID %BACKEND_PID%
:: Stop docker-compose
docker-compose down

