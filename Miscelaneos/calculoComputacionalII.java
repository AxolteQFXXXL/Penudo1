package src2.Miscelaneos;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

public class calculoComputacionalII {
    
    void main(){
        double[][]  matriz= new double[9][9];
        //{{2, 2.5, 3, 3.5, 4},{-0.2239, -0.484, -0.2601, -0.3801, -0.387}};
        cargarArchiva(matriz);
        double a = matriz[0][0];
        double b = matriz[0][matriz[0].length-1];
        double h, result, sum;
        int n = matriz[0].length-1;
        h=(b-a)/n;
        result = 0;
        sum = 0;

        for(int i = 1; i<n; i++) sum+=matriz[1][i];

        result = (h/2)*(matriz[1][0] + matriz[1][n]+2*(sum));

        System.out.println("resultado es: "+result);
    }

    public void cargarArchiva(double[][] mat){
        try(FileReader fr = new FileReader("src2/Miscelaneos/carga.txt");
            BufferedReader br = new BufferedReader(fr)){
            Short[] pos = {0,0};
            String line;
            while((line = br.readLine())!=null){
                StringTokenizer tk = new StringTokenizer(line, ";");

                LaCargaMatriz(pos,mat, tk);
            }

        }catch (IOException io){
            String msg = "Error al leer el archiv: "+io.getMessage();
            System.err.println(msg);
        }
    }

    private void LaCargaMatriz(Short[] pos,double[][]mat, StringTokenizer st){
        Short i=0;
        while(st.hasMoreTokens()){
            double a = Double.parseDouble(st.nextToken());
            mat[i][pos[i]] = a;
            i++;
        }
        pos[0]++;
        pos[1]++;   
    }
}