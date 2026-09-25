package src.Concurrente;
import java.util.concurrent.Semaphore;

public class PC_TP3_3 {
    
    void main(){
        Impresora impresora = new Impresora();
        ProcessoTal[] procesos = new ProcessoTal[3];
        procesos[0]= new ProcessoTal("Quiero hacerlo ", impresora, (short) 1);
        procesos[1] = new ProcessoTal("con el chico lindo ", impresora, (short) 2);
        procesos[2] = new ProcessoTal("llamado Axel", impresora, (short) 3);
        Thread [] hilos = new Thread[3];

        for(byte a=0; a<3; a++) hilos[a] = new Thread(procesos[a], "Proceso"+a);
        
        for(byte a=0; a<3; a++) hilos[a].start();
        
    }
    
    private class ProcessoTal implements Runnable{
        String mensaje;
        Impresora imp;
        short num;
        public ProcessoTal(String texto, Impresora imp, short num){
            this.mensaje = texto; this.imp = imp; this.num=num;
        }

        public void run(){
            
            imp.imprimir(mensaje, (short)(num-1)); 
            
        }

    }

    private class Impresora{
        private Semaphore [] sems;
        public Impresora(){ 
            sems = new Semaphore[3];
            sems[0] = new Semaphore(1);
            sems[1] = new Semaphore(0);
            sems[2] = new Semaphore(0);
        }
        
        public void imprimir(String text, short sem){ 
            if(sems[sem].tryAcquire()){
                
                System.out.println(text);
                
                if(sem ==2 )sems[sem-2].release();
                else sems[sem+1].release();
                
            }
            
        }
    }
}
