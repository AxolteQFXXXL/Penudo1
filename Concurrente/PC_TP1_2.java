package src2.Concurrente;

import java.util.concurrent.ExecutionException;

import tests.jerarquicas.catedraAG.testArbolGen;

public class PC_TP1_2{

    static class Hilo extends Thread{

        public void run() {
            ir();
        }   
        public void ir() {
            hacerMas();
        }

        public void hacerMas() {
            IO.println("En la pila");
        }
    }

    public static void main(String[] args) {
        short a = Short.parseShort(IO.readln("Escribe un numero:"));
        Thread miHilo = new Hilo();
        miHilo.start();

        IO.println("En el main y tu numero:"+a);
    }

    
}