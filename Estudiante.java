import java.util.ArrayList;

public class Estudiante {
    private String nombre;
    private String id;
    private ArrayList<SolicitudBeca> solicitudes;

    public Estudiante(String nombre, String id) {
        this.nombre = nombre;
        this.id = id;
        this.solicitudes = new ArrayList<>();
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public ArrayList<SolicitudBeca> getSolicitudes() { return solicitudes; }

    public String toString() {
        return "Estudiante: " + nombre + ", ID: " + id;
    }
}