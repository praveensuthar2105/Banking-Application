# Banking Management System - API Testing Script (PowerShell)
# Base URL
$BASE_URL = "http://localhost:8080"

Write-Host "🚀 Starting Banking Management System API Testing..." -ForegroundColor Green
Write-Host "Base URL: $BASE_URL" -ForegroundColor Yellow
Write-Host "==========================================" -ForegroundColor Cyan

# Function to test API endpoints
function Test-APIEndpoint {
    param(
        [string]$Method,
        [string]$Endpoint,
        [string]$Body = "",
        [string]$Token = "",
        [string]$Description
    )

    Write-Host ""
    Write-Host "📋 Testing: $Description" -ForegroundColor Blue
    Write-Host "Method: $Method | Endpoint: $Endpoint" -ForegroundColor Gray

    $headers = @{
        "Content-Type" = "application/json"
    }

    if ($Token -ne "") {
        $headers["Authorization"] = "Bearer $Token"
    }

    try {
        $startTime = Get-Date

        if ($Body -eq "") {
            $response = Invoke-RestMethod -Uri "$BASE_URL$Endpoint" -Method $Method -Headers $headers
        } else {
            $response = Invoke-RestMethod -Uri "$BASE_URL$Endpoint" -Method $Method -Headers $headers -Body $Body
        }

        $endTime = Get-Date
        $duration = ($endTime - $startTime).TotalSeconds

        Write-Host "✅ SUCCESS - Time: $([math]::Round($duration, 2))s" -ForegroundColor Green
        Write-Host "Response: $($response | ConvertTo-Json -Depth 3)" -ForegroundColor White

        return $response
    }
    catch {
        $endTime = Get-Date
        $duration = ($endTime - $startTime).TotalSeconds

        Write-Host "❌ ERROR - Time: $([math]::Round($duration, 2))s" -ForegroundColor Red
        Write-Host "Error: $($_.Exception.Message)" -ForegroundColor Red

        if ($_.Exception.Response) {
            $statusCode = $_.Exception.Response.StatusCode.value__
            Write-Host "Status Code: $statusCode" -ForegroundColor Red
        }

        return $null
    }

    Write-Host "----------------------------------------" -ForegroundColor Gray
}

# Check if application is running
Write-Host "⏳ Checking if application is running..." -ForegroundColor Yellow

try {
    $healthCheck = Invoke-RestMethod -Uri "$BASE_URL/api/users/" -Method GET -TimeoutSec 5
    Write-Host "✅ Application is running!" -ForegroundColor Green
}
catch {
    Write-Host "❌ Application is not running. Please start it first." -ForegroundColor Red
    Write-Host "To start: ./mvnw spring-boot:run" -ForegroundColor Yellow
    exit 1
}

# Variables to store test data
$global:ACCESS_TOKEN = ""
$global:REFRESH_TOKEN = ""
$global:USER_EMAIL = "john.doe@test.com"
$global:EMPLOYEE_EMAIL = "jane.smith@bank.com"

Write-Host ""
Write-Host "🔐 AUTHENTICATION TESTING" -ForegroundColor Magenta
Write-Host "==========================================" -ForegroundColor Cyan

# Test 1: Create User
Write-Host ""
Write-Host "1️⃣ Creating Test User..." -ForegroundColor Yellow

$userData = @{
    name = "John Doe"
    email = "john.doe@test.com"
    password = "password123"
    about = "Test user for API testing"
} | ConvertTo-Json

$userResponse = Test-APIEndpoint -Method "POST" -Endpoint "/api/users/create" -Body $userData -Description "Create Test User"

# Test 2: User Login
Write-Host ""
Write-Host "2️⃣ Testing User Login..." -ForegroundColor Yellow

$loginData = @{
    username = "john.doe@test.com"
    password = "password123"
} | ConvertTo-Json

$loginResponse = Test-APIEndpoint -Method "POST" -Endpoint "/api/users/login" -Body $loginData -Description "User Login"

if ($loginResponse) {
    $global:ACCESS_TOKEN = $loginResponse.accessToken
    $global:REFRESH_TOKEN = $loginResponse.refreshToken
    Write-Host "🔑 Tokens extracted for subsequent tests" -ForegroundColor Green
}

# Test 3: Get All Users
Test-APIEndpoint -Method "GET" -Endpoint "/api/users/" -Description "Get All Users"

Write-Host ""
Write-Host "✅ API TESTING COMPLETED" -ForegroundColor Green
Write-Host "Check results above for any issues." -ForegroundColor Cyan
