CREATE TABLE comentarios (
    id          BIGINT AUTO_INCREMENT PRIMARY KEY,
    ticket_id   BIGINT NOT NULL,
    usuario_id  BIGINT NOT NULL,
    contenido   TEXT NOT NULL,
    tipo        ENUM('USUARIO','SISTEMA') NOT NULL DEFAULT 'USUARIO',
    creado_en   DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP
);
