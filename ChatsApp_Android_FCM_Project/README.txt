ChatsApp Android skeleton - Android package: com.crystalapps.app

REQUIRED BEFORE BUILD:
1. Firebase Console -> Project Settings -> Your apps -> Android app with package com.crystalapps.app -> download google-services.json.
2. Place it at app/google-services.json (NOT the Firebase Admin service-account private key).
3. Upload this project's files to GitHub, preserving folders and replacing .github/workflows/build-apk.yml.
4. GitHub Actions -> Build ChatsApp APK -> Run workflow.

This project wraps https://chatsapp-online.pages.dev/ and receives native FCM pushes that a trusted backend sends. It DOES NOT yet create/send pushes on Firebase database writes, map user IDs to device tokens, or aggregate unread message counts. Those require secure integration. The generic alert protects message text from unintended lock-screen disclosure.

GitHub Actions debug APK uses its debug signing key; this is not suitable as a stable production update signing identity.
