@echo off
where renovate >nul 2>nul
if %errorlevel% neq 0 (
    echo Error: Renovate not found. Install it with: npm install -g renovate
    pause
    exit /b
)

set /p RENOVATE_TOKEN="Enter GitHub PAT: "
set RENOVATE_CONFIG_FILE=renovate.json
set RENOVATE_LOG_LEVEL=debug
renovate --dry-run gennadyyonov/hello-okta
pause