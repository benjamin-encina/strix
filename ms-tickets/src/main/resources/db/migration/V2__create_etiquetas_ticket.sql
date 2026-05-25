-- Relación @OnetoMany para Tickets y @ManytoOne para EtiquetaTickets
CREATE TABLE etiquetas_ticket (
id        BIGINT AUTO_INCREMENT PRIMARY KEY,
ticket_id BIGINT      NOT NULL,
nombre    VARCHAR(50) NOT NULL,
CONSTRAINT fk_etiqueta_ticket
    FOREIGN KEY (ticket_id)
    REFERENCES tickets(id)
    ON DELETE CASCADE
);