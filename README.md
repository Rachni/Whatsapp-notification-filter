# 📱 WhatsApp Notification Filter

An Android Notification Listener designed to filter and silence WhatsApp notifications containing TikTok or Twitter/X links. This app was built to focus on **silent notification handling** for **WhatsApp** messages but can be extended to support other apps or patterns.

---

## 🚀 Features
- Automatically detects WhatsApp notifications containing specific links (TikTok or Twitter/X).
- Cancels the original notification and reissues it silently, with no sound or vibration.
- Modular codebase designed for future extensibility.
- Built using **Android's NotificationListenerService**.

---

## 🛠️ Requirements
- **Android Studio** for development and testing.
- Compatible with Android devices running **API 21 (Lollipop)** or later.
- A test device to run the app and verify behavior.

---

## 📂 Project Structure
```
NotificationFilter/
├── app/                # Main application source code
├── gradle/             # Gradle build scripts
├── .gitignore          # Excluded files from version control
├── build.gradle        # Project-level Gradle config
├── LICENSE             # MIT License file
├── README.md           # This file!
└── settings.gradle     # Gradle settings
```

---

## 🖥️ How to Run
1. Clone the repository:
   ```bash
   git clone https://github.com/Rachni/Whatsapp-notification-filter.git
   ```
2. Open the project in **Android Studio**.
3. Sync Gradle and build the project.
4. Run the app on a compatible Android device or emulator.

---

## 📋 To-Do
### ✅ Current Functionality
- Detects WhatsApp notifications.
- Correctly identifies if the notification contains exclusively a TikTok or Twitter/X link.
- TO-DO: Silence the original notification on Realme devices.
- Reissues them on a silent notification channel.

### 🔧 Known Issue
- **Realme-specific problem**: The app does not work as expected on Realme devices. Notifications still make sound and vibrate despite attempts to silence them. This might be due to Realme's custom **ColorOS** behavior restricting notification handling.

### 📝 Next Steps
- Test the app on other devices (e.g., Xiaomi, Samsung) to validate functionality.
- Investigate workarounds for Realme's restrictive behavior.
- Add support for filtering based on additional conditions or apps.

---

## 📧 Contact
For questions or suggestions, feel free to reach out:
- **GitHub**: [@Rachni](https://github.com/Rachni)

