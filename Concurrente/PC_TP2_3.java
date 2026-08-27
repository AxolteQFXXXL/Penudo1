package src2.Concurrente;

public class PC_TP2_3 {
    class Jaula{
        private int plato;
        private boolean camaLibre;
        public Jaula(){this.plato = 0;
        }

        public synchronized void comer() throws InterruptedException{
            this.plato+=1;
            System.out.println("Comieron: "+this.plato);
            Thread.sleep(150);
        }

        public synchronized void usarRueda() throws InterruptedException{ 
            System.out.println(Thread.currentThread().getName()+" esta usando la Rueda");
            Thread.sleep(150);
        }

        public synchronized void dormir() throws InterruptedException{ 
            System.out.println(Thread.currentThread().getName()+" esta durmiendo.");
            Thread.sleep(150);
        }


    }

    class Hamster implements Runnable{
        Jaula casa;
        public Hamster(Jaula unajaula){ this.casa = unajaula;}

        public void run(){
            try{
                casa.comer(); 
                casa.usarRueda();
                casa.dormir();
            }catch(InterruptedException ex){
                System.out.println(ex);
            }
        }
    }

    void main(){
        Jaula jaula = new Jaula();
        Hamster[]manada = new Hamster[10];
        Thread[] hilos = new Thread[10];

        for(Byte i = 0; i<10; i++){
            manada[i] = new Hamster(jaula);
            hilos[i] = new Thread(manada[i], "sujeto "+i);
        }

        for(Byte i = 0; i<10; i++){
            hilos[i].start();
        }

        /*for(Byte i = 0; i<10; i++){ 
            try{
                hilos[i].join();
            }catch(InterruptedException e){
                System.out.println(e);
            }
        }*/

        System.out.println("Termina main.");
    }
}
