
public class Parcial_PC_1{

    void main(){
        Pizarra board = new Pizarra();
        //System.out.println("Cuantos Hilos interactuan con pizarra?: ");
        short cantidad = 5;
        Hilo [] hilos = new Hilo[cantidad];
        String[] palabras = {"axel", "lexa", "martian", "matian", "yazman"};
        for(byte a = 4; a>=0; a--){
            hilos[a] = new Hilo(palabras[a], board);
        }
        for(byte a =0; a<cantidad; a++) hilos[a].start();

        System.out.println("Termina el main.");

    }
    public class Pizarra{
        public Pizarra(){}

        public synchronized void ocupar(Hilo unHilo) throws InterruptedException{
            System.out.println(unHilo.getTexto());
            System.out.flush();
            Thread.sleep(200);
        }
    }

    public class Hilo extends Thread{
        private String unTexto;
        private Pizarra board;
        public Hilo(String txt, Pizarra b){
            this.unTexto=txt;
            this.board=b;
        }

        public void escribir(){System.out.println(unTexto);}
        public String getTexto(){return unTexto;}

        public void run(){
            try{
                for(byte a=0; a<5; a++){
                    this.board.ocupar(this);
                }
                System.out.println(currentThread().getName()+" ya cumplio su cometido.");
            }catch(InterruptedException e){System.out.println( e);}
        }
    }
}
