# CatWave

[![License: MIT](https://img.shields.io/github/license/vinhvrs/CatWave)](https://github.com/vinhvrs/CatWave/blob/main/LICENSE)
[![GitHub stars](https://img.shields.io/github/stars/vinhvrs/CatWave.svg?style=social\&label=Star)](https://github.com/vinhvrs/CatWave)
[![GitHub forks](https://img.shields.io/github/forks/vinhvrs/CatWave.svg?style=social\&label=Fork)](https://github.com/vinhvrs/CatWave)
[![Issues](https://img.shields.io/github/issues/vinhvrs/CatWave)](https://github.com/vinhvrs/CatWave/issues)
[![Pull Requests](https://img.shields.io/github/issues-pr/vinhvrs/CatWave)](https://github.com/vinhvrs/CatWave/pulls)
[![GitHub last commit](https://img.shields.io/github/last-commit/vinhvrs/CatWave)](https://github.com/vinhvrs/CatWave/commits/main)
![Java](https://img.shields.io/badge/Java-17+-blue.svg)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-2.7-green.svg)
![Docker](https://img.shields.io/badge/Docker-enabled-blue.svg)
![Redis](https://img.shields.io/badge/Redis-supported-red.svg)

**CatWave** is a collaborative software engineering project for music management.
It allows users to create, share, and manage playlists with intuitive controls and efficient backend logic. The system is built with Java, Spring Boot, Docker, and Redis, following the MVC (Model-View-Controller) architecture.

---

## 🌟 Project Purpose

CatWave is designed for anyone who wants to organize, play, and share music playlists easily.
It demonstrates software engineering best practices with an emphasis on clean backend design, REST APIs, and modern Java stack usage.

---

## 🚀 Key Features

* **Play/Pause music tracks**
* **Create, delete, and share playlists**
* **Add or remove songs from playlists**
* **Search for songs and playlists**

---

## 🛠 Technologies Used

* **Java 17+**
* **Spring Boot 2.7+**
* **Spring Security & JWT** (for authentication)
* **Spring Data JPA (Hibernate)**
* **MySQL/PostgreSQL**
* **Docker** (for easy deployment)
* **Redis** (for caching or session management)
* **Static JavaScript/CSS** (no React frontend)

---

## 📐 Architecture

CatWave follows the classic **MVC** (Model-View-Controller) architecture:

* **Model:** JPA Entities, data access via Spring Data JPA
* **View:** Static resources (HTML/JS/CSS)
* **Controller:** RESTful endpoints (Spring Boot controllers)

---

## 🧑‍💻 Getting Started

### Prerequisites

* Java 17 or higher
* Maven
* MySQL or PostgreSQL
* Docker (optional, for containerized deployment)
* Redis (if enabled)

### Installation

```bash
git clone https://github.com/vinhvrs/CatWave.git
cd CatWave
```

#### Backend Setup

```bash
mvn clean install
mvn spring-boot:run
```

#### Docker Compose (optional)

If you want to use Docker for easy deployment, make sure you have Docker and Docker Compose installed:

```bash
docker-compose up --build
```

---

## 🔑 Usage

* Access the app at: `http://localhost:1212`
* Log in or register an account (if authentication is enabled)
* Create, delete, and share playlists
* Add or remove songs to your playlists
* Play/pause tracks with one click
* Search for songs and playlists

---

## 📌 Contribution Guidelines

Contributions are welcome!
Please fork the repository, create your feature branch (`git checkout -b feature/YourFeature`), commit your changes, and open a pull request.

---

## 📜 License

CatWave is distributed under the **MIT License**.
See the [LICENSE](https://github.com/vinhvrs/CatWave/blob/main/LICENSE) file for details.

---

## 🔗 Useful Links

* [Repository on GitHub](https://github.com/vinhvrs/CatWave)
* [Issue Tracker](https://github.com/vinhvrs/CatWave/issues)

---

Happy coding! 🚀
