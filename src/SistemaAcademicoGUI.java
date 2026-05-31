package archivos;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.awt.event.ActionEvent;

import grafo.GrafoMaterias;
import modelo.*;
import grafo.contenedores.ListaDoubleLinkedL;
import grafo.dirigido.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.ActionEvent;

public class SistemaAcademicoGUI extends JFrame {

    // Tablas del dashboard
    private JTable tablaMaterias;
    private JTable tablaCorrelatividades;
    private JTable tablaAlumnos;
    private JTable tablaHistorial;

    // Lector
    private Lectura lector;

    // Datos cargados
    private GrafoMaterias grafoMaterias;
    private ListaDoubleLinkedL alumnos;

    public SistemaAcademicoGUI() {

        lector = new Lectura();
        setTitle("Sistema de Gestión Académica");
        setSize(1100, 700);
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        inicializarComponentes();
    }

    private void inicializarComponentes() {

        setLayout(new BorderLayout());


        // PANEL SUPERIOR
        JPanel panelMenu = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton btnMaterias = new JButton("Cargar Materias");
        JButton btnCorrelatividades = new JButton("Cargar Correlatividades");
        JButton btnAlumno = new JButton("Cargar Alumnos");
        JButton btnHistorial = new JButton("Cargar Historial Académico");
        JButton btnSolicitud = new JButton("Cargar Solicitud");

        panelMenu.add(btnMaterias);
        panelMenu.add(btnCorrelatividades);
        panelMenu.add(btnAlumno);
        panelMenu.add(btnHistorial);
        panelMenu.add(btnSolicitud);
        add(panelMenu, BorderLayout.NORTH);


        // DASHBOARD CENTRAL 2x2

        JPanel dashboard = new JPanel(new GridLayout(2, 2, 10, 10));
        tablaMaterias = crearTablaVacia();
        tablaCorrelatividades = crearTablaVacia();
        tablaAlumnos = crearTablaVacia();
        tablaHistorial = crearTablaVacia();
        dashboard.add(crearPanel("Materias", tablaMaterias));
        dashboard.add(crearPanel("Materias", tablaCorrelatividades));       
        dashboard.add(crearPanel("Alumnos", tablaAlumnos));
        dashboard.add(crearPanel("Historial Académico", tablaHistorial));
        add(dashboard, BorderLayout.CENTER);

        // EVENTOS

        btnMaterias.addActionListener(e -> cargarMaterias());
        btnCorrelatividades.addActionListener(e -> cargarCorrelatividades());
        btnAlumno.addActionListener(e -> cargarAlumnos());
        btnHistorial.addActionListener(e -> cargarHistorial());
        btnSolicitud.addActionListener(this::mostrarVentanaSolicitud);
    }

    private JTable crearTablaVacia() {
        JTable tabla = new JTable();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Información");
        tabla.setModel(modelo);
        return tabla;
    }

    private JPanel crearPanel(String titulo,JTable tabla) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBorder(
                		BorderFactory.createTitledBorder(
                        BorderFactory.createEtchedBorder(),
                        titulo,
                        TitledBorder.LEFT,
                        TitledBorder.TOP
                ));
        panel.add(new JScrollPane(tabla),BorderLayout.CENTER);
        return panel;
    }

    private void mostrarMensaje(JTable tabla,String mensaje) {
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Estado");
        modelo.addRow(new Object[]{mensaje});
        tabla.setModel(modelo);
    }

    private String seleccionarArchivo() {

        JFileChooser chooser = new JFileChooser();
        int opcion = chooser.showOpenDialog(this);
        if (opcion ==JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile().getAbsolutePath();
        }
        return null;
    }


    // CARGAR MATERIAS


    private void cargarMaterias() {
        String ruta = seleccionarArchivo();
        if (ruta == null) {return;}
        grafoMaterias = lector.cargarMaterias(ruta);
        if (grafoMaterias != null) {
        	DefaultTableModel modelo = new DefaultTableModel();
            modelo.addColumn("ID");
            modelo.addColumn("Nombre");
            modelo.addColumn("Año");
            for (int i = 0;i < grafoMaterias.getCapacidad(); i++) {
                Materia m = grafoMaterias.getMateria(i);
                modelo.addRow( new Object[]{
                     m.getId(),
                     m.getNombre(),
                     m.getAnio()
                     }
                );
            }
            tablaMaterias.setModel(modelo);        	
            //mostrarMensaje(tablaMaterias,"Materias cargadas. Cantidad: " + grafoMaterias.getCapacidad());
            //JOptionPane.showMessageDialog(this,"Materias cargadas correctamente.");
        }
    }


    // CARGAR CORRELATIVIDADES

    private void cargarCorrelatividades() {
        /*if (grafoMaterias == null) {
            JOptionPane.showMessageDialog(this,
            		"Debe cargar materias primero.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;}*/
        String ruta = seleccionarArchivo();
        if (ruta == null) {return;}
        lector.cargarCorrelativas(ruta,grafoMaterias);
        
        DefaultTableModel modelo =
                new DefaultTableModel(
                        new Object[]{
                                "Materia Necesaria",
                                "Estado",
                                "Materia"
                        }, 0);
        GrafoDirigido grafo = grafoMaterias.getGrafo();
        int orden = grafo.getOrden();
        for (int i = 0; i < orden; i++) {
            for (int j = 0; j < orden; j++) {
                Object obj = grafo.getMatrizCosto().devolver(i, j);
                if (obj == null) {continue;}
                double valor = (double) obj;
                String tipo;
                if (valor == 1.0) { tipo = "R";}
                else if (valor == 2.0) {tipo = "A"; }
                else {continue;}
                Materia origen = grafoMaterias.getMateria(i);
                Materia destino = grafoMaterias.getMateria(j);
                modelo.addRow(
                        new Object[]{
                                origen.getNombre(),
                                tipo,
                                destino.getNombre()
                        }
                );
            }
        }

        tablaCorrelatividades.setModel(modelo);
        
        /*mostrarMensaje(
                tablaCorrelatividades,
                "Correlatividades cargadas correctamente"
        );

        JOptionPane.showMessageDialog(
                this,
                "Correlatividades cargadas."
        );*/
    }

    // ==================================
    // CARGAR ALUMNOS
    // ==================================

    private void cargarAlumnos() {
        if (grafoMaterias == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe cargar materias primero.","Advertencia",
                    JOptionPane.WARNING_MESSAGE);
            return;
        }
        String ruta = seleccionarArchivo();
        if (ruta == null) { return;}
        alumnos = lector.cargarAlumnos(ruta,grafoMaterias);
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Legajo");
        modelo.addColumn("Nombre");
        for (int i = 0;i < alumnos.tamanio();i++) {
            Alumno a = (Alumno) alumnos.devolver(i);
            modelo.addRow(new Object[]{
                            a.getLegajo(),
                            a.getNombre()
                    }
            );
        }

        tablaAlumnos.setModel(modelo);
    }

    // ==================================
    // CARGAR HISTORIAL
    // ==================================

    private void cargarHistorial() {
 
        if (grafoMaterias == null || alumnos == null) {
            JOptionPane.showMessageDialog(
                    this,
                    "Debe cargar materias y alumnos primero.",
                    "Advertencia",
                    JOptionPane.WARNING_MESSAGE
            );
            return;}
        
        String ruta = seleccionarArchivo();
        if (ruta == null) {return;}
        String legajoTexto =
                JOptionPane.showInputDialog(
                        this,
                        "Ingrese el legajo del alumno:"
                );
        if (legajoTexto == null ||legajoTexto.trim().isEmpty()) {return;}

        try {

            int legajo =
                    Integer.parseInt(
                            legajoTexto
                    );

            Alumno alumno =
                    buscarAlumno(legajo);

            if (alumno == null) {

                JOptionPane.showMessageDialog(
                        this,
                        "Alumno no encontrado.",
                        "Error",
                        JOptionPane.ERROR_MESSAGE
                );

                return;
            }

            lector.cargarHistorial(ruta,alumno,grafoMaterias);
            mostrarMensaje(tablaHistorial,"Historial cargado para " + alumno.getNombre());

            JOptionPane.showMessageDialog(
                    this,
                    "Historial cargado correctamente."
            );

        } catch (NumberFormatException ex) {

            JOptionPane.showMessageDialog(
                    this,
                    "Legajo inválido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    }

    // ==================================
    // BUSCAR ALUMNO
    // ==================================

    private Alumno buscarAlumno(int legajo) {
        for (int i = 0;i < alumnos.tamanio();i++) {
            Alumno alumno =(Alumno) alumnos.devolver(i);
            if (alumno.getLegajo() == legajo) {
                return alumno;}
        }
        return null;
    }

    // ==================================
    // VENTANA SOLICITUD
    // ==================================

    private void mostrarVentanaSolicitud(ActionEvent e) {
        JDialog dialogo = new JDialog( this,"Solicitud Académica",true);
        dialogo.setSize(600, 400);
        dialogo.setLocationRelativeTo(this);
        dialogo.setLayout(new BorderLayout());
        JLabel titulo = new JLabel("Gestión de Solicitud",SwingConstants.CENTER);
        JTable tablaSolicitud = new JTable();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Información");
        modelo.addRow(new Object[]{"Funcionalidad pendiente de implementación"});
        tablaSolicitud.setModel(modelo);
        JButton btnCerrar = new JButton("Cerrar");
        btnCerrar.addActionListener(evento -> dialogo.dispose() );
        dialogo.add(titulo,BorderLayout.NORTH);
        dialogo.add(new JScrollPane(tablaSolicitud),BorderLayout.CENTER);
        dialogo.add(btnCerrar,BorderLayout.SOUTH);
        dialogo.setVisible(true);
    }

    // ==================================
    // Testeo
    // ==================================

    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            SistemaAcademicoGUI gui =
                    new SistemaAcademicoGUI();

            gui.setVisible(true);
        });
    }
}