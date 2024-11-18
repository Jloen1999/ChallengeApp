package es.uex.challengeapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.Comentario;
import es.uex.challengeapp.repository.ComentarioRepository;

@Service
public class ComentarioServiceImpl implements ComentarioService{

	@Autowired
	private ComentarioRepository comentarioRepository;
	
	@Override
	public Comentario hacerComentario(Comentario comentario) {
		return comentarioRepository.save(comentario);
	}

	@Override
	public List<Comentario> obtenerComentariosPorReto(Long retoId) {
		return comentarioRepository.findByRetoId(retoId);
	}

}
