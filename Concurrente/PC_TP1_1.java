package src2.Concurrente;
public class PC_TP1_1{
    static class Cliente extends Thread{
    
    private Recurso miRecurso;    
        public Cliente(Recurso miRecurso) {
        this.miRecurso = miRecurso;
        }  
             
        public void run() {
        System.out.println("soy" + Thread.currentThread().getName());
         this.miRecurso.uso();
         try {
         Thread.sleep(2000);
         } catch (InterruptedException e) {
         System.out.println("Error");
         }
        }
    }   

    static class Recurso{
        public Recurso(){}

        public void uso(){
            Thread t = Thread.currentThread();
            System.out.println("en Recurso: Soy "+t.getName());
        }
    }


    public static void main(String[] args) {
    Recurso unRecurso = new Recurso();  

    Cliente juan = new Cliente(unRecurso);
    juan.setName("Juan Lopez");
    Cliente ines = new Cliente(unRecurso);
    ines.setName("Ines Garcia");    
     
    ines.start();
    juan.start();
    unRecurso.uso();

    }
    
}