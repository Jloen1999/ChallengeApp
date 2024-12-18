package es.uex.challengeapp.service;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import es.uex.challengeapp.model.Notificacion;
import es.uex.challengeapp.model.Notificacion.TipoNotificacion;
import es.uex.challengeapp.model.ParticipantesReto;
import es.uex.challengeapp.model.ProgresoReto;
import es.uex.challengeapp.model.Reto;
import es.uex.challengeapp.model.Usuario;
import es.uex.challengeapp.repository.RetoRepository;

@Service
public class RetoServiceImpl implements RetoService {

    @Value("${google.api.key}")
    private String apiKey;
    @Autowired
    private RetoRepository retoRepository;

    @Autowired
    private ProgresoRetoService progresoRetoService;

    @Autowired
    private ParticipantesRetoService participantesRetoService;

    @Autowired
    private NotificacionService notificacionService;

    @Autowired
    private AmistadService amistadService;

    @Override
    public List<Reto> getNovedososRetos() {
        return retoRepository.findByNovedadTrue();
    }

    @Override
    public Reto guardarReto(Reto reto) {
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
    public List<Reto> obtenerRetos(Usuario userActual) {
        List<Reto> todosLosRetos = retoRepository.findAll();
        List<Reto> retos = new ArrayList<Reto>();

        if (userActual == null) {
            for (Reto reto : todosLosRetos) {
                if (reto.getVisibilidad()) {
                    retos.add(reto);
                }
            }
            return retos;
        }

        for (Reto reto : todosLosRetos) {
            if (reto.getVisibilidad() || userActual.getId() == reto.getCreador().getId()
                    || amistadService.sonAmigos(userActual, reto.getCreador())) {
                retos.add(reto);
            }
        }
        return retos;
    }

    @Override
    public List<Reto> obtenerRetosNovedosos(Usuario userActual) {
        gestionarRetosNovedosos();

        List<Reto> todosLosRetosNovedosos = retoRepository.findByNovedadTrue();
        List<Reto> retos = new ArrayList<Reto>();

        if (userActual == null) {
            for (Reto reto : todosLosRetosNovedosos) {
                if (reto.getVisibilidad()) {
                    retos.add(reto);
                }
            }
            return retos;
        }

        for (Reto reto : todosLosRetosNovedosos) {
            if (reto.getVisibilidad() || userActual.getId() == reto.getCreador().getId()
                    || amistadService.sonAmigos(userActual, reto.getCreador())) {
                retos.add(reto);
            }
        }
        return retos;
    }

    @Override
    public float tiempoEnCompletado(Usuario usuario, Reto reto) {
        ProgresoReto progresoReto = progresoRetoService.buscarProgresoReto(usuario, reto);
        ParticipantesReto participantesReto = participantesRetoService.obtenerParticipacionReto(usuario, reto);
        float tiempo = 0.0f;

        if (progresoReto != null && participantesReto != null) {
            Date fechaInicio;

            if (participantesReto.getFechaUnion() != null) {
                fechaInicio = participantesReto.getFechaUnion();
            } else {
                fechaInicio = reto.getFechaCreacion();
            }

            Instant inicio = fechaInicio.toInstant();
            Instant fin = progresoReto.getFechaActualizacion().toInstant();

            Duration duracion = Duration.between(inicio, fin);
            Long tiempoLong = duracion.getSeconds();
            tiempo = (float) (tiempoLong / 3600);
        }

        return tiempo;
    }

    @Override
    public List<Reto> obtenerRetosPrivados(Usuario usuario) {
        return retoRepository.findByCreadorAndVisibilidad(usuario, false);
    }

    @Override
    public List<Reto> mostrarRetosPrivadosAmigos(Usuario userActual) {
        List<Usuario> amigos = amistadService.obtenerAmigos(userActual.getId());
        List<Reto> retosPrivados = new ArrayList<>();

        for (Usuario amigo : amigos) {
            retosPrivados.addAll(obtenerRetosPrivados(amigo));
        }

        return retosPrivados;
    }

    // FUNCIONES PRIVADAS AUXILIARES
    private void gestionarRetosNovedosos() {
        List<Reto> retosNovedosos = retoRepository.findByNovedadTrue();
        LocalDateTime fechaActual = LocalDateTime.now();

        for (Reto reto : retosNovedosos) {
            Date fechaCreacionDate = reto.getFechaCreacion();
            LocalDateTime fechaCreacion = fechaCreacionDate.toInstant().atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            if (fechaCreacion.plusDays(5).isBefore(fechaActual)) {
                reto.setNovedad(false);
                retoRepository.save(reto);
            }
        }
    }

    @Override
    public void eliminarReto(Reto reto) {
        List<Notificacion> notificaciones = notificacionService.obtenerNotificacionesPorReto(reto);
        for (Notificacion notificacion : notificaciones) {
            notificacion.setReto(null);
        }

        List<Usuario> participantes = participantesRetoService.obtenerParticipantesDeReto(Long.valueOf(reto.getId()));
        if (!participantes.isEmpty()) {
            Notificacion notificacion = new Notificacion();
            notificacion.setFechaEnvio(new Date(System.currentTimeMillis()));
            notificacion.setMensaje("El reto '" + reto.getNombre() + "', al que te habías unido, ¡ha sido eliminado!");
            notificacion.setLeido(false);
            notificacion.setTipoNotificacion(TipoNotificacion.ELIMINACION_RETO);

            for (Usuario participante : participantes) {
                notificacion.setUsuario(participante);
                notificacionService.crearNotificacion(notificacion);
            }
        }

        retoRepository.delete(reto);
    }

    @Override
    public List<Reto> buscarPorNombre(String criterioBusqueda) {
        return retoRepository.findByNombreContainingIgnoreCase(criterioBusqueda);
    }

    @Override
    public List<Reto> obtenerTodosLosRetos() {
        return retoRepository.findAll();
    }

    @Override
    public List<Reto> buscarRetosPorUbicacion(Usuario usuario, double lat, double lon, int radio) {
        List<Reto> todosLosRetos = obtenerRetos(usuario);
        List<Reto> retosEnRango = new ArrayList<>();

        for (Reto reto : todosLosRetos) {
            String ubicacionCreador = reto.getCreador().getUbicacion();
            Map<String, Double> coordenadasCreador = obtenerLatitudLongitudDeUbicacion(ubicacionCreador);
            double latCreador = coordenadasCreador.get("lat");
            double lonCreador = coordenadasCreador.get("lon");

            double distancia = calcularDistancia(lat, lon, latCreador, lonCreador);

            if (distancia <= radio) {
                retosEnRango.add(reto);
            }
        }

        return retosEnRango;
    }

    private Map<String, Double> obtenerLatitudLongitudDeUbicacion(String ubicacion) {
        String url = String.format("https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s", ubicacion, apiKey);
        Map<String, Double> coordenadas = new HashMap<>();

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> response = restTemplate.getForEntity(url, String.class);
            JSONObject jsonResponse = new JSONObject(response.getBody());

            if (jsonResponse.has("results") && jsonResponse.getJSONArray("results").length() > 0) {
                JSONObject location = jsonResponse.getJSONArray("results").getJSONObject(0).getJSONObject("geometry").getJSONObject("location");
                coordenadas.put("lat", location.getDouble("lat"));
                coordenadas.put("lon", location.getDouble("lng"));
            }
        } catch (Exception e) {
            coordenadas.put("lat", 0.0);
            coordenadas.put("lon", 0.0);
        }

        return coordenadas;
    }

    private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radius of the earth in kilometers
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2) + Math.cos(Math.toRadians(lat1))
                * Math.cos(Math.toRadians(lat2)) * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c; // Distance in kilometers
    }
}