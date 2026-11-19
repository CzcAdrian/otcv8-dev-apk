# Tibia 8.60 — private client builds

Windows: extract Tibia860-Windows.zip. Copy your authorized `Tibia.dat` and `Tibia.spr` into `data/things/860`, then run `Tibia860.exe`. The executable targets x86 and runs on x64 Windows; the C/C++ runtime is statically linked. An OpenGL-capable graphics driver is required.

Android: install Tibia860-Android-arm64.apk (development-signed). On first launch, select your authorized DAT and SPR through the Android document picker. They stay in this app's private storage. No storage-wide permission is needed. This is an ARM64 build, minimum Android 8, target Android 15; native libraries must pass the 16 KiB alignment check. A build does not establish device compatibility; install/login/play testing is still required.

Connect the device to the authorized Tailscale network. Default server is `100.109.160.22:7171`, protocol 860. The server must advertise the reachable private address for game port 7172 too. Custom servers remain configurable. Loopback is only appropriate with an explicit tunnel on that device.

The public packages intentionally omit DAT/SPR and credentials. No external updater, crash service or telemetry is enabled. OpenTibia RSA and standard 8.60 feature negotiation are used; no extra protocol features are added.

Before calling a build functional, verify DAT/SPR loading, account login, character selection, world rendering, movement, inventory, NPC/trade, combat, logout and reconnection on each platform. Never put passwords or proprietary assets into Actions logs/artifacts.

Baseline: ba5aa2806586acf11d9d1fc9a0d456c200284b4f. Historical workflows are preserved in docs/legacy-workflows and never invoked by the new workflow. Source license: MIT (see LICENSE); that license does not grant redistribution rights to game assets.
