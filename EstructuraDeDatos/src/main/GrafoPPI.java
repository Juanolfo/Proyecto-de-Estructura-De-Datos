/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Juan B
 */
import java.util.*;

public class GrafoPPI {
    private List<NodoProteina> nodos = new ArrayList<>();
    private int[][] matrizAdy;
    private int numVertices;

    public void agregarProteina(NodoProteina p) {
        nodos.add(p);
        numVertices++;
        // redimensionar la matriz
    }

    public void agregarInteraccion(NodoProteina a, NodoProteina b, int peso) {
        // lógica para actualizar matrizAdy
    }

    public void eliminarProteina(String nombre) {
        nodos.removeIf(p -> p.getNombre().equals(nombre));
    }

    public boolean existeArista(int a, int b) {
        return matrizAdy[a][b] != 0;
    }
}
