import java.io.PrintStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class SistemaDeGestion {
    private HashMap<String, Estudiante> estudiantesMap = new HashMap();
    private ArrayList<Beca> becas = new ArrayList();
    private ArrayList<SolicitudBeca> solicitudes = new ArrayList();

    public SistemaDeGestion() {
        super();
    }

    public void agregarEstudiante(String nombre, String id) {
        Estudiante nuevoEstudiante = new Estudiante(nombre, id);
        this.estudiantesMap.put(id, nuevoEstudiante);
    }

    public void mostrarEstudiantes() {
        if (this.estudiantesMap.isEmpty()) {
            System.out.println("No hay estudiantes registrados.");
        } else {
            for(Estudiante estudiante : this.estudiantesMap.values()) {
                System.out.println(estudiante);
            }
        }

    }

    public Estudiante buscarEstudiantePorID(String id) {
        return (Estudiante)this.estudiantesMap.get(id);
    }

    public void editarEstudiante(String id, String nuevoNombre) {
        Estudiante estudiante = (Estudiante)this.estudiantesMap.get(id);
        if (estudiante != null) {
            estudiante.setNombre(nuevoNombre);
            System.out.println("Datos del estudiante actualizados.");
        } else {
            System.out.println("Estudiante no encontrado.");
        }

    }

    public void eliminarEstudiante(String id) {
        if (this.estudiantesMap.remove(id) != null) {
            System.out.println("Estudiante eliminado.");
        } else {
            System.out.println("Estudiante no encontrado.");
        }

    }

    public void agregarBeca(String tipo, String criterio) {
        Beca nuevaBeca = new Beca(tipo, criterio);
        this.becas.add(nuevaBeca);
    }

    public void mostrarBecas() {
        if (this.becas.isEmpty()) {
            System.out.println("No hay becas registradas.");
        } else {
            for(Beca beca : this.becas) {
                System.out.println(beca);
            }
        }

    }

    public Beca buscarBecaPorCodigo(String tipo) {
        for(Beca beca : this.becas) {
            if (beca.getTipo().equals(tipo)) {
                return beca;
            }
        }

        return null;
    }

    public void editarBeca(String tipo, String nuevoCriterio) {
        Beca beca = this.buscarBecaPorCodigo(tipo);
        if (beca != null) {
            beca.setCriterio(nuevoCriterio);
            System.out.println("Criterio de la beca actualizado.");
        } else {
            System.out.println("Beca no encontrada.");
        }

    }

    public void eliminarBeca(String tipo) {
        Beca beca = this.buscarBecaPorCodigo(tipo);
        if (beca != null) {
            this.becas.remove(beca);
            System.out.println("Beca eliminada.");
        } else {
            System.out.println("Beca no encontrada.");
        }

    }

    // Crear solicitud pasando ID y tipo de beca
    public void crearSolicitud(String idEstudiante, String tipoBeca) {
        Estudiante estudiante = estudiantesMap.get(idEstudiante);
        if (estudiante != null) {
            SolicitudBeca solicitud = new SolicitudBeca(estudiante, tipoBeca);
            solicitudes.add(solicitud);
            estudiante.getSolicitudes().add(solicitud);
            System.out.println("Solicitud creada.");
        } else {
            System.out.println("Estudiante no encontrado.");
        }
    }

    // Sobrecarga: crear solicitud pasando directamente objetos
    public void crearSolicitud(Estudiante estudiante, Beca beca) {
        if (estudiante != null && beca != null) {
            SolicitudBeca solicitud = new SolicitudBeca(estudiante, beca.getTipo());
            solicitudes.add(solicitud);
            estudiante.getSolicitudes().add(solicitud);
            System.out.println("Solicitud creada.");
        } else {
            System.out.println("Estudiante o beca no válido.");
        }
    }

    public void mostrarSolicitudes() {
        if (this.solicitudes.isEmpty()) {
            System.out.println("No hay solicitudes registradas.");
        } else {
            for(SolicitudBeca solicitud : this.solicitudes) {
                System.out.println(solicitud);
            }
        }

    }

    public void mostrarSolicitudesPorEstado(String estado) {
        for(SolicitudBeca solicitud : this.solicitudes) {
            if (solicitud.getEstado().equalsIgnoreCase(estado)) {
                System.out.println(solicitud);
            }
        }

    }

    public void aprobarRechazarSolicitud(String idEstudiante, String tipoBeca, String estado) {
        for(SolicitudBeca solicitud : this.solicitudes) {
            if (solicitud.getEstudiante().getId().equals(idEstudiante) && solicitud.getTipoBeca().equals(tipoBeca)) {
                solicitud.setEstado(estado);
                System.out.println("Solicitud " + estado);
                return;
            }
        }

        System.out.println("Solicitud no encontrada.");
    }

    public void listadoDeEstudiantesConSolicitudes() {
        for(Estudiante estudiante : this.estudiantesMap.values()) {
            if (!estudiante.getSolicitudes().isEmpty()) {
                System.out.println(estudiante);

                for(SolicitudBeca solicitud : estudiante.getSolicitudes()) {
                    System.out.println("\t" + String.valueOf(solicitud));
                }
            }
        }

    }

    public void totalSolicitudesPorEstado() {
        Map<String, Integer> estadoCount = new HashMap();

        for(SolicitudBeca solicitud : this.solicitudes) {
            estadoCount.put(solicitud.getEstado(), (Integer)estadoCount.getOrDefault(solicitud.getEstado(), 0) + 1);
        }

        for(Map.Entry<String, Integer> entry : estadoCount.entrySet()) {
            PrintStream var10000 = System.out;
            String var10001 = (String)entry.getKey();
            var10000.println("Estado: " + var10001 + ", Total: " + String.valueOf(entry.getValue()));
        }

    }
}