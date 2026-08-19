package src2.Concurrente;

public class PC_TP1_3 {
    static class ThreadEjemplo extends Thread{
        public ThreadEjemplo(String str){super(str);}

        public void run(){
            for (int i = 0; i < 10 ; i++) System.out.println(i + " " + getName());

            IO.println("Termina thread "+ getName());
        }
    }

    public static void main(String[] args) {
        new ThreadEjemplo("Platon Teton").start();
        new ThreadEjemplo("Heracles Zeuses").start();

        IO.println("Termina Thread main");
    }
}
