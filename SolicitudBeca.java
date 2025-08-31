public class SolicitudBeca {
    private Estudiante estudiante;
    private String tipoBeca;
    private String estado;

    public SolicitudBeca(Estudiante estudiante, String tipoBeca) {
        this.estudiante = estudiante;
        this.tipoBeca = tipoBeca;
        this.estado = "Pendiente";
    }

    public Estudiante getEstudiante() { return estudiante; }
    public String getTipoBeca() { return tipoBeca; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String toString() {
        return estudiante.getNombre() + " - Beca: " + tipoBeca + ", Estado: " + estado;
    }
}