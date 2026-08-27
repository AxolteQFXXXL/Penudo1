package src2.Concurrente.dinamicas;

public class Nodo {
    private Object elem;
    private Nodo enlace;

        public Nodo(Object elem, Nodo enlace){
            this.elem=elem;
            this.enlace=enlace;
        }

        Object getElem(){
            return this.elem;
        }

        void setElem(Object newElem){
            this.elem=newElem;
        }

        Nodo getEnlace(){
            return this.enlace;
        }

        void setEnlace(Nodo newenlace){
            this.enlace=newenlace;
        }
}
