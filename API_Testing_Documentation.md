# Banking Management System - API Testing Documentation

## Base URL
`http://localhost:8080`

## Authentication Headers
All authenticated endpoints require:
```
Authorization: Bearer <access_token>
Content-Type: application/json
```

---

## 🔐 AUTHENTICATION ENDPOINTS

### 1. User Authentication

#### User Login
- **POST** `/api/users/login`
- **Body:**
```json
{
    "username": "user@example.com",
    "password": "password123"
}
```
- **Response:**
```json
{
    "user": {
        "id": 1,
        "name": "John Doe",
        "email": "user@example.com",
        "roles": "[User]"
    },
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9...",
    "tokenType": "Bearer",
    "expiresIn": 3600,
    "issuedAt": "2025-07-09T10:30:00"
}
```

#### User Token Refresh
- **POST** `/api/users/refresh-token`
- **Body:**
```json
{
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

#### User Logout
- **POST** `/api/users/logout`
- **Body:**
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

### 2. Employee Authentication

#### Employee Login
- **POST** `/employee/login`
- **Body:**
```json
{
    "username": "employee@bank.com",
    "password": "emp123"
}
```

#### Employee Token Refresh
- **POST** `/employee/refresh-token`
- **Body:**
```json
{
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

#### Employee Logout
- **POST** `/employee/logout`
- **Body:**
```json
{
    "accessToken": "eyJhbGciOiJIUzI1NiJ9...",
    "refreshToken": "eyJhbGciOiJIUzI1NiJ9..."
}
```

---

## 👥 USER MANAGEMENT ENDPOINTS

### Create User
- **POST** `/api/users/create`
- **Body:**
```json
{
    "name": "John Doe",
    "email": "john@example.com",
    "password": "password123",
    "about": "Customer details"
}
```

### Update User
- **PUT** `/api/users/{id}`
- **Auth Required:** ✅ (User role)
- **Body:**
```json
{
    "name": "John Updated",
    "email": "john.updated@example.com",
    "about": "Updated details"
}
```

### Delete User
- **DELETE** `/api/users/{id}`
- **Auth Required:** ✅ (User role)

### Get All Users
- **GET** `/api/users/`

### Get User by Email
- **GET** `/api/users/{email}`
- **Auth Required:** ✅ (User role)

---

## 🏢 EMPLOYEE MANAGEMENT ENDPOINTS

### Add Employee
- **POST** `/employee/addEmployee`
- **Body:**
```json
{
    "employeeName": "Jane Smith",
    "employeeEmail": "jane@bank.com",
    "employeePassword": "emp123",
    "employeePhone": "1234567890",
    "employeeAddress": "123 Bank St"
}
```

### Update Employee
- **PUT** `/employee/updateEmployee/{employeeId}`
- **Body:**
```json
{
    "employeeName": "Jane Updated",
    "employeeEmail": "jane.updated@bank.com",
    "employeePhone": "0987654321"
}
```

### Assign Employee to Branch
- **PUT** `/employee/assignEmployeeToBranch/{employeeId}/{branchCode}/{roleAssignerId}`

### Assign Role to Employee
- **PUT** `/employee/assignRole/{employeeId}/{roleName}/{roleAssignerId}`

### Get Employee by ID
- **GET** `/employee/getEmployeeById/{employeeId}`

### Get Employee by Email
- **GET** `/employee/getEmployeeByEmail/{email}`

### Delete Employee
- **DELETE** `/employee/deleteEmployee/{employeeId}`

### Get Employees by Branch
- **GET** `/employee/getEmployeeByBranch/{branchCode}`

### Get Employees by Role
- **GET** `/employee/getEmployeeByRole/{roleId}`

---

## 🏦 ACCOUNT MANAGEMENT ENDPOINTS

### Create Account
- **POST** `/api/account/create/{branchCode}/{username}`
- **Body:**
```json
{
    "accountType": "SAVINGS",
    "balance": 1000.00,
    "accountStatus": "ACTIVE"
}
```

### Update Account
- **POST** `/api/account/{accountNumber}/{branchCode}`
- **Body:**
```json
{
    "accountType": "CURRENT",
    "balance": 2000.00,
    "accountStatus": "ACTIVE"
}
```

### Get Account by Number
- **GET** `/api/account/{accountNumber}`

### Delete Account
- **DELETE** `/api/account/{accountNumber}`

---

## 🏢 BRANCH MANAGEMENT ENDPOINTS

### Create Branch
- **POST** `/api/branch/create`
- **Body:**
```json
{
    "branchName": "Main Branch",
    "branchCode": "MB001",
    "branchAddress": "123 Main St",
    "branchPhone": "1234567890"
}
```

### Update Branch
- **PUT** `/api/branch/{branchCode}`

### Get Branch by Code
- **GET** `/api/branch/{branchCode}`

### Get All Branches
- **GET** `/api/branch/`

### Delete Branch
- **DELETE** `/api/branch/{branchCode}`

---

## 💰 LOAN MANAGEMENT ENDPOINTS

### Apply for Loan
- **POST** `/api/loan/apply/{accountNumber}`
- **Body:**
```json
{
    "loanType": "PERSONAL",
    "loanAmount": 50000.00,
    "loanTerm": 24,
    "purpose": "Home renovation"
}
```

### Update Loan
- **PUT** `/api/loan/{loanId}`

### Get Loan by ID
- **GET** `/api/loan/{loanId}`

### Get Loans by Account
- **GET** `/api/loan/account/{accountNumber}`

### Delete Loan
- **DELETE** `/api/loan/{loanId}`

---

## 🧪 API TESTING SCENARIOS

### 1. Complete User Flow Test
```bash
# 1. Create User
curl -X POST http://localhost:8080/api/users/create \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Test User",
    "email": "test@example.com",
    "password": "test123"
  }'

# 2. Login User
curl -X POST http://localhost:8080/api/users/login \
  -H "Content-Type: application/json" \
  -d '{
    "username": "test@example.com",
    "password": "test123"
  }'

# 3. Use access token for authenticated requests
curl -X GET http://localhost:8080/api/users/ \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN"
```

### 2. Token Refresh Test
```bash
# Refresh access token
curl -X POST http://localhost:8080/api/users/refresh-token \
  -H "Content-Type: application/json" \
  -d '{
    "refreshToken": "YOUR_REFRESH_TOKEN"
  }'
```

### 3. Account Management Test
```bash
# Create account (requires user authentication)
curl -X POST http://localhost:8080/api/account/create/MB001/test@example.com \
  -H "Authorization: Bearer YOUR_ACCESS_TOKEN" \
  -H "Content-Type: application/json" \
  -d '{
    "accountType": "SAVINGS",
    "balance": 1000.00
  }'
```

---

## ⚠️ COMMON ISSUES & SOLUTIONS

### 1. JWT Token Expired
- **Issue:** `401 Unauthorized - Token expired`
- **Solution:** Use refresh token endpoint to get new access token

### 2. Invalid Credentials
- **Issue:** `401 Unauthorized - Invalid credentials`
- **Solution:** Check username/password, ensure user exists

### 3. Missing Authorization Header
- **Issue:** `403 Forbidden`
- **Solution:** Add `Authorization: Bearer <token>` header

### 4. Role-based Access Denied
- **Issue:** `403 Forbidden - Access Denied`
- **Solution:** Ensure user has required role (User/Employee/Admin)

---

## 📊 RESPONSE CODES

| Code | Status | Description |
|------|--------|-------------|
| 200 | OK | Request successful |
| 201 | Created | Resource created successfully |
| 400 | Bad Request | Invalid request data |
| 401 | Unauthorized | Authentication required/invalid |
| 403 | Forbidden | Access denied |
| 404 | Not Found | Resource not found |
| 500 | Internal Server Error | Server error |

---

## 🔧 TESTING TOOLS

### Recommended Tools:
1. **Postman** - For manual API testing
2. **cURL** - For command-line testing
3. **JUnit** - For automated testing
4. **Rest Assured** - For API test automation

### Postman Collection Setup:
1. Create environment variables:
   - `baseUrl`: `http://localhost:8080`
   - `accessToken`: (set after login)
   - `refreshToken`: (set after login)

2. Set up pre-request scripts for automatic token management

---

## ✅ VALIDATION CHECKLIST

- [ ] All authentication endpoints working
- [ ] Token refresh mechanism functional
- [ ] User CRUD operations working
- [ ] Employee management working
- [ ] Account operations functional
- [ ] Branch management working
- [ ] Loan operations working
- [ ] Role-based access control enforced
- [ ] Error handling proper
- [ ] JWT security implemented correctly
