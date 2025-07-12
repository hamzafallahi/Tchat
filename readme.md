# Tchat - Chat System & Task Manager

A modern Android application that combines real-time messaging with personal task management, built with Firebase backend and native Android development.

## 📱 Overview

Tchat is a comprehensive mobile application that integrates two essential productivity features:
- **Real-time Chat System**: Instant messaging with multiple users
- **Personal Task Manager**: Organize and track your daily tasks

## ✨ Features

### 🔐 Authentication
- **Phone Number Authentication**: Secure login using Firebase Auth with OTP verification
- **User Profiles**: Customizable usernames and profile management
- **Country Code Picker**: International phone number support

### 💬 Chat System
- **Real-time Messaging**: Instant message delivery using Firebase Firestore
- **User Search**: Find and connect with other users
- **Chat History**: Persistent message storage with timestamps
- **Modern Chat UI**: Clean, intuitive messaging interface with left/right message alignment
- **Push Notifications**: Real-time notification system for new messages

### ✅ Task Management
- **Create & Edit Tasks**: Add detailed tasks with titles and descriptions
- **Task Status Tracking**: Mark tasks as completed/incomplete
- **Pin Important Tasks**: Priority system for urgent tasks
- **Search Functionality**: Quickly find specific tasks
- **Delete Tasks**: Remove completed or unnecessary tasks
- **User-specific Tasks**: Personal task lists tied to user accounts

### 🎨 User Interface
- **Modern Design**: Clean, Material Design-inspired interface
- **Bottom Navigation**: Easy switching between Chats, Profile, and Task Manager
- **Responsive Layout**: Optimized for various screen sizes
- **Splash Screen**: Professional app launch experience

## 🛠️ Technical Stack

### **Platform & Language**
- **Android SDK**: Target API 34, Minimum API 26
- **Programming Language**: Java
- **IDE**: Android Studio

### **Backend & Database**
- **Firebase Firestore**: Real-time NoSQL database for messages and tasks
- **Firebase Authentication**: Phone number verification and user management
- **Firebase Cloud Messaging**: Push notifications

### **Key Libraries & Dependencies**
- **FirebaseUI**: Firestore RecyclerAdapter for efficient data binding
- **OkHttp**: HTTP client for API communications
- **Country Code Picker**: International phone number input
- **RecyclerView**: Efficient list rendering for chats and tasks
- **Material Design Components**: Modern UI components

## 📁 Project Structure

```
app/
├── src/main/java/com/example/easychat/
│   ├── activities/
│   │   ├── MainActivity.java              # Main app navigation
│   │   ├── ChatActivity.java              # Individual chat interface
│   │   ├── TaskManagerActivity.java       # Task management interface
│   │   ├── LoginPhoneNumberActivity.java  # Phone authentication
│   │   ├── LoginOtpActivity.java          # OTP verification
│   │   └── LoginUsernameActivity.java     # Username setup
│   ├── adapters/
│   │   ├── ChatRecyclerAdapter.java       # Chat message list adapter
│   │   └── TaskViewHolder.java            # Task item view holder
│   ├── models/
│   │   ├── ChatMessageModel.java          # Message data structure
│   │   ├── TaskModel.java                 # Task data structure
│   │   ├── UserModel.java                 # User data structure
│   │   └── ChatroomModel.java             # Chatroom data structure
│   ├── utils/
│   │   ├── FirebaseUtil.java              # Firebase helper methods
│   │   └── AndroidUtil.java               # Android utility functions
│   └── fragments/
│       ├── ChatFragment.java              # Chat list fragment
│       └── ProfileFragment.java           # User profile fragment
├── res/
│   ├── layout/                            # XML layout files
│   ├── drawable/                          # Icons and graphics
│   └── values/                            # Colors, strings, themes
└── google-services.json                   # Firebase configuration
```

## 🚀 Installation & Setup

### Prerequisites
- Android Studio Arctic Fox or later
- Android SDK API 26+
- Firebase project setup
- Google Services JSON file

### Setup Instructions

1. **Clone the Repository**
   ```bash
   git clone [repository-url]
   cd android-chatsystem-taskmanager
   ```

2. **Firebase Configuration**
   - Create a Firebase project at [Firebase Console](https://console.firebase.google.com)
   - Add your Android app to the project
   - Download `google-services.json` and place it in `app/` directory
   - Enable Firebase Authentication (Phone) and Firestore Database

3. **Open in Android Studio**
   - Open the project in Android Studio
   - Sync Gradle files
   - Ensure all dependencies are resolved

4. **Build and Run**
   - Connect an Android device or start an emulator
   - Build and run the application

## 💾 Database Schema

### Firestore Collections

**Users Collection** (`users/`)
```json
{
  "userId": "string",
  "username": "string", 
  "phone": "string",
  "createdTimestamp": "timestamp",
  "fcmToken": "string"
}
```

**Tasks Collection** (`tasks/`)
```json
{
  "taskId": "string",
  "title": "string",
  "description": "string", 
  "isCompleted": "boolean",
  "isPinned": "boolean",
  "createdAt": "timestamp",
  "userId": "string"
}
```

**Chatrooms Collection** (`chatrooms/`)
```json
{
  "chatroomId": "string",
  "userIds": ["string", "string"],
  "lastMessage": "string",
  "lastMessageTimestamp": "timestamp",
  "lastMessageSenderId": "string"
}
```

## 🔧 Configuration

### Firebase Security Rules
Ensure proper Firestore security rules are configured:

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    // Users can only access their own data
    match /users/{userId} {
      allow read, write: if request.auth != null && request.auth.uid == userId;
    }
    
    // Tasks are user-specific
    match /tasks/{taskId} {
      allow read, write: if request.auth != null && 
        resource.data.userId == request.auth.uid;
    }
    
    // Chatroom access for participants only
    match /chatrooms/{chatroomId} {
      allow read, write: if request.auth != null && 
        request.auth.uid in resource.data.userIds;
    }
  }
}
```

## 📱 Screenshots & Usage

### Authentication Flow
1. **Phone Number Entry**: Users enter their phone number with country code
2. **OTP Verification**: Receive and enter verification code
3. **Username Setup**: Choose a display name for the profile

### Chat Features
1. **Chat List**: View all active conversations
2. **User Search**: Find new users to chat with
3. **Real-time Messaging**: Send and receive messages instantly

### Task Management
1. **Task Overview**: View all personal tasks
2. **Add New Tasks**: Create tasks with titles and descriptions
3. **Task Actions**: Mark complete, pin important tasks, or delete
4. **Search Tasks**: Filter tasks by title

## 🔒 Security Features

- **Firebase Authentication**: Secure phone number verification
- **User Data Isolation**: Users can only access their own data
- **Firestore Security Rules**: Server-side data protection
- **Input Validation**: Client-side data validation and sanitization

## 🎯 Performance Optimizations

- **FirestoreRecyclerAdapter**: Efficient real-time data binding
- **Pagination**: Optimized loading for large datasets
- **Background Processes**: Non-blocking UI operations
- **Memory Management**: Proper lifecycle handling for activities and fragments

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/new-feature`)
3. Commit your changes (`git commit -am 'Add new feature'`)
4. Push to the branch (`git push origin feature/new-feature`)
5. Create a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 👨‍💻 Developer

**Hamza** - Android Developer
- Project demonstrates expertise in Android development, Firebase integration, and modern mobile app architecture

## 🚀 Future Enhancements

- [ ] Group chat functionality
- [ ] Task categories and tags
- [ ] File sharing in chats
- [ ] Dark mode theme
- [ ] Task due dates and reminders
- [ ] Voice messages
- [ ] User status (online/offline)
- [ ] Task collaboration features

---

*This project showcases modern Android development practices including Firebase integration, real-time data synchronization, user authentication, and clean architecture patterns.*
