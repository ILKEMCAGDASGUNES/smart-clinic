#  Smart Clinic Management System

A full-stack backend application designed to manage clinic operations such as patients, doctors, appointments, and prescriptions. Built using modern backend technologies and clean architecture principles.

---

##  Features

- Manage Doctors, Patients, and Appointments
- Role-based access (Admin / Doctor)
- RESTful API design
- JWT-based Authentication & Authorization
- Integration with both relational and NoSQL databases
- Scalable and modular architecture

---

##  Architecture

This project follows **Clean Architecture** principles:

- **Domain Layer** → Core business logic and entities
- **Application Layer** → Services, use cases, and business rules
- **Infrastructure Layer** → Database, external services
- **Presentation Layer** → Controllers (REST API)

---

##  Tech Stack

- **Backend:** ASP.NET Core
- **Language:** C#
- **Database (SQL):** MySQL
- **Database (NoSQL):** MongoDB
- **ORM:** Entity Framework Core / Spring Data equivalent concepts
- **Authentication:** JWT (JSON Web Token)
- **Version Control:** Git
- **Containerization:** Docker (optional)

---

##  Project Structure

src/
├── Domain/
├── Application/
├── Infrastructure/
├── WebAPI/

---



---

##  Getting Started

### Prerequisites

- .NET SDK
- MySQL
- MongoDB
- Docker (optional)

---

### Run Locally

```bash
git clone https://github.com/ILKEMCAGDASGUNES/smart-clinic.git
cd smart-clinic
dotnet restore
dotnet build
dotnet run


 Authentication
Uses JWT-based authentication
Secure endpoints with role-based authorization

 API Endpoints (Example)
GET /api/doctors
POST /api/patients
POST /api/appointments
GET /api/prescriptions


 Testing
Unit testing supported (xUnit)
Designed with testability in mind
 Future Improvements
Add CI/CD pipeline
Improve test coverage
Implement caching (Redis)
Add frontend UI (React/Angular)


 Author
Ilkem Cagdas Gunes

 Notes
This project is part of my ongoing effort to improve my backend engineering and system design skills, focusing on scalable architecture and real-world applications.
