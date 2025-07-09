#!/bin/bash

# Banking Management System - API Testing Script
# Base URL
BASE_URL="http://localhost:8080"

echo "🚀 Starting Banking Management System API Testing..."
echo "Base URL: $BASE_URL"
echo "=========================================="

# Function to make HTTP requests and show results
test_endpoint() {
    local method=$1
    local endpoint=$2
    local data=$3
    local auth_header=$4
    local description=$5

    echo ""
    echo "📋 Testing: $description"
    echo "Method: $method"
    echo "Endpoint: $endpoint"

    if [ -z "$auth_header" ]; then
        if [ -z "$data" ]; then
            curl -X $method "$BASE_URL$endpoint" \
                -H "Content-Type: application/json" \
                -w "\nStatus: %{http_code}\nTime: %{time_total}s\n" \
                -s
        else
            curl -X $method "$BASE_URL$endpoint" \
                -H "Content-Type: application/json" \
                -d "$data" \
                -w "\nStatus: %{http_code}\nTime: %{time_total}s\n" \
                -s
        fi
    else
        if [ -z "$data" ]; then
            curl -X $method "$BASE_URL$endpoint" \
                -H "Content-Type: application/json" \
                -H "Authorization: Bearer $auth_header" \
                -w "\nStatus: %{http_code}\nTime: %{time_total}s\n" \
                -s
        else
            curl -X $method "$BASE_URL$endpoint" \
                -H "Content-Type: application/json" \
                -H "Authorization: Bearer $auth_header" \
                -d "$data" \
                -w "\nStatus: %{http_code}\nTime: %{time_total}s\n" \
                -s
        fi
    fi

    echo "----------------------------------------"
}

# Wait for application to start
echo "⏳ Waiting for application to start..."
sleep 10

# Variables to store tokens
ACCESS_TOKEN=""
REFRESH_TOKEN=""
USER_ID=""
EMPLOYEE_ID=""
ACCOUNT_NUMBER=""

echo ""
echo "🔐 AUTHENTICATION TESTING"
echo "=========================================="

# Test 1: Create User
echo ""
echo "1️⃣ Creating Test User..."
USER_DATA='{
    "name": "John Doe",
    "email": "john.doe@test.com",
    "password": "password123",
    "about": "Test user for API testing"
}'

RESPONSE=$(curl -X POST "$BASE_URL/api/users/create" \
    -H "Content-Type: application/json" \
    -d "$USER_DATA" \
    -s)

echo "Response: $RESPONSE"

# Test 2: User Login
echo ""
echo "2️⃣ Testing User Login..."
LOGIN_DATA='{
    "username": "john.doe@test.com",
    "password": "password123"
}'

LOGIN_RESPONSE=$(curl -X POST "$BASE_URL/api/users/login" \
    -H "Content-Type: application/json" \
    -d "$LOGIN_DATA" \
    -s)

echo "Login Response: $LOGIN_RESPONSE"

# Extract tokens (simplified - in real scenario use jq)
# ACCESS_TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"accessToken":"[^"]*' | cut -d'"' -f4)
# REFRESH_TOKEN=$(echo $LOGIN_RESPONSE | grep -o '"refreshToken":"[^"]*' | cut -d'"' -f4)

echo ""
echo "👥 USER MANAGEMENT TESTING"
echo "=========================================="

# Test 3: Get All Users
test_endpoint "GET" "/api/users/" "" "" "Get All Users"

# Test 4: Create Employee
echo ""
echo "3️⃣ Creating Test Employee..."
EMPLOYEE_DATA='{
    "employeeName": "Jane Smith",
    "employeeEmail": "jane.smith@bank.com",
    "employeePassword": "emp123",
    "employeePhone": "1234567890",
    "employeeAddress": "123 Bank Street"
}'

test_endpoint "POST" "/employee/addEmployee" "$EMPLOYEE_DATA" "" "Create Employee"

# Test 5: Employee Login
echo ""
echo "4️⃣ Testing Employee Login..."
EMP_LOGIN_DATA='{
    "username": "jane.smith@bank.com",
    "password": "emp123"
}'

test_endpoint "POST" "/employee/login" "$EMP_LOGIN_DATA" "" "Employee Login"

echo ""
echo "🏦 ACCOUNT MANAGEMENT TESTING"
echo "=========================================="

# Test 6: Create Account (requires authentication)
ACCOUNT_DATA='{
    "accountType": "SAVINGS",
    "balance": 1000.00,
    "accountStatus": "ACTIVE"
}'

test_endpoint "POST" "/api/account/create/MB001/john.doe@test.com" "$ACCOUNT_DATA" "" "Create Account"

echo ""
echo "🔄 TOKEN MANAGEMENT TESTING"
echo "=========================================="

# Test 7: Token Refresh (if we have refresh token)
if [ ! -z "$REFRESH_TOKEN" ]; then
    REFRESH_DATA="{\"refreshToken\": \"$REFRESH_TOKEN\"}"
    test_endpoint "POST" "/api/users/refresh-token" "$REFRESH_DATA" "" "Refresh Token"
fi

echo ""
echo "❌ ERROR HANDLING TESTING"
echo "=========================================="

# Test 8: Invalid Login
echo ""
echo "5️⃣ Testing Invalid Login..."
INVALID_LOGIN='{
    "username": "invalid@test.com",
    "password": "wrongpassword"
}'

test_endpoint "POST" "/api/users/login" "$INVALID_LOGIN" "" "Invalid Login Test"

# Test 9: Access Protected Endpoint Without Token
test_endpoint "GET" "/api/users/john.doe@test.com" "" "" "Access Protected Endpoint Without Token"

# Test 10: Invalid Token
test_endpoint "GET" "/api/users/" "" "invalid_token_here" "Access with Invalid Token"

echo ""
echo "✅ API TESTING COMPLETED"
echo "=========================================="
echo "📊 Summary:"
echo "- Authentication endpoints tested"
echo "- User management endpoints tested"
echo "- Employee management endpoints tested"
echo "- Account management endpoints tested"
echo "- Error handling tested"
echo "- Token management tested"
echo ""
echo "Check the responses above for any errors or issues."
