@echo off
TITLE ProctorIQ - Mission Control Launcher
COLOR 0B

echo [MANDATORY_PHASE_1] Configuring JAVA_HOME Environment...
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.0.17.10-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%

echo [MANDATORY_PHASE_2] Clearing Telemetry Ports (8080/5173)...
:: Kill Port 8080 (Backend)
for /f "tokens=5" %%a in ('netstat -aon ^| findstr :8080') do (
    echo Terminating Backend Conflict [PID %%a]
    taskkill /F /PID %%a /T 2>nul
)

:: Kill Port 5173 (Frontend)
for /f "tokens=5" %%b in ('netstat -aon ^| findstr :5173') do (
    echo Terminating Frontend Conflict [PID %%b]
    taskkill /F /PID %%b /T 2>nul
)

echo [MANDATORY_PHASE_3] Starting Application Ecosystem...
echo // Backend: Mission Core (Spring Boot)
echo // Frontend: HUD HUD (Electron + React)

npx concurrently -k "mvn spring-boot:run" "cd frontend && npm run dev"

pause
