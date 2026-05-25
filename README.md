# Smart PetCare Assistant

Smart PetCare Assistant is a Java-based object-oriented application designed to manage basic veterinary information such as pet owners, pets, appointments, vaccines, and medical records.

The system also includes a simulated AI assistant that provides basic pet care recommendations and only answers questions related to the veterinary domain.

## Project Objective

The main objective of this project is to demonstrate the use of Object-Oriented Programming principles in Java, including encapsulation, inheritance, polymorphism, abstraction, modularity, and code reuse.

## Main Features

- Register pet owners
- Register pets
- Register appointments
- Show registered owners
- Show registered pets
- Show registered appointments
- Generate basic pet care recommendations
- Validate appointment status
- Simulated AI assistant with domain control
- SQL database design script

## Technologies Used

- Java
- Object-Oriented Programming
- SQL
- Visual Studio Code
- GitHub

## Object-Oriented Programming Concepts Applied

### Encapsulation

The system uses private attributes and public methods to control access to data.

### Inheritance

The classes `Owner` and `Veterinarian` inherit from the abstract class `Person`.

### Abstraction

The class `Person` defines common attributes and behavior for different types of people in the system.

### Polymorphism

Objects from child classes can be treated as objects of the parent class `Person`.

### Interface

The interface `CrudRepository` defines common CRUD operations used by DAO classes.

## Main Classes

- `Person`
- `Owner`
- `Veterinarian`
- `Pet`
- `Appointment`
- `Vaccine`
- `MedicalRecord`
- `CrudRepository`
- `OwnerDAO`
- `PetDAO`
- `AppointmentDAO`
- `PetCareService`
- `AIService`
- `ConsoleMenu`
- `Main`

## Database

The project includes a SQL script located in:

```text
database/smart_petcare.sql