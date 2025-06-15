# 🆘 Emergency App for Deaf and Mute People

Android app designed to help deaf and mute individuals report emergencies using sign language support, GPS tracking, and smart branch dispatching.

---

## 👤 User Types

### 🔹 Deaf & Mute Users
- Account creation includes ID photos, personal info, written & map address.
- Two emergency options:
  - **Urgent Help**: Sends full details + location instantly.
  - **Specific Help**: Choose from medical, police, or fire, with predefined and custom options.
- Notifications: Receive status updates and estimated arrival time.
- Sign language communication.

### 🔹 Admin
- View/approve/reject users.
- Create & manage emergency branches with working hours and capacity.
- Monitor all reports (pending, in-progress, done).
- Communicate via translated sign language messages.

### 🔹 Branch Employees
- Receive filtered reports based on:
  - Working time
  - Capacity
  - Proximity to user
- Handle reports and update status upon completion.

---

## 🛠️ Tech Stack

- **Android (Kotlin)**  
For native app development with modern architecture.

- **Firebase Authentication**  
Secure user registration and login management.

- **Firebase Firestore**  
Cloud NoSQL database to store user data, reports, branches, and logs.

- **Firebase Storage**  
To save user ID images and profile photos.

- **Local Notifications**  
Immediate on-device alerts for user updates.

- **Google Maps API**  
For geolocation, distance calculations, and dispatch routing.

---

## 🖼️ Screenshots

### 👨‍🦯 Deaf & Mute Users

![User](images/User%20screen.png)

---

### 🛡️ Admin

![Admin](images/Admin%20screen.png)
---

### 🏥 Branch Employees

![Branch](images/branches%20Screens.png)

