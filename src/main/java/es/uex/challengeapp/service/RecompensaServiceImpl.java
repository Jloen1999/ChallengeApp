package es.uex.challengeapp.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import es.uex.challengeapp.repository.RecompensaRepository;

@Service
public class RecompensaServiceImpl implements RecompensaService {

	@Autowired
	private RecompensaRepository recompensaRepository;

}
