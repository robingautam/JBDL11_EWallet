# JBDL11_EWallet - API Specification

## Overview
JBDL11_EWallet is a microservices-based digital wallet application built with Java. The system is composed of multiple services handling user management, authentication, and financial transactions.

## Architecture
- **User Service**: Handles user registration and OTP verification (Port: 8080)
- **Transaction Service**: Manages transaction history and payment processing (Port: 8083)
- **Authentication**: Basic HTTP Authentication

---

## API Endpoints

### 1. User Service

#### 1.1 Create User
**Endpoint:** `POST /user-service/create/user`

**Base URL:** `http://localhost:8080`

**Description:** Creates a new user account in the system with personal information and identity verification.

**Request Headers:**
| Header | Value | Required |
|--------|-------|----------|
| Content-Type | application/json | Yes |

**Request Body:**
```json
{
  "name": "Satyam",
  "email": "satyampandey7248@gmail.com",
  "phoneNo": "8790345680",
  "password": "123456",
  "userIdentifier": "AADHAR_CARD",
  "userIdentifierValue": "778956146525",
  "dob": "14/08/1998"
}
```

**Request Body Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| name | String | Yes | Full name of the user |
| email | String | Yes | Email address of the user |
| phoneNo | String | Yes | Phone number of the user (10 digits) |
| password | String | Yes | Password for account login |
| userIdentifier | String | Yes | Type of identity document (e.g., AADHAR_CARD) |
| userIdentifierValue | String | Yes | Identity document number |
| dob | String | Yes | Date of birth in DD/MM/YYYY format |

**Response:**
- **Success (200 OK):** User account created successfully
- **Error (400 Bad Request):** Invalid input parameters
- **Error (409 Conflict):** User already exists

**Example cURL Request:**
```bash
curl -X POST 'http://localhost:8080/user-service/create/user' \
  --header 'Content-Type: application/json' \
  --body '{
    "name": "Satyam",
    "email": "satyampandey7248@gmail.com",
    "phoneNo": "8790345680",
    "password": "123456",
    "userIdentifier": "AADHAR_CARD",
    "userIdentifierValue": "778956146525",
    "dob": "14/08/1998"
  }'
```

---

#### 1.2 Validate OTP
**Endpoint:** `POST /user-service/validate/otp`

**Base URL:** `http://localhost:8080`

**Description:** Verifies the one-time password (OTP) sent to the user's email for account verification.

**Request Headers:**
| Header | Value | Required |
|--------|-------|----------|
| Content-Type | application/json | Yes |

**Request Body:**
```json
{
  "email": "satyampandey7248@gmail.com",
  "otp": "394790"
}
```

**Request Body Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| email | String | Yes | Email address of the user |
| otp | String | Yes | One-time password received via email (6 digits) |

**Response:**
- **Success (200 OK):** OTP verified successfully
- **Error (400 Bad Request):** Invalid OTP
- **Error (404 Not Found):** User not found
- **Error (410 Gone):** OTP expired

**Example cURL Request:**
```bash
curl -X POST 'http://localhost:8080/user-service/validate/otp' \
  --header 'Content-Type: application/json' \
  --body '{
    "email": "satyampandey7248@gmail.com",
    "otp": "394790"
  }'
```

---

### 2. Transaction Service

#### 2.1 Get Transaction History
**Endpoint:** `GET /txn-service/get/txn/history`

**Base URL:** `http://localhost:8083`

**Description:** Retrieves the complete transaction history for an authenticated user.

**Request Headers:**
| Header | Value | Required |
|--------|-------|----------|
| Content-Type | application/json | Yes |
| Authorization | Basic [base64-encoded-credentials] | Yes |

**Authentication:**
- **Type:** HTTP Basic Authentication
- **Format:** `Basic base64(phoneNo:password)`
- **Example:** `Basic ODc5MDM0NTY4MDoxMjM0NTY=` (for phoneNo: 8790345680, password: 123456)

**Request Body:**
```json
{
  "receiverId": "8790345679",
  "amount": 102,
  "purpose": "Test Transfer"
}
```

**Note:** The request body parameters appear to be optional or used for filtering purposes.

**Query/Body Parameters:**
| Parameter | Type | Optional | Description |
|-----------|------|----------|-------------|
| receiverId | String | Yes | Filter transactions by receiver ID |
| amount | Number | Yes | Filter transactions by amount |
| purpose | String | Yes | Filter transactions by purpose/description |

**Response:**
- **Success (200 OK):** Returns transaction history array
- **Error (401 Unauthorized):** Invalid authentication credentials
- **Error (404 Not Found):** User not found

**Example cURL Request:**
```bash
curl -X GET 'http://localhost:8083/txn-service/get/txn/history' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Basic ODc5MDM0NTY4MDoxMjM0NTY=' \
  --body '{
    "receiverId": "8790345679",
    "amount": 102,
    "purpose": "Test Transfer"
  }' \
  --auth-basic-username '8790345680' \
  --auth-basic-password '123456'
```

---

#### 2.2 Initiate Transaction
**Endpoint:** `POST /txn-service/initiate/transaction`

**Base URL:** `http://localhost:8083`

**Description:** Initiates a financial transaction/money transfer from the authenticated user to a receiver.

**Request Headers:**
| Header | Value | Required |
|--------|-------|----------|
| Content-Type | application/json | Yes |
| Authorization | Basic [base64-encoded-credentials] | Yes |

**Authentication:**
- **Type:** HTTP Basic Authentication
- **Format:** `Basic base64(phoneNo:password)`
- **Example:** `Basic ODc5MDM0NTY4MDoxMjM0NTY=` (for phoneNo: 8790345680, password: 123456)

**Request Body:**
```json
{
  "receiverId": "8790345679",
  "amount": 102,
  "purpose": "Test Transfer"
}
```

**Request Body Parameters:**
| Parameter | Type | Required | Description |
|-----------|------|----------|-------------|
| receiverId | String | Yes | Phone number or unique identifier of the transaction receiver |
| amount | Number | Yes | Amount to transfer (in base currency units) |
| purpose | String | Yes | Transaction purpose/description |

**Response:**
- **Success (200 OK):** Transaction initiated successfully, returns transaction ID/reference
- **Error (400 Bad Request):** Invalid transaction parameters
- **Error (401 Unauthorized):** Invalid authentication credentials
- **Error (402 Payment Required):** Insufficient balance
- **Error (404 Not Found):** Receiver not found
- **Error (422 Unprocessable Entity):** Invalid receiver or transaction rules violation

**Example cURL Request:**
```bash
curl -X POST 'http://localhost:8083/txn-service/initiate/transaction' \
  --header 'Content-Type: application/json' \
  --header 'Authorization: Basic ODc5MDM0NTY4MDoxMjM0NTY=' \
  --body '{
    "receiverId": "8790345679",
    "amount": 102,
    "purpose": "Test Transfer"
  }' \
  --auth-basic-username '8790345680' \
  --auth-basic-password '123456'
```

---

## Service Ports

| Service | Port | Description |
|---------|------|-------------|
| User Service | 8080 | User registration and OTP verification |
| Transaction Service | 8083 | Transaction management and history |

---

## Authentication

### Basic Authentication
The API uses HTTP Basic Authentication for protected endpoints (Transaction Service).

**Format:** `Authorization: Basic base64(username:password)`

**How to Generate:**
1. Combine phoneNo and password with a colon: `phoneNo:password`
2. Encode the string in base64
3. Prepend "Basic " to the encoded string

**Example:**
- PhoneNo: `8790345680`
- Password: `123456`
- Combined: `8790345680:123456`
- Base64 Encoded: `ODc5MDM0NTY4MDoxMjM0NTY=`
- Authorization Header: `Basic ODc5MDM0NTY4MDoxMjM0NTY=`

---

## Data Types & Formats

### Date Formats
- **Date of Birth:** DD/MM/YYYY (e.g., "14/08/1998")

### Phone Number Format
- 10-digit Indian phone numbers

### Identifiers
- **User Identifier Types:** AADHAR_CARD (and potentially others)

### Authentication
- **Credentials:** base64-encoded phoneNo:password

---

## HTTP Status Codes

| Code | Description |
|------|-------------|
| 200 | OK - Request successful |
| 400 | Bad Request - Invalid parameters |
| 401 | Unauthorized - Authentication failed |
| 402 | Payment Required - Insufficient balance |
| 404 | Not Found - Resource not found |
| 409 | Conflict - User already exists |
| 410 | Gone - OTP expired |
| 422 | Unprocessable Entity - Business logic validation failed |

---

## Error Handling

All error responses should include:
- HTTP status code
- Error message
- Error code (if applicable)

---

## Rate Limiting
*Not specified in current documentation*

---

## Security Considerations

1. **Passwords:** Should meet minimum complexity requirements (not specified)
2. **OTP:** Time-limited (expiration not specified)
3. **Basic Auth:** Should only be used over HTTPS in production
4. **Identity Verification:** Requires valid government-issued ID

---

## Technology Stack

- **Language:** Java
- **Architecture:** Microservices
- **Services:** 2+ independent services
- **Authentication:** HTTP Basic Authentication

---

## Notes

- All services are currently running on localhost
- Services communicate independently
- User must complete OTP verification after account creation
- Transaction service requires proper authentication

---

## API Changelog

| Version | Date | Changes |
|---------|------|---------|
| 1.0 | 2026-04-24 | Initial API specification created from README |

---

## Support & Contact

For issues or questions about the API, please refer to the repository documentation or contact the development team.

Repository: https://github.com/robingautam/JBDL11_EWallet
