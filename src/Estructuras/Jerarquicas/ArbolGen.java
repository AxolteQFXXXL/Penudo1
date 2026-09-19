package src.Estructuras.Jerarquicas;


import src.Concurrente.dinamicas.Cola;
import src.Concurrente.dinamicas.Lista;

public class ArbolGen {
    private NodoGen raiz;

    public ArbolGen(){
        this.raiz=null;
    }

    public boolean insertar(Object nuevoElem, Object elemPadre){
        boolean listo = false;

        if(this.raiz !=null){
            NodoGen nPadre = obtenerNodo(this.raiz, elemPadre);

            if(nPadre != null){
                NodoGen aux = new NodoGen(nuevoElem, null, null);

                if(nPadre.getHijoIzquierdo()==null) {
                    nPadre.setHijoIzquierdo(aux);
                    listo=true;
                }else{
                    NodoGen unHijo = nPadre.getHijoIzquierdo();
                    while(unHijo.getHermanoDerecho()!=null){
                        unHijo = unHijo.getHermanoDerecho();
                    }
                    unHijo.setHermanoDerecho(aux);
                    listo = true;
                }
                
            }
        }else this.raiz=new NodoGen(nuevoElem, null, null); listo=true;

        
        return listo;
    }

    public boolean insertarPorPosicion(Object nuevoElem, int posPadre, boolean posiHijo){
        boolean listo = false;
        int[]i={0};
        NodoGen elPadre;

        if(!this.esVacio()){
                elPadre=buscarPosPadre(this.raiz, i, posPadre);
            if(elPadre!=null) {
                if (elPadre.getHijoIzquierdo() == null && !posiHijo) {
                    elPadre.setHijoIzquierdo(new NodoGen(nuevoElem, null, null));
                    listo = true;
                }
                if (elPadre.getHermanoDerecho() == null && posiHijo) {
                    elPadre.setHermanoDerecho(new NodoGen(nuevoElem, null, null));
                    listo = true;
                }
            }
        }

        return listo;
    }

    NodoGen buscarPosPadre(NodoGen n, int[]i, int posPadre){
        NodoGen n1=null;
        System.out.println(i[0]);
        if(n!=null){
            if(i[0]!=posPadre){
                i[0]+=1;
                    n1 = buscarPosPadre(n.getHijoIzquierdo(), i,posPadre);

                    if(n1==null)  buscarPosPadre(n.getHermanoDerecho(), i, posPadre);
            }else n1 = n;
        }

        return n1;
    }

    public int altura(){ //reparar, no sirve
        Cola laCola = new Cola();
        int alto=-1;
        int alto1 = 0, alto2 =0;
        laCola.poner(this.raiz);

        while(!laCola.esVacia()){
            NodoGen n1 = (NodoGen) laCola.obtenerFrente();
            laCola.sacar();
            if(n1.getHijoIzquierdo() != null){
                laCola.poner(n1.getHijoIzquierdo());
                alto1++;
            }
            if(n1.getHermanoDerecho()!=null) {
                laCola.poner(n1.getHermanoDerecho());
                alto2++;
            }
        }

        alto= (alto1+alto2)/2;

        if((alto1+alto2)%2==0) alto+=1;
        else alto-=1;

       return alto;
    }

    public int nivel(Object elemento){
        int elnivel=0;

        if(this.raiz!=null){
            elnivel = obtenerNodoAux(this.raiz, elemento) -1;
        }

        return elnivel;
    }

    int obtenerNodoAux(NodoGen n1, Object elem){
        int unNivel=0;

        if(n1!=null){
            if(n1.getElem().equals(elem))unNivel=1;
            else{
                unNivel=obtenerNodoAux(n1.getHijoIzquierdo(), elem);
                if(unNivel>0) unNivel+=1;
                if(unNivel==0){
                    unNivel=obtenerNodoAux(n1.getHermanoDerecho(),elem);
                    if(unNivel>0) unNivel+=1;
                }
            }
        }

        return unNivel;
    }

    public boolean esVacio(){return this.raiz==null;}

    public void vaciar(){ this.raiz=null;}

    public Lista listarPreorden(){
        Lista l1 = new Lista();
        listarPreordenAux(this.raiz, l1);
        return l1;
    }

    void listarPreordenAux(NodoGen n1, Lista lis){

        if(n1!=null){
            lis.insertar(n1.getElem(), lis.longitud()+1);

            listarPreordenAux(n1.getHijoIzquierdo(), lis);
            listarPreordenAux(n1.getHermanoDerecho(), lis);
        }
    }

    public Lista listarInorden(){
        Lista l1 = new Lista();
        listarInordenaux(this.raiz, l1);
        return l1;
    }

    void listarInordenaux(NodoGen n1, Lista lis){

        if(n1!=null) {
            if (n1.getHijoIzquierdo() != null) listarInordenaux(n1.getHijoIzquierdo(), lis);
            else if (n1.getHermanoDerecho()!=null){
                listarInordenaux(n1.getHermanoDerecho(), lis);
            }
                lis.insertar(n1.getElem(), lis.longitud() + 1);
            if(n1.getHermanoDerecho()!=null) listarInordenaux(n1.getHermanoDerecho(), lis);
        }

    }

    public Lista listarPosorden(){
        Lista l1 = new Lista();
        listarPosordenAux(this.raiz, l1);
        return l1;
    }

    void listarPosordenAux(NodoGen n1, Lista lis){

        if(n1!=null){

            listarPreordenAux(n1.getHijoIzquierdo(), lis);
            listarPreordenAux(n1.getHermanoDerecho(), lis);
            lis.insertar(n1.getElem(), lis.longitud()+1);
        }
    }

    public Lista frontera(){
        Lista lista = new Lista();

        fronteraAux(this.raiz, lista);

        return lista;
    }

    void fronteraAux(NodoGen nodo, Lista lista){

        if (nodo != null) {
            fronteraAux(nodo.getHijoIzquierdo(), lista);
            fronteraAux(nodo.getHermanoDerecho(), lista);

            if(nodo.getHijoIzquierdo()==null && nodo.getHermanoDerecho()==null) lista.insertar(nodo.getElem(), lista.longitud()+1);

        }
    }

    //esta en PreOrden
    NodoGen obtenerNodo(NodoGen n, Object buscado){
        NodoGen envio = null;

        if( n!=null){
            if(n.getElem().equals(buscado))envio=n;
            else{
                if(n.getHijoIzquierdo()!=null) envio = obtenerNodo(n.getHijoIzquierdo(), buscado);
                else if(envio ==null && n.getHermanoDerecho()!=null) envio=obtenerNodo(envio.getHermanoDerecho(), buscado);
                
            }
        }

        return envio;
    }

    public String toString(){
        return toStringAux(this.raiz);
    }

    String toStringAux(NodoGen n){
        String s="";

        if(n!=null){
            s+=n.getElem().toString() + " -> ";
            NodoGen hijo = n.getHijoIzquierdo();
            while(hijo!=null){
                s+=hijo.getElem().toString()+", ";
                hijo=hijo.getHermanoDerecho();
            }

            hijo=n.getHijoIzquierdo();
            while(hijo!=null){
                s+="\n"+ toStringAux(hijo);
            }
        }

        return s;
    }
}
