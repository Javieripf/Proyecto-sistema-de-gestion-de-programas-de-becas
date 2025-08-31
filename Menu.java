import java.util.Scanner;

public class Menu{
    public static void main(String[] args){
        Scanner scanner = new Scanner(System.in);
        SistemaDeGestion sistema = new SistemaDeGestion();
        boolean salir = false;

        sistema.agregarBeca("Beca Académica", "Promedio superior a 6.0");
        sistema.agregarBeca("Beca Deportiva", "Excelente desempeño deportivo");

        while (!salir){
            System.out.println("\n--- Menú de Gestión ---");
            System.out.println("1. Gestionar Estudiantes");
            System.out.println("2. Gestionar Becas");
            System.out.println("3. Gestionar Solicitudes");
            System.out.println("4. Reportes");
            System.out.println("5. Salir");
            System.out.print("Elija una opción: ");

            int opcion = validarEntrada(scanner, 1, 5);
            switch (opcion){
                case 1:
                    gestionarEstudiantes(scanner, sistema);
                    break;
                case 2:
                    gestionarBecas(scanner, sistema);
                    break;
                case 3:
                    gestionarSolicitudes(scanner, sistema);
                    break;
                case 4:
                    reportes(scanner, sistema);
                    break;
                case 5:
                    salir = true;
                    break;
            }
        }
        scanner.close();
    }
    public static int validarEntrada(Scanner scanner, int min, int max){
        int opcion;
        while (true) {
            try {
                opcion = Integer.parseInt(scanner.nextLine());
                if (opcion >= min && opcion <= max) {
                    break;
                } else {
                    System.out.println("Por favor ingresa una opción entre " + min + " y " + max);
                }
            } catch (NumberFormatException e) {
                System.out.println("Por favor ingresa un número válido.");
            }
        }
        return opcion;
    }
    private static void gestionarEstudiantes(Scanner scanner, SistemaDeGestion sistema) {
        System.out.println("\n1. Agregar Estudiante");
        System.out.println("2. Mostrar Todos");
        System.out.println("3. Buscar por ID");
        System.out.println("4. Editar Datos");
        System.out.println("5. Eliminar");
        int opcion = validarEntrada(scanner, 1, 5);

        switch (opcion) {
            case 1:
                System.out.print("Nombre: ");
                String nombre = scanner.nextLine();
                System.out.print("ID: ");
                String id = scanner.nextLine();
                sistema.agregarEstudiante(nombre, id);
                break;
            case 2:
                sistema.mostrarEstudiantes();
                break;
            case 3:
                System.out.print("Ingrese ID del estudiante: ");
                String idBuscar = scanner.nextLine();
                Estudiante estudiante = sistema.buscarEstudiantePorID(idBuscar);
                if (estudiante != null) {
                    System.out.println(estudiante);
                } else {
                    System.out.println("Estudiante no encontrado.");
                }
                break;
            case 4:
                System.out.print("Ingrese ID del estudiante a editar: ");
                String idEditar = scanner.nextLine();
                System.out.print("Nuevo nombre: ");
                String nuevoNombre = scanner.nextLine();
                sistema.editarEstudiante(idEditar, nuevoNombre);
                break;
            case 5:
                System.out.print("Ingrese ID del estudiante a eliminar: ");
                String idEliminar = scanner.nextLine();
                sistema.eliminarEstudiante(idEliminar);
                break;
        }
    }
    private static void gestionarBecas(Scanner scanner, SistemaDeGestion sistema) {
        System.out.println("\n1. Agregar Beca");
        System.out.println("2. Mostrar Todas");
        System.out.println("3. Buscar por Código");
        System.out.println("4. Editar Datos");
        System.out.println("5. Eliminar");
        int opcion = validarEntrada(scanner, 1, 5);

        switch (opcion) {
            case 1:
                System.out.print("Tipo de Beca: ");
                String tipoBeca = scanner.nextLine();
                System.out.print("Criterio: ");
                String criterio = scanner.nextLine();
                sistema.agregarBeca(tipoBeca, criterio);
                break;
            case 2:
                sistema.mostrarBecas();
                break;
            case 3:
                System.out.print("Ingrese código de la beca: ");
                String tipoBecaBuscar = scanner.nextLine();
                Beca beca = sistema.buscarBecaPorCodigo(tipoBecaBuscar);
                if (beca != null) {
                    System.out.println(beca);
                } else {
                    System.out.println("Beca no encontrada.");
                }
                break;
            case 4:
                System.out.print("Ingrese código de la beca a editar: ");
                String tipoBecaEditar = scanner.nextLine();
                System.out.print("Nuevo criterio: ");
                String nuevoCriterio = scanner.nextLine();
                sistema.editarBeca(tipoBecaEditar, nuevoCriterio);
                break;
            case 5:
                System.out.print("Ingrese código de la beca a eliminar: ");
                String tipoBecaEliminar = scanner.nextLine();
                sistema.eliminarBeca(tipoBecaEliminar);
                break;
        }
    }
    private static void gestionarSolicitudes(Scanner scanner, SistemaDeGestion sistema) {
        System.out.println("\n1. Crear Solicitud");
        System.out.println("2. Mostrar Todas");
        System.out.println("3. Mostrar por Estado");
        System.out.println("4. Aprobar/Rechazar");
        int opcion = validarEntrada(scanner, 1, 4);

        switch (opcion) {
            case 1:
                System.out.print("ID del estudiante: ");
                String idEstudiante = scanner.nextLine();
                System.out.print("Tipo de beca: ");
                String tipoBeca = scanner.nextLine();
                sistema.crearSolicitud(idEstudiante, tipoBeca);
                break;
            case 2:
                sistema.mostrarSolicitudes();
                break;
            case 3:
                System.out.print("Ingrese el estado (Pendiente, Aprobada, Rechazada): ");
                String estado = scanner.nextLine();
                sistema.mostrarSolicitudesPorEstado(estado);
                break;
            case 4:
                System.out.print("Ingrese ID del estudiante para aprobar/rechazar: ");
                String idSolicitud = scanner.nextLine();
                System.out.print("Ingrese tipo de beca: ");
                String tipoSolicitud = scanner.nextLine();
                System.out.print("Ingrese el estado (Aprobar/Rechazar): ");
                String estadoSolicitud = scanner.nextLine();
                sistema.aprobarRechazarSolicitud(idSolicitud, tipoSolicitud, estadoSolicitud);
                break;
        }
    }
    private static void reportes(Scanner scanner, SistemaDeGestion sistema) {
        System.out.println("\n1. Listado de estudiantes con solicitudes");
        System.out.println("2. Total de solicitudes por estado");
        int opcion = validarEntrada(scanner, 1, 2);

        switch (opcion) {
            case 1:
                sistema.listadoDeEstudiantesConSolicitudes();
                break;
            case 2:
                sistema.totalSolicitudesPorEstado();
                break;
        }
    }
}