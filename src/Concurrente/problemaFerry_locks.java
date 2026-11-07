package src.Concurrente;

import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class problemaFerry_locks {
    
    void main(){
        Ferry ferry = new Ferry(40);
        Pasajero [] pajeros = new Pasajero[20];
        AutoMovil [] moviles = new AutoMovil[20];
        Thread [] hilos = new Thread[40];
        byte a;

        for(a=0; a<pajeros.length ; a++){
            pajeros[a] = new Pasajero(ferry);
            moviles[a] = new AutoMovil(ferry, 4);
            hilos[a] = new Thread(pajeros[a], "pasajero "+a);
            hilos[a+20] = new Thread(moviles[a], "automovil "+a);
        }

        for(Thread t: hilos) t.start();

        System.out.println("termina main;");
    }

    private class Ferry{
        private final Lock pajeritos = new ReentrantLock();
        private final Condition pajeritosConEspacio = pajeritos.newCondition();
        private final Condition pajeritosBajar = pajeritos.newCondition();

        private int cantMaxEspacio, ocupadoAct = 0;
        private boolean enMarcha = false, aBajar = false;

        public Ferry(int cantMax){
            this.cantMaxEspacio = cantMax;
        }

        /** 
        * @author: axol
        * @throws InterruptedException
        *   Cada pasajero sube en orden, hasta que el espacio Maximo en el ferry se 
        *   alcanza o excede. 
        *   << cabe aclarar >> que cuando se llama a .signal() para cuando algun hilo
        *   
        *   
         */
        public void subePajero() throws InterruptedException{
            pajeritos.lock();

            try{
                while(ocupadoAct>=cantMaxEspacio || enMarcha) pajeritosConEspacio.await();
                ocupadoAct++;

                //pajeritosConEspacio.signal();
            }catch(InterruptedException e){e.printStackTrace();}
            finally{
                if(ocupadoAct >= cantMaxEspacio) {
                    this.enMarcha=true;
                    this.ferryNavega();
                }

                pajeritos.unlock();
            }

        }

        public void suBehiculo(AutoMovil auto) throws InterruptedException{
            pajeritos.lock();
            
            try{
                while(ocupadoAct+auto.getVolumen() >= cantMaxEspacio || enMarcha) pajeritosConEspacio.await();
                ocupadoAct+=auto.getVolumen();

                //pajeritosConEspacio.signal();
            }catch(InterruptedException e){e.printStackTrace();}
            finally{
                if(ocupadoAct+auto.getVolumen() >= cantMaxEspacio) {
                    this.enMarcha=true;
                    this.ferryNavega();
                }
                
                    
                pajeritos.unlock();
            }
        }

        /** 
        * @author: axol
        * @throws InterruptedException
        *    algun hilo entra, si el ferry aun esta en marcha espera.
        *    cuando enMarcha --> false , comenzara a bajar algun hilo.
        *    << cabe aclarar >> que se realiza un .signal() con el fin que bajen en orden.
         */
        
        public void bajaPajero() throws InterruptedException{
            pajeritos.lock();

            try{
                while(enMarcha || !aBajar) pajeritosBajar.await();

                ocupadoAct--;
                pajeritosBajar.signal(); //Solo se bajan de a uno

            }catch(InterruptedException e){e.printStackTrace();}
            finally{
                if(ocupadoAct==0) {
                    this.aBajar=false;
                    pajeritosConEspacio.signal();
                }
                pajeritos.unlock();
            }
        }

        public void bajarVehiculo(AutoMovil auto) throws InterruptedException{
            pajeritos.lock();
            
            try{
                while(enMarcha || !aBajar) pajeritosBajar.await();

                ocupadoAct-=auto.getVolumen();
                pajeritosBajar.signal();    //Solo se bajan de a uno

            }catch(InterruptedException e){e.printStackTrace();}
            finally{
                if(ocupadoAct==0) {
                    this.aBajar=false;
                    pajeritosConEspacio.signal();
                }
                pajeritos.unlock();
            }
        }

        private void ferryNavega() throws InterruptedException{
            
            for(int a = 1000; a>0; a--){
                if(a%3==0)System.out.print("~~... ");
                if(a==1) {
                    this.aBajar=true;
                    this.enMarcha=false;
                }
            }

            pajeritosBajar.signal();
                
        }

    }

    private class AutoMovil implements Runnable{
        private Ferry perry;
        private int volumen;
        public AutoMovil(Ferry f, int vol){this.perry=f; this.volumen=vol;}

        public int getVolumen(){return this.volumen;}

        public void run(){
                try{
                    perry.suBehiculo(this);
                    System.out.println(Thread.currentThread().getName()+" ingresó al ferry.");

                    Thread.sleep(1100);

                    perry.bajarVehiculo(this);
                }catch(InterruptedException e){ e.printStackTrace();}
                finally{
                    System.out.println(Thread.currentThread().getName()+" llego a su destino.");
                }
        }
    }

    private class Pasajero implements Runnable{
        private Ferry perry;
        public Pasajero(Ferry f){this.perry=f;}

        public void run(){
            try{
                perry.subePajero();
                System.out.println(Thread.currentThread().getName()+" ingresó al ferry.");

                Thread.sleep(1100);

                perry.bajaPajero();
            }catch(InterruptedException e){e.printStackTrace();}
            finally{
                System.out.println(Thread.currentThread().getName()+" llego a su destino.");
            }
        }
    }
}
