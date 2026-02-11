/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Juan B
 */
public class GrafoPPI {
    private ListaProteina nodos;    // TDA 
    private int[][] matrizAdy;     // Matriz de adyacencia robusta
    private int numVertices;
    private static final int MAX_PROTEINAS = 50; //capacidad escalable 

    public GrafoPPI() {
        this.nodos = new ListaProteina();
        this.matrizAdy = new int[MAX_PROTEINAS][MAX_PROTEINAS];
        this.numVertices = 0;
    }

    public void agregarProteina(NodoProteina p) {
        if (numVertices < MAX_PROTEINAS) {
            nodos.agregar(p);
            numVertices++;
        }
    }

    public void agregarInteraccion(NodoProteina a, NodoProteina b, int peso) {
        // acceso por índice de matriz
        matrizAdy[a.getIndice()][b.getIndice()] = peso;
        matrizAdy[b.getIndice()][a.getIndice()] = peso;
    }

    public boolean existeArista(int a, int b) {
        return matrizAdy[a][b] != 0;
    }
    
    // getters
    public int getNumVertices() { 
        return numVertices; 
    }
    public int getPeso(int i, int j) { 
        return matrizAdy[i][j]; 
    }
    public ListaProteina getNodos() {
        return nodos; 
    }
}
