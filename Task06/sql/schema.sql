CREATE DATABASE IF NOT EXISTS campus_events;
USE campus_events;

CREATE TABLE IF NOT EXISTS students (
    student_id VARCHAR(30) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL,
    phone VARCHAR(20) NOT NULL,
    department VARCHAR(100) NOT NULL
);

CREATE TABLE IF NOT EXISTS events (
    event_id INT PRIMARY KEY AUTO_INCREMENT,
    event_name VARCHAR(150) NOT NULL,
    event_date DATE NOT NULL,
    venue VARCHAR(150) NOT NULL
);

CREATE TABLE IF NOT EXISTS registrations (
    registration_id INT PRIMARY KEY AUTO_INCREMENT,
    student_id VARCHAR(30) NOT NULL,
    event_id INT NOT NULL,
    registered_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_registration_student FOREIGN KEY (student_id)
        REFERENCES students(student_id),
    CONSTRAINT fk_registration_event FOREIGN KEY (event_id)
        REFERENCES events(event_id),
    CONSTRAINT uq_student_event UNIQUE (student_id, event_id)
);

INSERT INTO events (event_name, event_date, venue)
SELECT 'Java Workshop', '2026-12-01', 'Seminar Hall A'
WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_name = 'Java Workshop');

INSERT INTO events (event_name, event_date, venue)
SELECT 'Robotics Expo', '2026-12-05', 'Innovation Lab'
WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_name = 'Robotics Expo');

INSERT INTO events (event_name, event_date, venue)
SELECT 'Literary Meet', '2026-12-10', 'Auditorium'
WHERE NOT EXISTS (SELECT 1 FROM events WHERE event_name = 'Literary Meet');
