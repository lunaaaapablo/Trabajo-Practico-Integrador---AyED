package modelo;

import grafo.GrafoMaterias;

public class SimuladorAcademico {

    private GrafoMaterias grafoMaterias;
    private EstadoAcademico[] estados;

    public SimuladorAcademico(GrafoMaterias grafoMaterias) {
        this.grafoMaterias = grafoMaterias;
        int cant = grafoMaterias.getCapacidad();
        this.estados = new EstadoAcademico[cant];
        for (int i = 0; i < cant; i++) {
            this.estados[i] = EstadoAcademico.NO_CURSADA;
        }
    }

    public void cargarDesde(Alumno alumno) {
        for (int i = 0; i < estados.length; i++) {
            estados[i] = alumno.getEstado(i);
        }
    }

    public EstadoAcademico[] getEstados() {
        return estados;
    }

    public EstadoAcademico getEstado(int indice) {
        return estados[indice];
    }

    public void setEstado(int indice, EstadoAcademico estado) {
        estados[indice] = estado;
    }

    public void ciclarEstado(int indice) {
        EstadoAcademico actual = estados[indice];
        switch (actual) {
            case NO_CURSADA -> estados[indice] = EstadoAcademico.CURSANDO;
            case CURSANDO -> estados[indice] = EstadoAcademico.REGULAR;
            case REGULAR -> estados[indice] = EstadoAcademico.APROBADA;
            case APROBADA -> estados[indice] = EstadoAcademico.LIBRE;
            case LIBRE -> estados[indice] = EstadoAcademico.DESAPROBADA;
            case DESAPROBADA -> estados[indice] = EstadoAcademico.NO_CURSADA;
        }
    }

    public boolean puedeCursarRegular(int indiceMateria) {
        int orden = grafoMaterias.getGrafo().getOrden();
        boolean[] visitado = new boolean[orden];
        return bpf(indiceMateria, visitado);
    }

    private boolean bpf(int v, boolean[] visitado) {
        visitado[v] = true;
        int orden = grafoMaterias.getGrafo().getOrden();
        double inf = grafoMaterias.getGrafo().getInfinito();

        for (int w = 0; w < orden; w++) {
            double valor = (double) grafoMaterias.getGrafo().getMatrizCosto().devolver(w, v);
            if (valor != inf && !visitado[w]) {
                if (valor == 1.0) {
                    if (estados[w] != EstadoAcademico.REGULAR && estados[w] != EstadoAcademico.APROBADA) {
                        return false;
                    }
                } else if (valor == 2.0) {
                    if (estados[w] != EstadoAcademico.APROBADA) {
                        return false;
                    }
                }
                if (!bpf(w, visitado)) {
                    return false;
                }
            }
        }
        return true;
    }
}
