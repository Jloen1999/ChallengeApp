package es.uex.challengeapp.service;

import java.util.List;

import es.uex.challengeapp.model.Comentario;

public interface ComentarioService {
	Comentario hacerComentario(Comentario comentario);

	List<Comentario> obtenerComentariosPorReto(Long retoId);
}
