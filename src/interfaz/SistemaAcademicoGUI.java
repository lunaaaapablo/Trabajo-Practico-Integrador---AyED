package interfaz;

import javax.swing.*;
import javax.swing.border.*;
import javax.swing.table.*;
import java.awt.*;
import java.awt.event.*;
import grafo.GrafoMaterias;
import modelo.*;
import reglas.*;
import grafo.contenedores.ListaDoubleLinkedL;
import grafo.dirigido.*;
import archivos.Lectura;

public class SistemaAcademicoGUI extends JFrame {
    // Colores
    private static final Color AZUL_OSCURO  = new Color(31, 56, 100);
    private static final Color AZUL_MEDIO   = new Color(24, 95, 165);
    private static final Color AZUL_CLARO   = new Color(230, 241, 251);
    private static final Color VERDE_CLARO  = new Color(234, 243, 222);
    private static final Color VERDE_OSCURO = new Color(59, 109, 17);
    private static final Color ROJO_CLARO   = new Color(252, 235, 235);
    private static final Color ROJO_OSCURO  = new Color(163, 45, 45);
    private static final Color AMBER_CLARO  = new Color(250, 238, 218);
    private static final Color AMBER_OSCURO = new Color(133, 79, 11);
    private static final Color GRIS_CLARO   = new Color(241, 239, 232);
    private static final Color GRIS_OSCURO  = new Color(95, 94, 90);
    private static final Color TEAL_CLARO   = new Color(225, 245, 238);
    private static final Color TEAL_OSCURO  = new Color(15, 110, 86);

    // Componentes GUI
    private JTable tablaMaterias;
    private JTable tablaCorrelatividades;
    private JTable tablaAlumnos;
    private JTable tablaHistorial;
    private JLabel lblStatus;
    private JLabel lblContMaterias;
    private JLabel lblContCorrelativas;
    private JLabel lblContAlumnos;


    // Lector de archivos
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
        add(crearEncabezado(), BorderLayout.NORTH);
        add(crearDashboard(), BorderLayout.CENTER);
        add(crearStatusBar(), BorderLayout.SOUTH);
    }

        private JPanel crearEncabezado() {
        JPanel panelEncabezado = new JPanel(new BorderLayout());

        //titulo
        JPanel titulo = new JPanel(new BorderLayout());
        titulo.setBackground(AZUL_OSCURO);
        titulo.setBorder(BorderFactory.createEmptyBorder(10, 16, 10, 16));

        JLabel lblTitulo = new JLabel("Sistema de Gestión Académica");
        lblTitulo.setForeground(Color.WHITE);
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 15));

        JLabel lblSubtitulo = new JLabel("Tecnicatura Universitaria en Programación");
        lblSubtitulo.setForeground(Color.WHITE);
        lblSubtitulo.setFont(new Font("SansSerif", Font.PLAIN, 11));

        JPanel Left = new JPanel(new GridLayout(2, 1, 0,2));
        Left.setOpaque(false);
        Left.add(lblTitulo);
        Left.add(lblSubtitulo);

        JLabel lblRight = new JLabel("TPI - Algoritmos y Estructuras de Datos ");
        lblRight.setForeground(Color.WHITE);
        lblRight.setFont(new Font("SansSerif", Font.ITALIC, 11));

        titulo.add(Left, BorderLayout.WEST);
        titulo.add(lblRight, BorderLayout.EAST);

        //toolbar
        JPanel toolbar = new JPanel(new FlowLayout(FlowLayout.LEFT, 8,8));
        toolbar.setBackground(AZUL_MEDIO);
        toolbar.setBorder(BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)));
       
        JButton btnMaterias = crearBoton("Cargar Materias", AZUL_CLARO, AZUL_MEDIO);
        JButton btnCorrelatividades = crearBoton("Cargar Correlatividades", AZUL_CLARO, AZUL_MEDIO);
        JButton btnAlumno = crearBoton("Cargar Alumnos", AZUL_CLARO, AZUL_MEDIO);
        JButton btnHistorial = crearBoton("Cargar Historial Académico", AZUL_CLARO, AZUL_MEDIO);
        JButton btnSolicitud = crearBoton("Cargar Solicitud", AZUL_CLARO, AZUL_MEDIO);
        btnSolicitud.setFont(new Font("SansSerif",Font.BOLD,12));

        toolbar.add(btnMaterias);
        toolbar.add(btnCorrelatividades);
        toolbar.add(btnAlumno);
        toolbar.add(btnHistorial);
        toolbar.add(Box.createHorizontalStrut(20)); // Espacio entre botones y título
        toolbar.add(btnSolicitud);

        btnMaterias.addActionListener(e -> cargarMaterias());
        btnCorrelatividades.addActionListener(e -> cargarCorrelatividades());
        btnAlumno.addActionListener(e -> cargarAlumnos());
        btnHistorial.addActionListener(e -> cargarHistorial());
        btnSolicitud.addActionListener(this::mostrarVentanaSolicitud);
        
        panelEncabezado.add(titulo, BorderLayout.NORTH);
        panelEncabezado.add(toolbar, BorderLayout.SOUTH);
        return panelEncabezado;
    }   

    private JButton crearBoton(String texto, Color colorFondo, Color colorHover) {
        JButton boton = new JButton(texto);
            boton.setBackground(colorFondo);
            boton.setForeground(colorHover);
            boton.setFont(new Font("SansSerif", Font.PLAIN, 12));
            boton.setBorder(BorderFactory.createCompoundBorder(
                    BorderFactory.createLineBorder(colorHover.brighter(), 1),
                    BorderFactory.createEmptyBorder(5, 12, 5, 12)
            ));
            boton.setFocusPainted(false);
            boton.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            return boton;
        }

    //DASHBOARD , significa el panel central donde se muestran las tablas
    private JPanel crearDashboard(){
        JPanel dashboard = new JPanel(new GridLayout(2, 2, 6, 6));
        dashboard.setBorder(BorderFactory.createEmptyBorder(6,6,6,6));
        dashboard.setBackground(new Color(235,235,235));
        //dashboard.setBackground(new Color(45, 52, 65)); // gris azulado oscuro
        tablaMaterias = crearTablaVacia();
        tablaCorrelatividades = crearTablaVacia();
        tablaAlumnos = crearTablaVacia();
        tablaHistorial = crearTablaVacia();

        lblContMaterias = new JLabel("0 materias cargadas");
        lblContCorrelativas = new JLabel("0 correlatividades cargadas");
        lblContAlumnos = new JLabel("0 alumnos cargados");
        JLabel lblContHistorial = new JLabel("0 historiales cargados");

        dashboard.add(crearPanel("Materias", "📚", tablaMaterias, lblContMaterias));
        dashboard.add(crearPanel("Correlatividades", "🔗", tablaCorrelatividades,lblContCorrelativas));
        dashboard.add(crearPanel("Alumnos", "👥", tablaAlumnos, lblContAlumnos));
        dashboard.add(crearPanel("Historial Académico", "📜", tablaHistorial,lblContHistorial));
        return dashboard;
    }
    private JTable crearTablaVacia() {
        JTable tabla = new JTable();
        DefaultTableModel modelo = new DefaultTableModel();
        modelo.addColumn("Información");
        tabla.setModel(modelo);
        return tabla;
    }

    private JPanel crearPanel(String titulo,String icono,JTable tabla,JLabel contador) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(Color.WHITE);
        panel.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220), 1));
       
       //header
       JPanel header = new JPanel(new BorderLayout());
       header.setBackground(new Color(248, 248, 248));
       header.setBorder(BorderFactory.createCompoundBorder(
            BorderFactory.createMatteBorder(0, 0, 1, 0, new Color(220, 220, 220)),
            BorderFactory.createEmptyBorder(6, 12, 6, 12)
        ));

         JLabel lblTitulo = new JLabel(icono + "  " + titulo.toUpperCase());
        lblTitulo.setFont(new Font("SansSerif", Font.BOLD, 11));
        lblTitulo.setForeground(GRIS_OSCURO);

        contador.setFont(new Font("SansSerif", Font.PLAIN, 11));
        contador.setForeground(GRIS_OSCURO);

        header.add(lblTitulo, BorderLayout.WEST);
        header.add(contador,  BorderLayout.EAST);

        // tabla
        tabla.setFont(new Font("SansSerif", Font.PLAIN, 12));
        tabla.setRowHeight(26);
        tabla.getTableHeader().setFont(new Font("SansSerif", Font.BOLD, 11));
        tabla.getTableHeader().setBackground(new Color(248, 248, 248));
        tabla.getTableHeader().setForeground(GRIS_OSCURO);
        tabla.setShowGrid(false);
        tabla.setIntercellSpacing(new Dimension(0, 0));
        tabla.setSelectionBackground(AZUL_CLARO);

        panel.add(header,              BorderLayout.NORTH);
        panel.add(new JScrollPane(tabla), BorderLayout.CENTER);

        return panel;
    }

     // ── STATUS BAR ────────────────────────────────────────────────────
    private JPanel crearStatusBar() {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(AZUL_OSCURO);
        bar.setBorder(BorderFactory.createEmptyBorder(4, 16, 4, 16));

        lblStatus = new JLabel("● Sistema listo");
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
        if (grafoMaterias == null) return; 

    	DefaultTableModel modelo = new DefaultTableModel(){
    public boolean isCellEditable(int r, int c) { return false; }
};

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
            colorearColumnaAnio(tablaMaterias, 2);
            lblContMaterias.setText(grafoMaterias.getCapacidad() + " materias cargadas");
            setStatus("Materias cargadas correctamente - " + grafoMaterias.getCapacidad() + " materias cargadas");
            //mostrarMensaje(tablaMaterias,"Materias cargadas. Cantidad: " + grafoMaterias.getCapacidad());
            //JOptionPane.showMessageDialog(this,"Materias cargadas correctamente.");
        }
    


    // CARGAR CORRELATIVIDADES

    private void cargarCorrelatividades() {
        if(grafoMaterias == null){
            advertencia("Debe cargar materias antes de cargar correlatividades.");
            return; 
        }
        String ruta = seleccionarArchivo();
        if (ruta == null) return;

        lector.cargarCorrelativas(ruta,grafoMaterias);
        
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Materia Necesaria","Estado","Materia"}, 0){
            public boolean isCellEditable(int r, int c) { return false; }
            };
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
        colorearColumnaEstado(tablaCorrelatividades, 1);
        lblContCorrelativas.setText(modelo.getRowCount() + " correlatividades cargadas");
        setStatus("Correlatividades cargadas correctamente - " + modelo.getRowCount() + " correlatividades cargadas");
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
            advertencia("Debe cargar materias antes de cargar alumnos.");
            return;
        }
        String ruta = seleccionarArchivo();
        if (ruta == null) return;

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
        lblContAlumnos.setText(alumnos.tamanio() + " alumnos cargados");
        setStatus("Alumnos cargados correctamente - " + alumnos.tamanio() + "   alumnos cargados");
    }

    // ==================================
    // CARGAR HISTORIAL
    // ==================================

  /*   private void cargarHistorial() {
 
        if (grafoMaterias == null || alumnos == null) {
            advertencia("Debe cargar materias y alumnos primero.");
            return;
        }

        String ruta = seleccionarArchivo();
        if (ruta == null) {return;}
        String legajoTexto =
                JOptionPane.showInputDialog(
                        this,
                        "Ingrese el legajo del alumno:"
                );
        if (legajoTexto == null ||legajoTexto.trim().isEmpty()) {return;}
        try {
            int legajo = Integer.parseInt(legajoTexto);
            Alumno alumno =buscarAlumno(legajo);
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
*/
 private void cargarHistorial() {
        if (grafoMaterias == null || alumnos == null) {
            advertencia("Debe cargar materias y alumnos primero.");
            return;
        }

        // seleccionar alumno con combo
        Alumno[] arr = new Alumno[alumnos.tamanio()];
        for (int i = 0; i < alumnos.tamanio(); i++)
            arr[i] = (Alumno) alumnos.devolver(i);

        Alumno alumno = (Alumno) JOptionPane.showInputDialog(
            this, "Seleccione el alumno:", "Cargar Historial",
            JOptionPane.PLAIN_MESSAGE, null, arr, arr[0]);
        if (alumno == null) return;

        String ruta = seleccionarArchivo();
        if (ruta == null) return;
        lector.cargarHistorial(ruta, alumno, grafoMaterias);

        // mostrar historial con colores
        DefaultTableModel modelo = new DefaultTableModel(
            new Object[]{"Materia", "Estado"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        for (int i = 0; i < grafoMaterias.getCapacidad(); i++) {
            Materia m = grafoMaterias.getMateria(i);
            EstadoAcademico estado = alumno.getEstado(i);
            modelo.addRow(new Object[]{ m.getNombre(), estadoTexto(estado) });
        }
        tablaHistorial.setModel(modelo);
        colorearColumnaEstado(tablaHistorial, 1);
        setStatus("Historial cargado — " + alumno.getNombre());
    }

    // BUSCAR ALUMNO

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
       if (grafoMaterias == null || alumnos == null) {
       advertencia("Debe cargar materias y alumnos primero.");
        return;
    }
    
    JDialog dialogo = new JDialog(this,"Solicitud de Cursado Condicional",true);
    dialogo.setSize(550, 420);
    dialogo.setLocationRelativeTo(this);
    dialogo.setLayout(new BorderLayout());
    // PANEL SUPERIOR
    JPanel panelDatos = new JPanel(new GridLayout(3, 2, 10, 10));
    panelDatos.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
    JComboBox<Alumno> comboAlumnos = new JComboBox<>();
    for (int i = 0;i < alumnos.tamanio(); i++) {comboAlumnos.addItem((Alumno) alumnos.devolver(i));}
    JTextField txtMateria = new JTextField();
    panelDatos.add(new JLabel("Alumno:"));
    panelDatos.add(comboAlumnos);
    panelDatos.add(new JLabel("ID Materia:"));
    panelDatos.add(txtMateria);
    JButton btnAnalizar =new JButton("Analizar Solicitud");
    panelDatos.add(new JLabel());
    panelDatos.add(btnAnalizar);
    dialogo.add(panelDatos,BorderLayout.NORTH);
    // RESULTADO
    JTextArea areaResultado = new JTextArea();
    areaResultado.setEditable(false);
    areaResultado.setFont(new Font("Monospaced",Font.PLAIN,14));
    dialogo.add(new JScrollPane(areaResultado),BorderLayout.CENTER);
    // BOTON ANALIZAR
    btnAnalizar.addActionListener(ev -> {
        try {
            Alumno alumno =(Alumno)comboAlumnos.getSelectedItem();
            int idMateria =Integer.parseInt( txtMateria.getText().trim());
            MotorReglas motor = new MotorReglas(grafoMaterias);
            Dictamen dictamen = motor.analizar(alumno,idMateria);
            StringBuilder sb = new StringBuilder();
            sb.append("ALUMNO = " + alumno.getNombre());
            sb.append("\nRESULTADO = ");
            sb.append(dictamen.isAprobado()? "APROBADO" : "RECHAZADO");
            sb.append("\n" + dictamen.getMensaje());
            areaResultado.setText(sb.toString());
        } catch (
                NumberFormatException ex
        ) {

            JOptionPane.showMessageDialog(
                    dialogo,
                    "Ingrese un ID de materia válido.",
                    "Error",
                    JOptionPane.ERROR_MESSAGE
            );
        }
    });
    dialogo.setVisible(true);
    }

        // ── COLOREADO DE TABLAS ───────────────────────────────────────────
    private void colorearColumnaAnio(JTable tabla, int col) {
        tabla.getColumnModel().getColumn(col).setCellRenderer(
            new DefaultTableCellRenderer() {
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

    private void colorearColumnaTipo(JTable tabla, int col) {
        tabla.getColumnModel().getColumn(col).setCellRenderer(
            new DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                    super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                    setHorizontalAlignment(CENTER);
                    String val = v != null ? v.toString() : "";
                    if (val.equals("R")) { setBackground(AZUL_CLARO);  setForeground(AZUL_MEDIO); }
                    else                 { setBackground(AMBER_CLARO); setForeground(AMBER_OSCURO); }
                    setBorder(BorderFactory.createEmptyBorder(0, 8, 0, 8));
                    return this;
                }
            }
        );
    }

    private void colorearColumnaEstado(JTable tabla, int col) {
        tabla.getColumnModel().getColumn(col).setCellRenderer(
            new DefaultTableCellRenderer() {
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

    // ── HELPERS ───────────────────────────────────────────────────────
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

   /*  private String seleccionarArchivo() {
        JFileChooser chooser = new JFileChooser();
        int opcion = chooser.showOpenDialog(this);
        return opcion == JFileChooser.APPROVE_OPTION
            ? chooser.getSelectedFile().getAbsolutePath()
            : null;
    }
*/
    private void advertencia(String msg) {
        JOptionPane.showMessageDialog(this, msg, "Advertencia", JOptionPane.WARNING_MESSAGE);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SistemaAcademicoGUI().setVisible(true));
    }
}
    /*  Testeo
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            SistemaAcademicoGUI gui = new SistemaAcademicoGUI();
            gui.setVisible(true);
        });
    }*/
