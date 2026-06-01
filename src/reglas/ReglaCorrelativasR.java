package reglas;

import grafo.GrafoMaterias;
import grafo.contenedores.ListaDoubleLinkedL;
import modelo.Alumno;
import modelo.EstadoAcademico;

public class ReglaCorrelativasR extends ReglaBase {

    private boolean cumple;

    public ReglaCorrelativasR(GrafoMaterias grafoMaterias) {
        super(grafoMaterias);
    }

    @Override
    public boolean cumple(Alumno alumno, int indiceMateria) {
        // excepcion: si la materia es de 1er año no verificamos correlativas
    if (grafoMaterias.getMateria(indiceMateria).getAnio() == 1) {
        mensaje = "Materia de 1er año, regla no aplica";
        return true;
    }
        cumple = true;// asume que cumple hasta encontrar una correlativa regular adeudada
        ListaDoubleLinkedL listaMarca = iniciarMarcas();// marca todos los nodos como no visitados
        bpf(alumno, indiceMateria, listaMarca);// recorre las correlativas y verifica que no adeuda ninguna regular
        return cumple;
    }

    @Override
    protected void verificarNodo(Alumno alumno, int indice, double valor) {
        if (valor == 1.0) { // solo correlativas Regulares
            EstadoAcademico estado = alumno.getEstado(indice);
            if (estado != EstadoAcademico.REGULAR &&
                estado != EstadoAcademico.APROBADA) {
                mensaje = "No cumple correlativa regular: "
                        + grafoMaterias.getMateria(indice).getNombre();
                cumple = false;
            }
        }
    }
}


