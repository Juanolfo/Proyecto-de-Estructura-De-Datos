/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Juan B
 */
public class NodoProteina {
    private String nombre;
    private int indice;

    public NodoProteina(String nombre, int indice) {
        this.nombre = nombre;
        this.indice = indice;
    }

    public String getNombre() { 
        return nombre; 
    }
    public int getIndice() { 
        return indice; 
    }
    
    @Override
    public String toString() { 
        return nombre; 
    }
}