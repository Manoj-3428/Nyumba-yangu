# Home Rental App 🏠

A comprehensive Android application that serves as an end-to-end rental marketplace, connecting guests, property owners, and administrators within a single, seamless mobile experience. Built with modern Android development practices and Material Design 3.

## 📱 Project Overview

The Home Rental App delivers a complete rental journey ecosystem that acts as a living catalogue of available stays, a communication bridge between hosts and guests, and an oversight console for staff who curate the marketplace. The application features contextual guidance, resilient placeholder content, and consistent visual cues that remain legible in both light and dark display preferences.

## ✨ Key Features

### 🎯 User Experience
- **Unified Design Language**: Consistent Material Design 3 theming across all screens
- **Dark/Light Mode Support**: Automatic theme adaptation based on system preferences
- **Responsive UI**: Optimized for various screen sizes and orientations
- **Contextual Guidance**: Intuitive onboarding and helpful placeholders
- **Error Handling**: Human-readable error messages with actionable next steps

### 👤 Account Management
- **Secure Authentication**: Email/password and social authentication (Google Sign-In)
- **User Registration**: Create profiles with personal details and preferences
- **Session Management**: Persistent sessions with automatic login state checks
- **Profile Management**: Update name, contact information, avatar, and host-specific data
- **Role-Based Access**: Support for guests, hosts, and administrators

### 🔍 Property Discovery
- **Rich Property Listings**: Scroll through property cards with photos, rates, and amenities
- **Advanced Search**: Filter by location, budget range, property type, bedroom count, or keywords
- **Real-time Updates**: Fast-filtering with loading indicators for network operations
- **Empty States**: Helpful messages when filters yield no results
- **Quick Filters**: Preset filters for Luxury, Budget, Family, and Studio properties

### 📸 Property Details
- **Immersive Image Gallery**: Swipeable image slider with pagination dots
- **Comprehensive Information**: Title, location, type, size, bed/bath counts, and pricing
- **Rich Descriptions**: Formatted text support for host narratives and house rules
- **Amenity Badges**: Visual chips for Wi-Fi, parking, laundry, and more
- **Availability Indicators**: Real-time vacancy status and booking availability
- **Placeholder Handling**: Graceful fallbacks for missing images or data

### 🗺️ Location Intelligence
- **Distance Calculation**: Real-time distance from user location to properties
- **Interactive Maps**: Embedded map previews in property details
- **Full Map View**: Dedicated map screen with property pins and routing
- **Location Permissions**: Smart permission requests with educational prompts
- **Geocoding Support**: Convert coordinates to readable addresses
- **Route Planning**: Integration with turn-by-turn navigation apps

### ❤️ Personalization
- **Favorites System**: Save properties for quick access later
- **Persistent State**: Favorite status survives across sessions
- **Quick Contact**: Direct phone, WhatsApp, or email integration
- **Booking History**: Track upcoming and past reservations
- **User Preferences**: Customizable filters and display options

### 📞 Communication
- **Direct Contact**: Phone calls, WhatsApp messages, and emails
- **Host Information**: Accessible contact details for each property
- **Error Handling**: Clear alerts when contact apps are unavailable
- **Sanitized Data**: Secure handling of contact information

### 📅 Booking System
- **Date Selection**: Calendar interface respecting existing reservations
- **Availability Validation**: Prevents double-booking and invalid date ranges
- **Guest Information Capture**: Required fields for booking submission
- **Booking Management**: View, modify, and cancel reservations
- **Status Updates**: Real-time availability indicators
- **Notification System**: Toast messages and alerts for booking actions

### 🏘️ Host Features
- **Property Management**: Add, edit, and delete listings
- **Multi-Step Form**: Comprehensive property creation workflow
- **Image Upload**: Multiple image support with thumbnail previews
- **Auto-Location**: Automatic coordinate capture for address fields
- **Property Analytics**: View counts and performance metrics
- **Vacancy Tracking**: Monitor remaining units and occupancy
- **Ownership Verification**: Permission checks for property controls

### 📊 Analytics Dashboard (Hosts)
- **View Counts**: Track property visibility and interest
- **Occupancy Insights**: Understand booking patterns
- **Performance Metrics**: Identify popular listings
- **Booking Overview**: Manage all reservations for owned properties
- **Data-Driven Decisions**: Insights for promotions and pricing

### 🛡️ Administrator Tools
- **Full CRUD Control**: Complete property management regardless of ownership
- **User Management**: View, edit, and deactivate user accounts
- **Role Assignment**: Adjust user permissions and roles
- **Booking Oversight**: Inspect all reservations and override statuses
- **Audit Trail**: Track administrative actions
- **Dispute Resolution**: Tools for handling marketplace conflicts
- **Policy Enforcement**: Maintain high-quality inventory standards

### 🔒 Reliability & Security
- **Null Safety**: Comprehensive null checks preventing crashes
- **Network Resilience**: Offline support and connection error handling
- **Permission Management**: Graceful handling of denied permissions
- **Data Validation**: Input validation and sanitization
- **Confirmation Dialogs**: Prevent unintended actions
- **Secure Storage**: Encrypted local data storage

## 🛠️ Android Tech Stack

### Core Framework
- **Language**: Kotlin 2.0.21
- **Minimum SDK**: 24 (Android 7.0 Nougat)
- **Target SDK**: 36 (Android 15)
- **Compile SDK**: 36
- **Build Tools**: Android Gradle Plugin 8.11.2
- **Java Version**: 11

### UI & Design
- **Jetpack Compose**: Modern declarative UI toolkit
  - Compose BOM: 2024.09.00
  - Material Design 3
  - Material Icons
- **Navigation**: Navigation Compose 2.8.4
- **Image Loading**: Coil 2.5.0
- **Animations**: Compose Animation APIs

### Architecture
- **MVVM Pattern**: Model-View-ViewModel architecture
- **State Management**: StateFlow and StateFlow-based UI state
- **ViewModel**: Lifecycle-aware ViewModels
  - Lifecycle ViewModel Compose: 2.9.4
  - Lifecycle Runtime Compose: 2.9.4
- **Dependency Injection**: Hilt 2.57.2
  - Hilt Navigation Compose: 1.2.0
- **Coroutines**: Kotlin Coroutines with Play Services integration
  - Coroutines Play Services: 1.8.1

### Backend & Database
- **Firebase Authentication**: 24.0.1
  - Email/Password authentication
  - Google Sign-In integration
  - Credentials API: 1.5.0
- **Cloud Firestore**: 26.0.2
  - Real-time database
  - Offline persistence
- **Firebase Storage**: 22.0.1
  - Image upload and retrieval
- **Room Database**: 2.6.1
  - Local caching
  - Offline data access
  - Room KTX: 2.6.1
- **DataStore Preferences**: 1.1.1
  - Key-value data storage
  - User preferences

### Location & Maps
- **Google Play Services Location**: 21.3.0
  - Fused Location Provider
  - Geocoding services
- **Google Maps SDK**: 18.2.0
- **Maps Compose**: 4.3.0
  - Compose-friendly map integration
  - Custom markers and overlays

### Build & Development Tools
- **Kotlin Compose Compiler**: 2.0.21
- **KAPT**: Kotlin Annotation Processing Tool
- **Gradle Version Catalog**: Centralized dependency management
- **Google Services Plugin**: 4.4.4

### Testing
- **JUnit**: 4.13.2
- **AndroidX Test JUnit**: 1.3.0
- **Espresso**: 3.7.0
- **Compose UI Test**: JUnit4 integration
- **UI Test Manifest**: Debug test utilities

### Additional Libraries
- **Core KTX**: 1.17.0
- **Activity Compose**: 1.11.0
- **Lifecycle Runtime KTX**: 2.9.4

## 📁 Project Structure

```
app/src/main/java/com/example/homerentalapp/
├── HomeRentalApp.kt              # Application class with Hilt setup
├── MainActivity.kt                # Main entry point
├── local/                         # Room database
│   ├── HouseDatabase.kt
│   ├── HouseDao.kt
│   ├── HouseEntity.kt
│   └── Converters.kt
├── model/                         # Data models
│   ├── House.kt
│   ├── User.kt
│   └── Filter.kt
├── navigation/                    # Navigation setup
│   ├── NavGraph.kt
│   └── Screen.kt
├── repository/                    # Data repositories
│   ├── AuthRepository.kt
│   └── HouseRepository.kt
├── ui/
│   ├── components/                # Reusable UI components
│   │   ├── HouseCard.kt
│   │   ├── ImageCarousel.kt
│   │   ├── FilterBottomSheet.kt
│   │   ├── AuthTextField.kt
│   │   └── ...
│   ├── screens/                   # Screen composables
│   │   ├── home/
│   │   ├── landing/
│   │   ├── login/
│   │   ├── signup/
│   │   ├── details/
│   │   ├── addhouse/
│   │   └── nearby/
│   └── theme/                     # Theme configuration
│       ├── Color.kt
│       ├── Theme.kt
│       └── Type.kt
├── util/                          # Utility classes
│   ├── LocationHelper.kt
│   ├── LocationStorage.kt
│   ├── SessionManager.kt
│   └── AuthResult.kt
└── viewmodel/                     # ViewModels
    ├── HomeViewModel.kt
    ├── AuthViewModel.kt
    ├── LandingViewModel.kt
    ├── HouseDetailsViewModel.kt
    └── AddHouseViewModel.kt
```

## 🚀 Getting Started

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 11 or later
- Android SDK with API 36
- Google account for Firebase setup
- Google Maps API key

### Installation

1. **Clone the repository**
   ```bash
   git clone <repository-url>
   cd HomeRentalApp
   ```

2. **Configure Firebase**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com/)
   - Add Android app with package name: `com.example.homerentalapp`
   - Download `google-services.json` and place it in `app/` directory
   - Enable Authentication (Email/Password and Google Sign-In)
   - Enable Firestore Database
   - Enable Firebase Storage

3. **Configure Google Maps**
   - Get a Google Maps API key from [Google Cloud Console](https://console.cloud.google.com/)
   - Add the key to `app/src/main/res/values/strings.xml`:
     ```xml
     <string name="google_maps_key">YOUR_API_KEY</string>
     ```

4. **Sync and Build**
   - Open the project in Android Studio
   - Wait for Gradle sync to complete
   - Build and run the application

### Required Permissions

The app requires the following permissions (already configured):
- `INTERNET` - Network access
- `ACCESS_NETWORK_STATE` - Check connectivity
- `ACCESS_FINE_LOCATION` - Precise location for distance calculation
- `ACCESS_COARSE_LOCATION` - Approximate location fallback
- `READ_MEDIA_IMAGES` - Image selection for property uploads

## 🎨 Features by User Role

### 👥 Guest Features
- Browse property listings
- Search and filter properties
- View detailed property information
- Calculate distance to properties
- View properties on map
- Save favorites
- Contact hosts
- Book properties
- Manage bookings

### 🏠 Host Features
- All guest features
- Add new properties
- Edit owned properties
- Delete owned properties
- Upload property images
- View property analytics
- Track bookings
- Manage availability

### 👨‍💼 Administrator Features
- All host features
- Full property management (all properties)
- User management
- Booking oversight
- Role assignment
- Dispute resolution

## 🔐 Security Features

- Firebase Authentication with secure session management
- Encrypted local storage for sensitive data
- Input validation and sanitization
- Permission-based access control
- Secure API key management
- HTTPS communication for all network requests



