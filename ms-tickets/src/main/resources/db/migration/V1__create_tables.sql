CREATE TABLE tickets (
    id              BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo          VARCHAR(200) NOT NULL,
    descripcion     TEXT NOT NULL,
    estado          ENUM('ABIERTO','EN_PROCESO','CERRADO','CANCELADO') NOT NULL DEFAULT 'ABIERTO',
    categoria_id    BIGINT NOT NULL,
    prioridad_id    BIGINT NOT NULL,
    usuario_id      BIGINT NOT NULL,
    tecnico_id      BIGINT,
    solucion        TEXT,
    etiquetas       VARCHAR(500),
    creado_en       DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
    actualizado_en  DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    cerrado_en      DATETIME
);
