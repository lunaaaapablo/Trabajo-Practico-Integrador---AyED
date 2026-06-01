//reglas base es una clase abstracta que define la estructura 
// común para todas las reglas de correlativas
package reglas;
import grafo.GrafoMaterias;
import grafo.contenedores.ListaDoubleLinkedL;
import modelo.Alumno;

public abstract class ReglaBase {

    protected GrafoMaterias grafoMaterias;
    protected String mensaje;

    public ReglaBase(GrafoMaterias grafoMaterias) {
        this.grafoMaterias = grafoMaterias;
    }

    // cada regla implementa su propia lógica
    public abstract boolean cumple(Alumno alumno, int indiceMateria);

    public String getMensaje() {
        return mensaje;
    }

    // algoritmo de búsqueda en profundidad para recorrer las correlativas
    protected void bpf(Alumno alumno, int v, ListaDoubleLinkedL listaMarca) {
        listaMarca.reemplazar(true, v);// marca el nodo actual como visitado
        int orden = grafoMaterias.getGrafo().getOrden();// cantidad de materias

        for (int w = 0; w < orden; w++) {
            boolean marcado = (boolean) listaMarca.devolver(w);
            double valor = (double) grafoMaterias.getGrafo().getMatrizCosto().devolver(w, v);

            if (valor != grafoMaterias.getGrafo().getInfinito() && !marcado) {
                verificarNodo(alumno, w, valor); // cada regla decide qué hacer
                bpf(alumno, w, listaMarca);
            }
        }
    }

    // cada regla implementa qué verificar en cada nodo visitado
    protected abstract void verificarNodo(Alumno alumno, int indice, double valor);

    // inicializa la lista de marcas
    protected ListaDoubleLinkedL iniciarMarcas() {
        ListaDoubleLinkedL listaMarca = new ListaDoubleLinkedL();
        for (int v = 0; v < grafoMaterias.getGrafo().getOrden(); v++) {
            listaMarca.insertar(false, v);
        }
        return listaMarca;
    }
}
