package src.Estructuras.Jerarquicas;

public class NodoGen {
    private Object elem;
    private NodoGen theLeft;
    private NodoGen theRight;

    NodoGen(Object elem, NodoGen izquierda, NodoGen derecha){
        this.elem=elem;
        this.theLeft=izquierda;
        this.theRight=derecha;
    }

    //getters

    public Object getElem(){
        return  this.elem;
    }

    public NodoGen getHijoIzquierdo(){
        return this.theLeft;
    }

    public NodoGen getHermanoDerecho(){
        return this.theRight;
    }

    //setters
    public void setElem(Object newElem){
        this.elem=newElem;
    }

    public void setHijoIzquierdo(NodoGen newIzquierdo){
        this.theLeft = newIzquierdo;
    }

    public void setHermanoDerecho(NodoGen newDerecho){
        this.theRight=newDerecho;
    }
}
