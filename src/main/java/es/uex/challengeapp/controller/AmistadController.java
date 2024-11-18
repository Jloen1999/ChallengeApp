package es.uex.challengeapp.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/usuario/amigos")
public class AmistadController {
	
	@GetMapping("/anadirAmigo")
	public String anadirAmigo() {
		return "";
	}

}
