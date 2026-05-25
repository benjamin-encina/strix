# strix



STRIX - Sistema de Gestión de Tickets



Proyecto de API para gestión de Tickets orientado a Soporte Técnico y Troubleshooting.



\## Equipo de Trabajo

\- Benjamin Encina - ms-usuarios, ms-tickets, ms-reportes

\- Damian Marro - ms-categorias, ms-prioridades, ms-historial, ms-adjuntos

\- Kevin Pincheira - ms-gateway, ms-comentarios, ms-notificaciones



&#x20;Descripción

Sistema colaborativo para la gestión de tickets de soporte técnico con microservicios.



&#x20;Configuración

Requiere Java 17+ y Maven. Cada microservicio se ejecuta en puertos diferentes.



Configuración de bases de datos



SQL

CREATE DATABASE strix\_usuarios        CHARACTER SET utf8mb4;

CREATE DATABASE strix\_mscategorias    CHARACTER SET utf8mb4;

CREATE DATABASE strix\_msprioridades   CHARACTER SET utf8mb4;

CREATE DATABASE strix\_mstickets       CHARACTER SET utf8mb4;

CREATE DATABASE strix\_mscomentarios   CHARACTER SET utf8mb4;

CREATE DATABASE strix\_mshistorial     CHARACTER SET utf8mb4;

CREATE DATABASE strix\_msadjuntos      CHARACTER SET utf8mb4;

CREATE DATABASE strix\_msnotificaciones CHARACTER SET utf8mb4;









**Gateway corriendo en localhost:8080**

Todos los endpoints requieren JWT en el header, excepto /auth/login



**Autenticación**



POST  /auth/login     → retorna JWT (no requiere auth)

POST  /auth/logout    → invalida el token





**Usuarios (ADMIN)**

&#x09;

GET     /api/usuarios           → listar todos los usuarios

POST    /api/usuarios           → crear usuario

PUT     /api/usuarios/{id}      → editar usuario

DELETE  /api/usuarios/{id}      → desactivar usuario





**Tickets**



POST   /api/tickets                    → crear ticket                  \[USUARIO]

GET    /api/tickets/mis-tickets        → ver tickets propios           \[USUARIO]

GET    /api/tickets                    → listar todos (con filtros)    \[ADMIN]

GET    /api/tickets/{id}               → detalle del ticket            \[todos]

PATCH  /api/tickets/{id}/asignar       → asignar a técnico             \[ADMIN]

PATCH  /api/tickets/{id}/cerrar        → cerrar con solución           \[TÉCNICO]

PATCH  /api/tickets/{id}/cancelar      → cancelar ticket               \[USUARIO / ADMIN]

PATCH  /api/tickets/{id}/reabrir       → volver a estado ABIERTO       \[ADMIN]





**Comentarios**



POST  /api/comentarios                → agregar comentario

GET   /api/comentarios/ticket/{id}   → ver hilo completo





**Adjuntos (multipart/form-data)**



POST   api/adjuntos/upload              → subir archivo

GET   /api/adjuntos/ticket/{ticketId}   → adjuntos de un ticket





**Historial**



GET  /api/historial/ticket/{ticketId}  → historial de cambios del ticket





**Categorías(Solamente Administrador)**



GET     /api/categorias        → listar

POST    /api/categorias        → crear

PUT     /api/categorias/{id}   → editar

DELETE  /api/categorias/{id}   → desactivar





**Prioridades**



GET     /api/prioridades        → listar

POST    /api/prioridades        → crear

PUT     /api/prioridades/{id}   → editar

DELETE  /api/prioridades/{id}   → desactivar





**Reportes**



GET  /api/reportes/estadisticas  → totales por estado, técnico y categoría

Parámetros opcionales: desde, hasta (yyyy-MM-dd), categoriaId, prioridadId, tecnicoId





**Notificaciones**



GET    /api/notificaciones/usuario/{id}            → todas las notificaciones

GET    /api/notificaciones/usuario/{id}/no-leidas  → solo no leídas

PATCH  /api/notificaciones/{id}/leer               → marcar como leída

