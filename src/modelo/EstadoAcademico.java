package modelo;

public enum EstadoAcademico {

    NO_CURSADA(0),
    CURSANDO(1),
    REGULAR(2),
    APROBADA(3),
    LIBRE(4),
    DESAPROBADA(5);

    private int codigo;

    EstadoAcademico(int codigo) {
        this.codigo = codigo;
    }

    public int getCodigo() {
        return codigo;
    }
}

//enum es una clase especial que representa un conjunto de constantes, 
// en este caso los estados academicos de un alumno, cada estado tiene un codigo asociado,
//  el codigo se puede obtener con el metodo getCodigo()
//Esto ayuda a evitar errores de tipeo al usar los estados academicos,
//  ya que se pueden usar las constantes en lugar de escribir el codigo directamente.