package src.Concurrente;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import src.Concurrente.dinamicas.Pila;

public class problemaFarmConc {
    
    void main(){
        int cantMedicos = 10;
        Farmacia farmacia = new Farmacia(20, 20, 20, cantMedicos);
        AuxiliarFarmacia []auxiliar = new AuxiliarFarmacia[10];
        AuxiliarContable []contable = new AuxiliarContable[10];
        EncargadoGeneral []encargado = new EncargadoGeneral[10];
        Thread[] hilos = new Thread[10];
        Thread[] hilosC = new Thread[10];
        Thread[] hilosE = new Thread[10];
        
        int a;
        for(a=0; a<auxiliar.length; a++){
            auxiliar[a] = new AuxiliarFarmacia(farmacia, cantMedicos);
            hilos[a] = new Thread(auxiliar[a], "farmaceutico "+a);
        }

        for(a=0; a<auxiliar.length; a++){
            auxiliar[a] = new AuxiliarFarmacia(farmacia, cantMedicos);
            hilosE[a] = new Thread(contable[a], "contable "+a);
        }

        for(a=0; a<auxiliar.length; a++){
            auxiliar[a] = new AuxiliarFarmacia(farmacia, cantMedicos);
            hilosC[a] = new Thread(encargado[a], "encargado "+a);
        }

        for(Thread t : hilos) t.start();
        for(Thread t : hilosE) t.start();
        for(Thread t : hilosC) t.start();
        
        System.out.println("termina main. ");
    }

    class Farmacia{
        private final Lock pilaCanasta = new ReentrantLock(true);
        private final Condition canastaGVacia = pilaCanasta.newCondition();
        //private final Condition canastaGLlena = pilaCanasta.newCondition();
        //private final Condition canEnLLena = pilaCanasta.newCondition();
        private final Condition canEnVacia = pilaCanasta.newCondition();
        //private final Condition canConLlena = pilaCanasta.newCondition();
        private final Condition canConVacia = pilaCanasta.newCondition();
        private final Condition vacios = pilaCanasta.newCondition();
        
    
        private Ficha[] fichasGeneral;
        private Ficha[] fichasContable;
        private Ficha[] fichasEncargado;
        //private int[] matriculas;
        private int[] susImportes;
        private int topeG = 0, topeC = 0, topeE = 0, contador = 120;
        private int maxG, maxC, maxE;
        private boolean continuar = true;
        

        public Farmacia(int a, int b, int c, int cantMedicos){
            this.fichasGeneral = new Ficha[a];
            this.fichasContable= new Ficha[b];
            this.fichasEncargado = new Ficha[c];
            this.susImportes = new int[cantMedicos];
            this.maxG=a;
            this.maxC=b;
            this.maxE=c;
        }

        public String mostrarPizarron(){
            String pizarra="";
            for(Integer i: susImportes) pizarra+=i+"; ";

            return pizarra;
        }

        public void crearFichaGeneral(Ficha ficha) throws InterruptedException{
            pilaCanasta.lock();

            try{

                while(topeG>=maxG) canastaGVacia.await();  //espera a que le general tenga un espacio

                fichasGeneral[topeG] =ficha;
                topeG++;
                vacios.signal();
                
            }catch(InterruptedException e ){e.printStackTrace();}
            finally{
                contador--;
                if(this.contador==0) this.continuar=false;
                pilaCanasta.unlock();
            }

        }

        public void revisarCanastaContable() throws InterruptedException{
            pilaCanasta.lock();
            
            try{
                while(topeG==0 || topeC==0) vacios.await();

                System.out.println(Thread.currentThread().getName()+" logro entrar a las canastas.");
                if(topeG>0){ //espera a que canasta tenga un elemento
                    susImportes[fichasGeneral[topeG].getNumColegiala()]+=fichasGeneral[topeG].getImporte(); 

                    while(topeE>=maxE) canEnVacia.await();  //espera a que encargado tenga espacio
                    fichasEncargado[topeE]=fichasGeneral[topeG];
                    topeE++;
                    vacios.signalAll();
                    
                    fichasGeneral[topeG]=null;
                    topeG--;
                    canastaGVacia.signalAll();  //le permitimos a auxiliar poner mas cosas en general
                }

                if(topeC>0){
                    susImportes[fichasContable[topeC].getNumColegiala()]=fichasContable[topeC].getImporte();
                    fichasContable[topeC] = null;
                    topeC--;

                    canConVacia.signalAll();
                }
                
            }catch(InterruptedException e){e.printStackTrace();}
                finally{ pilaCanasta.unlock();}
                

        }

        public void revisarGeneralDeEncardo() throws InterruptedException{
            pilaCanasta.lock();

            try{
                
                //espera a que general este con un elemento
                while(topeG==0 || topeE==0) vacios.await();
    
                System.out.println(Thread.currentThread().getName()+" logro entrar a las canastas.");
                if(topeG>0){
                    susImportes[fichasGeneral[topeG].getNumColegiala()]+=fichasGeneral[topeG].getImporte();
                    
                    while(topeC>=maxC) canConVacia.await();     //espera a que contable deje de estar llena
                    
                    fichasContable[topeC]=fichasGeneral[topeG];
                    topeC++;
                    vacios.signalAll();
        
                    fichasGeneral[topeG]=null;
                    topeG--;
                    canastaGVacia.signalAll();

                }

                if(topeE>0){
                    susImportes[fichasEncargado[topeE].getNumColegiala()]+=fichasEncargado[topeE].getImporte();
                    fichasEncargado[topeE]=null;
                    topeE--;

                    canEnVacia.signalAll();

                   
                }
                
            }catch(InterruptedException e){e.printStackTrace();}
            finally{pilaCanasta.unlock();}
        }
        
        public boolean getContinuar(){return continuar;}

    }

    class AuxiliarFarmacia implements Runnable{
        private Farmacia farm;
        private int cantMedicos;
        private Random ram = new Random();

        public AuxiliarFarmacia(Farmacia farm, int cantMedicos){ this.farm=farm;
            this.cantMedicos=cantMedicos;
        }

        public void run(){
            try{
                while(farm.getContinuar()){
                    int val1 = ram.nextInt(1, 1000);
                    int val2 = ram.nextInt(1, 300);
                    int val3 = ram.nextInt(0, cantMedicos);
                    Ficha rec = new Ficha(val1, val2, val3);
                    farm.crearFichaGeneral(rec);

                    System.out.println(Thread.currentThread().getName()+" subió una ficha.");
                    Thread.sleep(500);
                }
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }

    class AuxiliarContable implements Runnable{
        private Farmacia farm;
        public AuxiliarContable(Farmacia farm){this.farm=farm;}

        public void run(){
            try{
                while(farm.getContinuar()){
                    farm.revisarCanastaContable();

                    System.out.println(Thread.currentThread().getName()+" revisó fichas.");
                    Thread.sleep(600);
                }
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }

    class EncargadoGeneral implements Runnable{
        private Farmacia farm;
        public EncargadoGeneral(Farmacia farm){this.farm=farm;}

        public void run(){
            try{
                while(farm.getContinuar()){
                    farm.revisarGeneralDeEncardo();

                    System.out.println(Thread.currentThread().getName()+" revisó fichas.");
                    Thread.sleep(650);
                }
            }catch(InterruptedException e){e.printStackTrace();}
            finally{System.out.println(farm.mostrarPizarron());}
        }
    }

    class Ficha{
        private int importe;
        private int numFicha;
        private int numColegiala;

        public Ficha(int im, int nr, int nc){
            this.importe=im;
            this.numColegiala=nc;
            this.numFicha=nr;
        }

        public int getImporte() {
            return importe;
        }

        public int getNumFicha() {
            return numFicha;
        }

        public int getNumColegiala() {
            return numColegiala;
        }
    }
}
