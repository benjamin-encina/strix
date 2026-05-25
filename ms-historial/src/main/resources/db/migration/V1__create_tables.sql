CREATE TABLE historial_tickets (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id       BIGINT NOT NULL,
    estado_anterior VARCHAR(15),
    estado_nuevo    VARCHAR(15) NOT NULL,
    usuario_id      BIGINT NOT NULL,
    justificacion   TEXT,
    registrado_en   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
