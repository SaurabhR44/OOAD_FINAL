$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot"
$env:Path = "$env:JAVA_HOME\bin;$env:Path"

Write-Host "--- Cleaning Environment ---" -ForegroundColor Cyan
# Kill any process on port 8080
$port8080 = Get-NetTCPConnection -LocalPort 8080 -ErrorAction SilentlyContinue
if ($port8080) {
    Write-Host "Stopping process on port 8080 (PID: $($port8080[0].OwningProcess))..." -ForegroundColor Yellow
    Stop-Process -Id $port8080[0].OwningProcess -Force -ErrorAction SilentlyContinue
}

Write-Host "--- Starting Backend ---" -ForegroundColor Cyan
$env:JAVA_HOME = "C:\Program Files\Eclipse Adoptium\jdk-17.0.18.8-hotspot"
cd $PSScriptRoot
mvn clean
mvn spring-boot:run -DskipTests
