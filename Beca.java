public class Beca {
    private String tipo;
    private String criterio;

    public Beca(String tipo, String criterio) {
        this.tipo = tipo;
        this.criterio = criterio;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
    public String getCriterio() { return criterio; }
    public void setCriterio(String criterio) { this.criterio = criterio; }

    public String toString() {
        return "Beca: " + tipo + ", Criterio: " + criterio;
    }
}

