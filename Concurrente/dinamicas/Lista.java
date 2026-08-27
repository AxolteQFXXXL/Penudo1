package src2.Concurrente.dinamicas;

public class Lista {

    private Nodo cabecera;
    private int longit = 0;

    public Lista() { 
        cabecera = null;
    }

    public boolean insertar(Object elem, int pos) { //te inserta un elemento en una posicion valida, 
    // perteneciente a la lista*/
        boolean listo = false;

        if (pos >= 1 && pos <= longit + 1) {
            if (pos == 1) { //si la pos =1, entonces la cabecera cambia al nuevo nodo enlazado a la anterior cabecera
                this.cabecera = new Nodo(elem, this.cabecera);
                listo = true;
            } else {
                Nodo n1 = this.cabecera;
                int i = 1;  /* recorre la lista hasta la pos -1 para poder ingresar el elemento
                 en tal pos */
                while (i < pos - 1) {
                    n1 = n1.getEnlace();
                    i++;
                } /* crea un nuevo nodo enlazado al sig. y luego al anterior cambia el enlace 
                al nuevo nodo  */
                Nodo nuevo = new Nodo(elem, n1.getEnlace());
                n1.setEnlace(nuevo);
                listo = true;
            }
            longit++;
        }

        return listo;
    }

    public boolean eliminar(int pos) { /* elimina un elemento en una pos */
        boolean listo = false;

        if (pos >= 1 && pos <= longit) { /* verifica si la pos es valida, sino false
            si pos =1, entonces la cabera cambia a su siguiente */
            if (pos == 1) {
                this.cabecera = this.cabecera.getEnlace();
                listo = true;
            } else { /* con dos nodos, uno anterior y otro actual: 
                recorremos la lista hasta i+1 = pos , y ahi cambia el enlace del anterior a el enlace del actual */
                Nodo n1 = this.cabecera;
                Nodo n2 = this.cabecera.getEnlace();
                int i = 1;
                while (i < pos ) {
                    if (i + 1 == pos) {
                        n1.setEnlace(n2.getEnlace());
                    }
                    n1 = n2;
                    n2 = n2.getEnlace();
                    i++;
                }
                listo = true;
            }
            longit--;
        }
        
        return listo;
    }

    public int longitud() { //O(1) longit longitud como variable de instancia
        return longit;
    }

    public int localizar(Object o1) { /* te muestra en que posicion esta un obj de la lista
        si no se encuentra regresa 0  */
        int posi = 0;
        boolean encontrado = false;
        Nodo aux = this.cabecera;

        while (!encontrado && posi < longit) {
            posi++;
            if (aux.getElem() == o1) {
                encontrado = true;
            }
            aux = aux.getEnlace();
        }
        if (!encontrado) {
            posi = 0;
        }

        return posi;
    }

    public Object recuperar(int pos) { /* segun una pos, devuelve ese elemento en esa posicion
        si no se encuentra regresa ~null~ */
        Object algo = null;
        boolean found = false;
        int i = 1;
        Nodo a1 = this.cabecera;

        if (pos >= 1 && pos <= longit) { //si la pos es valida realiza el recorrido
        // llega a la posicion indicada, envia algo*/
            while (!found && i <= longit) {
                if (i == pos) {
                    algo = a1.getElem();
                    found = true;
                }
                i++;
                a1 = a1.getEnlace();
            }
        }

        return algo;
    }

    public Lista clone() { /* te crea una nueva lista sin ser una copia del this.cabecera */
        Lista nuevo = new Lista();
        Nodo a1 = this.cabecera;
        Nodo aux = null;
        
        for (int i = 1; i <= this.longit; i++) { /* se realiza un for ya que ya conocemos la longit de lista
            en el caso de ser vacia nueva lista, crea su cabecera
            sino, enlaza a un nueva nodo con los elem de a1, y en cada una aunmentar longit */
            if (nuevo.esVacia()) {
                nuevo.cabecera = new Nodo(a1.getElem(), null);
                aux = nuevo.cabecera;
                nuevo.longit++;
            } else {
                aux.setEnlace(new Nodo(a1.getElem(), null));
                aux = aux.getEnlace();
                nuevo.longit++;
            }
            a1 = a1.getEnlace();
        }

        return nuevo;
    }

    public void eliminarApariciones(Object oo1) {
        Nodo a1 = this.cabecera;
        Nodo b1 = null;
        int i = 1;

        while (a1 != null) {
            if (i == 1) {
                if (a1.getElem() == oo1) {
                    this.cabecera = a1.getEnlace();
                }
            } else {
                if (a1.getElem() == oo1) {
                    b1.setEnlace(a1.getEnlace());
                }
            }
            b1 = a1;
            a1 = a1.getEnlace();
            i++;
        }
        
    }

    public void invertir() {
        Nodo a1 = this.cabecera;
        Nodo b1 = null,
            c1 = null;
        int i = 1;

        while (a1 != null && i <= longit) {
            if (i > 1) {
                b1.setEnlace(c1);
                c1 = b1;
                if (a1.getEnlace() == null) {
                    a1.setEnlace(b1);
                    this.cabecera = a1;
                }
            }
            b1 = a1;
            a1 = a1.getEnlace();
            i++;
        }

    }

    public void vaciar() { /* lo vacia */
        this.cabecera = null;
        longit = 0;
    }

    public Lista obtenerMultiplos(int n) {
        int i = 1;
        Lista nuevo = new Lista();
        Nodo n1 = this.cabecera;
        Nodo n2 = null;

        while (i <= this.longit) {
            if (i % n == 0) {
                if (nuevo.esVacia()) {
                    nuevo.cabecera = new Nodo(n1.getElem(), null);
                    n2 = nuevo.cabecera;
                    nuevo.longit++;
                } else {
                    n2.setEnlace(new Nodo(n1.getElem(), null));
                    n2 = n2.getEnlace();
                    nuevo.longit++;
                }
            }

            n1 = n1.getEnlace();
            i++;
        }

        return nuevo;
    }

    public boolean moverAlFrente(int pos) {
        boolean listo = false;
        Nodo n1 = this.cabecera,
            n2 = null;
        int i = 1;

        while (i <= this.longit && !listo) {
            if (i == pos) {
            
                n2.setEnlace(n1.getEnlace());
                n1.setEnlace(this.cabecera);
                this.cabecera = n1;
                listo = true;
            } else{
                n2=n1;
                 n1 = n1.getEnlace();
            }
            i++;
        }


        return listo;
    }

    public boolean esVacia() { return this.cabecera == null && longit == 0;}

    public String toString() { //te crea un string con todos los elementos de la lista entre[] 
        String sa = "[";
        Nodo aux = this.cabecera;
        int i = 1;

        if (!this.esVacia()) {
            while (i <= this.longit && aux != null) {
                sa += aux.getElem();
                if (aux.getEnlace() != null) {
                    sa += ",";
                }
                aux = aux.getEnlace();
                i++;
            }
        }

        sa += "]";
        return sa;
    }
}
