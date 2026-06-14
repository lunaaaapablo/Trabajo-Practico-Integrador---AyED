package reglas;

import grafo.GrafoMaterias;
import modelo.Alumno;
import modelo.EstadoAcademico;
import modelo.Materia;

public class ReglaMateriasMinimas extends ReglaBase {

    public ReglaMateriasMinimas(GrafoMaterias grafoMaterias) {
        super(grafoMaterias);
    }

    @Override
    public boolean cumple(Alumno alumno, int indiceMateria) {
        Materia materiaDestino = grafoMaterias.getMateria(indiceMateria);
        int anioDestino = materiaDestino.getAnio();

        if (anioDestino == 1) {
            mensaje = "Materia de 1er año, regla no aplica";
            return true;
        }

        int aprobadas = 0;

        for (int i = 0; i < grafoMaterias.getCapacidad(); i++) {
            Materia m = grafoMaterias.getMateria(i);
            if (m.getAnio() < anioDestino) {
                if (alumno.getEstado(i) == EstadoAcademico.APROBADA) {
                    aprobadas++;
                }
            }
        }

        int minimo = (anioDestino - 1) * 2;

        if (aprobadas >= minimo) {
            mensaje = "Cumple con minimo de " + minimo + " materias aprobadas de años anteriores (tiene " + aprobadas + ")";
            return true;
        } else {
            mensaje = "No cumple: solo tiene " + aprobadas + " materia(s) aprobada(s) de años anteriores, minimo " + minimo;
            return false;
        }
    }

    @Override
    protected void verificarNodo(Alumno alumno, int indice, double valor) {
        // no se usa para esta regla
    }
}
