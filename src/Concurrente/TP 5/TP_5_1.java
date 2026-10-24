import java.util.concurrent.Semaphore;


/**
 * TP_5_1
 */
public class TP_5_1 {

    class GestorPiscina{
    Semaphore espacio, mutex = new Semaphore(1);
    Piscina piscina;

    public GestorPiscina(int cantEspa, Piscina pis){
        this.espacio = new Semaphore(cantEspa);
        this.piscina = pis;
    }

    public void acceder() throws InterruptedException{
        espacio.acquire();
        mutex.acquire();
        piscina.entrar();
        mutex.release();
    }

    public void evacuar() throws InterruptedException{
        mutex.acquire();
        piscina.salir();
        mutex.release();
        espacio.release();
    }
}

class Piscina{
    public Piscina(){}

    public void entrar() throws InterruptedException{
        System.out.println(Thread.currentThread().getName()+" se moja en la piscina.");

    }

    public void salir() throws InterruptedException{
        System.out.println(Thread.currentThread().getName()+" quiere salir de piscina.");
    }

}

void main(){
    Piscina p = new Piscina();
    GestorPiscina g = new GestorPiscina(15, p);
    Persona[] per = new Persona[100];
    Thread[] hilos = new Thread[100];
    byte a;

    for(a=0; a<per.length; a++){
        per[a] = new Persona(g);
        hilos[a] = new Thread(per[a]);
    }
    for(a=0; a<hilos.length; a++) hilos[a].start();


    System.out.println("termina main.");


}

class Persona implements Runnable {
    GestorPiscina ger;
    public Persona(GestorPiscina g){
        this.ger=g;
    }

    public void run(){
        try{
            ger.acceder();
            Thread.sleep(3000);
            ger.evacuar();
        }catch(InterruptedException e){ e.printStackTrace();}

    }

}

}
