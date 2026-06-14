package Testeos;

import javax.swing.SwingUtilities;

import interfaz.SimuladorGUI;

public class TesteosMaxi {
    public static void main(String[] args) {

        SwingUtilities.invokeLater(() -> {

            SimuladorGUI gui =
                    new SimuladorGUI();

            gui.setVisible(true);
        });
    }
}
