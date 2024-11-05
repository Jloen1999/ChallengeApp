package es.uex.challengeapp.service;

import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.repository.RetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RetoService {
    @Autowired
    private RetoRepository retoRepository;

    public List<Reto> getNovedososRetos() {
        return retoRepository.findByNovedadTrue();
    }
}