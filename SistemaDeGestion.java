import java.util.ArrayList;
import java.util.HashMap;

public class SistemaDeGestion {
    private HashMap<String, Estudiante> estudiantesMap = new HashMap<>();
    private ArrayList<Beca> becas = new ArrayList<>();

    // --- Estudiantes ---
    public void agregarEstudiante(String nombre, String id) {
        estudiantesMap.put(id, new Estudiante(nombre, id));
    }

    public void mostrarEstudiantes() {
        if (estudiantesMap.isEmpty()) System.out.println("No hay estudiantes registrados.");
        else estudiantesMap.values().forEach(System.out::println);
    }

    public Estudiante buscarEstudiantePorID(String id) {
        return estudiantesMap.get(id);
    }

    public void editarEstudiante(String id, String nuevoNombre) {
        Estudiante est = estudiantesMap.get(id);
        if (est != null) {
            est.setNombre(nuevoNombre);
            System.out.println("Datos del estudiante actualizados.");
        } else System.out.println("Estudiante no encontrado.");
    }

    public void eliminarEstudiante(String id) {
        if (estudiantesMap.remove(id) != null) System.out.println("Estudiante eliminado.");
        else System.out.println("Estudiante no encontrado.");
    }

    // --- Becas ---
    public void agregarBeca(String tipo, String criterio) {
        becas.add(new Beca(tipo, criterio));
    }

    public void mostrarBecas() {
        if (becas.isEmpty()) System.out.println("No hay becas registradas.");
        else becas.forEach(System.out::println);
    }

    public Beca buscarBecaPorCodigo(String tipo) {
        for (Beca b : becas) if (b.getTipo().equals(tipo)) return b;
        return null;
    }

    public void editarBeca(String tipo, String nuevoCriterio) {
        Beca b = buscarBecaPorCodigo(tipo);
        if (b != null) {
            b.setCriterio(nuevoCriterio);
            System.out.println("Criterio de la beca actualizado.");
        } else System.out.println("Beca no encontrada.");
    }

    public void eliminarBeca(String tipo) {
        Beca b = buscarBecaPorCodigo(tipo);
        if (b != null) {
            becas.remove(b);
            System.out.println("Beca eliminada.");
        } else System.out.println("Beca no encontrada.");
    }
}