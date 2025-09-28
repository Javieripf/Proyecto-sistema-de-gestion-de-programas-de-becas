import java.util.ArrayList;
import java.util.List;

public class Estudiante extends Persona {
    private List<SolicitudBeca> solicitudes;

    public Estudiante(String nombre, String id) {
        super(nombre, id);
        this.solicitudes = new ArrayList<>();
    }

    public List<SolicitudBeca> getSolicitudes() { return solicitudes; }
    public void setSolicitudes(List<SolicitudBeca> solicitudes) { this.solicitudes = solicitudes; }

    // Sobreescritura: descripcion específica del estudiante
    @Override
    public String descripcion() {
        return "Estudiante: " + getNombre() + ", ID: " + getId();
    }

    @Override
    public String toString() {
        return descripcion();
    }
}
