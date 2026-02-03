# 🪐 Orbital
### *Next-Generation Smart Home Control Ecosystem*

[![Kotlin](https://img.shields.io/badge/Kotlin-1.9%2B-7F52FF?style=for-the-badge&logo=kotlin&logoColor=white)](https://kotlinlang.org/)
[![Compose](https://img.shields.io/badge/Jetpack_Compose-2026-4285F4?style=for-the-badge&logo=jetpackcompose&logoColor=white)](https://developer.android.com/jetpack/compose)
[![Ktor](https://img.shields.io/badge/Ktor-Real--time-087E8B?style=for-the-badge&logo=ktor&logoColor=white)](https://ktor.io/)
[![Architecture](https://img.shields.io/badge/Architecture-MVI--Clean-black?style=for-the-badge)](https://developer.android.com/topic/architecture)

**Orbital** is a luxury-tier automation interface that bridges the gap between professional AV engineering and modern mobile software. Built with a "State-First" philosophy, it delivers real-time synchronization with zero-latency feedback, wrapped in a glassmorphic design language.

---

## 📸 Experience the Interface



| **Minimalist Dashboard** | **Contextual Room Detail** | **Discovery Ripple** |
| :--- | :--- | :--- |
| High-level "at a glance" status | Operational high-density bento grid | Reactive mDNS & WebSocket discovery |
| Gaussian blur depth layers | Category-specific filtering | Self-healing connection logic |

---

## 🛠 Tech Stack & Architectural Decisions

This project is built on **Clean Architecture** to ensure that the business logic (Domain) remains entirely independent of the UI and Frameworks.



| Layer | Responsibility | Technologies |
| :--- | :--- | :--- |
| **Presentation** | Reactive UI & State Management | Jetpack Compose, MVI, StateFlow, Hilt |
| **Domain** | Pure Business Logic & Use Cases | Kotlin Coroutines, Sealed Classes |
| **Data** | Real-time Persistence & Networking | Ktor WebSockets, Kotlinx Serialization, Repository Pattern |

---

## 🧪 Robustness & Testing
Orbital is "Tested by Design." I maintain a strict testing pyramid to ensure 100% reliability in hardware-critical environments.

* **Unit Testing:** Comprehensive coverage of ViewModels and Use Cases using **MockK** and **Turbine**.
* **Integration Testing:** Validation of the WebSocket handshake and polymorphic JSON serialization using **Ktor `testApplication`**.
* **Instrumented Testing:** Automated UI flows and navigation guards via **ComposeTestRule** and **Fakes**.



---

## 📐 The Design Engineer's Perspective
Orbital was engineered as a **low-latency control surface**, not just a collection of mobile screens. My background in drafting schematics and configuring professional processors (Extron/Crestron) heavily influenced the app's operational logic and ergonomic DNA.

> "In professional AV, status is everything. I designed the 'Macro-to-Micro' navigation pattern to ensure that critical room data is always a glance away, while precise control is only a tap away."



### 🔬 Key Technical Innovations:
* **Ergonomic Bento-Grid:** Intelligent spanning logic where Media and Climate cards occupy 2 columns for high-precision slider interaction, while Lighting remains in efficient 1x1 tiles.
* **State-Aware Resilience:** A centralized Navigation Guard monitors the Hub's heartbeat. If the connection drops, the UI instantly reverts to a discovery state, preventing "Zombie" control attempts and ensuring $100\%$ state accuracy.
* **Asynchronous Command Flow:** Implementation of an idempotent acknowledgment system. The mobile client tracks pending state changes, only updating the local UI once the Ktor backend confirms hardware execution.

---

## 🚀 Getting Started

### Prerequisites
* **Android Studio** Ladybug (2024.2.1) or newer.
* **Orbital Hub:** The companion [Ktor Backend Server](https://github.com/your-repo/orbital-server) must be running on your local network to facilitate WebSocket communication.

### Installation
1.  **Clone the Repository**
    ```bash
    git clone [https://github.com/your-username/orbital-android.git](https://github.com/your-username/orbital-android.git)
    ```
2.  **Initialize the Backend**
    Follow the instructions in the [Server README](https://github.com/your-repo/orbital-server) to boot the Ktor Hub.
3.  **Build & Sync**
    Open the project in Android Studio and allow Gradle to sync.
4.  **Run Tests**
    Verify the environment with the pre-built test suite:
    ```bash
    ./gradlew test
    ```
