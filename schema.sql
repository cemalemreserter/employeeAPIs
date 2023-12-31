  SET FOREIGN_KEY_CHECKS = 0;
-- Create syntax for TABLE 'user'
CREATE TABLE user (
                         id int AUTO_INCREMENT,
                        email varchar(100),
                        password varchar(150) ,
                        PRIMARY KEY (id),
                        UNIQUE  (email)
);

-- Create syntax for TABLE 'employee'
CREATE TABLE `employee` (
                        `id` BINARY(16) PRIMARY KEY,
                        `email` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
                        `fullName` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
                        `birthDay` DATE NOT NULL,
                        UNIQUE KEY `unq_email` (`email`)
)  DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

CREATE TABLE `hobby` (
                        `id` int unsigned NOT NULL AUTO_INCREMENT,
                        `name` varchar(100) CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci NOT NULL DEFAULT '',
                         PRIMARY KEY (id),
                        `employeeId` BINARY(16),
                         CONSTRAINT `hobby_employeeId_1` FOREIGN KEY (employeeId) REFERENCES employee(id)
                         ON DELETE CASCADE ON UPDATE CASCADE);

