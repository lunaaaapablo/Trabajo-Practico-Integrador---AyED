package modelo;
//objeto que representa a una materia, con su id, nombre y año
public class Materia {
    private int id;
    private String nombre;
    private int anio;
//constructor
    public Materia(int id, String nombre, int anio) {
        this.id = id;
        this.nombre = nombre;
        this.anio = anio;
    }
//getters
    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public int getAnio() {
        return anio;
    }

//funcion toString para mostrar la materia en el comboBox, comboBox es un componente grafico que permite 
// seleccionar una opcion de una lista desplegable, en este caso se mostrara el id y el nombre de la materia
    @Override
    public String toString() {
        return id + " - " + nombre;
    }
    
}
