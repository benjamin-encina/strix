-- ms-adjuntos: esquema de base de datos
-- Versión 1: creación de tablas con relación @ManyToOne entre Adjunto y ComentarioRef

-- Tabla de referencia local para comentarios (permite la relación @ManyToOne en JPA)
CREATE TABLE IF NOT EXISTS comentarios_ref (
    id         BIGINT       NOT NULL,
    ticket_id  BIGINT       NOT NULL,
    PRIMARY KEY (id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- Tabla principal de adjuntos
CREATE TABLE IF NOT EXISTS adjuntos (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    comentario_id   BIGINT       NOT NULL,
    ticket_id       BIGINT       NOT NULL,
    nombre_archivo  VARCHAR(255) NOT NULL,
    tipo_mime       VARCHAR(100) NOT NULL,
    tamano_bytes    BIGINT       NOT NULL,
    ruta_storage    VARCHAR(500) NOT NULL,
    subido_en       DATETIME     NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT fk_adjunto_comentario FOREIGN KEY (comentario_id) REFERENCES comentarios_ref(id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;
