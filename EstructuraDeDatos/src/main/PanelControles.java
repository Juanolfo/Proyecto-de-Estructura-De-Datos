/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Juan B
 */
import javax.swing.*;
import java.awt.event.ActionListener;

public class PanelControles extends JPanel{
    private JButton btnCargar, btnGuardar, btnAgregarArista, btnEliminarNodo, btnEjecutarDijkstra, btnBuscarHubs, btnDetectarComplejos;
    private JTextField txtNombreNodo, txtOrigen, txtDestino, txtPesoIngresado;

    // métodos del diagrama
    public String getProteinaOrigen() { return txtOrigen.getText(); }
    public String getProteinaDestino() { return txtDestino.getText(); }
    public int getPesoIngresado() { return Integer.parseInt(txtPesoIngresado.getText()); }

    public void escucharBotones(ActionListener controlador) {
        // asignar controlador a cada botón
    }
}
