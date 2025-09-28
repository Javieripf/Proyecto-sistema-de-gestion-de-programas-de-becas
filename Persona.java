public class Persona {
    private String nombre;
    private String id;

    public Persona(String nombre, String id) {
        this.nombre = nombre;
        this.id = id;
    }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    // Método que será sobrescrito por clases hijas
    public String descripcion() {
        return "Persona: " + nombre + ", ID: " + id;
    }

    @Override
    public String toString() {
        return descripcion();
    }
}