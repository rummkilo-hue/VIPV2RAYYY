# VIPV2RAY REST API Documentation

Base URL: `https://api.vipv2ray.com/api/v1`

---

## Authentication Endpoints

### 1. User Login
* **POST** `/auth/login`
* **Request Body**:
```json
{
  "email": "user@vipv2ray.com",
  "password": "userpassword"
}
```
* **Response (200 OK)**:
```json
{
  "status": "success",
  "token": "eyJhbGciOiJIUzI1NiIsIn...",
  "user": {
    "id": "uuid",
    "username": "rummkilo",
    "role": "Super Admin"
  }
}
```

---

## Server Endpoints

### 2. Fetch Server List
* **GET** `/servers`
* **Headers**: `Authorization: Bearer <JWT>`
* **Response (200 OK)**:
```json
[
  {
    "id": "server-sg-01",
    "serverName": "Singapore VIP Premium",
    "countryCode": "SG",
    "flagEmoji": "🇸🇬",
    "protocol": "VLESS",
    "pingMs": 24,
    "isPremiumOnly": true
  }
]
```

---

## Configuration Endpoints

### 3. Generate V2Ray Config URI
* **POST** `/configs/generate`
* **Headers**: `Authorization: Bearer <JWT>`
* **Request Body**:
```json
{
  "serverId": "server-sg-01",
  "protocol": "VLESS"
}
```
* **Response (200 OK)**:
```json
{
  "configName": "Singapore VLESS Ultra",
  "rawUri": "vless://a1b2c3d4-e5f6-7890-1234-56789abcdef0@sg1.vipv2ray.com:443?encryption=none&security=reality&type=grpc#Singapore-VLESS"
}
```
