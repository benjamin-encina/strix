# Strix — Help Desk IT

Sistema de gestión de tickets de soporte técnico desarrollado como API REST con arquitectura de microservicios.

Colaboradores de Proyecto: Benjamin Encina · Kevin Pincheira · Damián Marro

---

## ¿Qué es Strix?

Strix es una API REST para la gestión de incidencias y soporte técnico interno de una organización. Permite a los usuarios reportar problemas tecnológicos, a los técnicos gestionarlos y a los administradores supervisar el flujo completo de atención.

El sistema está construido con una arquitectura de **10 microservicios independientes**, cada uno con su propia base de datos, desplegados localmente y comunicados entre sí mediante HTTP.

---

## ¿Para qué sirve y qué problemas resuelve?

En muchas organizaciones el soporte técnico se gestiona de forma desordenada: solicitudes por correo, por mensaje directo o de forma verbal que se pierden o quedan sin seguimiento. Strix centraliza ese proceso.

**Problemas que resuelve:**

- Los usuarios no tienen forma de reportar incidencias y hacer seguimiento de su estado.
- Los técnicos no tienen visibilidad de los tickets asignados ni del historial de cambios.
- Los administradores no cuentan con reportes sobre tiempos de respuesta, carga por técnico o categorías más frecuentes.
- No existe trazabilidad — cuando un ticket cambia de estado no queda registro de quién lo hizo ni cuándo.

**Lo que Strix ofrece:**

- Creación de tickets con categoría, prioridad y SLA asociados.
- Flujo de estados controlado: `ABIERTO → EN_PROCESO → CERRADO` con validaciones de negocio en cada transición.
- Historial automático de cada cambio de estado, registrado via comunicación entre microservicios.
- Etiquetas por ticket con relación JPA real (`@OneToMany / @ManyToOne` con FK física).
- Comentarios de seguimiento asociados a cada ticket.
- Adjuntos con validación de tipo MIME y tamaño máximo.
- Notificaciones internas al usuario cuando su ticket cambia de estado.
- Reportes y estadísticas agrupadas por estado, técnico, categoría y prioridad.
- Autenticación con JWT y roles diferenciados: `USUARIO`, `TECNICO`, `ADMIN`.

---

## Stack Tecnológico

| Componente | Tecnología | Versión |
|---|---|---|
| Lenguaje | Java | 21 |
| Framework principal | Spring Boot | 4.0.6 |
| Persistencia | Spring Data JPA + Hibernate ORM | 7.2.12 |
| Migraciones de BD | Flyway | 11.14.1 |
| Base de datos | MySQL / MariaDB | 8 / 10.4 |
| Seguridad | Spring Security Crypto + JJWT | 7.0.5 / 0.12.6 |
| Comunicación entre servicios | Spring WebFlux WebClient | 4.0.6 |
| Validación de datos | Bean Validation (Hibernate Validator) | 9.0.1 |
| Reducción de boilerplate | Lombok | 1.18.46 |
| Logs | SLF4J + Logback | 2.0.17 / 1.5.32 |
| Gateway / Enrutamiento | ProxyRouter con RouterFunction (WebFlux) | — |
| Driver JDBC | MariaDB Java Client | 3.5.8 |
| Pool de conexiones | HikariCP | 7.0.2 |
| Servidor embebido | Apache Tomcat (microservicios) / Netty (gateway) | 11.0.21 |
| Gestor de dependencias | Maven | 3.x |

---

## Microservicios

| Microservicio | Puerto | Responsabilidad |
|---|---|---|
| ms-gateway | 8080 | Punto de entrada único. Valida JWT y enruta peticiones. |
| ms-usuarios | 8081 | Registro, autenticación y gestión de usuarios. |
| ms-categorias | 8082 | Datos maestros de categorías de incidencias. |
| ms-prioridades | 8083 | Datos maestros de prioridades y SLA. |
| ms-tickets | 8084 | Núcleo del sistema. Gestiona el ciclo de vida completo del ticket. |
| ms-comentarios | 8085 | Hilo de comentarios asociado a cada ticket. |
| ms-historial | 8086 | Registro automático de cambios de estado. |
| ms-adjuntos | 8087 | Subida y gestión de archivos adjuntos. |
| ms-notificaciones | 8088 | Notificaciones internas al usuario. |
| ms-reportes | 8089 | Estadísticas y reportes del sistema. |

---

## Requisitos previos

- Java 21
- Maven 3.x
- MySQL 8 o MariaDB corriendo en `localhost:3306`
- Usuario `root` sin contraseña (o configurar `spring.datasource.password` en cada `application.properties`)

```
