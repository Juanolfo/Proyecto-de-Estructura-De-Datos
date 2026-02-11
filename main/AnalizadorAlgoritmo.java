/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Juan B
 */
public class AnalizadorAlgoritmo {

    /**
     * Implementación de Dijkstra utilizando solo arreglos nativos.
     * Evalúa el criterio "Detección de Rutas".
     */
    public void dijkstra_RutaCorta(GrafoPPI g, int inicio, int fin) {
        int n = g.getNumVertices();
        int[] distancia = new int[n];
        boolean[] visitado = new boolean[n];
        int[] padre = new int[n];

        for (int i = 0; i < n; i++) {
            distancia[i] = Integer.MAX_VALUE;
            visitado[i] = false;
        }

        distancia[inicio] = 0;

        for (int count = 0; count < n - 1; count++) {
            int u = -1;
            // Encontrar el nodo con la distancia mínima no visitado
            for (int i = 0; i < n; i++) {
                if (!visitado[i] && (u == -1 || distancia[i] < distancia[u])) u = i;
            }

            visitado[u] = true;

            for (int v = 0; v < n; v++) {
                if (!visitado[v] && g.existeArista(u, v) && 
                    distancia[u] != Integer.MAX_VALUE && 
                    distancia[u] + g.getPeso(u, v) < distancia[v]) {
                    distancia[v] = distancia[u] + g.getPeso(u, v);
                    padre[v] = u;
                }
            }
        }
    }

    /**
     * BFS adaptado para detectar complejos (componentes conectados).
     */
    public void bfs_DetectarComplejos(GrafoPPI g) {
        int n = g.getNumVertices();
        boolean[] visitado = new boolean[n];
        int[] cola = new int[n]; // Cola manual con arreglo
        
        for (int i = 0; i < n; i++) {
            if (!visitado[i]) {
                // Iniciar BFS desde i
                int frente = 0, fin = 0;
                cola[fin++] = i;
                visitado[i] = true;
                
                while (frente < fin) {
                    int u = cola[frente++];
                    for (int v = 0; v < n; v++) {
                        if (g.existeArista(u, v) && !visitado[v]) {
                            visitado[v] = true;
                            cola[fin++] = v;
                        }
                    }
                }
            }
        }
    }
}
