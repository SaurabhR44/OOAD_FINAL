# Check if Maven is installed
$mvnCheck = Get-Command mvn -ErrorAction SilentlyContinue

if (-not $mvnCheck) {
    Write-Host "--- Maven not found. Attempting to install via winget... ---" -ForegroundColor Yellow
    winget install Apache.Maven
    
    Write-Host ""
    Write-Host "!!! IMPORTANT !!!" -ForegroundColor Red
    Write-Host "Maven has been installed, but Windows needs to refresh your environment."
    Write-Host "Please CLOSE this window and open a NEW PowerShell window."
    Write-Host "Then run: cd e:/OOAD; mvn spring-boot:run"
    pause
    exit
}

Write-Host "--- Maven found. Starting the Adaptive Exam System... ---" -ForegroundColor Green
mvn spring-boot:run
