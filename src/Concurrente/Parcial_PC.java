package src.Concurrente;
import java.util.concurrent.Semaphore;

public class Parcial_PC {

    void main(){
        byte cant = 24*4;
        Colectivo autobus = new Colectivo();
        Pajeros [] pasajeros = new Pajeros[cant];
        Control unControl = new Control(autobus);
        Thread [] hilos = new Thread[cant];
        Thread elControl = new Thread(unControl, "elControl");
        byte a;

        for(a=0; a<cant; a++){
            pasajeros[a] = new Pajeros(autobus);
            hilos[a] = new Thread(pasajeros[a], "pajero: "+a);
        }
        elControl.start();

        for(a=0; a<cant; a++) hilos[a].start();

        System.out.println("termina main.");
    }

    public class Colectivo{
        private Semaphore mutex = new Semaphore(1);
        private Semaphore puedeSubir = new Semaphore(1); // pasajeros esperan esto
        private Semaphore puedeBajar = new Semaphore(0);
        private Semaphore lleno = new Semaphore(0);   // avisa al control
        private Semaphore vacio = new Semaphore(0);
        private final int maxPasajero = 24;
        private int actPasajero = 0;
        private int pasados = 0;
        private byte actParada = 1, sigParada, viajes =0;
        private final byte viajesTotal = 3;

        public Colectivo(){}

        public void subir(Pajeros pasajero) throws InterruptedException{
            puedeSubir.acquire(); // espera a que el colectivo esté en parada 1
            mutex.acquire();
            if (actParada == 1 && actPasajero < maxPasajero && viajes<viajesTotal) {
                actPasajero++;
                pasajero.setParada((byte) 0);
                System.out.println(Thread.currentThread().getName() + " subio. Activos: " + actPasajero);
                Thread.sleep(20);
                if (actPasajero == maxPasajero) {
                    System.out.println("Colectivo lleno.");
                    lleno.release(); // avisa al control
                } else puedeSubir.release(); // deja subir al siguiente

            } else puedeSubir.release(); // No puede subir (colectivo lleno o cambió de parada), devuelve el permiso

            mutex.release();

        }

        public void bajar(Pajeros pasajero) throws InterruptedException{
            puedeBajar.acquire();
            mutex.acquire();
            if (actParada == 2 && actPasajero > 0) {
                actPasajero--;
                pasajero.setParada((byte) 2);
                System.out.println(Thread.currentThread().getName() + " bajo. Activos: " + actPasajero);
                sumarPasados();
                Thread.sleep(20);
                if (actPasajero == 0) {
                    System.out.println("Colectivo vacio.");
                    vacio.release(); // avisa al control
                } else puedeBajar.release(); // deja bajar al siguiente

            } else puedeBajar.release();

            mutex.release();
        }

        public void conducir() throws InterruptedException{

            /*orden.acquire();
            if(this.actParada != this.sigParada){
                this.actParada = this.sigParada;
                System.out.println("el colectivo se dirige a la parada: "+ this.sigParada);
                Thread.currentThread().sleep(100);
                if(this.actParada==2) puerta.acquire();
                if(this.actParada==1) puerta2.acquire();
            }*/
            lleno.acquire();
            mutex.acquire();
            actParada = 2;
            System.out.println("Colectivo se dirige a parada 2.");
            mutex.release();
            Thread.sleep(100);

            // Llegó a parada 2: habilita bajada
            mutex.acquire();
            actParada = 2;
            viajes++;
            mutex.release();
            puedeBajar.release();

            // Espera a que se vacíe
            vacio.acquire();
            mutex.acquire();
            actParada = 1;
            System.out.println("Colectivo se dirige a parada 1.");
            mutex.release();
            Thread.sleep(100);

            // Llegó a parada 1: habilita subida
            puedeSubir.release();


        }

        private void sumarPasados(){this.pasados++;}
        public int getPasados(){ return this.pasados;}
        public boolean estaVacio(){return this.actPasajero==0;}
        public boolean estaLleno(){return this.maxPasajero == this.actPasajero;}
        public int getCantMax(){return this.maxPasajero;}
        public int getCantAct(){return this.actPasajero;}
        public byte getSigParada(){return this.sigParada;}
        public byte getParada(){return this.actParada;}
        public byte getViajes(){return this.viajes;}
        public byte getViajesMax(){return this.viajesTotal;}

    }

    public class Pajeros implements Runnable{
        private byte parada;
        private Colectivo autobus;
        public Pajeros(Colectivo col){
            this.autobus = col;
            this.parada = 1;
        }

        public void run(){
            try{
                while(this.parada==1 && autobus.getViajes()<autobus.getViajesMax())autobus.subir(this);

                while(this.parada==0 && autobus.getViajes()<autobus.getViajesMax())autobus.bajar(this);
                if(this.parada==2) System.out.println("Llegue a mi parada: " + Thread.currentThread().getName());

            }catch(InterruptedException e){System.out.println(e);}


        }

        public void setParada(byte par){this.parada=par;}
    }

    public class Control implements Runnable{
        Colectivo autobus;

        public Control(Colectivo bus){this.autobus=bus;}

        public void run(){
            try{
                while(autobus.getViajes()<autobus.getViajesMax()) autobus.conducir();

            }catch(InterruptedException e){System.out.println(e);}
        }
    }


}
