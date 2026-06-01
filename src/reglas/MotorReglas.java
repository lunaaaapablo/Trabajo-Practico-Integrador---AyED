package reglas;

import grafo.GrafoMaterias;
import modelo.Alumno;
//clase principal de las reglas, que se encarga de analizar la solicitud de un alumno para cursar una materia,
//  aplicando las reglas en orden y 
// devolviendo un dictamen final

public class MotorReglas {
    private GrafoMaterias grafoMaterias;

    public MotorReglas(GrafoMaterias grafoMaterias) {// Inyectamos el grafo de materias al motor de reglas
        this.grafoMaterias = grafoMaterias;
    }

    public Dictamen analizar(Alumno alumno, int idMateria) {
        int indiceMateria = grafoMaterias.buscarIndicePorId(idMateria);
        if (indiceMateria == -1) {
            return new Dictamen(false, "Materia no encontrada en el plan de estudios");// Si la materia no existe, no se puede aprobar
        }

        // Regla 1: todas las correlativas regulares cumplidas
        ReglaCorrelativasR regla1 = new ReglaCorrelativasR(grafoMaterias);
        if (!regla1.cumple(alumno, indiceMateria)) {
            return new Dictamen(false, regla1.getMensaje());// Si no cumple la regla 1, no es necesario evaluar las siguientes
        }

        // Regla 2: solo puede adeudar una correlativa de aprobacion
        ReglaAprobacion regla2 = new ReglaAprobacion(grafoMaterias);
        if (!regla2.cumple(alumno, indiceMateria)) {
            return new Dictamen(false, regla2.getMensaje());
        }

        // Regla 3: materias criticas de primer anio
        ReglaPrimerAnio regla3 = new ReglaPrimerAnio(grafoMaterias);
        if (!regla3.cumple(alumno, indiceMateria)) {
            return new Dictamen(false, regla3.getMensaje());
        }

        return new Dictamen(true, "Condicional APROBADO");
    }
}
