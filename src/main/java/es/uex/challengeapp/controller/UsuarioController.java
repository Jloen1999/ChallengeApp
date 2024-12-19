package es.uex.challengeapp.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import es.uex.challengeapp.model.Comentario;
import es.uex.challengeapp.model.Estadistica;
import es.uex.challengeapp.model.Notificacion;
import es.uex.challengeapp.model.Notificacion.TipoNotificacion;
import es.uex.challengeapp.model.ParticipantesReto;
import es.uex.challengeapp.model.ProgresoReto;
import es.uex.challengeapp.model.Punto;
import es.uex.challengeapp.model.Recompensa;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Reto.Estado;
import es.uex.challengeapp.model.Reto.Tipo;
import es.uex.challengeapp.model.RetoComplejo;
import es.uex.challengeapp.model.RetoSimple;
import es.uex.challengeapp.model.Subtarea;
import es.uex.challengeapp.model.UbicacionDTO;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.service.AmistadService;
import es.uex.challengeapp.service.ComentarioService;
import es.uex.challengeapp.service.EstadisticaService;
import es.uex.challengeapp.service.NotificacionService;
import es.uex.challengeapp.service.ParticipantesRetoService;
import es.uex.challengeapp.service.ProgresoRetoService;
import es.uex.challengeapp.service.PuntoService;
import es.uex.challengeapp.service.RecompensaService;
import es.uex.challengeapp.service.RetoComplejoService;
import es.uex.challengeapp.service.RetoService;
import es.uex.challengeapp.service.RetoSimpleService;
import jakarta.servlet.http.HttpSession;

@Controller
@RequestMapping("/usuario")
public class UsuarioController {

    @Value("${google.api.key}")
    private String apiKey;

    @Autowired
    private RetoService retoService;

    @Autowired
    private RetoSimpleService retoSimpleService;

    @Autowired
    private RetoComplejoService retoComplejoService;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private ParticipantesRetoService participantesRetoService;

    @Autowired
    private ComentarioService comentarioService;

    @Autowired
    private AmistadService amistadService;

    @Autowired
    private ProgresoRetoService progresoRetoService;

    @Autowired
    private EstadisticaService estadisticaService;

    @Autowired
    private RecompensaService recompensaService;

    @Autowired
    private PuntoService puntoService;


    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {
        model.addAttribute("estaLogueado", true);
        model.addAttribute("usuario", new Usuario());
        return "registro";
    }

    @GetMapping("/dashboard")
    public String mostrarPerfilUsuario(Model model, HttpSession session) {
        Usuario userActual = (Usuario) session.getAttribute("userActual");
        if (userActual != null) {
            model.addAttribute("estaLogueado", true);
            model.addAttribute("nombreUsuario", userActual.getNombre());
            return "dashboard";
        }
        return "redirect:/login";
    }

    @GetMapping("/misEstadisticas")
    public String mostrarEstadisticasUsuario(Model model, HttpSession session) {
        Usuario usuario = (Usuario) session.getAttribute("userActual");
        if (usuario == null) {
            return "redirect:/login";
        }

        Estadistica estadistica = estadisticaService.actualizarEstadistica(usuario);
        String tiempoConvertido = convertirTiempoPromedio(estadistica.getTiempoPromedio());

        model.addAttribute("estaLogueado", true);
        model.addAttribute("tiempoConvertido", tiempoConvertido);
        model.addAttribute("estadistica", estadistica);

        return "misEstadisticas";
    }

    @GetMapping("/misRetos")
    public String mostrarRetosUsuario(Model model, HttpSession session) {
        Usuario userActual = (Usuario) session.getAttribute("userActual");
        if (userActual != null) {
            List<Reto> retos = participantesRetoService.obtenerRetosDeUsuario(Long.valueOf(userActual.getId()));
            for (Reto reto : retos) {
                if (progresoRetoService.estaCompletado(userActual, reto)) {
                    reto.setEstado(Estado.COMPLETADO);
                } else if (progresoRetoService.estaEnProgreso(userActual, reto)) {
                    reto.setEstado(Estado.EN_PROGRESO);
                } else if (progresoRetoService.estaFallido(userActual, reto)) {
                    reto.setEstado(Estado.FALLIDO);
                }
            }
            model.addAttribute("estaLogueado", true);
            model.addAttribute("retos", retos);
            return "misRetos";
        }
        return "redirect:/login";
    }

    @GetMapping("/retosCreados")
    public String mostrarRetosCreados(Model model, HttpSession session) {
        Usuario userActual = (Usuario) session.getAttribute("userActual");
        if (userActual != null) {
            List<Reto> retos = retoService.obtenerRetosCreadosPorUsuario(Long.valueOf(userActual.getId()));
            model.addAttribute("retos", retos);
            model.addAttribute("estaLogueado", true);
            return "retosCreados";
        }
        return "redirect:/login";
    }

    @GetMapping("/amigos")
    public String mostrarAmigos(HttpSession session, Model model) {
        Usuario userActual = (Usuario) session.getAttribute("userActual");
        if (userActual != null) {
            List<Usuario> listaAmigos = amistadService.obtenerAmigos(userActual.getId());
            model.addAttribute("amigos", listaAmigos);
            model.addAttribute("estaLogueado", true);
            return "amigos";
        }
        return "redirect:/login";
    }

    @GetMapping("/notificaciones")
    public String mostrarNotificacionesUsuario(Model model, HttpSession session) {
        Usuario userActual = (Usuario) session.getAttribute("userActual");
        if (userActual != null) {
            List<Notificacion> notificaciones = notificacionService
                    .obtenerNotificacionesPorUsuario(Long.valueOf(userActual.getId()));
            model.addAttribute("notificaciones", notificaciones);
            model.addAttribute("estaLogueado", true);
            return "notificaciones";
        }
        return "redirect:/login";
    }

    @GetMapping("/recompensas")
    public String mostrarRecompensas(Model model, HttpSession session) {
        Usuario userActual = (Usuario) session.getAttribute("userActual");
        if (userActual == null) {
            return "redirect:/login";
        }

        List<Recompensa> medallasBronce = recompensaService.obtenerRecompensasBronceUsuario(userActual);
        List<Recompensa> medallasPlata = recompensaService.obtenerRecompensasPlataUsuario(userActual);
        List<Recompensa> medallasOro = recompensaService.obtenerRecompensasOroUsuario(userActual);
        List<Recompensa> medallasDiamante = recompensaService.obtenerRecompensasDiamanteUsuario(userActual);

        int totalMedallasBronce = medallasBronce.size();
        int totalMedallasPlata = medallasPlata.size();
        int totalMedallasOro = medallasOro.size();
        int totalMedallasDiamante = medallasDiamante.size();

        List<Punto> puntos = puntoService.obtenerPuntosUsuario(userActual);
        int totalPuntos = puntoService.totalPuntosUsuario(userActual);

        model.addAttribute("puntos", puntos);
        model.addAttribute("totalPuntos", totalPuntos);

        model.addAttribute("medallasBronce", totalMedallasBronce);
        model.addAttribute("medallasPlata", totalMedallasPlata);
        model.addAttribute("medallasOro", totalMedallasOro);
        model.addAttribute("medallasDiamante", totalMedallasDiamante);

        model.addAttribute("estaLogueado", true);

        return "recompensas";
    }

    @GetMapping("/crearReto")
    public String mostrarFormularioCrearReto(Model model, HttpSession session) {
        Usuario userActual = (Usuario) session.getAttribute("userActual");
        if (userActual != null) {
            model.addAttribute("retoSimple", new RetoSimple());
            model.addAttribute("retoComplejo", new RetoComplejo());
            model.addAttribute("estaLogueado", true);
            return "crearReto";
        }
        return "redirect:/login";
    }

    @PostMapping("/crearReto")
    public String crearReto(@RequestParam("tipoReto") String tipoReto,
                            @ModelAttribute("retoSimple") RetoSimple retoSimple,
                            @ModelAttribute("retoComplejo") RetoComplejo retoComplejo, @RequestParam("imagenReto") MultipartFile imagen,
                            Model model, HttpSession session) {
        Usuario userActual = (Usuario) session.getAttribute("userActual");
        if (userActual == null) {
            return "redirect:/login";
        }

        Reto reto = "SIMPLE".equals(tipoReto) ? retoSimple : retoComplejo;

        reto.setCreador(userActual);
        reto.setEstado(Estado.PENDIENTE);
        reto.setNovedad(true);
        reto.setPorcentajeProgreso(0.0f);
        reto.setFechaCreacion(new Date(System.currentTimeMillis()));

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(reto.getFechaCreacion());
        calendar.add(Calendar.DAY_OF_MONTH, reto.getDuracion());
        reto.setFechaFinalizacion(calendar.getTime());

        if (!imagen.isEmpty()) {
            try {
                Path path = Paths.get("src/main/resources/templates/images/" + imagen.getOriginalFilename());
                Files.copy(imagen.getInputStream(), path);
                reto.setUrl(imagen.getOriginalFilename());
            } catch (IOException e) {
                model.addAttribute("error", "Error al subir la imagen.");
                System.err.println("Error al subir la imagen.");
                return "crearReto";
            }
        }

        try {
            if ("SIMPLE".equals(tipoReto)) {
                reto.setTipo(Tipo.SIMPLE);
                retoSimpleService.guardarRetoSimple(retoSimple);
            } else if ("COMPLEJO".equals(tipoReto)) {
                reto.setTipo(Tipo.COMPLEJO);
                retoComplejoService.guardarRetoComplejo(retoComplejo);
            } else {
                model.addAttribute("error", "Tipo de reto inválido.");
                return "crearReto";
            }
        } catch (Exception e) {
            model.addAttribute("error", "Error al guardar el reto: " + e.getMessage());
            System.err.println("Error al guardar el reto: " + e.getMessage());
            return "crearReto";
        }

        model.addAttribute("mensaje", "Reto añadido correctamente.");
        generarNotificacion(userActual, reto, "CREACION_RETO");
        model.addAttribute("reto", new Reto());
        return "crearReto";
    }

    @GetMapping("/unirse")
    public String unirseAunReto(@RequestParam Integer retoId, Model model, HttpSession session) {
        Usuario userActual = (Usuario) session.getAttribute("userActual");
        if (userActual != null) {
            Reto reto = retoService.obtenerReto(Long.valueOf(retoId));
            if (reto != null) {
                boolean yaUnido = participantesRetoService.unidoAlReto(Long.valueOf(userActual.getId()),
                        Long.valueOf(retoId));

                if (yaUnido) {
                    model.addAttribute("mensaje", "Ya estás unido a este reto.");
                    return "redirect:/usuario/reto/" + retoId;
                }

                ParticipantesReto participantesReto = new ParticipantesReto();
                participantesReto.setUsuario(userActual);
                participantesReto.setReto(reto);
                participantesReto.setFechaUnion(new Date(System.currentTimeMillis()));

                ProgresoReto progresoReto = new ProgresoReto();
                progresoReto.setProgresoActual(0.0f);
                progresoReto.setReto(reto);
                progresoReto.setUsuario(userActual);
                progresoReto.setFechaActualizacion(new Date(System.currentTimeMillis()));

                participantesRetoService.unirseAReto(participantesReto);
                progresoRetoService.actualizarProgreso(progresoReto);
                generarNotificacion(userActual, reto, "UNION_RETO");
                model.addAttribute("mensaje", "Te has unido correctamente");
                return "redirect:/usuario/reto/" + retoId;
            }
        }
        return "redirect:/login";
    }

    @GetMapping("/reto/{id}")
    public String mostrarReto(@PathVariable Integer id, HttpSession session, Model model) {
        Reto reto = retoService.obtenerReto(Long.valueOf(id));
        if (reto != null) {
            List<Usuario> participantes = participantesRetoService.obtenerParticipantesDeReto(Long.valueOf(id));
            List<Comentario> comentarios = comentarioService.obtenerComentariosPorReto(Long.valueOf(id));

            Usuario usuario = (Usuario) session.getAttribute("userActual");
            if (usuario == null) {
                return "redirect:/login";
            }

            Boolean estaUnido = participantesRetoService.unidoAlReto(Long.valueOf(usuario.getId()), Long.valueOf(id));

            if (estaUnido) {
                ProgresoReto progresoReto = progresoRetoService.buscarProgresoReto(usuario, reto);

                float progresoActual = 0.0f;
                if (progresoReto.getProgresoActual() != null) {
                    progresoActual = progresoReto.getProgresoActual();
                }
                reto.setPorcentajeProgreso(progresoActual);

                if (reto.getTipo() == Tipo.SIMPLE) {
                    RetoSimple retoSimple = (RetoSimple) reto;
                    int progreso = (int) ((progresoActual * retoSimple.getCantidad()) / 100);
                    retoSimple.setProgresoArray(progreso);
                    model.addAttribute("reto", retoSimple);
                } else {
                    RetoComplejo retoComplejo = (RetoComplejo) reto;
                    List<Subtarea> subtareas = retoComplejoService.obtenerSubtareas(retoComplejo);
                    int completados = (int) Math.round((progresoActual * subtareas.size()) / 100.0);

                    int cont = 0;
                    for (Subtarea subtarea : subtareas) {
                        if (cont < completados) {
                            subtarea.setEstado(Subtarea.Estado.COMPLETADA);
                        }
                        cont++;
                    }

                    model.addAttribute("subtareas", subtareas);
                    model.addAttribute("reto", retoComplejo);
                }
            } else {
                reto.setPorcentajeProgreso(0.0f);
                model.addAttribute("reto", reto);
            }

            model.addAttribute("esCreador", usuario.getId() == reto.getCreador().getId());
            model.addAttribute("estaUnido", estaUnido);
            model.addAttribute("participantes", participantes);
            model.addAttribute("comentarios", comentarios);
        }
        return "reto";
    }

    @PostMapping("/comentar")
    public String comentar(@RequestParam String comentarioTexto, @RequestParam Integer retoId, HttpSession session) {
        Usuario usuarioActual = (Usuario) session.getAttribute("userActual");
        if (usuarioActual != null) {
            Comentario comentario = new Comentario();
            comentario.setTexto(comentarioTexto);
            comentario.setFecha(new Date(System.currentTimeMillis()));
            comentario.setUsuario(usuarioActual);
            Reto reto = retoService.obtenerReto(Long.valueOf(retoId));
            if (reto != null) {
                comentario.setReto(reto);
                comentarioService.hacerComentario(comentario);
                return "redirect:/usuario/reto/" + retoId;
            }
        }
        return "redirect:/login";
    }

    @PostMapping("/borrarNotificacion/{id}")
    public String borrarNotificacion(@PathVariable Long id, HttpSession session, Model model) {
        if ((Usuario) session.getAttribute("userActual") != null) {
            notificacionService.eliminarNotificacion(id);
            mostrarNotificacionesUsuario(model, session);
        }
        return "notificaciones";

    }

    @GetMapping("/buscarUbicacionActual")
    public ResponseEntity<UbicacionDTO> buscarUbicacionActual(@RequestParam double lat, @RequestParam double lon) {
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?latlng=%s,%s&key=%s", lat, lon, apiKey);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject jsonResponse = new JSONObject(response.getBody());

            if (jsonResponse.has("results")) {
                JSONArray results = jsonResponse.getJSONArray("results");
                if (results.length() > 0) {
                    JSONObject result = results.getJSONObject(0);
                    String nombreUbicacion = result.getString("formatted_address");
                    return ResponseEntity.ok(new UbicacionDTO(null, nombreUbicacion));
                }
            }

            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @GetMapping("/buscarRetosPorUbicacion")
    public String buscarRetosPorUbicacion(@RequestParam String ubicacion, @RequestParam int radio,
                                          HttpSession session, Model model) {
        // Convertir la ubicación a coordenadas (lat, lon) usando la API de Google
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s", ubicacion, apiKey);

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject jsonResponse = new JSONObject(response.getBody());

            if (!jsonResponse.has("results") || jsonResponse.getJSONArray("results").length() == 0) {
                return "redirect:/";
            }

            JSONObject location = jsonResponse.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
            Usuario usuario = (Usuario) session.getAttribute("userActual");
            double lat = location.getDouble("lat");
            double lon = location.getDouble("lng");
            List<Reto> retos = retoService.buscarRetosPorUbicacion(usuario, lat, lon, radio);

            model.addAttribute("estaLogueado", true);
            model.addAttribute("retosBusqueda", retos);

            return "index";
        } catch (Exception e) {
            return "redirect:/";
        }
    }


    // FUNCIONES PRIVADAS AUXLIARES
    private void generarNotificacion(Usuario userActual, Reto reto, String tipoNotificacion) {
        Notificacion notificacion = new Notificacion();

        String mensaje = "";
        if ("CREACION_RETO".equals(tipoNotificacion)) {
            mensaje = "¡Has creado un nuevo reto: " + reto.getNombre() + "!";
            notificacion.setTipoNotificacion(TipoNotificacion.CREACION_RETO);
        } else if ("UNION_RETO".equals(tipoNotificacion)) {
            mensaje = "¡Te has unido al reto: " + reto.getNombre() + "!";
            notificacion.setTipoNotificacion(TipoNotificacion.UNION_RETO);
        }

        notificacion.setMensaje(mensaje);
        notificacion.setLeido(false);
        notificacion.setFechaEnvio(new Date(System.currentTimeMillis()));

        notificacion.setUsuario(userActual);
        notificacion.setReto(reto);

        notificacionService.crearNotificacion(notificacion);
    }

    private String convertirTiempoPromedio(float tiempoPromedio) {
        if (tiempoPromedio == 0) {
            return "0 minutos"; // Si es 0, se muestra como "0 minutos"
        }

        // Convertimos el tiempo en horas a días, horas y minutos
        int dias = (int) (tiempoPromedio / 24);
        int horas = (int) (tiempoPromedio % 24);
        int minutos = (int) ((tiempoPromedio - (int) tiempoPromedio) * 60);

        StringBuilder tiempoFormateado = new StringBuilder();

        // Solo agregamos días si no son 0
        if (dias > 0) {
            tiempoFormateado.append(dias).append(" días");
        }

        // Solo agregamos horas si no son 0
        if (horas > 0) {
            if (tiempoFormateado.length() > 0) {
                tiempoFormateado.append(", ");
            }
            tiempoFormateado.append(horas).append(" horas");
        }

        // Solo agregamos minutos si no son 0
        if (minutos > 0) {
            if (tiempoFormateado.length() > 0) {
                tiempoFormateado.append(", ");
            }
            tiempoFormateado.append(minutos).append(" minutos");
        }

        // Si todo es 0, mostramos "0 minutos"
        if (tiempoFormateado.length() == 0) {
            tiempoFormateado.append("0 minutos");
        }

        return tiempoFormateado.toString();
    }

}
