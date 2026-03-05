package com.mycompany.interfazproyecto;

import java.io.BufferedReader;
import java.io.FileReader;

public class GrafoArtesanal {
    public static final int MAX_NODOS = 100;
    public static final int INFINITO = 999999999;

    public String[] nombres = new String[MAX_NODOS];
    public int[][] matriz = new int[MAX_NODOS][MAX_NODOS];
    public int cantidadNodos = 0;
    public int[] distancias = new int[MAX_NODOS];

    // Busca si un nodo ya existe o lo agrega si es nuevo
    public int obtenerIndice(String nombre) {
        for (int i = 0; i < cantidadNodos; i++) {
            if (nombres[i].equals(nombre)) return i;
        }
        if (cantidadNodos < MAX_NODOS) {
            nombres[cantidadNodos] = nombre;
            cantidadNodos++;
            return cantidadNodos - 1;
        }
        return -1;
    }

    // Lee el archivo CSV y llena la matriz de adyacencia
    public void cargarCSV(String ruta) {
        cantidadNodos = 0; 
        // Inicializar matriz con ceros
        for(int i=0; i<MAX_NODOS; i++)
            for(int j=0; j<MAX_NODOS; j++) matriz[i][j] = 0;

        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    int u = obtenerIndice(partes[0].trim());
                    int v = obtenerIndice(partes[1].trim());
                    int peso = Integer.parseInt(partes[2].trim());
                    if (u != -1 && v != -1) {
                        matriz[u][v] = peso;
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Error al leer CSV: " + e.getMessage());
        }
    }

    // Algoritmo de Dijkstra
    
    public int[] predecesores = new int[MAX_NODOS];
   
    public void dijkstra(String nodoInicio) {
        int inicio = obtenerIndice(nodoInicio);
        if (inicio == -1) return;

        boolean[] visitados = new boolean[MAX_NODOS];
        for (int i = 0; i < cantidadNodos; i++) {
            distancias[i] = INFINITO;
            predecesores[i] = -1;
        }
        distancias[inicio] = 0;

        for (int i = 0; i < cantidadNodos; i++) {
            int min = INFINITO, u = -1;
            for (int j = 0; j < cantidadNodos; j++) {
                if (!visitados[j] && distancias[j] <= min) {
                    min = distancias[j];
                    u = j;
                }
            }
            if (u == -1 || distancias[u] == INFINITO) break;
            visitados[u] = true;

            for (int v = 0; v < cantidadNodos; v++) {
                if (matriz[u][v] > 0) {
                    int peso = matriz[u][v];
                    if (distancias[u] + peso < distancias[v]) {
                        distancias[v] = distancias[u] + peso;
                        predecesores[v] = u;
                    }
                }
            }
        }
    }
    
    public String obtenerCamino (int destino) {
        if (predecesores[destino] == -1) {
                return nombres[destino];
        }
        return obtenerCamino(predecesores[destino]) + " -> " + nombres[destino];
    }
           
}