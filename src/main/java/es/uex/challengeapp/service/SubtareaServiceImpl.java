package es.uex.challengeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.Subtarea;
import es.uex.challengeapp.repository.SubtareaRepository;

@Service
public class SubtareaServiceImpl implements SubtareaService {

	@Autowired
	private SubtareaRepository subtareaRepository;
	
	@Override
	public Subtarea guardarSubtarea(Subtarea subtarea) {
		return subtareaRepository.save(subtarea);
	}

	@Override
	public Subtarea buscarPorId(Integer subtareaId) {
		return subtareaRepository.findById(subtareaId).orElse(null);
	}

}
