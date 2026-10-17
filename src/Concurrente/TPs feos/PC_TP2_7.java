package src.Concurrente;

import java.util.Random;
import java.util.Timer;

public class PC_TP2_7 {
    private static int contador = 500;

    /*clase de auto que utiliza runnable para convertirse en hilo */
     private static class Auto implements Runnable{
        Random ran = new Random();
        private Planificador sup;
        private String patente;
        private String modelo;
        private String marca;
        private int km_record;
        private static byte maxGas = 70;
        private byte gas;

        /*
        *@Param: surtidor, patente, modelo, marca, km
        se inicia con una cant maxGas para todos los autos
          */
        public Auto(Planificador sur,String patente, String modelo, String marca, int km){
            this.sup=sur;
            this.patente=patente;
            this.modelo=modelo;
            this.marca=marca;
            this.km_record=km;
            this.gas=maxGas;

        }

        @Override
        public void run() {
            /*
            mientras los litros disponibles del surtidor sean validos, los autos avanzan
            consumiendo combustible
            Si les queda combustible siguen conduciendo
            sino el surtidor les llena el tanque
            para finalmente apagarse
             */
            try{
                do{
                    int kms = ran.nextInt(5,7);
                    if((gas - kms)>7)this.avanzar(kms);
                    else sup.quieroLlenar(this);
                }while(contador>0);
            }catch(InterruptedException ex){ System.out.println("Error en "+ex);}
            finally{
                System.out.println(this.modelo+" se ha apagado");
            }

        }

        public void addGas(byte num){ this.gas+=num;}

        /* avanzar requiere aumentar el kilometraje con los km recorridos
        y luego vaciar su respectiva cant de combustible */
        public void avanzar(int val){
            this.km_record += val;
            this.gas -= 7;
            System.out.println(this.modelo+" esta avanzando "+val+"km");
        }

        public byte getMaxGas(){ return Auto.maxGas;}
        public byte getGas(){return this.gas;}
        public String getModelo(){return this.modelo;}


    }

    private static class Surtidor{
        private static int litrosTotal = 300;
        private boolean enServicio = true;
        /* cuenta con una cant max de gasolina */
        public Surtidor(){}

        /*
        calculamos lo que le falta al tanque del auto para llenarse
        si le quedan litros de combustible lo llena
        sino deja un mensaje correspondiente
        */
        public synchronized void llenar(Auto vehiculo) throws InterruptedException{
            byte aLlenar = (byte) (vehiculo.getMaxGas() - vehiculo.getGas());
            if(litrosTotal>7){vehiculo.addGas(aLlenar);
            litrosTotal-= aLlenar;
            System.out.println("Surtidor a llenado: "+aLlenar+"L de gasolina para: "+Thread.currentThread().getName()+"\n le quedan: "+this.litrosTotal);
            }else{
                enServicio = false;
                System.out.println("Surtidor no puede abastecer el auto: "+Thread.currentThread().getName());
            }

            Thread.sleep(200);
        }

        public synchronized int getLitrosTotal(){return this.litrosTotal;}
        public synchronized void recargar(int recarga) throws InterruptedException{ System.out.println("Se esta Cargando gasolina");
            for(byte a=0; a<10; a++) {
                this.litrosTotal+=recarga;
                Thread.currentThread().sleep(30);
            }
            enServicio= true;
            Thread.currentThread().sleep(340);
        }
        public boolean getEstado(){ return enServicio;}
    }

    private static class Camion implements Runnable{
        Planificador sup;
        public Camion(Planificador sup){
            this.sup=sup;
        }

        public void run(){
            try{
                do{
                  this.sup.quieroCargar(15);
                }while(contador>0);
            }catch(InterruptedException ex){ System.out.println(ex);}
            finally{ System.out.println("camion termina jornada.");}
        }
    }

    private static class Planificador{
        private Surtidor sur;
        public Planificador(Surtidor sur){ this.sur=sur;}

        public synchronized void quieroLlenar(Auto carro) throws InterruptedException{ if(this.sur.getEstado()) this.sur.llenar(carro);}
        public synchronized void quieroCargar(int carga) throws InterruptedException{ if(!this.sur.getEstado())this.sur.recargar(carga);}
        public synchronized boolean VerEstado(){ return this.sur.getEstado();}
    }

    private static class Temporizador extends Thread{
        public Temporizador(){}

        public void run(){
            try{
                while(contador>0){
                    contador-=10;
                    Thread.sleep(100);
                    System.out.println(contador+"...");
                }
            }catch(Exception e){System.out.println(e);}
        }

    }


    void main(){
        /*
        declaramos los autos con parametros distintivos para mas personalidad
        declaramos los hilos: con su runnable, y su nombre
        iniciamos todo.
         */

        Surtidor elSur = new Surtidor();
        Planificador elSup = new Planificador(elSur);
        Auto[] autos = new Auto[5];
        Thread[] hilos = new Thread[6];
        autos[1]= new Auto(elSup, "MAR71N" , "Mars10", "Mitsubishi", 33);
        autos[0] = new Auto(elSup, "AXE101", "AliceX", "QF", 0);
        autos[2]=new Auto(elSup, "YAZ81M", "Yazz2U", "BWM", 17);
        autos[4]=new Auto(elSup, "ENZ01A", "EnZero", "Ermac", 23);
        autos[3]=new Auto(elSup, "MATH1Z", "MathMan", "Mazda", 0);
        Camion camion = new Camion(elSup);
        Temporizador temp = new Temporizador();

        for(byte a = 0; a<5; a++) hilos[a] = new Thread(autos[a], autos[a].getModelo());
        hilos[5] = new Thread(camion, "camionKun");

        for(byte a = 0; a<6; a++) hilos[a].start();
        temp.start();

        System.out.println("termina main.");

    }
}
