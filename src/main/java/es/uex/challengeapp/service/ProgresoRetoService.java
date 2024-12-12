package es.uex.challengeapp.service;

import es.uex.challengeapp.model.ProgresoReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;

public interface ProgresoRetoService {

	ProgresoReto actualizarProgreso(ProgresoReto progresoReto);

	ProgresoReto buscarProgresoReto(Usuario usuario, Reto reto);

}
