/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Juan B
 */
public class ListaProteina {
    private NodoLista inicio;
    private int tamano;

    private class NodoLista {
        NodoProteina dato;
        NodoLista siguiente;
        NodoLista(NodoProteina dato) { this.dato = dato; }
    }

    public void agregar(NodoProteina p) {
        NodoLista nuevo = new NodoLista(p);
        if (inicio == null) inicio = nuevo;
        else {
            NodoLista temp = inicio;
            while (temp.siguiente != null) temp = temp.siguiente;
            temp.siguiente = nuevo;
        }
        tamano++;
    }

    public int getTamano() { return tamano; }
    
    public NodoProteina get(int indice) {
        NodoLista temp = inicio;
        for (int i = 0; i < indice; i++) temp = temp.siguiente;
        return temp.dato;
    }

    public void eliminar(String nombre) {
        if (inicio == null) return;
        if (inicio.dato.getNombre().equals(nombre)) {
            inicio = inicio.siguiente;
            tamano--;
            return;
        }
        NodoLista temp = inicio;
        while (temp.siguiente != null && !temp.siguiente.dato.getNombre().equals(nombre)) {
            temp = temp.siguiente;
        }
        if (temp.siguiente != null) {
            temp.siguiente = temp.siguiente.siguiente;
            tamano--;
        }
    }
}
