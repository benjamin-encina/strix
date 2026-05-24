-- Contraseña: Admin123! (BCrypt hash)
INSERT INTO usuarios (nombre, apellido, email, password, rol)
VALUES ('Admin', 'Strix', 'admin@strix.com',
        '$2a$10$vp8y2eZdpmhMP.5ilF7jXOQ3tzrrQ3NQV0vhi98O3DJCoYqypMt/C', 'ADMIN');

-- Contraseña: Admin123! (BCrypt hash)
INSERT INTO usuarios (nombre, apellido, email, password, rol)
VALUES ('Juan', 'Pérez', 'tecnico@strix.com',
        '$2a$10$vp8y2eZdpmhMP.5ilF7jXOQ3tzrrQ3NQV0vhi98O3DJCoYqypMt/C', 'TECNICO');

-- Contraseña: Admin123! (BCrypt hash)
INSERT INTO usuarios (nombre, apellido, email, password, rol)
VALUES ('María', 'González', 'usuario@strix.com',
        '$2a$10$vp8y2eZdpmhMP.5ilF7jXOQ3tzrrQ3NQV0vhi98O3DJCoYqypMt/C', 'USUARIO');
