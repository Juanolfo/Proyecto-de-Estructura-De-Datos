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
import java.io.File;
import java.util.List;

public class VentanaPrincipal extends JFrame{
    private JFileChooser selectorArchivos;
    private Object visorGrafo; 
    private JTextField txtEntradaNombre, txtEntradaPeso;
    private PanelControles panelControles;

    public File getArchivoSeleccionado() { return selectorArchivos.getSelectedFile(); }
    public String getNombreProteinaInput() { return txtEntradaNombre.getText(); }
    
    public void mostrarGrafo(GrafoPPI grafo) { /* visual*/ }
    public void mostrarAlerta(String mensaje) { JOptionPane.showMessageDialog(this, mensaje); }
    public void resaltarCamino(List<NodoProteina> listaNodos) { /* visual */ }
}
