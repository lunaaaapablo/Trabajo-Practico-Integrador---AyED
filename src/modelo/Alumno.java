package modelo;

public class Alumno {//un alumno tiene un legajo, un nombre y un arreglo de estados académicos para cada materia del plan de estudios (índice = posición en el grafo)
    private int legajo;
    private String nombre;
    private EstadoAcademico[] estadoMaterias; // índice = posición en el grafo

    public Alumno(int legajo, String nombre, int cantMaterias) {
        this.legajo = legajo;
        this.nombre = nombre;
        this.estadoMaterias = new EstadoAcademico[cantMaterias];
        // inicializar todo en NO_CURSADA
        for (int i = 0; i < cantMaterias; i++) {
            estadoMaterias[i] = EstadoAcademico.NO_CURSADA;
        }
    }

    public int getLegajo(){ 
        return legajo; 
    }
    public String getNombre() {
         return nombre; 
    }

    public void setEstado(int indice, EstadoAcademico estado) {
        estadoMaterias[indice] = estado;
    }

    public EstadoAcademico getEstado(int indice) {
        return estadoMaterias[indice];
    }
    
    @Override
    public String toString() {
        return legajo + " - " + nombre;
    }
}
