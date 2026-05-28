CREATE TABLE owners (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    phone TEXT NOT NULL,
    email TEXT NOT NULL
);

CREATE TABLE veterinarians (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    phone TEXT NOT NULL,
    email TEXT NOT NULL,
    specialty TEXT NOT NULL
);

CREATE TABLE pets (
    id INTEGER PRIMARY KEY,
    name TEXT NOT NULL,
    species TEXT NOT NULL,
    breed TEXT NOT NULL,
    age INTEGER NOT NULL,
    owner_id INTEGER NOT NULL,
    FOREIGN KEY (owner_id) REFERENCES owners(id)
);

CREATE TABLE appointments (
    id INTEGER PRIMARY KEY,
    pet_id INTEGER NOT NULL,
    veterinarian_id INTEGER NOT NULL,
    appointment_date TEXT NOT NULL,
    reason TEXT NOT NULL,
    status TEXT NOT NULL,
    FOREIGN KEY (pet_id) REFERENCES pets(id),
    FOREIGN KEY (veterinarian_id) REFERENCES veterinarians(id)
);

CREATE TABLE vaccines (
    id INTEGER PRIMARY KEY,
    pet_id INTEGER NOT NULL,
    vaccine_name TEXT NOT NULL,
    application_date TEXT NOT NULL,
    next_due_date TEXT NOT NULL,
    FOREIGN KEY (pet_id) REFERENCES pets(id)
);

CREATE TABLE medical_records (
    id INTEGER PRIMARY KEY,
    pet_id INTEGER NOT NULL,
    description TEXT NOT NULL,
    diagnosis TEXT NOT NULL,
    treatment TEXT NOT NULL,
    record_date TEXT NOT NULL,
    FOREIGN KEY (pet_id) REFERENCES pets(id)
);

INSERT INTO owners (id, name, phone, email)
VALUES (1, 'Fabricio Ruiz', '8888-8888', 'fabricio@email.com');

INSERT INTO veterinarians (id, name, phone, email, specialty)
VALUES (1, 'Dr. Carlos Mora', '2222-2222', 'carlos@petcare.com', 'General Medicine');

INSERT INTO pets (id, name, species, breed, age, owner_id)
VALUES (1, 'Luna', 'Dog', 'Golden Retriever', 3, 1);

INSERT INTO appointments (id, pet_id, veterinarian_id, appointment_date, reason, status)
VALUES (1, 1, 1, '2026-05-30', 'General check-up', 'Scheduled');

INSERT INTO vaccines (id, pet_id, vaccine_name, application_date, next_due_date)
VALUES (1, 1, 'Rabies Vaccine', '2026-05-20', '2027-05-20');

INSERT INTO medical_records (id, pet_id, description, diagnosis, treatment, record_date)
VALUES (
    1,
    1,
    'The pet was brought for a general health check.',
    'Healthy condition',
    'Continue regular vaccination and check-ups',
    '2026-05-25'
);