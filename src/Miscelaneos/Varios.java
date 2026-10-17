package src.Miscelaneos;

import java.io.BufferedReader;
import java.io.Console;
import java.io.IOException;
import java.io.InputStreamReader;

public class Varios {
    void main() throws IOException{
        //BufferedReader read = new BufferedReader(new InputStreamReader(System.in));
        Console read = System.console();
        int numero, otroNum, nuevoNum;
        String a, b,c;
        nuevoNum =Integer.parseInt(IO.readln("Coloca cosas 3 veces."));
        numero = Integer.parseInt(System.console().readLine());
        otroNum = Integer.parseInt(read.readLine());
        c= IO.readln("Coloca cosas 3 veces otra vez.");
        a = System.console().readLine();
        b = read.readLine();

        System.out.println(numero +"\n"+otroNum+"\n"+nuevoNum);
        System.out.println(a+"\n"+b+"\n"+c);
    }
}
