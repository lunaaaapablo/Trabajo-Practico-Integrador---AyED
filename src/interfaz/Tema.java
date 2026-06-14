package interfaz;

import java.awt.Color;

public class Tema {

    public static Color AZUL_OSCURO;
    public static Color AZUL_MEDIO;
    public static Color AZUL_CLARO;

    public static Color FONDO;
    public static Color FONDO_SECUNDARIO;
    public static Color FONDO_CABECERA;
    public static Color FONDO_TABLA;
    public static Color FONDO_SELECCION;

    public static Color TEXTO;
    public static Color TEXTO_INVERTIDO;
    public static Color TEXTO_CONTADOR;
    public static Color BORDE;

    public static Color BOTON_FONDO;
    public static Color BOTON_TEXTO;
    public static Color BOTON_BORDE;

    public static Color STATUS_VERDE;
    public static Color STATUS_AZUL;

    private static boolean oscuro;

    static {
        claro();
    }

    public static void claro() {
        AZUL_OSCURO = new Color(31, 56, 100);
        AZUL_MEDIO = new Color(24, 95, 165);
        AZUL_CLARO = new Color(230, 241, 251);

        FONDO = Color.WHITE;
        FONDO_SECUNDARIO = new Color(235, 235, 235);
        FONDO_CABECERA = new Color(248, 248, 248);
        FONDO_TABLA = Color.WHITE;
        FONDO_SELECCION = new Color(232, 240, 254);

        TEXTO = Color.BLACK;
        TEXTO_INVERTIDO = Color.WHITE;
        TEXTO_CONTADOR = Color.BLACK;
        BORDE = new Color(220, 220, 220);

        BOTON_FONDO = new Color(230, 241, 251);
        BOTON_TEXTO = new Color(24, 95, 165);
        BOTON_BORDE = new Color(24, 95, 165).brighter();

        STATUS_VERDE = new Color(93, 202, 165);
        STATUS_AZUL = new Color(123, 179, 232);

        oscuro = false;
    }

    public static void oscuro() {
        AZUL_OSCURO = new Color(10, 20, 45);
        AZUL_MEDIO = new Color(15, 45, 90);
        AZUL_CLARO = new Color(25, 55, 110);

        FONDO = new Color(35, 35, 35);
        FONDO_SECUNDARIO = new Color(28, 28, 28);
        FONDO_CABECERA = new Color(45, 45, 45);
        FONDO_TABLA = new Color(40, 40, 40);
        FONDO_SELECCION = new Color(15, 40, 80);

        TEXTO = new Color(200, 200, 200);
        TEXTO_INVERTIDO = new Color(220, 220, 220);
        TEXTO_CONTADOR = new Color(180, 180, 180);
        BORDE = new Color(70, 70, 70);

        BOTON_FONDO = Color.BLACK;
        BOTON_TEXTO = Color.WHITE;
        BOTON_BORDE = new Color(80, 80, 80);

        STATUS_VERDE = new Color(60, 170, 130);
        STATUS_AZUL = new Color(100, 160, 220);

        oscuro = true;
    }

    public static boolean esOscuro() {
        return oscuro;
    }
}
