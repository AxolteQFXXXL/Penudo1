package src.Concurrente;

import java.util.ArrayList;
import java.util.Collections;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class problemaCocinerosRecetas {

    /**
     * @see Ex : se utilizan solo 3 cocineros.
     */
    void main(){

        DispensaPasta pepe = new DispensaPasta();
        ParrillaCarne carmen = new ParrillaCarne();
        DispensaVegetales geges = new DispensaVegetales();
        Cocinero [] cocineros = {new Cocinero(pepe, geges, carmen, 0), 
            new Cocinero(pepe, geges, carmen, 1),
            new Cocinero(pepe, geges, carmen, 2)
        };
        Thread [] hilos = {new Thread(cocineros[0], "cocinero 1"),
            new Thread(cocineros[1], "cocinero 2"),
            new Thread(cocineros[2], "cocinero 3")
        };

        for(Thread t: hilos) t.start();

        System.out.println("termina main.");
    }
    /**
     * 
     * DispensaVegetales
     * ParrillaCarne
     * DispensaPastas
     * @param: cantidad de vegetales/ boolean carne disponible/ cantidad de pastas 
     * @param: Lock "llave" + Condition "hayAbasto".
     * hace que cada hilo espere hasta que cada objeto este disponible.
     */
    private class DispensaVegetales{
        private final Lock llave = new ReentrantLock(true);
        private final Condition hayAbasto = llave.newCondition();
        private  int cantDisponible=10;

        private void usar(){this.cantDisponible=0;}
        private void noUsar(){this.cantDisponible=10;}

        public void utilizarVegetales(){
            llave.lock();

            try{
                while(cantDisponible==0) hayAbasto.await();

                this.usar();
            }catch(InterruptedException e){e.printStackTrace();}
            finally{
                llave.unlock();
            }
        }

        public void dejarVegetales(){
            llave.lock();

            this.noUsar();
            hayAbasto.signal();

            llave.unlock();
        }

    }

    private class ParrillaCarne{
        private final Lock llave = new ReentrantLock(true);
        private final Condition libre = llave.newCondition();
        private boolean enUso=false;

        public void utilizarParrilla(){
            llave.lock();

            try{
                while(enUso) libre.await();

                enUso=true;
            }catch(InterruptedException e){e.printStackTrace();}
            finally{
                llave.unlock();
            }
        }
        public void dejarParrilla(){
            llave.lock();
            enUso=false;
            libre.signal();
            llave.unlock();
        }

    }

    private class DispensaPasta{
        private final Lock llave = new ReentrantLock(true);
        private final Condition hayAbasto = llave.newCondition();
        private int cantDisponible=10;

        private void usar(){this.cantDisponible=0;}
        private void dejar(){this.cantDisponible=10;}
        public void utilizarPasta(){
            llave.lock();
            try{
                while(cantDisponible==0) hayAbasto.await();

                this.usar();
            }catch(InterruptedException e){e.printStackTrace();}
            finally{ 
                llave.unlock();
            }
        }
        public void dejarPasta(){
            llave.lock();
            this.dejar();
            hayAbasto.signal();
            llave.unlock();
        }
    }

    private class Cocinero implements Runnable{
        private DispensaPasta pastas;
        private DispensaVegetales vegetales;
        private ParrillaCarne parrilla;
        private ArrayList<Integer>aCocinar = new ArrayList<>(3);
        private int yaListo;

        public Cocinero(DispensaPasta p, DispensaVegetales v, ParrillaCarne c, int yaListo){
            this.pastas=p;
            this.vegetales=v;
            this.parrilla=c;
            this.aCocinar.add(0);
            this.aCocinar.add(1);
            this.aCocinar.add(2);
            Collections.shuffle(aCocinar);
            this.yaListo=yaListo;
        }

        public void run(){
            try{
                for(byte a = 0; a<3; a++){
                    if(a!=yaListo){
                        if(aCocinar.get(a)==0){
                            parrilla.utilizarParrilla();
                            System.out.println(Thread.currentThread().getName()+" esta usando Parrilla.");
                            Thread.sleep(1200);
                            parrilla.dejarParrilla();
                            System.out.println(Thread.currentThread().getName()+" dejó la Parrilla.");
                        }
                        if(aCocinar.get(a)==1){
                            pastas.utilizarPasta();
                            System.out.println(Thread.currentThread().getName()+" esta usando la pasta.");
                            Thread.sleep(1300);
                            pastas.dejarPasta();
                            System.out.println(Thread.currentThread().getName()+" dejó la pasta.");
                        }
                        if(aCocinar.get(a)==2){
                            vegetales.utilizarVegetales();
                            System.out.println(Thread.currentThread().getName()+" esta usando vegetales.");
                            Thread.sleep(1200);
                            vegetales.dejarVegetales();
                            System.out.println(Thread.currentThread().getName()+" dejó los vegetales.");
                        }
                    }
                }
            }catch(InterruptedException e){e.printStackTrace();}
            finally{
                System.out.println(Thread.currentThread().getName()+" Sirvió su plato, yes yes.");
            }
        }
    }
}
