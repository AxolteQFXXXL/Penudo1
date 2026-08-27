/**Autores**
 Ansberck Martin, legajo FAI-4861
 Lozano Axel, legajo FAI-5199
 Miriuka Matias, legajo FAI-5420
 Roca Jalil Yazmin, legajo FAI-5262
 */
package src2.Concurrente.dinamicas;

public class Cola {

    private Nodo frente;
    private Nodo fin;

    //constructor
    public Cola() {
        this.frente = null;
        this.fin = null;
    }

    //de tipo

    public boolean poner(Object obj) {
        //añade un elemento a la estructura
        boolean listo = false;
        Nodo nuevo;
        nuevo = new Nodo(obj, null);

        if (esVacia()) {
            //caso especial por si la pila esta vacía
            //se añade el primer elemento
            this.fin = nuevo;
            this.frente = nuevo;
            listo = true;
        } else {
            //se añade un nuevo elemento
            this.fin.setEnlace(nuevo);
            this.fin = nuevo;
            listo = true;
        }

        return listo;
    }

    public boolean sacar() {
        //elimina el elemento al frente de la Cola
        boolean listo = false;

        if (this.frente != null) {
            this.frente = this.frente.getEnlace();
            if (this.frente == null) {
                //verifica si es el ultimo elemento de la Cola
                this.fin = null;
            }
            listo = true;
        }

        return listo;
    }

    public Object obtenerFrente() {
        Object elem = null;
        //sirve para cambiar el elemento final
        //

        if (!esVacia()) {
            //verifica que no este vacia y devuelve el elemento en el frente
            elem = frente.getElem();
        }

        return elem;
    }

    public boolean esVacia() {
        //verifica si esta vacio, y retorna el resultado
        return this.frente == null && this.fin == null;
    }

    public void vaciar() {
        //vacia la Cola quitando todas las referencias a otros nodos
        this.frente = null;
        this.fin = null;
    }

    public Cola clone() {
        Cola nueva = new Cola();
        Nodo n1 = this.frente;

        while (n1 != null) {
            //se ejecuta hasta que la Cola este vacia
            //recorre la Cola, clonando elemento por elemento hasta que se termine
            Nodo aux = new Nodo(n1.getElem(), null);
            if (nueva.esVacia()) {
                nueva.frente = aux;
                nueva.fin = aux;
            } else {
                nueva.fin.setEnlace(aux);
                nueva.fin = aux;
            }
            n1 = n1.getEnlace();
        }

        return nueva;
    }

    public String toString() {
        //Transforma los elementos de una Cola en una lista
        String lista = "[]";
        Nodo n1 = this.frente;

        if (!esVacia()) {
            lista = "[";
            while (n1 != null) {
                //recorre la Cola y añade cada elemento a la lista
                lista += n1.getElem().toString();
                n1 = n1.getEnlace();
                if (n1 != null) {
                    //si faltan elementos por añadir a la lista, separa cada uno con ","
                    lista += ",";
                }
            }
            lista += "]";
        }

        return lista;
    }
}
