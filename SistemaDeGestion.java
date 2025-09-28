import java.io.*;
import java.util.*;
import java.nio.charset.StandardCharsets;


public class SistemaDeGestion {
    private Map<String, Estudiante> estudiantesMap = new HashMap<>();
    private List<Beca> becas = new ArrayList<>();
    private List<SolicitudBeca> solicitudes = new ArrayList<>();

    private final String DATA_FILE = "data.csv"; // persistencia básica

    public SistemaDeGestion() {
        // datos iniciales (SIA1.4)
        agregarBeca("Beca Academica", "Promedio superior a 6.0");
        agregarBeca("Beca Deportiva", "Excelente rendimiento deportivo");
        agregarBeca("Beca Cultural", "Participacion en actividades culturales");
        agregarBeca("Beca Socioeconomica", "Situacion socioeconomica vulnerable");
        agregarBeca("Beca Liderazgo", "Destacado liderazgo estudiantil");
        agregarBeca("Beca Innovacion", "Proyectos de investigacion o innovacion");
        agregarBeca("Beca Voluntariado", "Horas certificadas de servicio comunitario");
        agregarBeca("Beca Excelencia", "Rendimiento academico sobresaliente en toda la carrera");

        // ============ EJEMPLOS DE ESTUDIANTES ============
        inicializarEstudiantesEjemplo();

        // carga persistente al iniciar
        try {
            cargarDatos();
        } catch (IOException e) {
            System.out.println("No se pudo cargar data inicial: " + e.getMessage());
        }
    }

    private void inicializarEstudiantesEjemplo() {
        // Estudiante 1: Juan Pérez
        agregarEstudiante("Juan Perez", "20230001");

        // Estudiante 2: María López
        agregarEstudiante("Maria Lopez", "20230002");

        // Estudiante 3: Carlos García
        agregarEstudiante("Carlos Garcia", "20230003");

        // Crear algunas solicitudes de ejemplo para demostrar funcionalidad
        crearSolicitudesEjemplo();

    }

    private void crearSolicitudesEjemplo() {
        // Juan Pérez - Estudiante aplicando a becas académicas
        crearSolicitud("20230001", "Beca Academica");
        crearSolicitud("20230001", "Beca Excelencia");

        // María López - Estudiante con perfil diverso
        crearSolicitud("20230002", "Beca Cultural");
        crearSolicitud("20230002", "Beca Liderazgo");
        crearSolicitud("20230002", "Beca Socioeconomica");

        // Carlos García - Estudiante deportista
        crearSolicitud("20230003", "Beca Deportiva");
        crearSolicitud("20230003", "Beca Voluntariado");

        // Aprobar algunas solicitudes para tener datos variados
        aprobarRechazarSolicitud("20230001", "Beca Academica", "Aprobada");
        aprobarRechazarSolicitud("20230002", "Beca Cultural", "Aprobada");
        aprobarRechazarSolicitud("20230002", "Beca Socioeconomica", "Rechazada");
        aprobarRechazarSolicitud("20230003", "Beca Deportiva", "Aprobada");
    }


    // ------- ESTUDIANTES (Colección 1)
    public void agregarEstudiante(String nombre, String id) {
        Estudiante nuevo = new Estudiante(nombre, id);
        estudiantesMap.put(id, nuevo);
        System.out.println("Estudiante agregado: " + nombre);
    }

    public void mostrarEstudiantes() {
        if (estudiantesMap.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
            return;
        }
        for (Estudiante e : estudiantesMap.values()) {
            System.out.println(e);
        }
    }

    public Estudiante buscarEstudiantePorID(String id) {
        return estudiantesMap.get(id);
    }

    // usar excepción propia
    public Estudiante obtenerEstudianteOrThrow(String id) throws EstudianteNotFoundException {
        Estudiante e = estudiantesMap.get(id);
        if (e == null) throw new EstudianteNotFoundException("Estudiante con ID " + id + " no existe.");
        return e;
    }

    public void editarEstudiante(String id, String nuevoNombre) {
        Estudiante estudiante = estudiantesMap.get(id);
        if (estudiante != null) {
            estudiante.setNombre(nuevoNombre);
            System.out.println("Datos del estudiante actualizados.");
        } else {
            System.out.println("Estudiante no encontrado.");
        }
    }

    public void eliminarEstudiante(String id) {
        if (estudiantesMap.remove(id) != null) {
            // además eliminar solicitudes asociadas
            solicitudes.removeIf(s -> s.getEstudiante().getId().equals(id));
            System.out.println("Estudiante eliminado.");
        } else {
            System.out.println("Estudiante no encontrado.");
        }
    }

    // ------- BECAS
    public void agregarBeca(String tipo, String criterio) {
        // Verificar si ya existe una beca con el mismo tipo (nombre)
        for (Beca existente : becas) {
            if (existente.getTipo().equalsIgnoreCase(tipo.trim())) {
                System.out.println("Ya existe una beca con el tipo: " + tipo.trim());
                return; // No agregar la beca duplicada
            }
        }

        becas.add(new Beca(tipo.trim(), criterio.trim()));
        System.out.println("Beca agregada correctamente: " + tipo.trim());
    }


    public void mostrarBecas() {
        if (becas.isEmpty()) {
            System.out.println("No hay becas registradas.");
            return;
        }
        for (Beca b : becas) System.out.println(b);
    }

    // ahora lanza excepción si no encuentra la beca
    public Beca buscarBecaPorCodigo(String tipo) throws BecaNotFoundException {
        for (Beca b : becas) {
            if (b.getTipo().equalsIgnoreCase(tipo)) return b;
        }
        throw new BecaNotFoundException("Beca con tipo '" + tipo + "' no encontrada.");
    }


    public void editarBeca(String tipo, String nuevoCriterio) {
        try {
            Beca b = buscarBecaPorCodigo(tipo);
            b.setCriterio(nuevoCriterio);
            System.out.println("Criterio de la beca actualizado.");
        } catch (BecaNotFoundException ex) {
            // manejo local: imprimimos mensaje (podrías propagar si prefieres)
            System.out.println("No se pudo editar: " + ex.getMessage());
        }
    }

    public void eliminarBeca(String tipo) {
        try {
            Beca b = buscarBecaPorCodigo(tipo);
            becas.remove(b);
            System.out.println("Beca eliminada.");
        } catch (BecaNotFoundException ex) {
            System.out.println("No se pudo eliminar: " + ex.getMessage());
        }
    }

    // ------- SOLICITUDES (Colección anidada, SIA1.5 & SIA1.8)
    // validar también la existencia de la beca cuando se crea la solicitud por tipo

    // validar también la existencia de la beca cuando se crea la solicitud por tipo
    public void crearSolicitud(String idEstudiante, String tipoBeca) {
        Estudiante estudiante = estudiantesMap.get(idEstudiante);
        if (estudiante != null) {
            try {
                Beca b = buscarBecaPorCodigo(tipoBeca); // si no existe, lanza excepción

                // 🚨 Verificar si el estudiante ya tiene una solicitud para esta beca
                for (SolicitudBeca solicitudExistente : estudiante.getSolicitudes()) {
                    if (solicitudExistente.getTipoBeca().equalsIgnoreCase(tipoBeca)) {
                        System.out.println("El estudiante " + estudiante.getNombre() +
                                " ya tiene una solicitud para la beca: " + tipoBeca);
                        return;
                    }
                }

                SolicitudBeca s = new SolicitudBeca(estudiante, b.getTipo());
                solicitudes.add(s);
                estudiante.getSolicitudes().add(s);
                System.out.println("Solicitud creada exitosamente.");
            } catch (BecaNotFoundException ex) {
                System.out.println("No se puede crear la solicitud: " + ex.getMessage());
            }
        } else {
            System.out.println("Estudiante no encontrado.");
        }
    }

    // Sobrecarga (SIA1.6)
    public void crearSolicitud(Estudiante estudiante, Beca beca) {
        if (estudiante != null && beca != null) {
            // Verificar si el estudiante ya tiene una solicitud para esta beca
            for (SolicitudBeca solicitudExistente : estudiante.getSolicitudes()) {
                if (solicitudExistente.getTipoBeca().equalsIgnoreCase(beca.getTipo())) {
                    System.out.println("El estudiante " + estudiante.getNombre() +
                            " ya tiene una solicitud para la beca: " + beca.getTipo());
                    return;
                }
            }

            SolicitudBeca s = new SolicitudBeca(estudiante, beca.getTipo());
            solicitudes.add(s);
            estudiante.getSolicitudes().add(s);
            System.out.println("Solicitud creada exitosamente (sobrecarga).");
        } else {
            System.out.println("Estudiante o beca no válido.");
        }
    }

    // otra sobrecarga de mostrar (SIA1.6)
    public void mostrarSolicitudes() {
        if (solicitudes.isEmpty()) {
            System.out.println("No hay solicitudes registradas.");
            return;
        }
        for (SolicitudBeca s : solicitudes) System.out.println(s);
    }

    public void mostrarSolicitudes(String estado) {
        for (SolicitudBeca s : solicitudes) {
            if (s.getEstado().equalsIgnoreCase(estado)) System.out.println(s);
        }
    }

    public void mostrarSolicitudes(Estudiante estudiante) { // sobrecarga adicional
        for (SolicitudBeca s : solicitudes) {
            if (s.getEstudiante().getId().equals(estudiante.getId())) System.out.println(s);
        }
    }


    public void aprobarRechazarSolicitud(String idEstudiante, String tipoBeca, String estado) {
        for (SolicitudBeca s : solicitudes) {
            if (s.getEstudiante().getId().equals(idEstudiante) && s.getTipoBeca().equals(tipoBeca)) {
                s.setEstado(estado);
                System.out.println("Solicitud " + estado);
                return;
            }
        }
        System.out.println("Solicitud no encontrada.");
    }

    // eliminar/editar solicitud (SIA2.4)
    public void eliminarSolicitud(String idEstudiante, String tipoBeca) {
        Iterator<SolicitudBeca> it = solicitudes.iterator();
        while (it.hasNext()) {
            SolicitudBeca s = it.next();
            if (s.getEstudiante().getId().equals(idEstudiante) && s.getTipoBeca().equals(tipoBeca)) {
                it.remove();
                // también del estudiante
                s.getEstudiante().getSolicitudes().remove(s);
                System.out.println("Solicitud eliminada.");
                return;
            }
        }
        System.out.println("Solicitud no encontrada.");
    }



    // Búsqueda multi-nivel (SIA2.13)
    public SolicitudBeca buscarSolicitud(String idEstudiante, String tipoBeca) {
        for (SolicitudBeca s : solicitudes) {
            if (s.getEstudiante().getId().equals(idEstudiante) && s.getTipoBeca().equals(tipoBeca)) return s;
        }
        return null;
    }

    // ------- REPORTES / EXPORT (SIA2.10)
    public void exportarReportes(String archivo) {
        try (PrintWriter pw = new PrintWriter(new FileWriter(archivo))) {
            pw.println("Tipo,Detalle");
            // estudiantes
            for (Estudiante e : estudiantesMap.values()) {
                pw.println("Estudiante," + e.getNombre() + "," + e.getId());
            }
            // becas
            for (Beca b : becas) {
                pw.println("Beca," + b.getTipo() + "," + b.getCriterio());
            }
            // solicitudes
            for (SolicitudBeca s : solicitudes) {
                pw.println("Solicitud," + s.getEstudiante().getId() + "," + s.getTipoBeca() + "," + s.getEstado());
            }
            System.out.println("Reporte exportado a: " + archivo);
        } catch (IOException e) {
            System.out.println("Error al exportar reporte: " + e.getMessage());
        }
    }

    // ------- PERSISTENCIA SIMPLE (SIA2.2) CSV lineal
    public void guardarDatos() throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(DATA_FILE))) {
            // Estudiantes
            for (Estudiante e : estudiantesMap.values()) {
                pw.println("ESTUDIANTE;" + e.getNombre() + ";" + e.getId());
            }
            // Becas
            for (Beca b : becas) {
                pw.println("BECA;" + b.getTipo() + ";" + b.getCriterio());
            }
            // Solicitudes
            for (SolicitudBeca s : solicitudes) {
                pw.println("SOL;" + s.getEstudiante().getId() + ";" + s.getTipoBeca() + ";" + s.getEstado());
            }
        }
    }

    public void cargarDatos() throws IOException {
        File f = new File(DATA_FILE);
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(";");
                if (parts.length == 0) continue;
                switch (parts[0]) {
                    case "ESTUDIANTE":
                        if (parts.length >= 3) agregarEstudiante(parts[1], parts[2]);
                        break;
                    case "BECA":
                        if (parts.length >= 3) agregarBeca(parts[1], parts[2]);
                        break;
                    case "SOL":
                        if (parts.length >= 4) {
                            String idEst = parts[1];
                            String tipo = parts[2];
                            String estado = parts[3];
                            Estudiante e = estudiantesMap.get(idEst);
                            if (e != null) {
                                SolicitudBeca s = new SolicitudBeca(e, tipo);
                                s.setEstado(estado);
                                solicitudes.add(s);
                                e.getSolicitudes().add(s);
                            }
                        }
                        break;
                }
            }
        } catch (IOException e) {
            throw e;
        }
    }

    // Guardar al salir (usado por Menu)
    public void guardarAlSalir() {
        try {
            guardarDatos();
            System.out.println("Datos guardados en " + DATA_FILE);
        } catch (IOException e) {
            System.out.println("Error al guardar datos: " + e.getMessage());
        }
    }

    // Función de filtrado util (SIA2.5) - ejemplo: obtener estudiantes con > n solicitudes
    public List<Estudiante> filtrarEstudiantesPorNumSolicitudes(int minSolicitudes) {
        List<Estudiante> resultados = new ArrayList<>();
        for (Estudiante e : estudiantesMap.values()) {
            if (e.getSolicitudes().size() >= minSolicitudes) resultados.add(e);
        }
        return resultados;
    }

    // conteo por estado (ejemplo de reporte)
    public Map<String, Integer> totalSolicitudesPorEstadoMap() {
        Map<String, Integer> estadoCount = new HashMap<>();
        for (SolicitudBeca s : solicitudes) {
            estadoCount.put(s.getEstado(), estadoCount.getOrDefault(s.getEstado(), 0) + 1);
        }
        return estadoCount;
    }

    // ------- NUEVA FUNCIÓN
    public void listadoDeEstudiantesConSolicitudes() {
        boolean encontrado = false;
        for (Estudiante e : estudiantesMap.values()) {
            if (!e.getSolicitudes().isEmpty()) {
                System.out.println("Estudiante: " + e.getNombre() + " (ID: " + e.getId() + ")");
                for (SolicitudBeca s : e.getSolicitudes()) {
                    System.out.println("   - " + s.getTipoBeca() + " [" + s.getEstado() + "]");
                }
                encontrado = true;
            }
        }
        if (!encontrado) {
            System.out.println("No hay estudiantes con solicitudes registradas.");
        }
    }


    public void generarReporteTXT(String nombreArchivo) {
        try (FileWriter writer = new FileWriter(nombreArchivo, StandardCharsets.UTF_8)) {
            writer.write("=== REPORTE DEL SISTEMA DE BECAS ===\n\n");
            writer.write("ESTUDIANTES:\n");
            for (Estudiante e : estudiantesMap.values()) {
                writer.write("ID: " + e.getId() + ", Nombre: " + e.getNombre() + "\n");
            }
            writer.write("\nBECAS:\n");
            for (Beca b : becas) {
                writer.write("Tipo: " + b.getTipo() + ", Criterio: " + b.getCriterio() + "\n");
            }
            writer.write("\nSOLICITUDES:\n");
            for (SolicitudBeca s : solicitudes) {
                String criterio = "";
                try {
                    Beca b = buscarBecaPorCodigo(s.getTipoBeca());
                    criterio = b.getCriterio();
                } catch (BecaNotFoundException ex) {
                    criterio = "N/A";
                }
                writer.write(s.getEstudiante().getId() + "," + s.getEstudiante().getNombre() + ","
                        + s.getTipoBeca() + "," + criterio + "," + s.getEstado() + "\n");
            }

            System.out.println("Reporte generado en " + nombreArchivo);
        } catch (IOException e) {
            System.err.println("Error al generar el reporte: " + e.getMessage());
        }
    }



}
