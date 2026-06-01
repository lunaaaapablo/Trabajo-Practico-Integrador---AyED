package reglas;

import grafo.GrafoMaterias;
import grafo.contenedores.ListaDoubleLinkedL;
import modelo.Alumno;
import modelo.EstadoAcademico;

public class ReglaAprobacion extends ReglaBase {

    private int adeudaAprobacion;// contador de correlativas de aprobación adeudadas

    public ReglaAprobacion(GrafoMaterias grafoMaterias) {
        super(grafoMaterias);// inicializa el grafo y el mensaje
    }

    @Override
    public boolean cumple(Alumno alumno, int indiceMateria) {
        adeudaAprobacion = 0;
        ListaDoubleLinkedL listaMarca = iniciarMarcas();// marca todos los nodos como no visitados
        bpf(alumno, indiceMateria, listaMarca);// recorre las correlativas 
        // y cuenta las de aprobación adeudadas

        if (adeudaAprobacion > 1) {
            mensaje = "Adeuda " + adeudaAprobacion + " correlativas de aprobación, solo se permite una";
            return false;
        }
        mensaje = "Cumple regla de aprobación";
        return true;
    }

    @Override
    protected void verificarNodo(Alumno alumno, int indice, double valor) {
        if (valor == 2.0) { // solo correlativas de Aprobacion
            EstadoAcademico estado = alumno.getEstado(indice);
            if (estado != EstadoAcademico.APROBADA) {// si no está aprobada, cuenta como adeuda
                adeudaAprobacion++;
            }
        }
    }
}