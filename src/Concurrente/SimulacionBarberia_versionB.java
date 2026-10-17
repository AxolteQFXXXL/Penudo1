import java.util.concurrent.Semaphore;

class SimulacionBarberia_versionB{
    void main(){
        int cantidad = 10;
        Sillon elSillon = new Sillon(5);
        Barbero barbero = new Barbero(elSillon, "Barbon Juan");
        Cliente [] clientes = new Cliente[cantidad];
        Thread [] hilos = new Thread[cantidad];
        Thread hiloBar = new Thread(barbero, "Barbon Juan");

        for(byte a = 0; a<cantidad; a++){
            clientes[a] = new Cliente(elSillon);
            hilos[a] = new Thread(clientes[a], "cliente "+a);
        }
        hiloBar.start();
        for(byte a = 0; a<cantidad; a++) hilos[a].start();

        System.out.println("termina main.");

    }

    private class Barbero implements Runnable{
        private Sillon miSilla;
        private String nombre;
        public Barbero(Sillon sillita, String nombre){
            this.miSilla=sillita;
            this.nombre = nombre;

            System.out.println("Se abre la barberia ~*"+this.nombre+"*~");
        }

        public void run(){
            try{
                miSilla.atender();

            }catch(InterruptedException e){e.printStackTrace();}
        }
    }

    private class Cliente implements Runnable{
        private Sillon miLugar;
        public Cliente(Sillon lugar){this.miLugar=lugar;}

        public void run(){
            try{
                miLugar.ocupar();
                //System.out.println("se retira de la barberia.");
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }

    private class Sillon{
        private Semaphore dormido, atendiendo, control, atendido, atrabajar;
        private int asientosTotal, asientosOcupados=0;
        public Sillon(int val){
            dormido = new Semaphore(1);
            atendiendo = new Semaphore(0);
            control = new Semaphore(1);
            atendido = new Semaphore(0);
            atrabajar = new Semaphore(0);
            this.asientosTotal=val;
        }

        public void ocupar() throws InterruptedException{
            if(dormido.tryAcquire()){
                atendiendo.acquire();
                System.out.println(Thread.currentThread().getName()+" se va a cortar el pelo.");
                atrabajar.release();
                Thread.sleep(400);

                atendido.acquire();
                System.out.println(Thread.currentThread().getName()+" ya se corto el pelo.");
            }else{
                control.acquire();
                if(asientosOcupados<asientosTotal){
                    asientosOcupados++;
                    control.release();

                    System.out.println(Thread.currentThread().getName()+" se sentó.\nAhora espera..");

                    atendiendo.acquire();
                    System.out.println(Thread.currentThread().getName()+" se va a cortar el pelo.");
                    atrabajar.release();
                    Thread.sleep(400);

                    atendido.acquire();
                    System.out.println(Thread.currentThread().getName()+" ya se corto el pelo.");

                    control.acquire(); asientosOcupados--; control.release();

                }else control.release();
            }


            System.out.println(Thread.currentThread().getName()+" se va de barberia.");
            control.acquire(); if(asientosOcupados==0) dormido.release(); control.release();

        }

        public void atender() throws InterruptedException{
            boolean adormir = false;
            atendiendo.release();
            atrabajar.acquire();
            System.out.println(Thread.currentThread().getName()+" comienza a cortar pelo.");
            Thread.sleep(400);

            System.out.println("barbero terminó el corte.");
            atendido.release();

            control.acquire();
            if(asientosOcupados==0) adormir = true;
            control.release();

            while(!adormir){

                atendiendo.release();
                atrabajar.acquire();
                System.out.println(Thread.currentThread().getName()+" comienza a cortar pelo.");
                Thread.sleep(400);

                System.out.println("barbero terminó el corte.");
                atendido.release();

                control.acquire();
                if(asientosOcupados==0) adormir = true;
                control.release();
            }

        }
    }
}
