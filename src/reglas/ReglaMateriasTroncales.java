package reglas;

import grafo.GrafoMaterias;
import grafo.contenedores.ListaDoubleLinkedL;
import modelo.Alumno;
import modelo.EstadoAcademico;

public class ReglaMateriasTroncales extends ReglaBase {

    private boolean cumple;

    public ReglaMateriasTroncales(GrafoMaterias grafoMaterias) {
        super(grafoMaterias);
    }

    @Override
    public boolean cumple(Alumno alumno, int indiceMateria) {
        if (grafoMaterias.getMateria(indiceMateria).getAnio() == 1) {
            mensaje = "Materia de 1er año, regla no aplica";
            return true;
        }

        cumple = true;
        ListaDoubleLinkedL listaMarca = iniciarMarcas();
        bpf(alumno, indiceMateria, listaMarca);
        return cumple;
    }

    @Override
    protected void verificarNodo(Alumno alumno, int indice, double valor) {
        int materiasDependientes = 0;
        int orden = grafoMaterias.getGrafo().getOrden();
        for (int j = 0; j < orden; j++) {
            double v = (double) grafoMaterias.getGrafo().getMatrizCosto().devolver(indice, j);
            if (v != grafoMaterias.getGrafo().getInfinito()) {
                materiasDependientes++;
            }
        }

        if (materiasDependientes >= 2) {
            EstadoAcademico estado = alumno.getEstado(indice);
            if (estado != EstadoAcademico.REGULAR && estado != EstadoAcademico.APROBADA) {
                mensaje = "Materia troncal no regularizada: "
                        + grafoMaterias.getMateria(indice).getNombre()
                        + " (de ella dependen " + materiasDependientes + " materias)";
                cumple = false;
            }
        }
    }
}
