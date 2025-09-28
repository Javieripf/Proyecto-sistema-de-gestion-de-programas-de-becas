public class SolicitudBeca extends Registro {
    private Estudiante estudiante;
    private String tipoBeca;
    private String estado;

    public SolicitudBeca(Estudiante estudiante, String tipoBeca) {
        this.estudiante = estudiante;
        this.tipoBeca = tipoBeca;
        this.estado = "Pendiente";
    }

    public Estudiante getEstudiante() { return estudiante; }
    public void setEstudiante(Estudiante estudiante) { this.estudiante = estudiante; }

    public String getTipoBeca() { return tipoBeca; }
    public void setTipoBeca(String tipoBeca) { this.tipoBeca = tipoBeca; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    // Sobrescribo resumen() de Registro
    @Override
    public String resumen() {
        return estudiante.getNombre() + " - Beca: " + tipoBeca + ", Estado: " + estado;
    }

    @Override
    public String toString() {
        return resumen();
    }
}
