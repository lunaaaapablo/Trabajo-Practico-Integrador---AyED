package Testeos;
import grafo.GrafoMaterias;
import archivos.Lectura;


public class TesteosG {

    public static void main(String[] args) {
        Lectura lec = new Lectura();
        GrafoMaterias t1 = lec.cargarMaterias("src/Archivos txt/MateriasdeTUP.txt");
        for(int i = 0; i < t1.getCapacidad(); i++){
            System.out.println(t1.getMateria(i).getId() + " " + t1.getMateria(i).getNombre() + " " + t1.getMateria(i).getAnio());
        }
        
        lec.cargarCorrelativas("src/Archivos txt/CorrelativasTUP.txt", t1);
        t1.getGrafo().muestraGrafo();
    }
}
