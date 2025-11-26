# Undouse Hotel Reservation System

<img src="Undouse Hotel Reservation System/images/logo.jpg" alt="Undouse Hotel Logo" width="400" height="500">

A comprehensive hotel reservation and management system built with Java.

## 🏨 About The Project

Undouse Hotel Reservation System is a full-featured desktop application that allows hotels to manage reservations, room inventory, guest information, and billing operations efficiently. The system provides both administrative capabilities for hotel staff and a user-friendly interface for guests.

### Features

#### 🔐 Guest Features
- **User Authentication** - Secure login system
- **Room Browsing** - View available rooms with details
- **Booking Management** - Make, view, and manage reservations
- **Profile Management** - Update personal information

#### 🏢 Admin Features
- **Dashboard** - Overview of hotel operations
- **Room Management** - Add, edit, and manage room inventory
- **Reservation Management** - Handle bookings, check-ins, and check-outs
- **Guest Management** - Maintain guest profiles and history
- **Reporting** - Generate occupancy and revenue reports

## 🚀 Tech Stack

### Application
- **Java** - Core programming language
- **Swing/AWT** - Desktop GUI framework
- **JavaMail** - Email notifications
- **File-based Database** - Local data storage (.dat files)

### Optional Components
- **MySQL** - Relational database (optional)
- **SMTP** - Email service integration

## 📦 Installation

### Prerequisites
| Requirement | Version |
|-------------|---------|
| JDK (Java Development Kit) | 17 or higher |
| Operating System | Windows (PowerShell / Command Prompt) |
| Optional | MySQL Server 8.0+ |

### Verify Installation
```bash
java -version
javac -version
```

**Note:** No Maven/Gradle needed — all required .jar libraries are included in the `lib/` folder.

### Local Development

1. **Clone the repository**
   ```bash
   git clone https://github.com/yashiro-nyx/Undouse-Hotel-Reservation-System.git
   cd Undouse-Hotel-Reservation-System
   ```

2. **Run with Eclipse IDE (Recommended)**
   - Open Eclipse → `File > Import > Existing Projects into Workspace`
   - Select the project folder
   - Click `Finish`
   - (Optional) Add libraries from `lib/` folder to build path
   - Set main class: `undouse_hotel.UndouseHotelApp`
   - Run the application

3. **Manual Execution**
   ```bash
   # Compile
   javac -cp "lib/*" -d bin src/undouse_hotel/*.java src/undouse_hotel/**/*.java
   
   # Run
   java -cp "bin;lib/*" undouse_hotel.UndouseHotelApp
   ```

## ⚙️ Configuration

### Database Setup
The app runs with **file-based database** by default (.dat files). For MySQL:

```bash
mysql -u root -p < undouse_hotel_database.sql
```

### Email Setup
Edit `src/undouse_hotel/service/EmailService.java`:
```java
private static final String SENDER_EMAIL = "your_email@example.com";
private static final String SENDER_PASSWORD = "your_app_password";
```

## 🔑 Admin Access

### Keyboard Shortcut
| Action | Shortcut |
|--------|----------|
| Open Admin Panel | `Ctrl + Shift + A` |

### Default Admin Credentials
| Email | Password |
|-------|----------|
| `admin@undousehotel.com` | `Admin123!` |

## 🐛 Troubleshooting

| Issue | Solution |
|-------|----------|
| Could not find or load main class | Run from project root with `-cp "bin;lib/*"` |
| Images not showing | Ensure running from correct project folder |
| Compilation errors | Use JDK 17+ and ensure lib/* in classpath |
| Email errors | Check SMTP credentials and enable TLS (port 587) |

## 📁 Project Structure

```
Undouse-Hotel-Reservation-System/
├── src/undouse_hotel/
│   ├── UndouseHotelApp.java      # Main application class
│   ├── service/
│   │   └── EmailService.java     # Email functionality
│   └── ...                       # Other source files
├── lib/                          # All required JAR libraries
├── bin/                          # Compiled classes
├── undouse_hotel_database.sql    # MySQL database schema
└── README.md                     # This file
```

## 🤝 Contributing

1. Fork the project
2. Create your feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 👥 Authors
### Jervin Paul C. Romualdo

- **Yashiro** - *Initial work* - [yashiro-nyx](https://github.com/yashiro-nyx)

---
