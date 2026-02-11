package main;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

/**
 *
 * @author Juan B
 */
public class ControladorBioGraph {
    //private VentanaPrincipal vista;  Falta hacer el codigo de Ventana principal arreglado (sem 8)
    private GrafoPPI modelo;
    private GestorArchivos gestor;

    public void iniciar() {
        this.modelo = new GrafoPPI();
        this.gestor = new GestorArchivos();
       
    }

    public void eventoCargarArchivo() { /* Lógica */ }
    public void eventoEliminarProteina() { /* Lógica */ }
    public void eventoCalcularDijkstra() { /* Lógica */ }
}

