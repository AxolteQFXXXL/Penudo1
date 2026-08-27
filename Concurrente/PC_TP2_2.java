package src2.Concurrente;

public class PC_TP2_2 {
        
        class Energia{
            int valor;
            public Energia(int val){ this.valor=val;}

            public synchronized void drenar(int valor) throws InterruptedException{ 
                
                if(this.valor >0) this.valor-=valor;
                else System.out.println("sin energia.");
                System.out.println(Thread.currentThread()+" esta drenando, energia:"+this.valor);
            }
            public synchronized void sanar(int valor) throws InterruptedException{ this.valor+=valor;
                System.out.println(Thread.currentThread()+" esta sanando, energia:"+this.valor);
            }

            public int getValor(){  return this.valor;}

        }

        static class darkCreature implements Runnable{
            Energia energia;
            public darkCreature(Energia valor){
                this.energia = valor;
            }

            public void run(){
                try{
                    for(short i = 0; i<10; i++) {energia.drenar(3);
                        Thread.sleep(120);
                    }                
                }catch (InterruptedException ex){ System.out.println("Error en: "+ex);}
                System.out.println("Creatura termina de drenar.");
            }
        }
        
        static class Sanador implements Runnable{
            Energia energia;
            
            public Sanador(Energia valor){
                this.energia = valor;
            }
            
            public void run(){
                try{
                    for(short i=0; i<10; i++) {energia.sanar(3);
                        Thread.sleep(100);
                    }
                }catch(InterruptedException ex){System.out.println("Error en: "+ex);}
                System.out.println("Sanador termina de sanar.");
            }
        }
        
        void main(){
        var energia = new Energia(10);
        darkCreature hilo1 = new darkCreature(energia);
        Sanador hilo2 = new Sanador(energia);
        Thread creatura = new Thread(hilo1, "creatura");
        Thread sanador = new Thread(hilo2, "sanador");
        creatura.start();
        sanador.start();
        
            System.out.println("Main termina: "+energia.getValor());
        }
}