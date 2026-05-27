package archivos;

import grafo.GrafoMaterias;
import modelo.Alumno;
import modelo.EstadoAcademico;
import modelo.Materia;
import grafo.contenedores.ListaDoubleLinkedL;
import java.io.BufferedReader;
import java.io.FileReader;

public class Lectura {

    // Lee materias.txt "1;EP;1"
    public GrafoMaterias cargarMaterias(String ruta) {//ruta del archivo materias.txt
        GrafoMaterias grafoMaterias = null;// se inicializa después de contar líneas para saber tamaño

        try {
            // Pasada 1: contar líneas para saber el tamaño de la matriz
            BufferedReader reader = new BufferedReader(new FileReader(ruta));//
            int cant = 0;// se cuenta la cantidad de líneas para dimensionar el grafo
            while (reader.readLine() != null) cant++;//mientras haya líneas, se cuenta
            reader.close();

            grafoMaterias = new GrafoMaterias(cant);// se crea el grafo con la cantidad de materias

            // Pasada 2: cargar materias
            reader = new BufferedReader(new FileReader(ruta));
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] p    = linea.split(";");
                int id        = Integer.parseInt(p[0].trim());
                String nombre = p[1].trim();
                int anio      = Integer.parseInt(p[2].trim());
                grafoMaterias.agregarMateria(new Materia(id, nombre, anio));
            }
            reader.close();

        } catch (Exception e) {
            System.err.println("Error al leer materias: " + e.getMessage());
        }

        return grafoMaterias;
    }

    // Lee correlativas.txt → "7;R;3" (idMateria;tipo;idCorrelativa)
    public void cargarCorrelativas(String ruta, GrafoMaterias grafoMaterias) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;

                String[] p        = linea.split(";");
                int idMateria      = Integer.parseInt(p[0].trim()); // la que necesita correlativa
                String tipo        = p[1].trim();                   // R o A
                int idCorrelativa  = Integer.parseInt(p[2].trim()); // la que debe tener cursada

                int valor = tipo.equals("R") ? 1 : 2;// R = requisito (debe estar aprobada), 
                // A = antirequisito (no debe estar aprobada)
                // arista va de idCorrelativa → idMateria
                grafoMaterias.agregarCorrelativa(idCorrelativa, idMateria, valor);// se agrega la correlativa al grafo
            }
        } catch (Exception e) {// se maneja cualquier error de lectura o formato
            System.err.println("Error al leer correlativas: " + e.getMessage());
        }
    }

    // Lee alumnos.txt → "10254;Juan Perez"
    public ListaDoubleLinkedL cargarAlumnos(String ruta, GrafoMaterias grafoMaterias) {// se devuelve una lista de alumnos, cada uno con un array de estados del tamaño del grafo
        ListaDoubleLinkedL alumnos = new ListaDoubleLinkedL();// se crea la lista de alumnos
        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {
            String linea = "";//linea para almacenar cada línea leída
            while ((linea = reader.readLine()) != null) {//mientras haya líneas, se procesa cada una
                linea = linea.trim();// se elimina espacios al inicio y al final
                if (linea.isEmpty()) continue;// si la línea está vacía, se salta

                String[] p = linea.split(";");// se divide la línea en partes usando ';' como separador
                int legajo = Integer.parseInt(p[0].trim());// se convierte el legajo a entero
                String nombre = p[1].trim();
                alumnos.insertar(// se crea un nuevo alumno con el legajo, nombre y un array de estados del tamaño del grafo
                    new Alumno(legajo, nombre, grafoMaterias.getCapacidad()),// el array de estados se inicializa con el tamaño del grafo para cada alumno
                    alumnos.tamanio()// se inserta al final de la lista de alumnos
                );
            }
        } catch (Exception e) {
            System.err.println("Error al leer alumnos: " + e.getMessage());
        }
        return alumnos;
    }

    // Lee historial_10254.txt →"1;3" (idMateria;codigoEstado)
    public void cargarHistorial(String ruta, Alumno alumno, GrafoMaterias grafoMaterias) {
        try (BufferedReader reader = new BufferedReader(new FileReader(ruta))) {// se lee el historial del alumno específico
            String linea = "";// línea para almacenar cada línea leída
            while ((linea = reader.readLine()) != null) {
                linea = linea.trim();
                if (linea.isEmpty()) continue;// si la línea está vacía, se salta

                String[] p = linea.split(";");
                int idMateria = Integer.parseInt(p[0].trim());
                int codigoEstado = Integer.parseInt(p[1].trim());

                int indice = grafoMaterias.buscarIndicePorId(idMateria);// se busca el índice de la materia en el grafo para actualizar el estado del alumno
                if (indice != -1) {
                    alumno.setEstado(indice, codigoAEstado(codigoEstado));
                }
            }
        } catch (Exception e) {
            System.err.println("Error al leer historial: " + e.getMessage());
        }
    }

    // Convierte el número del archivo al enum correspondiente
    private EstadoAcademico codigoAEstado(int codigo) {
        for (EstadoAcademico e : EstadoAcademico.values()) {// se recorre el enum para encontrar el que tiene el código correspondiente
            if (e.getCodigo() == codigo) return e;
        }
        return EstadoAcademico.NO_CURSADA;// si no se encuentra el código, se asume que no cursada
    }
}