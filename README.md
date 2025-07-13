# contact-manager-java-mvc

A desktop application for managing contacts and groups using Java, Swing, and the MVC (Model-View-Controller) design pattern.

---

## 📝 Overview

This project is a user-friendly desktop-based contact and group management system. Built entirely with Java and Swing, it demonstrates a clean MVC architecture and object-oriented principles. It supports full CRUD (Create, Read, Update, Delete) operations, real-time search, and persistent data storage using Java serialization.

---

## ✨ Features

- ➕ Add, ✏️ edit, 👁️ view, and ❌ delete contacts
- 🔍 Live search with instant filtering as you type
- 📊 Sort contacts by:
  - First name (default)
  - Last name
  - City
- 👥 Create and manage groups with assigned contacts
- 💾 Save/load data from disk using `.ser` files (Java serialization)
- 🧠 Designed using the **MVC** architecture for separation of concerns

---

## 🔧 Technologies Used

- ☕ Java (JDK 11+)
- 🎨 Swing (Java GUI toolkit)
- 🧱 MVC (Model-View-Controller) architecture
- 💽 File I/O (Object Serialization)

---

## 🗂 Structure

```
src/
├── model/        # Contact and Group classes (data layer)
├── view/         # GUI: MainWindow, ContactWindow, GroupWindow, etc.
├── controller/   # Application logic and data coordination
├── util/         # File utility for serialization
└── Main.java     # Entry point
```

---

## 🚀 Getting Started

1. Clone the repo:
   ```
   git clone https://github.com/your-username/contact-manager-java-mvc.git
   ```

2. Open in Eclipse or IntelliJ.

3. Run `Main.java` and explore the UI.

---

## 📦 Future Enhancements

- Integration with a database (e.g., SQLite)
- Export/import contacts as CSV/JSON
- Enhanced group management and contact duplication checks

---

## 🧑‍💻 Author

Developed as part of the NFA035 project at CNAM Liban.

---

## 📄 License

This project is for educational use.
