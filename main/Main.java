/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Juan B
 */
// se utilizan librerias 'java.io.File', 'java.util.List' 'javax.swing' y 'java.awt.event.ActionListener'
// la documentación de cada una esta en la pagina de oracle, por lo que hay que usar VPN, yo uso el navegador Brave que viene con VPN integrada :D
// java.io.File = https://docs.oracle.com/javase/8/docs/api/java/io/File.html
// java.util.List = https://docs.oracle.com/javase/8/docs/api/java/util/List.html
// javax.swing = https://docs.oracle.com/javase/8/docs/api/javax/swing/package-summary.html
// java.awt.event.ActionListener = https://docs.oracle.com/javase/8/docs/api/java/awt/event/ActionListener.html
public class Main {
    public static void main(String[] args) {
        // Inicio del programa
        ControladorBioGraph app = new ControladorBioGraph();
        app.iniciar();
    }
}
