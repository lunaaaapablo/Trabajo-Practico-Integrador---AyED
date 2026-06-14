package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import archivos.Lectura;
import grafo.GrafoMaterias;
import grafo.contenedores.ListaDoubleLinkedL;
import modelo.Alumno;
import modelo.EstadoAcademico;
import modelo.Materia;
import modelo.SimuladorAcademico;

public class SimuladorGUI extends JFrame {

    private static final Color AZUL_OSCURO = new Color(31, 56, 100);
    private static final Color AZUL_MEDIO  = new Color(24, 95, 165);
    private static final Color AZUL_CLARO  = new Color(230, 241, 251);
    private static final Color ROJO_CLARO  = new Color(252, 235, 235);
    private static final Color ROJO_OSCURO = new Color(163, 45, 45);
    private static final Color VERDE_CLARO = new Color(234, 243, 222);
    private static final Color VERDE_OSCURO = new Color(59, 109, 17);
    private static final Color GRIS_CLARO  = new Color(241, 239, 232);
    private static final Color GRIS_OSCURO = new Color(95, 94, 90);
    private static final Color AMBER_CLARO = new Color(250, 238, 218);
    private static final Color AMBER_OSCURO = new Color(133, 79, 11);

    private GrafoMaterias grafoMaterias;
    private ListaDoubleLinkedL alumnos;
    private Alumno alumnoSeleccionado;
    private JTable tablaMaterias;
    private JTable tablaAlumnos;
    private JTable tablaHistorial;
    private JLabel lblContMaterias;
    private JLabel lblContAlumnos;
    private JLabel lblContHistorial;
    private JLabel lblStatus;

    public SimuladorGUI() {
        setTitle("Sistema de Gestion Academica");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1100, 700);
        setLocationRelativeTo(null);
        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearPanelCentral(), BorderLayout.CENTER);
        add(crearStatusBar(), BorderLayout.SOUTH);
    }

    private JPanel crearEncabezado() {
        JPanel panelEncabezado = new JPanel(new BorderLayout());

        JPanel titulo = new JPanel(new BorderLayout());
        titulo.setBackground(AZUL_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JLabel lblTitulo = new JLabel("Sistema de Gestion Academica");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel lblRight = new JLabel("TPI - Algoritmos y Estructuras de Datos");
        lblRight.setForeground(Color.WHITE);
        lblRight.setFont(new Font("SansSerif", Font.ITALIC, 11));

        titulo.add(lblTitulo, BorderLayout.WEST);
        titulo.add(lblRight, BorderLayout.EAST);

        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        toolbar.setBackground(AZUL_MEDIO);
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));

        JButton btnMaterias = crearBoton("Cargar Materias");
        JButton btnCorrelativas = crearBoton("Cargar Correlativas");
        JButton btnAlumnos = crearBoton("Cargar Alumnos");
        JButton btnEstado = crearBoton("Cargar Estado Academico");
        JButton btnSimulador = crearBoton("Abrir Simulador");

        btnMaterias.addActionListener(e -> cargarMaterias());
        btnCorrelativas.addActionListener(e -> cargarCorrelativas());
        btnAlumnos.addActionListener(e -> cargarAlumnos());
        btnEstado.addActionListener(e -> cargarHistorial());
        btnSimulador.addActionListener(e -> abrirSimulador());

        toolbar.add(btnMaterias);
        toolbar.add(btnCorrelativas);
        toolbar.add(btnAlumnos);
        toolbar.add(btnEstado);
        toolbar.add(Box.createHorizontalStrut(20));
        toolbar.add(btnSimulador);

        panelEncabezado.add(titulo, BorderLayout.NORTH);
        panelEncabezado.add(toolbar, BorderLayout.SOUTH);
        return panelEncabezado;
    }

    private JButton crearBoton(String texto) {
        JButton boton = new JButton(texto);
        boton.setBackground(AZUL_CLARO);
        boton.setForeground(AZUL_MEDIO);
        boton.setFont(new Font("SansSerif", Font.PLAIN, 12));
        boton.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(AZUL_MEDIO.brighter(), 1),
                BorderFactory.createEmptyBorder(5, 12, 5, 12)
        ));
        boton.setFocusPainted(false);
        boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        return boton;
    }

    private JPanel crearPanelCentral() {
        JPanel panel = new JPanel(new GridLayout(2, 2, 6, 6));
        panel.setBorder(BorderFactory.createEmptyBorder(6, 6, 6, 6));
        panel.setBackground(new Color(235, 235, 235));

        tablaMaterias = new JTable();
        DefaultTableModel modeloM = new DefaultTableModel();
        modeloM.addColumn("Informacion");
        tablaMaterias.setModel(modeloM);

        tablaAlumnos = new JTable();
        DefaultTableModel modeloA = new DefaultTableModel();
        modeloA.addColumn("Informacion");
        tablaAlumnos.setModel(modeloA);

        tablaHistorial = new JTable();
        DefaultTableModel modeloH = new DefaultTableModel();
        modeloH.addColumn("Informacion");
        tablaHistorial.setModel(modeloH);

        lblContMaterias = new JLabel("0 materias cargadas");
        lblContAlumnos = new JLabel("0 alumnos cargados");
        lblContHistorial = new JLabel("Sin historial cargado");

        JPanel panelVacio = new JPanel(new BorderLayout());
        panelVacio.setBackground(Color.WHITE);
        panelVacio.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        panel.add(crearPanelTabla("MATERIAS", tablaMaterias, lblContMaterias));
        panel.add(crearPanelTabla("ALUMNOS", tablaAlumnos, lblContAlumnos));
        panel.add(crearPanelTabla("ESTADO ACADEMICO", tablaHistorial, lblContHistorial));
        panel.add(panelVacio);

        return panel;
    }

    private JPanel crearPanelTabla(String titulo, JTable tabla, JLabel contador) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));

        JPanel header = new JPanel(new BorderLayout());
        header.setBackground(new Color(248, 248, 248));
        header.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
                BorderFactory.createEmptyBorder(8, 10, 8, 10)
        ));
        JLabel lblTitulo = new JLabel(titulo);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 12));
        header.add(lblTitulo, BorderLayout.WEST);
        header.add(contador, BorderLayout.EAST);

        panel.add(header, BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);
        return panel;
    }

    private void cargarMaterias() {
        FileDialog fd = new FileDialog(this, "Seleccionar archivo de materias", FileDialog.LOAD);
        fd.setFilenameFilter((dir, name) -> name.endsWith(".txt"));
        fd.setVisible(true);
        if (fd.getFile() == null) return;

        String ruta = new java.io.File(fd.getDirectory(), fd.getFile()).getAbsolutePath();
        Lectura lectura = new Lectura();
        grafoMaterias = lectura.cargarMaterias(ruta);
        if (grafoMaterias == null) return;

        DefaultTableModel modelo = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Año");

        for (int i = 0; i < grafoMaterias.getCapacidad(); i++) {
            Materia m = grafoMaterias.getMateria(i);
            modelo.addRow(new Object[]{ m.getId(), m.getNombre(), m.getAnio() });
        }

        tablaMaterias.setModel(modelo);
        colorearColumnaAnio(tablaMaterias, 2);
        lblContMaterias.setText(grafoMaterias.getCapacidad() + " materias cargadas");
        setStatus("Materias cargadas correctamente - " + grafoMaterias.getCapacidad() + " materias");
    }

    private void cargarCorrelativas() {
        if (grafoMaterias == null) {
            JOptionPane.showMessageDialog(this, "Debe cargar materias primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FileDialog fd = new FileDialog(this, "Seleccionar archivo de correlativas", FileDialog.LOAD);
        fd.setFilenameFilter((dir, name) -> name.endsWith(".txt"));
        fd.setVisible(true);
        if (fd.getFile() == null) return;

        String ruta = new java.io.File(fd.getDirectory(), fd.getFile()).getAbsolutePath();
        Lectura lectura = new Lectura();
        lectura.cargarCorrelativas(ruta, grafoMaterias);

        DefaultTableModel modelo = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        modelo.addColumn("ID");
        modelo.addColumn("Nombre");
        modelo.addColumn("Año");
        modelo.addColumn("Regular");

        int orden = grafoMaterias.getGrafo().getOrden();
        for (int i = 0; i < grafoMaterias.getCapacidad(); i++) {
            Materia m = grafoMaterias.getMateria(i);
            StringBuilder reqs = new StringBuilder();
            for (int w = 0; w < orden; w++) {
                double valor = (double) grafoMaterias.getGrafo().getMatrizCosto().devolver(w, i);
                if (valor == 1.0) {
                    if (reqs.length() > 0) reqs.append(", ");
                    reqs.append(grafoMaterias.getMateria(w).getId());
                }
            }
            modelo.addRow(new Object[]{ m.getId(), m.getNombre(), m.getAnio(), reqs.toString() });
        }

        tablaMaterias.setModel(modelo);
        colorearColumnaAnio(tablaMaterias, 2);
        centrarColumna(tablaMaterias, 3);
        lblContMaterias.setText(grafoMaterias.getCapacidad() + " materias cargadas");
        setStatus("Correlativas cargadas correctamente");
    }

    private void cargarAlumnos() {
        if (grafoMaterias == null) {
            JOptionPane.showMessageDialog(this, "Debe cargar materias primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        FileDialog fd = new FileDialog(this, "Seleccionar archivo de alumnos", FileDialog.LOAD);
        fd.setFilenameFilter((dir, name) -> name.endsWith(".txt"));
        fd.setVisible(true);
        if (fd.getFile() == null) return;

        String ruta = new java.io.File(fd.getDirectory(), fd.getFile()).getAbsolutePath();
        Lectura lectura = new Lectura();
        alumnos = lectura.cargarAlumnos(ruta, grafoMaterias);
        if (alumnos == null) return;

        DefaultTableModel modelo = new DefaultTableModel() {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        modelo.addColumn("Legajo");
        modelo.addColumn("Nombre");

        for (int i = 0; i < alumnos.tamanio(); i++) {
            Alumno a = (Alumno) alumnos.devolver(i);
            modelo.addRow(new Object[]{ a.getLegajo(), a.getNombre() });
        }

        tablaAlumnos.setModel(modelo);
        lblContAlumnos.setText(alumnos.tamanio() + " alumnos cargados");
        setStatus("Alumnos cargados correctamente - " + alumnos.tamanio() + " alumnos");
    }

    private void cargarHistorial() {
        if (grafoMaterias == null || alumnos == null) {
            JOptionPane.showMessageDialog(this, "Debe cargar materias y alumnos primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        Alumno[] arr = new Alumno[alumnos.tamanio()];
        for (int i = 0; i < alumnos.tamanio(); i++)
            arr[i] = (Alumno) alumnos.devolver(i);

        Alumno alumno = (Alumno) JOptionPane.showInputDialog(
            this, "Seleccione el alumno:", "Cargar Estado Academico",
            JOptionPane.PLAIN_MESSAGE, null, arr, arr[0]);
        if (alumno == null) return;

        FileDialog fd = new FileDialog(this, "Seleccionar archivo de historial", FileDialog.LOAD);
        fd.setFilenameFilter((dir, name) -> name.endsWith(".txt"));
        fd.setVisible(true);
        if (fd.getFile() == null) return;

        String ruta = new java.io.File(fd.getDirectory(), fd.getFile()).getAbsolutePath();
        Lectura lectura = new Lectura();
        lectura.cargarHistorial(ruta, alumno, grafoMaterias);
        alumnoSeleccionado = alumno;

        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Materia", "Estado"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (int i = 0; i < grafoMaterias.getCapacidad(); i++) {
            Materia m = grafoMaterias.getMateria(i);
            modelo.addRow(new Object[]{ m.getNombre(), estadoTexto(alumno.getEstado(i)) });
        }
        tablaHistorial.setModel(modelo);
        colorearColumnaEstado(tablaHistorial, 1);
        lblContHistorial.setText("Historial: " + alumno.getNombre());
        setStatus("Historial cargado - " + alumno.getNombre());
    }

    private void abrirSimulador() {
        if (grafoMaterias == null || alumnoSeleccionado == null) {
            JOptionPane.showMessageDialog(this, "Debe cargar materias y estado academico primero", "Advertencia", JOptionPane.WARNING_MESSAGE);
            return;
        }

        SimuladorAcademico sim = new SimuladorAcademico(grafoMaterias);
        sim.cargarDesde(alumnoSeleccionado);

        JDialog dialog = new JDialog(this, "Simulador - Explore caminos", true);
        dialog.setSize(600, 500);
        dialog.setLocationRelativeTo(this);

        JLabel instruccion = new JLabel("  Haga clic en una materia para cambiar su estado");
        instruccion.setFont(new Font("SansSerif", Font.PLAIN, 12));
        instruccion.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        DefaultTableModel modeloSim = new DefaultTableModel(new Object[]{"Materia", "Estado", "Acceso"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        JTable tablaSim = new JTable(modeloSim);
        tablaSim.setRowHeight(28);

        Runnable actualizarTabla = () -> {
            modeloSim.setRowCount(0);
            for (int i = 0; i < grafoMaterias.getCapacidad(); i++) {
                Materia m = grafoMaterias.getMateria(i);
                EstadoAcademico est = sim.getEstado(i);
                boolean disponible = sim.puedeCursarRegular(i);
                modeloSim.addRow(new Object[]{ m.getNombre(), estadoTexto(est), disponible ? "Si" : "No" });
            }
            tablaSim.getColumnModel().getColumn(2).setCellRenderer(
                new javax.swing.table.DefaultTableCellRenderer() {
                    public Component getTableCellRendererComponent(JTable t, Object v,
                        boolean sel, boolean foc, int r, int c) {
                        super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                        setHorizontalAlignment(CENTER);
                        boolean disp = sim.puedeCursarRegular(r);
                        if (disp) { setBackground(VERDE_CLARO); setForeground(new Color(59, 109, 17)); }
                        else      { setBackground(ROJO_CLARO);  setForeground(new Color(163, 45, 45)); }
                        setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                        return this;
                    }
                }
            );
        };

        tablaSim.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent e) {
                int row = tablaSim.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    sim.ciclarEstado(row);
                    actualizarTabla.run();
                }
            }
        });

        actualizarTabla.run();
        dialog.add(instruccion, BorderLayout.NORTH);
        dialog.add(new JScrollPane(tablaSim), BorderLayout.CENTER);
        dialog.setVisible(true);
    }

    private void centrarColumna(JTable tabla, int col) {
        javax.swing.table.DefaultTableCellRenderer r = new javax.swing.table.DefaultTableCellRenderer();
        r.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        tabla.getColumnModel().getColumn(col).setCellRenderer(r);
    }

    private void colorearColumnaAnio(JTable tabla, int col) {
        tabla.getColumnModel().getColumn(col).setCellRenderer(
            new javax.swing.table.DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                    super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                    setHorizontalAlignment(CENTER);
                    String val = v != null ? v.toString() : "";
                    if (val.startsWith("1")) { setBackground(ROJO_CLARO);  setForeground(ROJO_OSCURO); }
                    else if (val.startsWith("2")) { setBackground(AZUL_CLARO); setForeground(AZUL_MEDIO); }
                    else { setBackground(VERDE_CLARO); setForeground(VERDE_OSCURO); }
                    setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                    return this;
                }
            }
        );
    }

    private void colorearColumnaEstado(JTable tabla, int col) {
        tabla.getColumnModel().getColumn(col).setCellRenderer(
            new javax.swing.table.DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                    super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                    setHorizontalAlignment(CENTER);
                    String val = v != null ? v.toString() : "";
                    switch (val) {
                        case "Aprobada":    setBackground(VERDE_CLARO);  setForeground(VERDE_OSCURO); break;
                        case "Regular":     setBackground(AZUL_CLARO);   setForeground(AZUL_MEDIO);  break;
                        case "Cursando":    setBackground(AMBER_CLARO);  setForeground(AMBER_OSCURO); break;
                        case "Libre":
                        case "Desaprobada": setBackground(ROJO_CLARO);   setForeground(ROJO_OSCURO); break;
                        default:            setBackground(GRIS_CLARO);   setForeground(GRIS_OSCURO); break;
                    }
                    setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                    return this;
                }
            }
        );
    }

    private String estadoTexto(EstadoAcademico e) {
        switch (e) {
            case APROBADA:    return "Aprobada";
            case REGULAR:     return "Regular";
            case CURSANDO:    return "Cursando";
            case LIBRE:       return "Libre";
            case DESAPROBADA: return "Desaprobada";
            default:          return "No cursada";
        }
    }

    private JPanel crearStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AZUL_OSCURO);
        bar.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));

        lblStatus = new JLabel("● Simulador listo");
        lblStatus.setForeground(new Color(93, 202, 165));
        lblStatus.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JLabel lblAutores = new JLabel("Luna P. · Gamboa S. · Lopez M.");
        lblAutores.setForeground(new Color(123, 179, 232));
        lblAutores.setFont(new Font("SansSerif", Font.PLAIN, 11));

        bar.add(lblStatus,  BorderLayout.WEST);
        bar.add(lblAutores, BorderLayout.EAST);
        return bar;
    }

    private void setStatus(String msg) {
        lblStatus.setText("● " + msg);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SimuladorGUI().setVisible(true));
    }
}
