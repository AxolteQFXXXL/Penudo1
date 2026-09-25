package src.Concurrente;

public class PC_TP3_1 {

    /* Con el primer recurse se puede ver que vuelve al objeto inaccesible una vez que entra un hilo
    Por otro lado, en el segundo recurse vuelve inaccesible solamente la sentencia dentro del synchronized
    
    --Una correccion podria ser no utilizar this en el metodo decrementar del segundo recurse, debido
    a que bloqueas a todo el objeto de ser accedido y no solo al valor con el que deseas trabajar.
    */

    void main(){
        ContadorSincronico counter = new ContadorSincronico();
        ContadorObjetoSincronico otroCounter = new ContadorObjetoSincronico();
        HiloContador [] contadores = new HiloContador[5];
        Thread [] hilos = new Thread[5];

        for(byte a = 0; a< contadores.length ; a++){
            contadores[a] = new HiloContador(counter, otroCounter);
            hilos[a] = new Thread(contadores[a],"contador "+a);
            hilos[a].start();
        }
        //String algo = System.console().readLine();
        System.out.println("Termina main ");
    }
    
    public class ContadorSincronico {
        private int valor = 0;  
        
        public ContadorSincronico(){}
        public synchronized void incrementar() {
            System.out.println(Thread.currentThread().getName()+" entra normal");
            valor++;
            System.out.println(Thread.currentThread().getName()+" sale normal");
        }   
        public void decrementar() {
            System.out.println(Thread.currentThread().getName()+" entra normal");
            valor--;
            System.out.println(Thread.currentThread().getName()+" sale normal");
        }

        public synchronized int getValor() {
            return valor;
        }
    }

    public class ContadorObjetoSincronico {
        private int valor = 0;  
        
        public ContadorObjetoSincronico(){}
        public void incrementar() {
            System.out.println(Thread.currentThread().getName()+" entra raro");
            synchronized ((Integer) valor) {
                valor++;
            }
            System.out.println(Thread.currentThread().getName()+" sale raro");
        }   

        public void decrementar() { System.out.println(Thread.currentThread().getName()+" entra raro");
            synchronized (this) {
                valor--;
            }
            System.out.println(Thread.currentThread().getName()+" sale raro");
        }

        public synchronized int getValor() {
            return valor;
        }
    }

    private class HiloContador implements Runnable{
        private ContadorSincronico conter;
        private ContadorObjetoSincronico conterSync;
        public HiloContador(ContadorSincronico newConter, ContadorObjetoSincronico newConterSync){ 
            this.conter=newConter;
            this.conterSync=newConterSync;
        }

        public void run(){
            try{
                for(byte a = 0; a<7; a++){
                    if(a<3) conter.incrementar();
                    else if(a>5)conter.incrementar();
                    else conter.decrementar();
                    System.out.println("Contador ="+conter.getValor());
                }
                System.out.println("ahora con ContadorObjetoSincronico");

                for(byte a = 0; a<7; a++){
                    if(a<3) conterSync.incrementar();
                    else if(a>5)conterSync.incrementar();
                    else conterSync.decrementar();
                    System.out.println("Contador ="+conterSync.getValor());
                }
            }catch(Exception e){System.out.println(e);}
        }

    }

    /* Para sig. punto del TP3.
    a) En el orden P1 -> P3 -> P2 | P4
     primero p1 luego p3, luego p2 y p4 se ejecutan se manera concurrente debido a que ya cuenta con 
     los semaforos correspondientes.
     
     b) Si Sem2 se inicializa en 0, no se podria ejecutar el programa, ya no existen permisos acordes
     
     c) Podria ocurrir mas concurrencia ya que P1 y P3 serian concurrentes entre si y luego P2 y P4*/

}
