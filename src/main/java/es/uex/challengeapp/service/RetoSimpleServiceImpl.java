package es.uex.challengeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.model.RetoSimple;
import es.uex.challengeapp.repository.RetoSimpleRepository;

@Service
public class RetoSimpleServiceImpl implements RetoSimpleService {
	
	@Autowired
	private RetoSimpleRepository retoSimpleRepository;

	@Override
	public RetoSimple guardarRetoSimple(RetoSimple retoSimple) {
		return retoSimpleRepository.save(retoSimple);
	}

}
