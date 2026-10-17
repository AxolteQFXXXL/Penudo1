import java.util.concurrent.Semaphore;

public class PC_TP3_10{
    void main(){
        Barbero barbero = new Barbero("Juanes");
        Peludo [] peluditos = new Peludo[10];
        Thread [] hilos = new Thread[10];
        byte a;
        for( a = 0; a<10; a++){
            peluditos[a] = new Peludo(barbero);
            hilos[a] = new Thread(peluditos[a], "cliente "+a);
        }

        for(a = 0; a<10; a++) hilos[a].start();

        System.out.println("termina main.");
    }

    private class Barbero{
        private Semaphore dormido, atendiendo, control;
        private final int cantSillas=5;
        private int sillasOcupadas=0;

        public Barbero(String name){
            this.dormido=new Semaphore(1);
            this.atendiendo = new Semaphore(1);
            this.control = new Semaphore(1);
            System.out.println("Se abre la nueva barberia ~* "+name+" *~");
        }

        public void cortarPelo() throws InterruptedException{
            if(dormido.tryAcquire()){
                atendiendo.acquire();
                System.out.println("Cortando pelo a "+Thread.currentThread().getName());
                Thread.sleep(300);
                atendiendo.release();
            }else{
                control.acquire(); //aqui verifica si las sillas estan ocupadas, sino se va
                if(sillasOcupadas!=cantSillas){
                    sillasOcupadas++;
                    control.release();
                    System.out.println(Thread.currentThread().getName()+" se sento.");

                    atendiendo.acquire();   //Estan sentados hasta que el cliente actual termine se cortarse el pelo
                    System.out.println("Cortando pelo a "+Thread.currentThread().getName());
                    Thread.sleep(300);
                    atendiendo.release();

                }else control.release();
            }

            System.out.println(Thread.currentThread().getName()+" se fue.");
            if(sillasOcupadas==0) dormido.release(); //si ya no hay mas sillas ocupadas y tampoco cliente se duerme el barbero.
        }
    }

    private class Peludo implements Runnable{
        Barbero barbero;
        public Peludo(Barbero barbar){this.barbero=barbar;}

        public void run(){
            try{
                barbero.cortarPelo();
            }catch(InterruptedException e){System.out.println(e);}
            /*finally{
                System.out.println("presume su nuevo corte.");
            }*/
        }
    }

}
