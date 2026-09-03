package src2.Concurrente;

import java.util.Random;

public class PC_TP2_7 {


    /*clase de auto que utiliza runnable para convertirse en hilo */
     static class Auto implements Runnable{ 
        Random ran = new Random();
        private Surtidor sur;
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
        public Auto(Surtidor sur,String patente, String modelo, String marca, int km){
            this.sur=sur;
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
                while(sur.getLitrosTotal()>0){
                    int kms = ran.nextInt(5,7);
                    if((gas - 7)>7)this.avanzar(kms);
                    else sur.llenar(this);
                }
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

    static class Surtidor{
        private static int litrosTotal = 300;

        /* cuenta con una cant max de gasolina */
        public Surtidor(){}

        /*
        calculamos lo que le falta al tanque del auto para llenarse
        si le quedan litros de combustible lo llena
        sino deja un mensaje correspondiente
        */
        public synchronized void llenar(Auto vehiculo) throws InterruptedException{
            byte aLlenar = (byte) (vehiculo.getMaxGas() - vehiculo.getGas());
            if(litrosTotal>0){vehiculo.addGas(aLlenar);
            litrosTotal-= aLlenar;
            System.out.println("Surtidor a llenado: "+aLlenar+"L de gasolina para: "+Thread.currentThread().getName()+"\n le quedan: "+this.litrosTotal);
            }else{System.out.println("Surtidor no puede abastecer el auto: "+Thread.currentThread().getName());}
            
            Thread.sleep(200);
        }

        public synchronized int getLitrosTotal(){return this.litrosTotal;}
    }

    void main(){
        /*
        declaramos los autos con parametros distintivos para mas personalidad
        declaramos los hilos: con su runnable, y su nombre
        iniciamos todo.
         */
        Surtidor elSur = new Surtidor();
        Auto[] autos = new Auto[5];
        Thread[] hilos = new Thread[5];
        autos[0] = new Auto(elSur, "AXE101", "AliceX", "QF", 0);
        autos[1]= new Auto(elSur, "MAR71N" , "Mars10", "Mitsubishi", 33);
        autos[2]=new Auto(elSur, "YAZ81M", "Yazz2U", "BWM", 17);
        autos[3]=new Auto(elSur, "MATH1Z", "MathMan", "Mazda", 0);
        autos[4]=new Auto(elSur, "ENZ01A", "EnZero", "Ermac", 23);

        for(byte a = 0; a<5; a++) hilos[a] = new Thread(autos[a], autos[a].getModelo());
        
        for(byte a = 0; a<5; a++) hilos[a].start();

        System.out.println("termina main.");

    }
}
