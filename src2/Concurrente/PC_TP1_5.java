package src2.Concurrente;
public class PC_TP1_5{
    static class HiloContador implements Runnable{
    
    private String nombreHilo;    
        public HiloContador(String miRecurso) {
        this.nombreHilo = miRecurso;
        }  
        
        
        public void run() {
        System.out.println("Comenzando: " + this.nombreHilo);
       
         try {
            for(short contar = 0; contar<10; contar++){
                Thread.sleep(0);
                System.out.println("En "+nombreHilo
                    +", el rucuento "+contar
                );
            }
         } catch (InterruptedException e) {
         System.out.println(nombreHilo+" Interrumpido");
         }
         System.out.println("Terminando "+nombreHilo);
        }
    }   


    public static void main(String[] args) {
    System.out.println("Hilo principal iniciando.");
        
    HiloContador hc = new HiloContador("Papita");
    Thread hilo1 = new Thread(hc);
    hilo1.start();
     
    for(short i = 0; i<50; i++) System.out.println(" .");

    try{
        Thread.sleep(200);
    }catch(InterruptedException e){
        System.err.println("Hilo principal interrumpido");
    }
    System.out.println("Hilo principal finalizado");

    }
    
}