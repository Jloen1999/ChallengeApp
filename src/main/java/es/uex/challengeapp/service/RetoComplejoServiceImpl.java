package es.uex.challengeapp.service;

import java.sql.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.RetoComplejo;
import es.uex.challengeapp.model.Subtarea;
import es.uex.challengeapp.model.Subtarea.Estado;
import es.uex.challengeapp.repository.RetoComplejoRepository;
import es.uex.challengeapp.repository.SubtareaRepository;

@Service
public class RetoComplejoServiceImpl implements RetoComplejoService {

	@Autowired
	private RetoComplejoRepository retoComplejoRepository;

	@Autowired
	private SubtareaRepository subtareaRepository;

	@Override
	public RetoComplejo guardarRetoComplejo(RetoComplejo retoComplejo) {
		RetoComplejo savedRetoComplejo = retoComplejoRepository.save(retoComplejo);
		if (savedRetoComplejo.getSubtareas() != null) {
			for (Subtarea subtarea : savedRetoComplejo.getSubtareas()) {
				subtarea.setEstado(Estado.PENDIENTE);
				subtarea.setFechaCreacion(new Date(System.currentTimeMillis()));
				subtarea.setRetoComplejo(savedRetoComplejo);
				subtareaRepository.save(subtarea);
			}
		}
		return savedRetoComplejo;
	}

	@Override
	public List<Subtarea> obtenerSubtareas(RetoComplejo retoComplejo) {
		return subtareaRepository.findByRetoComplejoId(retoComplejo.getId());
	}
}
