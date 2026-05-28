package grafo.dirigido;

import java.util.Scanner;
import grafo.contenedores.*;
import grafo.recursos.*;

public class GrafoDirigido extends AbsGrafoD{
	public GrafoDirigido(int ordenGrafo){
		super(ordenGrafo);
	}
	
	@Override
	public void cargarGrafo(){
		double currCost;		
		Scanner scanner = new Scanner(System.in);
		
		for (int i=0; i<getOrden();i++){
			for (int j=0;j<getOrden();j++){
				if (i!=j){
					System.out.println("Ingrese costo[" + i + "," + j + "] (sino -1)");
					currCost=scanner.nextDouble();
					if (currCost!=-1){
						this.getMatrizCosto().actualizar(currCost, i, j);	
					}else{
						this.getMatrizCosto().actualizar(getInfinito(), i, j);
					}					
				}else{
					this.getMatrizCosto().actualizar(getInfinito(), i, j);
				}
			}
		} 
		scanner.close();
   	
	}
	
	public int getGradoSalida(int v){//contar fila v
		int grado = 0;

		for(int j = 0;j < getOrden();j++){

	        if((double)
	            getMatrizCosto().devolver(v,j)!= getInfinito()){
	        	grado++;
	        }
	    }

	    return grado;
	}
	
	public int getGradoEntrada(int v){//contar columna v
	    int grado = 0;

	    for(int i = 0;i < getOrden();i++){
	        if((double)getMatrizCosto().devolver(i,v) != getInfinito()){
	        	grado++;
	        }
	    }

	    return grado;
	}
	
	public ConnectionSet getArcosSalientes(int v){//obtener arcos salientes
	    ConnectionSet arcos = new ConnectionSet();//conjunto de conexiones 

	    for(int j = 0; j < getOrden(); j++){//recorro filas

	        if((double)getMatrizCosto().devolver(v,j)!= getInfinito()){//si hay conexion 
	            
	        	Connection c = new Connection(v,j,(double)getMatrizCosto().devolver(v,j));
	            arcos.meter(c);
	        }
	    }
	    return arcos;
	}
	
	public ConnectionSet getArcos(){//conjunto de todos los arcos
		ConnectionSet arcos = new ConnectionSet();
	    for(int i = 0;i < getOrden();i++){
	    	for(int j = 0;j < getOrden();j++){

	            if((double)
	                getMatrizCosto().devolver(i,j)
	                != getInfinito()){

	                Connection c = new Connection(i,j, (double)
	                        getMatrizCosto().devolver(i,j));

	                arcos.meter(c);
	            }
	        }
	    }

	    return arcos;
	}
	
	public boolean tieneCiclosD(){
		ListaDoubleLinkedL visitado = new ListaDoubleLinkedL();
		ListaDoubleLinkedL recursion = new ListaDoubleLinkedL();
		
		for(int i = 0; i < getOrden(); i++){
			visitado.insertar(false, i);
			recursion.insertar(false, i);
		}
		
		//recorrer vertices
		for(int i = 0; i < getOrden(); i++) {
			boolean marcado = (boolean)visitado.devolver(i);
			
			if(!marcado) {
				if(bpfCicloD(i, visitado, recursion)) {
					return true;
				}
			}
		}
		return false;
}
	
	private boolean bpfCicloD(int v,ListaDoubleLinkedL visitado, ListaDoubleLinkedL recursion) {
		visitado.reemplazar(true, v);
		recursion.reemplazar(true,v);
		//recorrer vecinos
		for(int j = 0; j < getOrden();j++) {
			if((double)getMatrizCosto().devolver(v, j) != getInfinito()) {
				//si hay arco --> obtener marcas
				boolean marcado = (boolean)visitado.devolver(j);
				boolean enRecursion = (boolean)recursion.devolver(j);
			    
				if(!marcado) {
					if(bpfCicloD(j,visitado,recursion)) {
						return true;
					}
			}else if(enRecursion) {
				return true;
			}
		}
			
		}
		
		recursion.reemplazar(false, v);
		return false;
		
	}
}
