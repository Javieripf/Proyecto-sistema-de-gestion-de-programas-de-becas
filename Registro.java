// Clase base para registros que pueden imprimirse; la usa SolicitudBeca
public class Registro {
    public String resumen() {
        return "Registro genérico";
    }

    @Override
    public String toString() {
        return resumen();
    }
}
