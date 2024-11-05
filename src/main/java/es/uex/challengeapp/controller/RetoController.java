package es.uex.challengeapp.controller;

import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.service.RetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/retos")
public class RetoController {
    @Autowired
    private RetoService retoService;

    @GetMapping
    public String index(Model model) {
        List<Reto> retosNovedosos = retoService.getNovedososRetos();
        model.addAttribute("retosNovedosos", retosNovedosos);
        return "index";
    }
}