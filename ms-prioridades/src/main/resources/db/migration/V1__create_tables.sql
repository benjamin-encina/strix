CREATE TABLE prioridades (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre      VARCHAR(50) NOT NULL UNIQUE,
    nivel       INT NOT NULL,
    sla_horas   INT NOT NULL,
    activo      BOOLEAN NOT NULL DEFAULT TRUE,
    creado_en   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
