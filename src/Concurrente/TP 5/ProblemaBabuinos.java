import java.util.concurrent.Semaphore;
public class ProblemaBabuinos{
    
    private class Cuerda{
        
        /*
        Re hacer todo de nuevo, semaforo para la cant de monos que pueden estar en la cuerda, y con semaforos binarios
        simular que los monos pelean al cruzarse, tenemos un control que nos indica cuantos pudieron llegar a su 
        destino.
        */
        private Semaphore turnoA, turnoB, control;
        private int enLadoA, enLadoB, enCuerda, totalACruzar;
        private final int resistencia = 5;
        private boolean verificacion;
        public Cuerda(){
            this.turnoA = new Semaphore(resistencia, true);
            this.turnoB = new Semaphore(resistencia, true);
            this.control = new Semaphore(1);
            this.enLadoA = 0; this.enLadoB = 0; this.enCuerda = 0;
            this.verificacion =false;
        }
        
        public void subirA() throws InterruptedException{
            this.turnoB.tryAcquire(resistencia);
            this.turnoA.acquire();
            
            this.control.acquire();
            this.enCuerda++;
            this.control.release();
            
        }
        
        public void subirB() throws InterruptedException{
            this.turnoA.tryAcquire(resistencia);
            this.turnoB.acquire();
            
            this.control.acquire();
            this.enCuerda++;
            this.control.release();
            
        }
        
        public void bajarA() throws InterruptedException{
            this.control.acquire();
            this.enLadoA++;
            this.enCuerda--;
            if(enCuerda == 0) this.turnoB.release(resistencia);
            this.control.release();
            
        }
        
        public void bajarB() throws InterruptedException{
            this.control.acquire();
            this.enLadoB++;
            this.enCuerda--;
            if((this.enLadoA + this.enLadoB) == this.totalACruzar) this.verificacion=true;
            if(enCuerda == 0) this.turnoA.release(resistencia);
            this.control.release();
            
        }
        
        public boolean getVerificacion() throws InterruptedException{
            return this.verificacion;
        }
        
        
        
    }
    
    private class Babuino implements Runnable{
        private Cuerda cuerdita;
        private char tipo;
        public Babuino (Cuerda q, char val ){
            this.cuerdita =q;
            this.tipo = val;
        }
        
        public void run(){
            try{
                if(tipo == 'a'){
                    cuerdita.subirA();
                    System.out.println("<esta en cuerda> "+Thread.currentThread().getName());
                    Thread.sleep(400);
                    cuerdita.bajarA();
                    System.out.println("<baja de cuerda> "+Thread.currentThread().getName());
                }
                if(tipo == 'b'){
                    cuerdita.subirB();
                    System.out.println("<esta en cuerda> "+Thread.currentThread().getName());
                    Thread.sleep(400);
                    cuerdita.bajarB();
                    System.out.println("<baja de cuerda> "+Thread.currentThread().getName());
                    
                }
            }catch(InterruptedException e){e.printStackTrace();}
            finally{
                if(cuerdita.getVerificacion()) 
            }
        }
    }
}