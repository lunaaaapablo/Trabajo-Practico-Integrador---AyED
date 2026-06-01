package Testeos;

import javax.swing.SwingUtilities;

import interfaz.SistemaAcademicoGUI;

public class TesteoInterfaz {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            SistemaAcademicoGUI gui =
                    new SistemaAcademicoGUI();

            gui.setVisible(true);
        });
    }
}

