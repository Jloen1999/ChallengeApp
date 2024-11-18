package es.uex.challengeapp.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.repository.RetoRepository;

@Service
public class RetoServiceImpl implements RetoService{
    @Autowired
    private RetoRepository retoRepository;

    @Override
    public List<Reto> getNovedososRetos() {
        return retoRepository.findByNovedadTrue();
    }

	@Override
	public Reto crearReto(Reto reto) {
		return retoRepository.save(reto);
	}

	@Override
	public List<Reto> obtenerRetosCreadosPorUsuario(Long usuarioId) {
		return retoRepository.findAllByCreadorId(usuarioId);
	}

	@Override
	public Reto obtenerReto(Long id) {
		return retoRepository.findById(id).orElse(null);
	}

	@Override
	public List<Reto> obtenerTodosLosRetos() {
		return retoRepository.findAll();
	}
    
}