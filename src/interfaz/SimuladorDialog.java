package interfaz;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import grafo.GrafoMaterias;
import modelo.EstadoAcademico;
import modelo.Materia;
import modelo.SimuladorAcademico;

public class SimuladorDialog extends JDialog {

    private static final Color VERDE_CLARO = new Color(234, 243, 222);
    private static final Color ROJO_CLARO  = new Color(252, 235, 235);
    private static final Color AMBER_CLARO = new Color(250, 238, 218);

    private SimuladorAcademico simulador;
    private GrafoMaterias grafoMaterias;
    private JTable tabla;
    private DefaultTableModel modelo;

    public SimuladorDialog(Frame owner, SimuladorAcademico simulador, GrafoMaterias grafoMaterias) {
        super(owner, "Simulador - Explore caminos", true);
        this.simulador = simulador;
        this.grafoMaterias = grafoMaterias;
        setSize(600, 500);
        setLocationRelativeTo(owner);

        JLabel instruccion = new JLabel("  Haga clic en una materia para cambiar su estado");
        instruccion.setFont(new Font("SansSerif", Font.PLAIN, 12));
        instruccion.setBorder(BorderFactory.createEmptyBorder(8, 8, 4, 8));

        modelo = new DefaultTableModel(new Object[]{"Materia", "Estado", "Acceso"}, 0) {
            public boolean isCellEditable(int r, int c) { return false; }
        };
        tabla = new JTable(modelo);
        tabla.setRowHeight(28);
        tabla.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                int row = tabla.rowAtPoint(e.getPoint());
                if (row >= 0) {
                    simulador.ciclarEstado(row);
                    actualizarTabla();
                }
            }
        });

        actualizarTabla();
        add(instruccion, BorderLayout.NORTH);
        add(new JScrollPane(tabla), BorderLayout.CENTER);
    }

    private void actualizarTabla() {
        modelo.setRowCount(0);
        for (int i = 0; i < grafoMaterias.getCapacidad(); i++) {
            Materia m = grafoMaterias.getMateria(i);
            EstadoAcademico est = simulador.getEstado(i);
            boolean disponible = simulador.puedeCursarRegular(i);
            modelo.addRow(new Object[]{ m.getNombre(), estadoTexto(est), disponible ? "Si" : "No" });
        }
        colorearFilas();
    }

    private void colorearFilas() {
        tabla.getColumnModel().getColumn(2).setCellRenderer(
            new javax.swing.table.DefaultTableCellRenderer() {
                public Component getTableCellRendererComponent(JTable t, Object v,
                    boolean sel, boolean foc, int r, int c) {
                    super.getTableCellRendererComponent(t, v, sel, foc, r, c);
                    setHorizontalAlignment(CENTER);
                    boolean disp = simulador.puedeCursarRegular(r);
                    if (disp) { setBackground(VERDE_CLARO); setForeground(new Color(59, 109, 17)); }
                    else      { setBackground(ROJO_CLARO);  setForeground(new Color(163, 45, 45)); }
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
}
