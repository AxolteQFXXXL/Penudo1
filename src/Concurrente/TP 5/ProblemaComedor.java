import java.util.concurrent.Semaphore;

public class ProblemaComedor{
    void main(){
        GestorComedor comedero = new GestorComedor((short)2);
        Thread [] perros = new Perro[10];
        Thread [] gatos = new Gato[10];

        byte a;

        for(a=0; a<perros.length; a++){
            perros[a] = new Perro(comedero); perros[a].setName("perro "+a);
            gatos[a] = new Gato(comedero); gatos[a].setName("gato "+a);
        }

        for(a = 0; a<perros.length; a++){
            gatos[a].start();
            perros[a].start();
        }

        System.out.println("termina main.");
        

    }

    class GestorComedor{
        private Semaphore semP, semG, control = new Semaphore(1,true), controlP, controlG;
        private short cantComedero;
        private short comiendo = 0;
        private byte quienCome;

        public GestorComedor(short cantComedero){
            this.semP = new Semaphore(cantComedero,true);
            this.semG = new Semaphore(cantComedero,true);
            this.controlP = new Semaphore(1); 
            this.controlG = new Semaphore(1);
            this.cantComedero = cantComedero;
        }

        public void comer(){
            System.out.println(Thread.currentThread().getName()+" comienza a comer.");

        }
        public void gatoSalir() throws InterruptedException{
            System.out.println(Thread.currentThread().getName()+" termino de comer.");
            control.acquire();
            comiendo--;
            if(comiendo == 0){
                semP.release(cantComedero);
                System.out.println("podran comer los perros");
            }
            control.release();
        }

        public void perroSalir() throws InterruptedException{
            System.out.println(Thread.currentThread().getName()+" termino de comer.");
            control.acquire();
            comiendo--;
            if(comiendo == 0){
                semG.release(cantComedero);
                System.out.println("podran comer los gatos");
            }
            control.release();
        }

        public void entrarPerros() throws InterruptedException{
            semG.tryAcquire(cantComedero);
            semP.acquire();

            control.acquire();
            comiendo++;
            control.release();
        }

        public void entrarGatos() throws InterruptedException{
            semP.tryAcquire(cantComedero);
            semG.acquire();

            control.acquire();
            comiendo++;
            control.release();
        }
    }

    class Perro extends Thread{
        GestorComedor comedero;
        public Perro(GestorComedor com){this.comedero = com;}

        public void run(){
            try{
                comedero.entrarPerros();
                comedero.comer();
                Thread.currentThread().sleep(700);
                comedero.perroSalir();
            }catch(InterruptedException e){e.printStackTrace();}
        }

    }

    class Gato extends Thread{
        GestorComedor comedero;
        public Gato(GestorComedor com){this.comedero = com;}

        public void run(){
            try{
                comedero.entrarGatos();
                comedero.comer();
                Thread.currentThread().sleep(700);
                comedero.gatoSalir();
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }

}
