package src.Concurrente;

/**
 * PC_monitor_1
 */
public class PC_monitor_1{
    
    void main(){
        int cant = Integer.parseInt(IO.readln("Ingrese cantidad de hilos: "));
        GestionaTrafico traficante = new GestionaTrafico();
        Thread [] hilos = new Thread[cant];
        Autito [] autos = new Autito[cant];
        int a;
        
        for(a=0; a<cant; a++){
            if(a%2 == 0){ 
                autos[a] = new Autito(traficante, 's');
                hilos[a] = new Thread(autos[a], "auto s "+a);
                
            }
            else{
                 autos[a] = new Autito(traficante, 'n');
                 hilos[a] = new Thread(autos[a], "auto n "+a);
             }
        }
        
        for(Thread t : hilos) t.start();
        
        
    }
    
    /*
    MONITOR DE TRAFICO
    */
    private class GestionaTrafico{
        private int esperaNorte, esperaSur, enPuente, cruzadoSur, cruzadoNorte;
        private final int cantTotal = 10;
        private boolean haciaSur, haciaNorte;
        
        public GestionaTrafico(){
            this.esperaNorte = 0;
            this.esperaSur = 0;
            this.cruzadoSur=0;
            this.cruzadoNorte =0;
            this.enPuente = 0;
            this.haciaNorte=false;
            this.haciaSur=false;
        }
        
        /* cada coche entra al puente si cumple la condicion
        @Enum: hay lugar en el puente
        @Enum: la direccion le corresponde
            en caso contrario debera espera (CE)
            cuando se llega al tope de lugar en el puente
            ya no pueden pasar mas. */
        public synchronized void entrarCocheNorte() throws InterruptedException{
            
            
            while(enPuente == cantTotal || haciaNorte) this.wait();
            
            haciaSur=true;
            enPuente++;
            if(enPuente == cantTotal) haciaNorte=true;
        }
        /* Al finalizar su recorrido en el puente
            cada coche que sale disminuye la cantidad de autos
            que se encuentran en el puente.
            si esta cantidad llega a 0... pueden pasar los del lado 
            contrario. */
        public synchronized void salirCocheNorte() throws InterruptedException{
            enPuente--;
            if(enPuente==0) haciaSur=false;
            
            System.out.println(Thread.currentThread().getName()+" salió del puente.");
            this.notifyAll();
        }
        
        public synchronized void entrarCocheSur() throws InterruptedException{
            
            
            while(enPuente >= cantTotal || haciaSur ) this.wait();
            
            haciaNorte=true;
            enPuente++;
            if(enPuente == cantTotal) this.haciaSur=true;
            
        }
        
        public synchronized void salirCocheSur() throws InterruptedException{
            enPuente--;
            if(enPuente==0)this.haciaNorte=false;
            
            System.out.println(Thread.currentThread().getName()+" salió del puente.");
            this.notifyAll();
        }
        
    }
    
    private class Autito implements Runnable{
        private GestionaTrafico traficante;
        char tipo;
        public Autito(GestionaTrafico gestor, char tipo){
            this.traficante = gestor;
            this.tipo = tipo;
        }
        
        public void run(){
            try{
                if(tipo=='s'){
                    traficante.entrarCocheSur();
                    IO.println(Thread.currentThread().getName()+" entra a puente.");
                    Thread.sleep(900);
                    traficante.salirCocheSur();
                    //IO.println(Thread.currentThread().getName()+" sale del puente.");
                }
                
                if(tipo=='n'){
                    traficante.entrarCocheNorte();
                    IO.println(Thread.currentThread().getName()+" entra a puente.");
                    Thread.sleep(900);
                    traficante.salirCocheNorte();
                    //IO.println(Thread.currentThread().getName()+" sale del puente.");
                }
                
            }catch(InterruptedException e){e.printStackTrace();}
        }
    }
}