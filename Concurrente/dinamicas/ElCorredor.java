package src2.Concurrente.dinamicas;

import java.util.Random;

public class ElCorredor extends Thread{

        static class Corredor implements Runnable{
        private int distRecorrida=0;
        private static Random ra = new Random();
        private String nombre;

        public Corredor(String n){
            this.nombre=n;
        }

        public void run(){
            int a = ra.nextInt(29, 51);

            try{
                while(distRecorrida<=100){
                    distRecorrida+=a;
                    a = ra.nextInt(10);
                    System.out.println("Corredor: "+this.nombre+" ha recorrido "+this.distRecorrida+"Km");
                    Thread.sleep(300);
                }
            }catch(InterruptedException e){
                System.err.println(e);
            }

            System.out.println("Terminando: "+this.nombre+" recorrió "+this.distRecorrida+"Km");
        }

        public int getDistancia() {
           return this.distRecorrida;
        }
        public String getNombre(){ return this.nombre;}
    }

    void main(){
        Corredor[]corredores = new Corredor[100];
        Thread[] hilos = new Thread[100];
        Corredor winners = null;

        int i = 0, puntMax = 0;

        for(short a = 0; a<100; a++){
            Corredor corr = new Corredor("runner "+ a);
            Thread hil = new Thread(corr);
            corredores[a] = corr;
            hilos[a]= hil;
        }

        for(short a = 0; a<100; a++){
            hilos[a].start();
        }

        for(short a = 0; a<100; a++){
            try{
                hilos[a].join();
            }catch(InterruptedException e){
                System.out.println(e);
            }
        }
        
        for(short a = 0; a<100; a++){
            i = corredores[a].getDistancia();
            if(i> puntMax){
                puntMax = i;
                winners = corredores[a];
            }
        }

        System.out.println("Ganó: "+winners.getNombre()+"recorriendo: "+puntMax+"Km");

    }

}
