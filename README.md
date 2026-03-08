# 🌐 NetToggle (v1.0.0)

![Android](https://img.shields.io/badge/Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)
![Kotlin](https://img.shields.io/badge/Kotlin-0095D5?&style=for-the-badge&logo=kotlin&logoColor=white)
![Root Required](https://img.shields.io/badge/Root-Required-red?style=for-the-badge)

NetToggle is a lightweight, open-source, root-level Android utility designed to quickly force your device's modem into 5G (NR Only) mode or restore it to Global Auto, bypassing the tedious `*#*#4636#*#*` dialer menus.

## ✨ Features
* **One-Tap Network Forcing:** Switch between 5G Only (NR_ONLY) and Auto (NR/LTE/WCDMA) instantly.
* **Dynamic Root Checking:** Built-in UI to verify root access status on launch.
* **KernelSU & Magisk Compatible:** Executes commands directly via standard `su` shell.
* **Material Design Adaptive Icon:** Blends seamlessly with your Android system theme.

## ⚠️ Important Disclaimer: The VoNR Reality
**Read this before using "Force 5G".** If your carrier (like Jio) relies on VoNR (Voice over New Radio) for phone calls, forcing your phone to 5G *will* prevent your device from automatically dropping back to 4G/LTE to receive a call if the 5G signal is unstable. **If you are in an area with poor VoNR coverage and you lock your network to 5G, your incoming calls may fail or go straight to voicemail.** Use this tool responsibly.

## 🚀 Installation & Usage
1. Download the latest `NetToggle apk file` from the **[Releases](../../releases)** tab.
2. Install the APK on your rooted Android device.
3. **For KernelSU Users:** Open your KSU Manager and manually grant Superuser access to NetToggle before using the buttons.
4. **For Magisk Users:** Tap the Root Access button and grant the root prompt when it appears.
5. Tap **Force 5G** or **Auto**.

> **💡 Pro-Tip (The Airplane Mode Trick):** When you force a network mode, the Android database updates instantly, but your physical modem hardware might not catch up right away. If your signal doesn't change immediately after tapping a button, simply **toggle Airplane Mode ON and OFF**. This forces the radio to wake up and read the new network rules.

## 🛠️ How it Works under the Hood
Android heavily restricts apps from modifying network states. NetToggle bypasses this sandbox by requesting superuser privileges to execute standard Linux shell commands against the Android global settings database:
* **Force 5G:** `settings put global preferred_network_mode 32`
* **Auto:** `settings put global preferred_network_mode 33`