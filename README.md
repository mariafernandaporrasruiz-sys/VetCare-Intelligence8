# VetCare Intelligence

VetCare Intelligence is a Java-based web application designed to manage veterinary clinical records for pets. The system allows users to register pet information, medical diagnoses, vaccines, next vaccination dates, veterinary follow-ups, and AI-generated recommendations.

The application runs locally in a web browser using a Java HTTP server and integrates the Gemini API to generate intelligent pet care recommendations and answer questions related to pet care.

## Project Objective

The main objective of this project is to demonstrate the use of Object-Oriented Programming principles in Java through a functional veterinary record management system with artificial intelligence integration.

## Main Features

- Register pet owner information
- Register pet clinical records
- Store vaccine information
- Register next vaccination dates
- Register next veterinary control dates
- Generate AI-based pet care recommendations
- Ask questions to an AI assistant specialized in pet care
- Store clinical records during the active session
- Display pet clinical history
- Validate that AI questions remain within the veterinary domain

## Technologies Used

- Java
- Object-Oriented Programming
- Local Java HTTP Server
- HTML
- CSS
- Gemini API
- SQL
- GitHub
- Visual Studio Code

## Object-Oriented Programming Concepts Applied

### Encapsulation

The system uses private attributes and public methods to protect and access object data.

### Inheritance

The classes `Owner` and `Veterinarian` inherit from the abstract class `Person`.

### Abstraction

The class `Person` defines common attributes and behavior for different types of people in the system.

### Polymorphism

Objects from child classes can be treated as objects of the parent class `Person`.

### Modularity

Each class has a specific responsibility, which improves maintainability and code organization.

### Interface

The project includes the `CrudRepository` interface to represent common CRUD operations.

## Main Classes

- `Main`
- `WebServer`
- `AIService`
- `Person`
- `Owner`
- `Veterinarian`
- `Pet`
- `Vaccine`
- `MedicalRecord`
- `Appointment`
- `PatientRecord`
- `VaccineSchedule`
- `CrudRepository`
- `OwnerDAO`
- `PetDAO`
- `AppointmentDAO`
- `PetCareService`

## AI Integration

The system integrates the Gemini API to generate pet care recommendations based on clinical information entered by the user.

The AI assistant is designed to answer only questions related to:

- Pets
- Vaccines
- Veterinary appointments
- Diagnoses
- Treatments
- General pet care

Questions outside the pet care domain are rejected by the system.

## Database

The project includes a SQL script with tables for:

- Owners
- Veterinarians
- Pets
- Vaccines
- Medical records
- Appointments
- AI consultations

The database script is located in:

```text
database/vetcare_database.sql
```

## How to Run

1. Open the project in Visual Studio Code.
2. Make sure Java is installed.
3. Configure the Gemini API key as an environment variable:

```bash
setx GEMINI_API_KEY "YOUR_API_KEY_HERE"
```

4. Restart Visual Studio Code.
5. Run `Main.java`.
6. Open the following URL in the browser:

```text
http://localhost:8085
```

## Security Note

The Gemini API key must not be written directly in the source code or uploaded to GitHub. It must be managed as an environment variable named `GEMINI_API_KEY`.

## Project Structure

```text
VetCare-Intelligence/
├── Main.java
├── WebServer.java
├── AIService.java
├── Person.java
├── Owner.java
├── Veterinarian.java
├── Pet.java
├── Vaccine.java
├── MedicalRecord.java
├── Appointment.java
├── PatientRecord.java
├── VaccineSchedule.java
├── CrudRepository.java
├── OwnerDAO.java
├── PetDAO.java
├── AppointmentDAO.java
├── PetCareService.java
├── database/
│   └── vetcare_database.sql
└── docs/
    ├── uml_diagram.txt
    └── flowchart.txt
```

## Author

Maria Fernanda Porras Ruiz
