USE `student-portal-oop`;
-- 'role' table (no foreign keys)
CREATE TABLE `role` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL, -- e.g., 'ROLE_STUDENT', 'ROLE_TEACHER', 'ROLE_ADMIN'
    PRIMARY KEY (`id`)
);

-- 'teacher' table (depends on 'role')
CREATE TABLE `teacher` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL UNIQUE, -- Email should be unique
    `first_name` VARCHAR(255) NOT NULL,
    `last_name` VARCHAR(255) NOT NULL,
    `username` VARCHAR(255) NOT NULL UNIQUE, -- Username should be unique
    `password` VARCHAR(255) NOT NULL,
    `role_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`role_id`) REFERENCES `role`(`id`)
    -- ON DELETE RESTRICT ON UPDATE CASCADE -- Consider cascade for updates, restrict for deletes
);

-- 'student' table (depends on 'role')
CREATE TABLE `student` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `email` VARCHAR(255) NOT NULL UNIQUE,
    `first_name` VARCHAR(255) NOT NULL,
    `last_name` VARCHAR(255) NOT NULL,
    `username` VARCHAR(255) NOT NULL UNIQUE,
    `password` VARCHAR(255) NOT NULL,
    `role_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`role_id`) REFERENCES `role`(`id`)
    -- ON DELETE RESTRICT ON UPDATE CASCADE
);

-- 'course' table (depends on 'teacher')
CREATE TABLE `course` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `code` VARCHAR(255) NOT NULL UNIQUE, 
    `name` VARCHAR(255) NOT NULL,
    `teacher_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`teacher_id`) REFERENCES `teacher`(`id`)
    -- ON DELETE RESTRICT ON UPDATE CASCADE -- If a teacher is deleted, courses might need to be reassigned or deleted
);

-- 'grade_details' table (no direct foreign keys shown, but used by student_course_details)
CREATE TABLE `grade_details` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `grade_one` INT, -- can be null until graded
    `grade_two` INT,
    `grade_three` INT,
    PRIMARY KEY (`id`)
);

-- 'student_course_details' table (junction table, depends on 'student', 'course', 'grade_details')
CREATE TABLE `student_course_details` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `course_id` INT NOT NULL,
    `student_id` INT NOT NULL,
    `grade_details_id` INT, -- Can be NULL if grades aren't yet assigned
    PRIMARY KEY (`id`),
    FOREIGN KEY (`course_id`) REFERENCES `course`(`id`),
    FOREIGN KEY (`student_id`) REFERENCES `student`(`id`),
    FOREIGN KEY (`grade_details_id`) REFERENCES `grade_details`(`id`)
    -- ON DELETE CASCADE ON UPDATE CASCADE -- Consider if deleting a course/student should remove their enrollment record
);

-- 'assignment' table (no direct foreign keys shown, but conceptually tied to courses/teachers)
-- assignments are linked to student-course enrollments, not directly to a course.
CREATE TABLE `assignment` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `days_remaining` INT, -- Could also be calculated from due_date
    `description` VARCHAR(255),
    `due_date` DATE NOT NULL,
    `name` VARCHAR(255) NOT NULL,
    PRIMARY KEY (`id`)
);

-- 'assignment_details' table (junction table, depends on 'assignment', 'student_course_details')
CREATE TABLE `assignment_details` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `assignment_id` INT NOT NULL,
    `is_done` TINYINT(1) DEFAULT 0, -- 0 for false, 1 for true
    `student_course_details_id` INT NOT NULL,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`assignment_id`) REFERENCES `assignment`(`id`),
    FOREIGN KEY (`student_course_details_id`) REFERENCES `student_course_details`(`id`)
    -- ON DELETE CASCADE ON UPDATE CASCADE
);

ALTER TABLE `assignment_details`
MODIFY COLUMN `is_done` TINYINT DEFAULT 0;

SHOW DATABASES;

INSERT IGNORE INTO role (name) VALUES ('ROLE_STUDENT');

INSERT IGNORE INTO role (name) VALUES ('ROLE_TEACHER');

INSERT IGNORE INTO role (name) VALUES ('ROLE_ADMIN');

-- 'curriculum' table
CREATE TABLE `curriculum` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(255) NOT NULL,
    `code` VARCHAR(50) NOT NULL UNIQUE,
    `description` TEXT,
    `is_active` TINYINT DEFAULT 1,
    `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `updated_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    PRIMARY KEY (`id`)
);

-- 'curriculum_course' table (many-to-many relationship)
CREATE TABLE `curriculum_course` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `curriculum_id` INT NOT NULL,
    `course_id` INT NOT NULL,
    `semester` INT NOT NULL, -- Which semester this course is offered
    `year_level` INT NOT NULL, -- Which year level (1st year, 2nd year, etc.)
    `is_required` TINYINT DEFAULT 1, -- 1 for required, 0 for elective
    PRIMARY KEY (`id`),
    FOREIGN KEY (`curriculum_id`) REFERENCES `curriculum`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE,
    UNIQUE KEY `unique_curriculum_course` (`curriculum_id`, `course_id`)
);

-- curriculum_id to student table
ALTER TABLE `student` 
ADD COLUMN `curriculum_id` INT,
ADD FOREIGN KEY (`curriculum_id`) REFERENCES `curriculum`(`id`) ON DELETE SET NULL;

-- 'section' table
CREATE TABLE `section` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `name` VARCHAR(100) NOT NULL,
    `description` TEXT,
    `curriculum_id` INT NOT NULL,
    `year_level` INT NOT NULL,
    `semester` INT NOT NULL,
    `school_year` VARCHAR(20) NOT NULL,
    `capacity` INT NOT NULL DEFAULT 30,
    `is_active` TINYINT DEFAULT 1,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`curriculum_id`) REFERENCES `curriculum`(`id`) ON DELETE CASCADE
);

-- section_id to student table
ALTER TABLE `student` 
ADD COLUMN `section_id` INT,
ADD FOREIGN KEY (`section_id`) REFERENCES `section`(`id`) ON DELETE SET NULL;

-- 'schedule' table
CREATE TABLE `schedule` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `course_id` INT NOT NULL,
    `day_of_week` VARCHAR(10) NOT NULL, -- MONDAY, TUESDAY, etc.
    `start_time` TIME NOT NULL,
    `end_time` TIME NOT NULL,
    `room` VARCHAR(100),
    `semester` VARCHAR(20) NOT NULL, -- '1st Semester', '2nd Semester', 'Summer'
    `school_year` VARCHAR(20) NOT NULL, -- '2024-2025'
    `is_active` TINYINT DEFAULT 1,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE
);

-- 'enrollment_request' table
CREATE TABLE `enrollment_request` (
    `id` INT NOT NULL AUTO_INCREMENT,
    `student_id` INT NOT NULL,
    `course_id` INT NOT NULL,
    `curriculum_course_id` INT NOT NULL,
    `status` ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    `request_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    `processed_date` TIMESTAMP NULL,
    `processed_by` INT NULL, -- student who processed the request
    `notes` TEXT,
    PRIMARY KEY (`id`),
    FOREIGN KEY (`student_id`) REFERENCES `student`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`course_id`) REFERENCES `course`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`curriculum_course_id`) REFERENCES `curriculum_course`(`id`) ON DELETE CASCADE,
    FOREIGN KEY (`processed_by`) REFERENCES `teacher`(`id`) ON DELETE SET NULL,
    UNIQUE KEY `unique_student_course_request` (`student_id`, `course_id`, `curriculum_course_id`)
);

-- Updated student_course_details table to include enrollment_request reference
ALTER TABLE `student_course_details` 
ADD COLUMN `enrollment_request_id` INT,
ADD COLUMN `enrollment_date` TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
ADD COLUMN `status` ENUM('ENROLLED', 'DROPPED', 'COMPLETED') DEFAULT 'ENROLLED',
ADD FOREIGN KEY (`enrollment_request_id`) REFERENCES `enrollment_request`(`id`) ON DELETE SET NULL;

INSERT INTO `curriculum` (`name`, `code`, `description`) VALUES
('Bachelor of Science in Computer Science', 'BSCS', 'Four-year degree program in Computer Science');

UPDATE curriculum SET code = 'BSIT' WHERE id = 1;

INSERT INTO `curriculum_course` (`curriculum_id`, `course_id`, `semester`, `year_level`, `is_required`) VALUES
(1, 10, 1, 1, 1), 
(1, 11, 1, 1, 1), 
(1, 12, 1, 1, 1), 
(1, 13, 1, 1, 1), 
(1, 14, 2, 1, 1), 
(1, 15, 2, 1, 1), 
(1, 16, 2, 1, 1), 
(1, 17, 2, 1, 1);

INSERT INTO `schedule` (`course_id`, `day_of_week`, `start_time`, `end_time`, `room`, `semester`, `school_year`) VALUES
(10, 'MONDAY', '09:00:00', '11:00:00', 'Room 101', '1st Semester', '2024-2025'),
(11, 'TUESDAY', '08:00:00', '10:00:00', 'Room 302', '1st Semester', '2024-2025'),
(12, 'MONDAY', '13:00:00', '15:00:00', 'Room 201', '1st Semester', '2024-2025'),
(13, 'TUESDAY', '14:00:00', '16:00:00', 'Room 301', '1st Semester', '2024-2025'),
(14, 'WEDNESDAY', '11:00:00', '13:00:00', 'Room 104', '2nd Semester', '2024-2025'),
(15, 'FRIDAY', '8:00:00', '10:00:00', 'Room 202', '2nd Semester', '2024-2025'),
(16, 'WEDNESDAY', '8:00:00', '10:00:00', 'Room 101', '2nd Semester', '2024-2025'),
(17, 'FRIDAY', '13:00:00', '15:00:00', 'Room 103', '2nd Semester', '2024-2025');

-- Sample section data
INSERT INTO `section` (`name`, `description`, `curriculum_id`, `year_level`, `semester`, `school_year`, `capacity`) VALUES
('BSIT 1-1', 'BSIT First Year First Semester Section 1', 1, 1, 1, '2024-2025', 30),
('BSIT 1-2', 'BSIT First Year First Semester Section 2', 1, 1, 1, '2024-2025', 30),
('BSIT 1-3', 'BSIT First Year First Semester Section 3', 1, 1, 1, '2024-2025', 30),
('BSIT 2-1', 'BSIT Second Year First Semester Section 1', 1, 2, 1, '2024-2025', 30),
('BSIT 2-2', 'BSIT Second Year First Semester Section 2', 1, 2, 1, '2024-2025', 30),
('BSIT 3-1', 'BSIT Third Year First Semester Section 1', 1, 3, 1, '2024-2025', 30),
('BSIT 4-1', 'BSIT Fourth Year First Semester Section 1', 1, 4, 1, '2024-2025', 30);

SHOW DATABASES;

USE `student-portal-oop`;

select * from role;