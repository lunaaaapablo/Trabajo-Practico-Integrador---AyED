package reglass;
//esta clase sirve para representar el resultado del analisis , indicando si aprueba o no y un mensaje explicativo
public class Dictamen {
    private boolean aprobado;
    private String mensaje;

    public Dictamen(boolean aprobado, String mensaje) {
        this.aprobado = aprobado;
        this.mensaje  = mensaje;
    }

    public boolean isAprobado() {
        return aprobado;
    }
    public String getMensaje()  { 
        return mensaje; 
    }

    @Override
    public String toString() {
        return (aprobado ? "APROBADO" : "RECHAZADO") + " - " + mensaje;
    }
}
