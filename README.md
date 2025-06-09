# 📱 Doctor Appointment Android App

This is a **mobile application** built using **Java** and **XML** for Android. It connects to a Spring Boot backend via REST API and provides features for doctors and patients to manage appointments, medical records, notifications, and more.

---

## 🧩 Architecture Overview

- 🔗 **API Consumption** via Retrofit (`ApiService` interface)
- 🧠 **MVVM Architecture** using `ViewModel`, `LiveData`, and `Repository` patterns
- 📸 Supports **profile picture** upload via camera or gallery
- 🔔 **Push notifications** via Firebase
- 📆 **Google Calendar** integration for doctors
- 📄 Handles PDF and medical data visualization

---

## 📁 Project Structure

```plaintext
📦 com.example.mobileproject
├── 📁 api                 # Retrofit interface (ApiService)
├── 📁 dto                 # DTOs used for API communication
├── 📁 entity              # Model classes (Doctor, Patient, etc.)
├── 📁 repository          # Repositories to manage data operations
├── 📁 implementation      # Implements the repositories interfaces
├── 📁 fragments           # Contais Application Fragments
├── 📁 viewmodel           # ViewModel
├── 📁 utils               # Adapter Design Patterns
└── 📄 AndroidManifest.xml
