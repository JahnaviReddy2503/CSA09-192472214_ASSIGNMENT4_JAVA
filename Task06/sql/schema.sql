CREATE DATABASE IF NOT EXISTS campus_events;
USE campus_events;
CREATE TABLE students (
  student_id VARCHAR(30) PRIMARY KEY,
  name VARCHAR(100) NOT NULL,
  email VARCHAR(150) NOT NULL,
  department VARCHAR(100) NOT NULL
);
CREATE TABLE events (
  event_id INT PRIMARY KEY AUTO_INCREMENT,
  event_name VARCHAR(150) NOT NULL,
  event_date DATE NOT NULL,
  venue VARCHAR(150) NOT NULL
);
CREATE TABLE registrations (
  registration_id INT PRIMARY KEY AUTO_INCREMENT,
  student_id VARCHAR(30) NOT NULL,
  event_id INT NOT NULL,
  registered_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
  CONSTRAINT fk_reg_student FOREIGN KEY (student_id) REFERENCES students(student_id),
  CONSTRAINT fk_reg_event FOREIGN KEY (event_id) REFERENCES events(event_id),
  CONSTRAINT uq_student_event UNIQUE (student_id, event_id)
);
INSERT INTO events(event_name,event_date,venue) VALUES
('Java Workshop','2026-12-01','Seminar Hall'),
('Robotics Expo','2026-12-05','Innovation Lab');
