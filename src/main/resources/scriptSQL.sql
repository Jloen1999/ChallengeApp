CREATE USER IF NOT EXISTS 'retoapp'@'localhost' IDENTIFIED BY 'retoapp';

GRANT ALL PRIVILEGES ON *.* TO 'retoapp'@'localhost';

FLUSH PRIVILEGES;

CREATE DATABASE IF NOT EXISTS retoapp;

USE retoapp;

CREATE TABLE Usuario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    correo VARCHAR(100) UNIQUE,
    contrasena VARCHAR(100),
    perfil_info TEXT,
    ubicacion VARCHAR(100)
);

CREATE TABLE Reto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    nombre VARCHAR(100),
    descripcion TEXT,
    duracion INT,
    url VARCHAR(100),
    estado ENUM('pendiente', 'en_progreso', 'completado', 'fallido'),
    tipo ENUM('simple', 'complejo'),
    novedad BOOLEAN,
    visibilidad ENUM('publico', 'privado'),
    fecha_creacion DATE,
    fecha_finalizacion DATE,
    porcentaje_progreso FLOAT,
    creador_id INT,
    FOREIGN KEY (creador_id) REFERENCES Usuario(id)
);

CREATE TABLE RetoSimple (
    id INT PRIMARY KEY AUTO_INCREMENT,
    progreso int,
    FOREIGN KEY (id) REFERENCES Reto(id)
);

CREATE TABLE RetoComplejo (
    id INT PRIMARY KEY AUTO_INCREMENT,
    subtarea_actual VARCHAR(100),
    FOREIGN KEY (id) REFERENCES Reto(id)
);

CREATE TABLE Subtarea (
    id INT PRIMARY KEY AUTO_INCREMENT,
    descripcion TEXT,
    estado ENUM('pendiente', 'en_progreso', 'completada'),
    fecha_creacion DATE,
    fecha_finalizada DATE,
    reto_complejo_id INT,
    FOREIGN KEY (reto_complejo_id) REFERENCES RetoComplejo(id)
);

CREATE TABLE ProgresoReto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    progreso_actual FLOAT,
    fecha_actualizacion DATE,
    usuario_id INT,
    reto_id INT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    FOREIGN KEY (reto_id) REFERENCES Reto(id)
);

CREATE TABLE Recompensa (
    id INT PRIMARY KEY AUTO_INCREMENT,
    descripcion TEXT,
    tipo VARCHAR(50),
    puntos INT,
    usuario_id INT,
    reto_id INT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    FOREIGN KEY (reto_id) REFERENCES Reto(id)
);

CREATE TABLE Estadistica (
    id INT PRIMARY KEY AUTO_INCREMENT,
    total_retos INT,
    retos_completados INT,
    retos_fallidos INT,
    progreso_promedio FLOAT,
    tiempo_promedio FLOAT,
    usuario_id INT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id)
);

CREATE TABLE Notificacion (
    id INT PRIMARY KEY AUTO_INCREMENT,
    mensaje TEXT,
    leido BOOLEAN,
    fecha_envio DATE,
    usuario_id INT,
    reto_id INT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    FOREIGN KEY (reto_id) REFERENCES Reto(id)
);

CREATE TABLE Comentario (
    id INT PRIMARY KEY AUTO_INCREMENT,
    texto TEXT,
    fecha DATE,
    usuario_id INT,
    reto_id INT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    FOREIGN KEY (reto_id) REFERENCES Reto(id)
);

CREATE TABLE ParticipantesReto (
    id INT PRIMARY KEY AUTO_INCREMENT,
    usuario_id INT,
    reto_id INT,
    FOREIGN KEY (usuario_id) REFERENCES Usuario(id),
    FOREIGN KEY (reto_id) REFERENCES Reto(id)
);

CREATE TABLE Amistad (
    usuario_id1 INT,
    usuario_id2 INT,
    PRIMARY KEY (usuario_id1, usuario_id2),
    FOREIGN KEY (usuario_id1) REFERENCES Usuario(id),
    FOREIGN KEY (usuario_id2) REFERENCES Usuario(id)
);


/*INSERCIONES*/
-- Insertar un usuario
INSERT INTO Usuario (nombre, correo, contrasena, perfil_info, ubicacion) VALUES ('user', 'user@gmail.com', '1234', 'Estudiante de la Universidad de los Andes', 'Merida');

-- Insertar 10 retos para el usuario con id 1
INSERT INTO Reto (nombre, descripcion, duracion, estado, tipo, novedad, visibilidad, fecha_creacion, fecha_finalizacion, porcentaje_progreso, creador_id) VALUES ('Reto 1', 'Descripcion reto 1', 10, 'pendiente', 'simple', 0, 'publico', '2021-05-01', '2021-05-11', 0, 1);
INSERT INTO Reto (nombre, descripcion, duracion, estado, tipo, novedad, visibilidad, fecha_creacion, fecha_finalizacion, porcentaje_progreso, creador_id) VALUES ('Reto 2', 'Descripcion reto 2', 10, 'pendiente', 'simple', 0, 'publico', '2021-05-01', '2021-05-11', 0, 1);
INSERT INTO Reto (nombre, descripcion, duracion, estado, tipo, novedad, visibilidad, fecha_creacion, fecha_finalizacion, porcentaje_progreso, creador_id) VALUES ('Reto 3', 'Descripcion reto 3', 10, 'pendiente', 'simple', 0, 'publico', '2021-05-01', '2021-05-11', 0, 1);
INSERT INTO Reto (nombre, descripcion, duracion, estado, tipo, novedad, visibilidad, fecha_creacion, fecha_finalizacion, porcentaje_progreso, creador_id) VALUES ('Reto 4', 'Descripcion reto 4', 10, 'pendiente', 'simple', 0, 'publico', '2021-05-01', '2021-05-11', 0, 1);
INSERT INTO Reto (nombre, descripcion, duracion, estado, tipo, novedad, visibilidad, fecha_creacion, fecha_finalizacion, porcentaje_progreso, creador_id) VALUES ('Reto 5', 'Descripcion reto 5', 10, 'pendiente', 'simple', 0, 'publico', '2021-05-01', '2021-05-11', 0, 1);
INSERT INTO Reto (nombre, descripcion, duracion, estado, tipo, novedad, visibilidad, fecha_creacion, fecha_finalizacion, porcentaje_progreso, creador_id) VALUES ('Reto 6', 'Descripcion reto 6', 10, 'pendiente', 'simple', 0, 'publico', '2021-05-01', '2021-05-11', 0, 1);
INSERT INTO Reto (nombre, descripcion, duracion, estado, tipo, novedad, visibilidad, fecha_creacion, fecha_finalizacion, porcentaje_progreso, creador_id) VALUES ('Reto 7', 'Descripcion reto 7', 10, 'pendiente', 'simple', 0, 'publico', '2021-05-01', '2021-05-11', 0, 1);
INSERT INTO Reto (nombre, descripcion, duracion, estado, tipo, novedad, visibilidad, fecha_creacion, fecha_finalizacion, porcentaje_progreso, creador_id) VALUES ('Reto 8', 'Descripcion reto 8', 10, 'pendiente', 'simple', 0, 'publico', '2021-05-01', '2021-05-11', 0, 1);
INSERT INTO Reto (nombre, descripcion, duracion, estado, tipo, novedad, visibilidad, fecha_creacion, fecha_finalizacion, porcentaje_progreso, creador_id) VALUES ('Reto 9', 'Descripcion reto 9', 10, 'pendiente', 'simple', 0, 'publico', '2021-05-01', '2021-05-11', 0, 1);
INSERT INTO Reto (nombre, descripcion, duracion, estado, tipo, novedad, visibilidad, fecha_creacion, fecha_finalizacion, porcentaje_progreso, creador_id) VALUES ('Reto 10', 'Descripcion reto 10', 10, 'pendiente', 'simple', 0, 'publico', '2021-05-01', '2021-05-11', 0, 1);


