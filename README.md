# ⚡ VIPV2RAY - Ultimate AI Premium VPN Ecosystem

[![Android Build](https://img.shields.io/badge/Android-Kotlin%20%7C%20Jetpack%20Compose-3DDC84?logo=android&logoColor=white)](https://developer.android.com)
[![Backend](https://img.shields.io/badge/REST%20API-Node.js%20%7C%20Express%20%7C%20PostgreSQL-339933?logo=node.js&logoColor=white)](https://nodejs.org)
[![Admin Panel](https://img.shields.io/badge/Admin%20Dashboard-Next.js%20%7C%20Tailwind-000000?logo=next.js&logoColor=white)](https://nextjs.org)
[![Docker](https://img.shields.io/badge/DevOps-Docker%20%7C%20Nginx%20%7C%20GitHub%20Actions-2496ED?logo=docker&logoColor=white)](https://docker.com)
[![License](https://img.shields.io/badge/License-MIT-blue.svg)](LICENSE)

VIPV2RAY is a high-performance, enterprise-grade V2Ray / Xray VPN management ecosystem. It includes an Android Client app built with Jetpack Compose & Material 3, a REST API server, PostgreSQL database, Docker containerization, and automated CI/CD pipelines.

---

## 🌟 Ecosystem Architecture

```
                               ┌────────────────────────────────┐
                               │   VIPV2RAY Android App         │
                               │  (Kotlin + Jetpack Compose)    │
                               └───────────────┬────────────────┘
                                               │ REST / JWT
                                               ▼
┌──────────────────┐  Nginx Reverse Proxy  ┌────────────────────┐
│   Landing Page   ├──────────────────────►│  REST API Gateway  │
└──────────────────┘                       └─────────┬──────────┘
                                                     │
                       ┌─────────────────────────────┼─────────────────────────────┐
                       ▼                             ▼                             ▼
            ┌───────────────────┐         ┌───────────────────┐         ┌───────────────────┐
            │   PostgreSQL DB   │         │    Redis Cache    │         │  Admin Dashboard  │
            │ (User & Configs)  │         │  (Sessions & Rate)│         │     (Next.js)     │
            └───────────────────┘         └───────────────────┘         └───────────────────┘
```

---

## 🚀 Key Features

### 📱 Android Application
* **Modern Compose UI**: AMOLED Dark Theme, Glassmorphic cards, Electric Purple & Neon Cyan dynamic colors, and high-contrast accessibility.
* **Supported Protocols**: VLESS, VMess, Trojan, Shadowsocks, Reality, Hysteria2, TUIC, SOCKS5, WireGuard.
* **Smart Features**: Smart Routing, Split Tunneling, Kill Switch, Custom DoH/DoT DNS, IPv6 Support, Auto Best Server.
* **Real-time Traffic & Ping**: Dynamic traffic monitor, ping latency graphs, connection duration tracking, and daily usage metrics.
* **Multilingual Support**: Instant toggle between **Khmer (🇰🇭)** and **English (🇬🇧)**.
* **Security & Biometrics**: App PIN lock, Biometric authentication, encrypted local Datastore, and secure JWT handling.

### 🛠️ Backend REST API & Database
* **Authentication**: JWT auth, Refresh Token rotation, PIN / Password reset, Email verification.
* **PostgreSQL Schema**: High-concurrency schema for users, servers, subscriptions, protocol node configs, traffic logs, and audit logs.
* **Super Admin Seed**: Default super admin user `@error_rum`.
* **API Documentation**: OpenAPI / Swagger compliant endpoints.

### 🐳 DevOps & Deployment
* **Docker Compose**: Single-command container orchestration (`docker-compose up -d`).
* **Nginx Proxy**: SSL termination, rate-limiting, and WebSocket proxying for V2Ray protocol nodes.
* **GitHub Actions**: Automated Gradle APK builds and release deployments.

---

## ⚙️ Quick Start Guide

### 1. Android Client Build
```bash
# Clone repository
git clone https://github.com/your-org/vipv2ray.git
cd vipv2ray

# Build Debug APK using Gradle
gradle assembleDebug
# (Or if using Gradle Wrapper in local environment: ./gradlew assembleDebug)
```

### 2. Backend & Docker Deployment
```bash
# Navigate to deployment directory
cd docker

# Start PostgreSQL, Redis, REST API, and Nginx
docker-compose up -d --build
```

---

## 👤 Default Admin Account

* **Username**: `@error_rum`
* **Role**: `Super Admin`
* **Permissions**: Full system access (Server management, user management, billing, traffic stats, configs generator).

---

## 📜 License
Released under the [MIT License](LICENSE).
