package src2.Miscelaneos;

public class CalculoComputacional {
    
    void main(){
        double a,b,n,h;

        /*  Se piden los valores a,b,n */
        a = Double.parseDouble(IO.readln("Ingrese variable a: "));
        b = Double.parseDouble(IO.readln("Ingrese variable b: "));
        n = Double.parseDouble(IO.readln("Ingrese variable n: "));

        /* Calcular el ancho del trapecio apartir de las variables ingresadas */
        h=(b-a)/n;

        System.out.println("El resultado aprox.: "+metodoTrapecio(a,b,n,h));

    }

    public static double metodoTrapecio(double a, double b, double n, double h){
        double result = 0, sum = 0;

        /* sumatoria de los valores repetidos para la funcion */

        for(int i=1; i<n; i++) sum +=calcularFuncion(a+h*i);
        /* resultado de toda la ecuacion */
        result = (h/2)*(calcularFuncion(a)+calcularFuncion(b)+(2*sum));
        return result;
    }

    /* se calcula la funcion cargada en parametro */
    public static double calcularFuncion(double x){
        //return (Math.pow(Math.E, x));
        //return (Math.sqrt(x));
        return (Math.log(x));
    }
}
