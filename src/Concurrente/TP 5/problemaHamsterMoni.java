import java.util.ArrayList;
import java.util.Collections;

public class problemaHamsterMoni{
    
    void main(){
        final var val = 30;
        Thread [] manada = new Thread[val];
        Hamster [] ham = new Hamster[val];
        Jaula jaula = new Jaula(3);
        byte a;
         
        
        for(a=0; a<val; a++){
            ham[a] = new Hamster(jaula);
            manada[a] = new Thread(ham[a], "hamster n"+a);
        }
        
        for(Thread t : manada) t.start();
        
        
    }
    
    class Jaula{
        private int cantEnPlato, comiendo;
        private boolean enHamaca, enRueda;

        public Jaula(int cant){
            this.comiendo =0;
            this.cantEnPlato = cant;
            this.enHamaca = false;
            this.enRueda = false;
        }

        public synchronized void aDormir() throws InterruptedException{
            while(enHamaca) this.wait();
            
            this.enHamaca=true;
        }

        public synchronized void despertar() throws InterruptedException{
            //System.out.println(Thread.currentThread().getName()+" deja la hamaca.");
            this.enHamaca=false;
            this.notifyAll();
        }

        public synchronized void subirRueda() throws InterruptedException{
            while(enRueda) this.wait();

            this.enRueda=true;
        }

        public synchronized void bajarRueda() throws InterruptedException{
            //System.out.println(Thread.currentThread().getName()+" deja la rueda.");
            this.enRueda = false;
            this.notifyAll();
        }

        public synchronized void comer() throws InterruptedException{
            /*
            aqui se quedan en espera de los que no pueden entrar en el plato.
             */
            while(this.comiendo == this.cantEnPlato) this.wait();

            this.comiendo++;
        }

        public synchronized void buenProvecho() throws InterruptedException{
            this.comiendo--;
            this.notifyAll();
        }
    }

    class Hamster implements Runnable{
        Jaula miCasa;
        ArrayList<Integer> lista = new ArrayList<>(3);
        public Hamster(Jaula jaula){ 
            this.miCasa = jaula;
            lista.add(1);
            lista.add(2);
            lista.add(3);
            Collections.shuffle(lista);
        }

        public void run(){
            try{
                for(byte a = 0; a<=2; a++){
                    switch (lista.get(a)) {
                        case 1:
                                miCasa.comer();
                                System.out.println(Thread.currentThread().getName()+" <esta comiendo.>");
                                Thread.sleep(600);
                                miCasa.buenProvecho();
                            break;
                        case 2:
                                miCasa.subirRueda();
                                System.out.println("<estoy ejercitandome> dijo "+ Thread.currentThread().getName());
                                Thread.sleep(1000);
                                miCasa.bajarRueda();
                                System.out.println(Thread.currentThread().getName()+" dejo la rueda.");
                            break;
                        case 3:
                                miCasa.aDormir();
                                System.out.println("<que bonito es estar en un isekai> soñó "+Thread.currentThread().getName());
                                Thread.sleep(1200);
                                miCasa.despertar();
                            break;
                    
                        default:
                            break;
                    }
                }
            }catch(InterruptedException e){e.printStackTrace();}
            
        }
    }
}
