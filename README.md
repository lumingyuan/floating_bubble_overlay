<img src="doc/fondo.png" width="100%" alt="Dash Bubble Banner" />

<h2 align="center">
  Floating Bubble Overlay
</h2>

<p align="center">
  <a href="https://flutter.dev">
    <img src="https://img.shields.io/badge/Platform-Flutter-02569B?logo=flutter" alt="Platform">
  </a>
  <a href="#">
    <img alt="Version" src="https://img.shields.io/badge/Version-1.0.5-blue">
  </a>
  <br>
</p>

---

## 💡 Overview

**Floating Bubble Overlay** is a Flutter plugin that allows you to create a floating bubble similar to Facebook Messenger chat heads.  
It is fully customizable and built on top of **Floating-Bubble-View**, optimized for modern Android versions (15).

This version includes enhancements:
- ✔ Tap the bubble → Opens your app  
- ✔ Drag the bubble → Shows a closing “X” zone  
- ✔ Custom icon support  
- ✔ Uses your app icon if no bubbleIcon is provided  
- ✔ Smooth drag physics & animations  
- ✔ Clean separation of Bubble Options & Notification Options  

> **Note:** Only **Android** is supported. iOS does not allow overlays over other apps.

<br>

<p align="center">
<img src="doc/demo.gif" width="30%" alt="Floating Bubble Overlay Demo" />
<br>
This animation represents how the plugin behaves
</p>

---

## 🔧 Setup

Set Min SDK in your `android/app/build.gradle`:

```gradle
android {
    defaultConfig {
        minSdkVersion 21
    }
}
```

Permissions are automatically injected:

```xml
<uses-permission android:name="android.permission.SYSTEM_ALERT_WINDOW" />
<uses-permission android:name="android.permission.FOREGROUND_SERVICE" />
<uses-permission android:name="android.permission.POST_NOTIFICATIONS" />
```

### Register the Service

```xml
<service
    android:name="com.example.floating_bubble_overlay.src.BubbleService"
    android:exported="false"
    android:foregroundServiceType="mediaProjection" />
```

---

## 💻 Usage

Import:

```dart
import 'package:floating_bubble_overlay/floating_bubble_overlay.dart';
```

Start the bubble:

```dart
await FloatingBubbleOverlay.startBubble(
  bubbleIcon: 'bubble_icon',   
  bubbleSize: 64,
  opacity: 1.0,
  enableClose: true,
  enableBottomShadow: true,
  closeBehavior: 1,
);
```

Stop:

```dart
await FloatingBubbleOverlay.stopBubble();
```

---

## 🎨 Bubble Customization Options

| Option | Description | Default | Notes |
|-------|-------------|---------|-------|
| `bubbleIcon` | Icon of the bubble | `null` | Must be inside `/android/src/main/res/drawable/` without extension |
| `closeIcon` | Icon of close bubble | Default Android icon | Same path rule |
| `bubbleSize` | Size of the bubble | `60` | px |
| `opacity` | Transparency | `1.0` | 0.0 → 1.0 |
| `distanceToClose` | Distance to show X | `100` | px |
| `closeBehavior` | Close behavior enum | `1` | Drag = X, Tap = open app |
| `enableAnimateToEdge` | Auto-align to edges | `true` | — |
| `keepAliveWhenAppExit` | Persistent after exiting | `false` | — |

---

## 📌 Click → Open App  
Implemented in:

```
BubbleCallbackListener.kt
```

A tap now opens your app without triggering the close zone.

---

## 🧱 Internal Structure

```
android/
  src/main/kotlin/com/example/floating_bubble_overlay/
    FloatingBubbleOverlayPlugin.kt
    src/
      BubbleService.kt
      BubbleManager.kt
      BubbleOptions.kt
      NotificationOptions.kt
      BubbleCallbackListener.kt
```

---

## ⚙ Service Notification
Uses `NotificationCompat` with complete customization:
- Channel ID
- Icon
- Title / Body  
- Foreground service compliance (Android 13+)

---

## 📱 Compatibility

| Platform | Support |
|---------|---------|
| Android | ✔ |
| iOS | ❌ Overlay not allowed |

---

## 🛣 Roadmap

- [ ] Add full example UI  
- [ ] Add foreground-app auto-hide logic  
- [ ] Support widget-based bubble icons  
- [x] Tap bubble → open app  
- [x] Drag bubble → show close zone  

---

## 🧑‍💻 Author

Developed and maintained by **Michael Anthony Valdiviezo Maza**  
Enhanced for production overlay performance and modern Android behavior.

```
