package reglas;

import grafo.GrafoMaterias;
import grafo.contenedores.ListaDoubleLinkedL;
import modelo.Alumno;
import modelo.EstadoAcademico;

public class ReglaPrimerAnio extends ReglaBase {

    private boolean cumple;

    public ReglaPrimerAnio(GrafoMaterias grafoMaterias) {
        super(grafoMaterias);
    }

    @Override
    public boolean cumple(Alumno alumno, int indiceMateria) {

        // excepcion: si la materia que quiere cursar es de 1er año no aplica
        if (grafoMaterias.getMateria(indiceMateria).getAnio() == 1) {
            mensaje = "Materia de 1er año, regla no aplica";
            return true;
        }

        cumple = true;
        ListaDoubleLinkedL listaMarca = iniciarMarcas();
        bpf(alumno, indiceMateria, listaMarca);

        if (cumple) {
            mensaje = "Todas las correlativas de primer año aprobadas";
        }
        return cumple;
    }

    @Override
    protected void verificarNodo(Alumno alumno, int indice, double valor) {// solo verifica correlativas de primer año
        if (valor != grafoMaterias.getGrafo().getInfinito()) {//si hay correlativa
            int anio = grafoMaterias.getMateria(indice).getAnio();// si es de primer año, verifica que esté aprobada
            if (anio == 1) {// si es de primer año, verifica que esté aprobada
                EstadoAcademico estado = alumno.getEstado(indice);
                if (estado != EstadoAcademico.APROBADA) {// si no está aprobada, no cumple la regla
                    mensaje = "No cumple regla de primer año, adeuda: "
                            + grafoMaterias.getMateria(indice).getNombre();
                    cumple = false;
                }
            }
        }
    }
}