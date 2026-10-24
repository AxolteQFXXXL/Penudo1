package src.Miscelaneos;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.ImageIcon;
import javax.swing.*;

public class Ventana  {

    public static void main(String[]args){
        JFrame ventana = new JFrame("ventanilla");
        ventana.setSize(500, 350);
        Image icono = Toolkit.getDefaultToolkit().getImage("code.png");
        
        ventana.setIconImage(icono);

        ventana.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        ventana.setResizable(false);
        ventana.setVisible(true);
        

    }
}
