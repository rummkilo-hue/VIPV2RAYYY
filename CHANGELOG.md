# Changelog - VIPV2RAY Ecosystem

All notable changes to the VIPV2RAY project are documented in this file.

## [1.0.0] - 2026-07-26

### 🚀 Added
- **Android App**:
  - Built with Kotlin, Jetpack Compose, Material Design 3, and Glassmorphic AMOLED dark aesthetic.
  - Quick Connect with animated status badge, glowing power button, live speed graph, and current ping indicator.
  - Multi-protocol configuration manager supporting VLESS, VMess, Trojan, Shadowsocks, Reality, Hysteria2, TUIC, and SOCKS5.
  - Smart Routing, Split Tunneling app selector, Kill Switch toggle, DoH / DoT custom DNS settings.
  - High-precision statistics tab with daily traffic breakdown, connection history, and peak speed logs.
  - Profile screen with subscription status, membership tier badge, and account renewal options.
  - Security settings including PIN Lock setup, Biometric auth toggle, and Encrypted local Datastore storage.
  - Full Khmer (🇰🇭) and English (🇬🇧) dual-language localization with instant runtime toggling.
- **Backend & Database**:
  - PostgreSQL schema (`schema.sql`) for scalable user management, server node load balancing, traffic logging, and audit logs.
  - Super Admin seed created for `@error_rum`.
  - Node.js REST API with JWT authentication and OpenAPI / Swagger specs.
- **DevOps & Infrastructure**:
  - `docker-compose.yml` for unified containerization.
  - Nginx reverse proxy configuration with WebSocket header forwarding for V2Ray protocol endpoints.
  - GitHub Actions workflow (`.github/workflows/build-and-deploy.yml`) for automated APK compilation and testing.
