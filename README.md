# TENANT MANAGEMENT SYSTEM

## 1. Project Overview

The Tenant Management System is an Android application developed using Kotlin. It is designed to help landlords manage tenant information, rental records and reports efficiently.

## 2. System Features

- User registration and login.
- Dashboard displaying tenant statistics.
- Add and manage tenant records.
- View registered tenants.
- Store tenant information using a local Room database.
- Call tenants directly using the phone dialer.
- View rental reports and total rent information.
- Navigation drawer for accessing different application sections.

## 3. Technologies Used

- Kotlin
- Android Studio
- XML Layouts
- Android Jetpack
- ViewModel and LiveData
- Room Database
- SQLite
- Navigation Component
- Gradle
- Git and GitHub

## 4. Project Architecture

The application follows a structured architecture that separates the user interface, data models, database and repository logic.

```text
com.example.tenantmanagmentsystem
│
├── data
│   ├── local
│   │   ├── TenantDao.kt
│   │   └── TenantDatabase.kt
│   ├── model
│   │   └── Tenant.kt
│   └── repository
│       └── TenantRepository.kt
│
└── ui
    ├── auth
    ├── dashboard
    ├── main
    ├── reports
    ├── tenants
    └── viewmodel
```

## 5. Installation Instructions

1. Clone the repository:

   `git clone https://github.com/mwamur3/TenantManagementSystem.git`

2. Open the project using Android Studio.
3. Allow Gradle to synchronize.
4. Build and run the application on an Android emulator or compatible Android device.

## 6. Group Contributions

| Member   | Admission Number | Contribution        |
|----------|------------------|---------------------|
| Member A | [178130]         | Stage 1 and stage 2 |
| Member B | [191730] | Stages 3            |
| Member C | [189921] | Stage 4             |
| Member D | [179219] | Stage 5             |
| Member E | [191297] | Stage 6             |
## 7. GitHub Repository

https://github.com/mwamur3/TenantManagementSystem

## 8. Release Information

Version: 1.0

The signed APK is provided under the GitHub Releases section.

## 9. License

This project was developed for academic coursework purposes.