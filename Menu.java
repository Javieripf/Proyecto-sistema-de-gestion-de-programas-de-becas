import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.PrintStream;

public class Menu extends JFrame {
    private SistemaDeGestion sistema;
    private JTextArea areaSalida;

    public Menu() {
        sistema = new SistemaDeGestion();

        setTitle("📚 Sistema de Gestion de Becas");
        setSize(900, 550);
        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        setLocationRelativeTo(null);

        // Confirmacion al cerrar
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent e) {
                int opcion = JOptionPane.showConfirmDialog(
                        Menu.this,
                        "Desea guardar antes de salir?",
                        "Confirmar salida",
                        JOptionPane.YES_NO_CANCEL_OPTION
                );
                if (opcion == JOptionPane.YES_OPTION) {
                    sistema.guardarAlSalir();
                    System.exit(0);
                } else if (opcion == JOptionPane.NO_OPTION) {
                    System.exit(0);
                }
            }
        });

        // Panel principal
        setLayout(new BorderLayout());

        // Area de salida
        areaSalida = new JTextArea();
        areaSalida.setEditable(false);
        areaSalida.setFont(new Font("Consolas", Font.PLAIN, 13));
        JScrollPane scroll = new JScrollPane(areaSalida);
        add(scroll, BorderLayout.CENTER);

        // Redirigir System.out a JTextArea
        PrintStream printStream = new PrintStream(new TextAreaOutputStream(areaSalida));
        System.setOut(printStream);
        System.setErr(printStream);

        // Panel lateral
        JPanel panelLateral = new JPanel();
        panelLateral.setLayout(new BoxLayout(panelLateral, BoxLayout.Y_AXIS));
        panelLateral.setBackground(new Color(240, 240, 240));

        // ==== SUBMENU ESTUDIANTES ====
        JPanel panelEstudiantes = crearSubMenu("👨‍🎓 Estudiantes");
        GridBagConstraints gbcE = baseGBC();
        addBoton(panelEstudiantes, gbcE, "Agregar Estudiante", e -> {
            String nombre = JOptionPane.showInputDialog(this, "Nombre del estudiante:");
            if (nombre == null || nombre.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El nombre no puede estar vacio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            String id = JOptionPane.showInputDialog(this, "ID del estudiante:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ID no puede estar vacio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar duplicados
            Estudiante existente = sistema.buscarEstudiantePorID(id.trim());
            if (existente != null) {
                JOptionPane.showMessageDialog(this,
                        "Ya existe un estudiante con el ID: " + id.trim(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            sistema.agregarEstudiante(nombre.trim(), id.trim());
            JOptionPane.showMessageDialog(this, "Estudiante agregado correctamente.");
        });

        addBoton(panelEstudiantes, gbcE, "Buscar Estudiante", e -> {
            String id = JOptionPane.showInputDialog(this, "Ingrese el ID del estudiante a buscar:");
            if (id == null || id.trim().isEmpty()) return;

            try {
                Estudiante est = sistema.obtenerEstudianteOrThrow(id.trim());
                JOptionPane.showMessageDialog(this,
                        "Estudiante encontrado:\nNombre: " + est.getNombre() + "\nID: " + est.getId(),
                        "Resultado",
                        JOptionPane.INFORMATION_MESSAGE);
            } catch (EstudianteNotFoundException ex) {
                JOptionPane.showMessageDialog(this,
                        "❌ " + ex.getMessage(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
            }
        });

        addBoton(panelEstudiantes, gbcE, "Mostrar Estudiantes", e -> sistema.mostrarEstudiantes());

        addBoton(panelEstudiantes, gbcE, "Editar Estudiante", e -> {
            String id = JOptionPane.showInputDialog(this, "Ingrese el ID del estudiante a editar:");
            if (id == null || id.trim().isEmpty()) return;

            // Verificar si el estudiante existe antes de continuar
            Estudiante existente = sistema.buscarEstudiantePorID(id.trim());
            if (existente == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró un estudiante con el ID: " + id.trim(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nuevoNombre = JOptionPane.showInputDialog(this, "Ingrese el nuevo nombre:");
            if (nuevoNombre == null || nuevoNombre.trim().isEmpty()) return;

            sistema.editarEstudiante(id.trim(), nuevoNombre.trim());
            JOptionPane.showMessageDialog(this, "Estudiante actualizado.");
        });

        addBoton(panelEstudiantes, gbcE, "Eliminar Estudiante", e -> {
            String id = JOptionPane.showInputDialog(this, "Ingrese el ID del estudiante a eliminar:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un ID valido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si el estudiante existe antes de continuar
            Estudiante existente = sistema.buscarEstudiantePorID(id.trim());
            if (existente == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró un estudiante con el ID: " + id.trim(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que desea eliminar al estudiante " + existente.getNombre() +
                            " con ID: " + id + "?\nSe eliminarán también sus solicitudes.",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                sistema.eliminarEstudiante(id.trim());
                JOptionPane.showMessageDialog(this, "Estudiante eliminado correctamente.");
            }
        });

        addBoton(panelEstudiantes, gbcE, "Filtrar por numero de Solicitudes", e -> {
            String input = JOptionPane.showInputDialog(this, "Ingrese el minimo de solicitudes:");
            if (input != null && !input.trim().isEmpty()) {
                try {
                    int minSolicitudes = Integer.parseInt(input.trim());
                    java.util.List<Estudiante> filtrados = sistema.filtrarEstudiantesPorNumSolicitudes(minSolicitudes);
                    if (filtrados.isEmpty()) {
                        System.out.println("No hay estudiantes con " + minSolicitudes + " o mas solicitudes.");
                    } else {
                        System.out.println("Estudiantes con al menos " + minSolicitudes + " solicitudes:");
                        for (Estudiante est : filtrados) {
                            System.out.println("- " + est.getNombre() + " (ID: " + est.getId() + ") -> " + est.getSolicitudes().size() + " solicitudes");
                        }
                    }
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Ingrese un numero valido.", "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ==== SUBMENU SOLICITUDES ====
        JPanel panelSolicitudes = crearSubMenu("📝 Solicitudes");
        GridBagConstraints gbcS = baseGBC();
        addBoton(panelSolicitudes, gbcS, "Crear Solicitud", e -> {
            String id = JOptionPane.showInputDialog(this, "ID del estudiante:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ID no puede estar vacio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            String beca = JOptionPane.showInputDialog(this, "Tipo de beca:");
            if (beca == null || beca.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El tipo de beca no puede estar vacio.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }
            sistema.crearSolicitud(id.trim(), beca.trim());
        });
        addBoton(panelSolicitudes, gbcS, "Mostrar Solicitudes", e -> sistema.mostrarSolicitudes());
        addBoton(panelSolicitudes, gbcS, "Estudiantes con Solicitudes", e -> sistema.listadoDeEstudiantesConSolicitudes());

        addBoton(panelSolicitudes, gbcS, "Buscar Solicitud", e -> {
            String id = JOptionPane.showInputDialog(this, "Ingrese el ID del estudiante:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ID no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si el estudiante existe
            Estudiante existente = sistema.buscarEstudiantePorID(id.trim());
            if (existente == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró un estudiante con el ID: " + id.trim(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String beca = JOptionPane.showInputDialog(this, "Ingrese el tipo de beca:");
            if (beca == null || beca.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El tipo de beca no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            SolicitudBeca s = sistema.buscarSolicitud(id.trim(), beca.trim());
            if (s != null) {
                System.out.println("Solicitud encontrada:");
                System.out.println("Estudiante: " + s.getEstudiante().getNombre() + " (ID: " + s.getEstudiante().getId() + ")");
                System.out.println("Beca: " + s.getTipoBeca());
                System.out.println("Estado: " + s.getEstado());
            } else {
                System.out.println("No se encontró la solicitud para la beca '" + beca.trim() +
                        "' del estudiante con ID: " + id.trim());
            }
        });

        addBoton(panelSolicitudes, gbcS, "Eliminar Solicitud", e -> {
            String id = JOptionPane.showInputDialog(this, "ID del estudiante:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El ID no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si el estudiante existe
            Estudiante existente = sistema.buscarEstudiantePorID(id.trim());
            if (existente == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró un estudiante con el ID: " + id.trim(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String beca = JOptionPane.showInputDialog(this, "Tipo de beca a eliminar:");
            if (beca == null || beca.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El tipo de beca no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si la solicitud existe antes de mostrar confirmación
            SolicitudBeca solicitudExistente = sistema.buscarSolicitud(id.trim(), beca.trim());
            if (solicitudExistente == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró una solicitud para la beca '" + beca.trim() +
                                "' del estudiante " + existente.getNombre() + " (ID: " + id.trim() + ")",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que desea eliminar la solicitud de beca '" + beca +
                            "' del estudiante " + existente.getNombre() + " (ID: " + id + ")?",
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                sistema.eliminarSolicitud(id.trim(), beca.trim());
                JOptionPane.showMessageDialog(this, "Solicitud eliminada correctamente.");
            }
        });

        addBoton(panelSolicitudes, gbcS, "Aprobar/Rechazar Solicitud", e -> {
            String id = JOptionPane.showInputDialog(this, "Ingrese el ID del estudiante:");
            if (id == null || id.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El ID no puede estar vacío.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si el estudiante existe
            Estudiante existente = sistema.buscarEstudiantePorID(id.trim());
            if (existente == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró un estudiante con el ID: " + id.trim(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String beca = JOptionPane.showInputDialog(this, "Ingrese el tipo de beca:");
            if (beca == null || beca.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this,
                        "El tipo de beca no puede estar vacío.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si la solicitud existe
            SolicitudBeca solicitudExistente = sistema.buscarSolicitud(id.trim(), beca.trim());
            if (solicitudExistente == null) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró una solicitud para la beca '" + beca.trim() +
                                "' del estudiante " + existente.getNombre() + " (ID: " + id.trim() + ")",
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String[] opciones = {"Aprobada", "Rechazada"};
            String estado = (String) JOptionPane.showInputDialog(
                    this,
                    "Solicitud actual: " + solicitudExistente.getEstado() +
                            "\n\nSeleccione el nuevo estado para la solicitud de " +
                            existente.getNombre() + ":",
                    "Aprobar/Rechazar",
                    JOptionPane.QUESTION_MESSAGE,
                    null,
                    opciones,
                    opciones[0]
            );

            if (estado != null) {
                sistema.aprobarRechazarSolicitud(id.trim(), beca.trim(), estado);
                JOptionPane.showMessageDialog(this,
                        "Solicitud de " + existente.getNombre() +
                                " actualizada a: " + estado);
            }
        });

        addBoton(panelSolicitudes, gbcS, "Reporte por Estado", e -> {
            java.util.Map<String, Integer> conteo = sistema.totalSolicitudesPorEstadoMap();
            if (conteo.isEmpty()) {
                System.out.println("No hay solicitudes registradas.");
            } else {
                System.out.println("Total de solicitudes por estado:");
                for (var entry : conteo.entrySet()) {
                    System.out.println("- " + entry.getKey() + ": " + entry.getValue());
                }
            }
        });

        // ==== SUBMENU BECAS ====
        JPanel panelBecas = crearSubMenu("🎓 Becas");
        GridBagConstraints gbcB = baseGBC();


        addBoton(panelBecas, gbcB, "Agregar Beca", e -> {
            String tipo = JOptionPane.showInputDialog(this, "Ingrese el tipo de beca:");
            if (tipo == null || tipo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El tipo de beca no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si ya existe una beca con ese tipo antes de pedir el criterio
            try {
                sistema.buscarBecaPorCodigo(tipo.trim());
                // Si llega aquí, significa que la beca ya existe
                JOptionPane.showMessageDialog(this,
                        "Ya existe una beca con el tipo: " + tipo.trim(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            } catch (BecaNotFoundException ex) {
                // Si lanza excepción, significa que la beca NO existe, podemos continuar
            }

            String criterio = JOptionPane.showInputDialog(this, "Ingrese el criterio de la beca:");
            if (criterio == null || criterio.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El criterio no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            sistema.agregarBeca(tipo.trim(), criterio.trim());
            JOptionPane.showMessageDialog(this, "Beca agregada correctamente.");
        });

        addBoton(panelBecas, gbcB, "Mostrar Becas", e -> sistema.mostrarBecas());

        addBoton(panelBecas, gbcB, "Editar Beca", e -> {
            String tipo = JOptionPane.showInputDialog(this, "Ingrese el tipo de beca a editar:");
            if (tipo == null || tipo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un tipo de beca válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si la beca existe antes de continuar
            Beca becaExistente;
            try {
                becaExistente = sistema.buscarBecaPorCodigo(tipo.trim());
            } catch (BecaNotFoundException ex) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró una beca con el tipo: " + tipo.trim(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            String nuevoCriterio = JOptionPane.showInputDialog(this,
                    "Criterio actual: " + becaExistente.getCriterio() +
                            "\n\nIngrese el nuevo criterio para la beca:");
            if (nuevoCriterio == null || nuevoCriterio.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "El criterio no puede estar vacío.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            try {
                sistema.editarBeca(tipo.trim(), nuevoCriterio.trim());
                JOptionPane.showMessageDialog(this, "Beca editada correctamente.");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
        addBoton(panelBecas, gbcB, "Eliminar Beca", e -> {
            String tipo = JOptionPane.showInputDialog(this, "Ingrese el tipo de beca a eliminar:");
            if (tipo == null || tipo.trim().isEmpty()) {
                JOptionPane.showMessageDialog(this, "Debe ingresar un tipo de beca válido.", "Error", JOptionPane.ERROR_MESSAGE);
                return;
            }

            // Verificar si la beca existe antes de continuar
            Beca becaExistente;
            try {
                becaExistente = sistema.buscarBecaPorCodigo(tipo.trim());
            } catch (BecaNotFoundException ex) {
                JOptionPane.showMessageDialog(this,
                        "No se encontró una beca con el tipo: " + tipo.trim(),
                        "Error",
                        JOptionPane.ERROR_MESSAGE);
                return;
            }

            int confirm = JOptionPane.showConfirmDialog(this,
                    "¿Seguro que desea eliminar la beca: " + becaExistente.getTipo() +
                            "?\nCriterio: " + becaExistente.getCriterio(),
                    "Confirmar eliminación",
                    JOptionPane.YES_NO_OPTION);

            if (confirm == JOptionPane.YES_OPTION) {
                try {
                    sistema.eliminarBeca(tipo.trim());
                    JOptionPane.showMessageDialog(this, "Beca eliminada correctamente.");
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(this, "❌ " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            }
        });

        // ==== SUBMENU SISTEMA ====
        JPanel panelSistema = crearSubMenu("⚙️ Sistema");
        GridBagConstraints gbcSys = baseGBC();

        addBoton(panelSistema, gbcSys, "Generar Reporte TXT", e -> {
            sistema.generarReporteTXT("reporte_becas.txt");
            JOptionPane.showMessageDialog(this, "Reporte generado en archivo reporte_becas.txt");
        });

        addBoton(panelSistema, gbcSys, "Exportar Reportes CSV", e -> {
            String archivo = JOptionPane.showInputDialog(this, "Ingrese el nombre del archivo CSV:", "reporte.csv");
            if (archivo != null && !archivo.trim().isEmpty()) {
                sistema.exportarReportes(archivo.trim());
                JOptionPane.showMessageDialog(this, "Reporte exportado en " + archivo);
            }
        });

        addBoton(panelSistema, gbcSys, "Guardar Datos", e -> sistema.guardarAlSalir());

        // Añadir los submenues al lateral
        panelLateral.add(panelEstudiantes);
        panelLateral.add(panelSolicitudes);
        panelLateral.add(panelBecas);
        panelLateral.add(panelSistema);

        add(panelLateral, BorderLayout.WEST);
    }

    // 🔧 Helper: crear panel con titulo
    private JPanel crearSubMenu(String titulo) {
        JPanel panel = new JPanel(new GridBagLayout());
        panel.setBorder(new TitledBorder(titulo));
        panel.setBackground(new Color(230, 230, 250));
        return panel;
    }

    // 🔧 Helper: configuracion base de GridBagConstraints
    private GridBagConstraints baseGBC() {
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 5, 5, 5);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.gridx = 0;
        gbc.gridy = 0;
        return gbc;
    }

    // 🔧 Helper: añadir boton
    private void addBoton(JPanel panel, GridBagConstraints gbc, String texto, java.awt.event.ActionListener action) {
        JButton btn = new JButton(texto);
        btn.setBackground(new Color(200, 220, 240));
        btn.setFocusPainted(false);
        gbc.gridy++;
        panel.add(btn, gbc);
        btn.addActionListener(action);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new Menu().setVisible(true));
    }
}