CREATE TABLE notificaciones (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    destinatario_id BIGINT NOT NULL,
    tipo        VARCHAR(50) NOT NULL,
    mensaje     TEXT NOT NULL,
    leida       BOOLEAN NOT NULL DEFAULT FALSE,
    creada_en   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
