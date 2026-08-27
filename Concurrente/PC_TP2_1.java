package src2.Concurrente;

import java.lang.System.Logger;
import java.lang.System.Logger.Level;

public class PC_TP2_1 {
    static class CuentaBanco{
        private int balance;
        public CuentaBanco(int balance) {
        this.balance = balance;
        }

        public int getBalance() {
        return this.balance;
        }

        public  void retiroBancario(int retiro) {
        this.balance -= retiro;
        }
        
        public synchronized void hacerRetiro(int cantidad) throws InterruptedException {
            if (this.getBalance() >= cantidad) {
            System.out.println (Thread.currentThread().getName() +
            " está realizando un retiro de: " + cantidad);
            
            Thread.sleep(600);
            this.retiroBancario(cantidad);
            System.out.println(Thread.currentThread().getName() +
            ": Retiro realizado");
            
            System.out.println(Thread.currentThread().getName() +
            ": Los fondos son de: " + this.getBalance());

            } else {
            System.out.println("No hay suficiente dinero en la cuenta"
            + " para realizar el retiro Sr. "
            + Thread.currentThread().getName());
            
            System.out.println("Su saldo actual es de: "
            + this.getBalance());
            Thread.sleep(600);

            }
        }
    }

    class VerificarCuenta implements Runnable{
        private CuentaBanco cb;
        public VerificarCuenta(CuentaBanco unaCuenta) {
        this.cb = unaCuenta;
        }
        

        public void run() {
            for (int i = 0; i <= 3; i++) {
                try {
                    cb.hacerRetiro(10);
                    if(cb.getBalance() < 0)  System.out.println("Cuenta está sobregirada");

                } catch (InterruptedException ex) {
                    System.out.println("Error en :" +ex);
                    
                 
                }
            }
        }

    }

    void main(){
        CuentaBanco cb = new CuentaBanco(50);
        VerificarCuenta vcL = new VerificarCuenta(cb);
        VerificarCuenta vcJ = new VerificarCuenta(cb);
        Thread lucas = new Thread(vcL, "Lucas");
        Thread jere = new Thread(vcJ, "Jere");
        lucas.start();
        jere.start();
    }
    
}
