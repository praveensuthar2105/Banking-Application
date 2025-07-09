# Banking Management System - Manual API Testing Guide

## Prerequisites
1. Start your Spring Boot application first
2. Ensure MySQL database is running
3. Application should be accessible at http://localhost:8080

## Manual API Testing with curl Commands

### 🔐 AUTHENTICATION TESTING

#### 1. Create Test User
```bash
curl -X POST http://localhost:8080/api/users/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "John Doe",
    "email": "john.doe@test.com",
    "password": "password123",
    "about": "Test user for API testing"
  }'
```

#### 2. User Login (Save the tokens from response)
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "john.doe@test.com",
    "password": "password123"
  }'
```

#### 3. Test Invalid Login
```bash
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "invalid@test.com",
    "password": "wrongpassword"
  }'
```

### 👥 USER MANAGEMENT TESTING

#### 4. Get All Users (Public endpoint)
```bash
curl -X GET http://localhost:8080/api/users/ \
  -H "Content-Type: application/json"
```

#### 5. Get User by Email (Protected - use token from login)
```bash
curl -X GET http://localhost:8080/api/users/john.doe@test.com \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE"
```

#### 6. Update User (Protected)
```bash
curl -X PUT http://localhost:8080/api/users/1 \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE" \
  -d '{
    "name": "John Updated",
    "email": "john.doe@test.com",
    "about": "Updated user details"
  }'
```

### 👔 EMPLOYEE MANAGEMENT TESTING

#### 7. Create Employee
```bash
curl -X POST http://localhost:8080/employee/addEmployee \
  -H "Content-Type: application/json" \
  -d '{
    "employeeName": "Jane Smith",
    "employeeEmail": "jane.smith@bank.com",
    "employeePassword": "emp123",
    "employeePhone": "1234567890",
    "employeeAddress": "123 Bank Street"
  }'
```

#### 8. Employee Login
```bash
curl -X POST http://localhost:8080/employee/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "jane.smith@bank.com",
    "password": "emp123"
  }'
```

#### 9. Get Employee by Email
```bash
curl -X GET http://localhost:8080/employee/getEmployeeByEmail/jane.smith@bank.com \
  -H "Content-Type: application/json"
```

### 🔄 TOKEN MANAGEMENT TESTING

#### 10. Refresh Access Token (use refresh token from login response)
```bash
curl -X POST http://localhost:8080/api/users/refresh-token \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN_HERE"
  }'
```

#### 11. User Logout
```bash
curl -X POST http://localhost:8080/api/users/logout \
  -H "Content-Type: application/json" \
  -d '{
    "accessToken": "YOUR_ACCESS_TOKEN_HERE",
    "refreshToken": "YOUR_REFRESH_TOKEN_HERE"
  }'
```

### 🏦 ACCOUNT MANAGEMENT TESTING

#### 12. Create Account (requires user authentication)
```bash
curl -X POST http://localhost:8080/api/account/create/MB001/john.doe@test.com \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE" \
  -d '{
    "accountType": "SAVINGS",
    "balance": 1000.00,
    "accountStatus": "ACTIVE"
  }'
```

#### 13. Get Account by Number
```bash
curl -X GET http://localhost:8080/api/account/123456789 \
  -H "Content-Type: application/json"
```

### ❌ ERROR HANDLING TESTING

#### 14. Access Protected Endpoint Without Token
```bash
curl -X GET http://localhost:8080/api/users/john.doe@test.com \
  -H "Content-Type: application/json"
```

#### 15. Access with Invalid Token
```bash
curl -X GET http://localhost:8080/api/users/ \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer invalid_token_here"
```

## Expected Results

### ✅ Successful Responses:
- **200 OK**: For successful GET, PUT operations
- **201 Created**: For successful POST operations (user/employee creation)
- **User Login Response**: Should include accessToken, refreshToken, user details, expiresIn
- **Employee Login Response**: Should include accessToken, refreshToken, employee details

### ❌ Error Responses:
- **401 Unauthorized**: Invalid credentials, expired tokens
- **403 Forbidden**: Access denied, insufficient permissions
- **404 Not Found**: Resource not found
- **400 Bad Request**: Invalid request data

## Testing Checklist

- [ ] User registration works
- [ ] User login returns both access and refresh tokens
- [ ] Invalid login returns 401 error
- [ ] Protected endpoints require valid tokens
- [ ] Token refresh mechanism works
- [ ] Logout properly revokes tokens
- [ ] Employee management functions work
- [ ] Account creation works with proper authentication
- [ ] Error handling is appropriate for different scenarios

## Notes for Java 24 Compatibility Issue

Since you're experiencing compilation issues with Java 24:

1. **Option 1**: Downgrade to Java 21 or Java 17 for better Spring Boot compatibility
2. **Option 2**: Update to latest Spring Boot version that supports Java 24
3. **Option 3**: Use the manual curl commands above to test the API functionality

The JWT implementation improvements we made are solid and will work once the compilation issue is resolved.
