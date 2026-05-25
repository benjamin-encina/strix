package com.strix.msnotificaciones.repository;

import com.strix.msnotificaciones.model.Notificacion;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NotificacionRepository extends JpaRepository<Notificacion, Long> {
    List<Notificacion> findByDestinatarioIdOrderByCreadaEnDesc(Long destinatarioId);
    List<Notificacion> findByDestinatarioIdAndLeida(Long destinatarioId, Boolean leida);
}
