package grafo;

import grafo.contenedores.ListaDoubleLinkedL;
import grafo.dirigido.GrafoDirigido;
import modelo.Materia;

public class GrafoMaterias {

    private GrafoDirigido grafo;
    private ListaDoubleLinkedL materias;

    public GrafoMaterias(int cantMaterias) {
        this.grafo    = new GrafoDirigido(cantMaterias);
        this.materias = new ListaDoubleLinkedL();
    }

    public void agregarMateria(Materia m) {
        materias.insertar(m, materias.tamanio());
    }

    public int buscarIndicePorId(int id) {
        for (int i = 0; i < materias.tamanio(); i++) {
            Materia m = (Materia) materias.devolver(i);
            if (m.getId() == id) return i;
        }
        return -1;
    }

    public void  agregarCorrelativa(int idOrigen, int idDestino, int valor){
        int i  = buscarIndicePorId(idOrigen);
        int j = buscarIndicePorId(idDestino);

        if (i == -1 || j == -1) {
            System.err.println("Error: Materia no encontrada (idOrigen: " + idOrigen + ", idDestino: " + idDestino + ")");
            return;
        }

        grafo.getMatrizCosto().actualizar((double) valor, i, j);
    }

    public Materia getMateria(int indice) {
        return (Materia) materias.devolver(indice);
    }

    public GrafoDirigido getGrafo()         { return grafo; }
    public ListaDoubleLinkedL getMaterias() { return materias; }

    public int getCapacidad() {
        return materias.tamanio();
    }

    public void mostrarPlanEstudios() {//funcion para mostrar el plan de estudios con sus correlativas
    int orden = grafo.getOrden();
    
    for (int i = 0; i < orden; i++) {
        for (int j = 0; j < orden; j++) {
            double valor = (double) grafo.getMatrizCosto().devolver(i, j);
            
            if (valor != grafo.getInfinito()) {
                Materia origen  = getMateria(i);
                Materia destino = getMateria(j);
                String etiqueta = valor == 1.0 ? "R" : "A";
                
                System.out.println(origen.getNombre() 
                    + " → " + destino.getNombre() 
                    + " (" + etiqueta + ")");
            }
        }
    }
}

}

