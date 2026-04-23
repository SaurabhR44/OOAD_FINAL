Write-Host "--- Cleaning Environment ---" -ForegroundColor Cyan
# Kill any process on port 5173 (Vite default)
$port5173 = Get-NetTCPConnection -LocalPort 5173 -ErrorAction SilentlyContinue
if ($port5173) {
    Write-Host "Stopping process on port 5173 (PID: $($port5173[0].OwningProcess))..." -ForegroundColor Yellow
    Stop-Process -Id $port5173[0].OwningProcess -Force -ErrorAction SilentlyContinue
}

Write-Host "--- Starting Frontend ---" -ForegroundColor Green
cd "$PSScriptRoot\frontend"
npm run dev
