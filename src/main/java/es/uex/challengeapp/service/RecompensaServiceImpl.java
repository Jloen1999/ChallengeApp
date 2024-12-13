package es.uex.challengeapp.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.Recompensa;
import es.uex.challengeapp.model.Recompensa.TipoMedalla;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.RecompensaRepository;

@Service
public class RecompensaServiceImpl implements RecompensaService {

	@Autowired
	private RecompensaRepository recompensaRepository;

	@Override
	public List<Recompensa> obtenerRecompensasDeUsuario(Usuario userActual) {
		return recompensaRepository.findByUsuario(userActual);
	}

	@Override
	public List<Recompensa> obtenerRecompensasBronceUsuario(Usuario usuario) {
		List<Recompensa> recompensasBronce=new ArrayList<Recompensa>();
		List<Recompensa> todasRecompensas=obtenerRecompensasDeUsuario(usuario);
		
		for(Recompensa recompensa:todasRecompensas) {
			if(recompensa.getTipo()==TipoMedalla.BRONCE) {
				recompensasBronce.add(recompensa);
			}
		}
		return recompensasBronce;
	}

	@Override
	public List<Recompensa> obtenerRecompensasPlataUsuario(Usuario usuario) {
		List<Recompensa> recompensasPlata=new ArrayList<Recompensa>();
		List<Recompensa> todasRecompensas=obtenerRecompensasDeUsuario(usuario);
		
		for(Recompensa recompensa:todasRecompensas) {
			if(recompensa.getTipo()==TipoMedalla.PLATA) {
				recompensasPlata.add(recompensa);
			}
		}
		return recompensasPlata;
	}

	@Override
	public List<Recompensa> obtenerRecompensasOroUsuario(Usuario usuario) {
		List<Recompensa> recompensasOro=new ArrayList<Recompensa>();
		List<Recompensa> todasRecompensas=obtenerRecompensasDeUsuario(usuario);
		
		for(Recompensa recompensa:todasRecompensas) {
			if(recompensa.getTipo()==TipoMedalla.ORO) {
				recompensasOro.add(recompensa);
			}
		}
		return recompensasOro;
	}

	@Override
	public List<Recompensa> obtenerRecompensasDiamanteUsuario(Usuario usuario) {
		List<Recompensa> recompensasDiamante=new ArrayList<Recompensa>();
		List<Recompensa> todasRecompensas=obtenerRecompensasDeUsuario(usuario);
		
		for(Recompensa recompensa:todasRecompensas) {
			if(recompensa.getTipo()==TipoMedalla.DIAMANTE) {
				recompensasDiamante.add(recompensa);
			}
		}
		return recompensasDiamante;
	}

	@Override
	public Recompensa guardarRecompensa(Recompensa recompensa) {
		return recompensaRepository.save(recompensa);
	}

	@Override
	public List<Recompensa> obtenerRecompensasPorTipoYUsuario(TipoMedalla tipo, Usuario usuario) {
		return recompensaRepository.findByTipoAndUsuario(tipo, usuario);
	}


}
