package Testeos;

import archivos.Lectura;
import grafo.GrafoMaterias;
import modelo.Alumno;
import grafo.contenedores.ListaDoubleLinkedL;
import reglas.Dictamen;
import reglas.MotorReglas;

public class Test {

    public static void main(String[] args) {

        // ─── 1. CARGAR PLAN DE ESTUDIOS ───────────────────────────────
        Lectura lectura = new Lectura();
        GrafoMaterias grafo = lectura.cargarMaterias("Archivos_txt/MateriasdeTUP.txt");
        lectura.cargarCorrelativas("Archivos_txt/CorrelativasTUP.txt", grafo);

        System.out.println("=== PLAN DE ESTUDIOS ===");
        grafo.mostrarPlanEstudios();
        System.out.println();

        // ─── 2. CARGAR ALUMNOS ────────────────────────────────────────
      // ─── 2. CARGAR ALUMNOS ────────────────────────────────────────
ListaDoubleLinkedL alumnos = lectura.cargarAlumnos("Archivos_txt/AlumnosTUP.txt", grafo);

Alumno juan   = (Alumno) alumnos.devolver(0);
Alumno maria  = (Alumno) alumnos.devolver(1);
Alumno carlos = (Alumno) alumnos.devolver(2);

lectura.cargarHistorial("Archivos_txt/HistorialJuan.txt", juan, grafo);
lectura.cargarHistorial("Archivos_txt/HistorialMaria.txt", maria, grafo);
lectura.cargarHistorial("Archivos_txt/HistorialCarlos.txt", carlos, grafo);

        // ─── 3. ANALIZAR SOLICITUDES ──────────────────────────────────
        MotorReglas motor = new MotorReglas(grafo);
        System.out.println("=== DICTAMENES ===");

        // Caso 1: Juan quiere cursar AYED (7) → APROBADO
        Dictamen d1 = motor.analizar(juan, 7);
        System.out.println("Juan - AYED (7): " + d1);

        // Caso 2: Maria quiere cursar AYED (7) → RECHAZADO
        Dictamen d2 = motor.analizar(maria, 7);
        System.out.println("Maria - AYED (7): " + d2);

        // Caso 3: Carlos quiere cursar Programacion (5) → RECHAZADO
        Dictamen d3 = motor.analizar(carlos, 5);
        System.out.println("Carlos - Programacion (5): " + d3);

        // Caso 4: Juan quiere cursar Paradigmas (10) → APROBADO
        Dictamen d4 = motor.analizar(juan, 10);
        System.out.println("Juan - Paradigmas (10): " + d4);

        // Caso 5: Carlos quiere cursar Analisis Matematico I (3) 
        // es de 1er año → excepcion, no aplica regla de primer año → APROBADO
        Dictamen d5 = motor.analizar(carlos, 3);
        System.out.println("Carlos - Analisis Matematico I (3): " + d5);

        // Caso 6: Maria quiere cursar Bases de Datos (14) → RECHAZADO
        // no tiene primer año aprobado
        Dictamen d6 = motor.analizar(maria, 14);
        System.out.println("Maria - Bases de Datos (14): " + d6);

        System.out.println();

        // ─── 4. ESTADO DE JUAN ────────────────────────────────────────
        System.out.println("=== ESTADO DE JUAN ===");
        for (int i = 0; i < grafo.getCapacidad(); i++) {
            System.out.println(grafo.getMateria(i).getNombre()
                    + " -> " + juan.getEstado(i));
        }
    }
}