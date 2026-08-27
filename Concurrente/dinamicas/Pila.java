package src2.Concurrente.dinamicas;

/**Autores**
Ansberck Martin, legajo FAI-4861
Lozano Axel, legajo FAI-5199
Miriuka Matias, legajo FAI-5420
Roca Jalil Yazmin, legajo FAI-5262
*/
public class Pila {
private Nodo tope;

    //construye la pila con valor null
    public Pila(){
        //contructor
        this.tope=null;
    }
    //de tipo

   //Te apila un elemento enviado por parametro
    public boolean apilar(Object elemento){
        Nodo nuevo= new Nodo(elemento, this.tope);
        this.tope=nuevo;
        return true;
    }

    //desapila un solo elemento
    public boolean desapilar(){
        Nodo n1 = this.tope;
        boolean listo=false;

        if(n1!=null){   //le asigna al tope de la pila su anterior elemento, si es falso entonces devuelve false
            this.tope=n1.getEnlace();
            listo=true;
        }

        return listo;
    }

    //convierte el tope como null y los demas enlazados dejan de estarlo
    public void vaciar(){
        this.tope.setEnlace(null);
        this.tope=null;
    }

    //comienzo clonar ______
    public Pila clone(){
        //te clona una pila que ya tengas hecha.
        Pila otraPila = new Pila();

        if(!this.esVacia()) { //si la pila contiene elementos llama el clonante
            otraPila.tope= clonante(this.tope);
        }

        return otraPila;
    }

    //te clona de forma recursiva una pila, llegando hasta el primer elemento hasta el tope
    private Nodo clonante(Nodo n1){
        Nodo aux;

        if(n1.getEnlace()==null){
            aux = new Nodo(n1.getElem(), null);
        }else{
            aux = new Nodo(n1.getElem(), clonante(n1.getEnlace()));
        }

        return aux;

    
    }  //final clonar_______

    public boolean esVacia(){
        //verifica si esta vacio, asume que esta vacio al principio
        return this.tope==null;
    }

    public Object obtenerTope(){
        //retorna el elemento asignado al tope de la Pila, se asume que es vacia
        Object elem=null;

        if(this.tope!=null){ //si no es vacia
            elem=this.tope.getElem();
        }

        return elem;
    }

     public String toString(){
        //transforma los elementos de la Pila en una lista
        String lista;

        if(this.tope==null){
            lista="[]";
        }else {
            lista = "[";
            Nodo n1 = this.tope;
            while (n1 != null) { //recorre cada nodo y los añade a lista
                lista += n1.getElem();
                if(n1.getEnlace()!=null){
                    lista+=",";
                }
                n1 = n1.getEnlace();
            }
        lista+="]";
        }

        return lista;
    }
}