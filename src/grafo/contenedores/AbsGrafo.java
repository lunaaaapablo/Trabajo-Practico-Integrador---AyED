package grafo.contenedores;

import grafo.recursos.OperacionesGrafo;

public abstract class AbsGrafo implements OperacionesGrafo {
    private MatrizGrafo matrizCosto;
	protected int ordenGrafo;
	private static double infinito=10000;
   

    public AbsGrafo(int ordenGrafo) {
		this.ordenGrafo = ordenGrafo;
		this.setMatrizCosto(new MatrizGrafo(getOrden()));
		for(int i=0;i<getOrden();i++){
			for(int j=0;j<getOrden();j++){

	            getMatrizCosto().actualizar(getInfinito(),i,j);
	            }
			}
		}
	
	public static double getInfinito() {
        return infinito;
        
    }

    public static void setInfinito(double infinito) {
        AbsGrafo.infinito = infinito;
        
    }

    public int getOrden(){
		return this.ordenGrafo;
	}
	
	public abstract void cargarGrafo();
	public abstract void muestraGrafo();
	
	private void bpf(ListaDoubleLinkedL listaMarca, int v){// busqueda primero en profundadad
		boolean marcado;
		double currCost;
		
		listaMarca.reemplazar(Boolean.valueOf(true), v);//marco el vertice v como visitado
		System.out.println("vertice "+ v);
		for (int w=0;w<getOrden();w++){
			marcado=(boolean)listaMarca.devolver(w);
			currCost=(double)this.getMatrizCosto().devolver(v,w);
			if (currCost!=getInfinito() && !marcado){//si hay conexion entre el vertice v y el vertice w, y el vertice w no ha sido visitado,
			                                    // entonces se llama a la funcion bpf para visitar el vertice w y sus adyacentes
				bpf(listaMarca,w);
			}
		}
	}
	
	public void muestraBPF(){
		ListaDoubleLinkedL listaMarca;//lista de booleanos para marcar los vertices visitados
	    boolean marcado;//variable para almacenar el valor de la marca de un vertice
		
		listaMarca = new ListaDoubleLinkedL();
		for (int v=0;v<getOrden();v++){
			listaMarca.insertar(false, v);//inicializo la lista de marcas con false, es decir, que ningun vertice ha sido visitado
		}
		
		for (int v=0;v<getOrden();v++){
			marcado=(boolean)listaMarca.devolver(v);//si el vertice v no ha sido visitado, entonces se llama a la funcion bpf para visitar el vertice v y sus adyacentes
			if (!marcado){
				bpf(listaMarca,v);//llamada recursiva para visitar el vertice v y sus adyacentes
			}
		}		
	}
	
	private void bea(ListaDoubleLinkedL listaMarca, int v){//busqueda de amplitud 
		boolean marcado;
		double currCost;
		ColaSLinkedList cola;
		int w;
		
		listaMarca.reemplazar(true, v);
		System.out.println("vertice "+ v);
		cola = new ColaSLinkedList();
		cola.meter(v);
		
		while (!cola.estaVacia()){
			w=(int)cola.sacar();
			for (int z=0;z<getOrden();z++){
				marcado=(boolean)listaMarca.devolver(z);
				currCost=(double)this.getMatrizCosto().devolver(w,z);
				if (currCost!=getInfinito() && !marcado){
					listaMarca.reemplazar(true, z);
					cola.meter(z);
					System.out.println("arista visitada " + w + " - " + z);
				}		
			}
		}
	}
	

	public void muestraBEA(){
		ListaDoubleLinkedL listaMarca;
		boolean marcado;
		
		listaMarca = new ListaDoubleLinkedL();
		for (int v=0;v<getOrden();v++){
			listaMarca.insertar(false, v);
		}
		
		for (int v=0;v<getOrden();v++){
			marcado=(boolean)listaMarca.devolver(v);
			if (!marcado){
				bea(listaMarca,v);
			}
		}		
	}

    public MatrizGrafo getMatrizCosto() {
		return matrizCosto;
	}

	public void setMatrizCosto(MatrizGrafo matrizCosto) {
		this.matrizCosto = matrizCosto;
	}
}
