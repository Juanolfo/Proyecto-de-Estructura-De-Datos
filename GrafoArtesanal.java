/* SI NETBEANS TE DA ERROR EN LA PRIMERA LINEA, 
   ESCRIBE AQUÍ: package tareagrafos;  
   (O el nombre de paquete que tenías antes de borrar todo) */

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class GrafoArtesanal {

    static final int MAX_NODOS = 100;
    static final int INFINITO = 999999999;

    String[] nombres;
    int[][] matriz;
    int cantidadNodos;

    public GrafoArtesanal() {
        this.nombres = new String[MAX_NODOS];
        this.matriz = new int[MAX_NODOS][MAX_NODOS];
        this.cantidadNodos = 0;

        for (int i = 0; i < MAX_NODOS; i++) {
            for (int j = 0; j < MAX_NODOS; j++) {
                matriz[i][j] = 0;
            }
        }
    }

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

    public void cargarCSV(String ruta) {
        System.out.println("--- INTENTANDO LEER: " + ruta + " ---");
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(","); 
                if (partes.length == 3) {
                    int u = obtenerIndice(partes[0].trim());
                    int v = obtenerIndice(partes[1].trim());
                    int peso = Integer.parseInt(partes[2].trim());
                    if (u != -1 && v != -1) matriz[u][v] = peso;
                }
            }
            System.out.println("--- ARCHIVO LEIDO CORRECTAMENTE ---");
        } catch (IOException e) {
            System.out.println("ERROR CRITICO: No encuentro el archivo.");
            System.out.println("Asegurate que el archivo " + ruta + " este en la carpeta del proyecto.");
        }
    }

    public void bfs(String nodoInicio) {
        int inicio = obtenerIndice(nodoInicio);
        if (inicio == -1) { System.out.println("Inicio no existe"); return; }

        System.out.println("\n>>> BFS desde " + nodoInicio);
        boolean[] visitados = new boolean[MAX_NODOS];
        int[] cola = new int[MAX_NODOS]; 
        int frente = 0, finalCola = 0;

        visitados[inicio] = true;
        cola[finalCola++] = inicio;

        while (frente < finalCola) {
            int actual = cola[frente++];
            System.out.print(nombres[actual] + " -> ");
            for (int i = 0; i < cantidadNodos; i++) {
                if (matriz[actual][i] > 0 && !visitados[i]) {
                    visitados[i] = true;
                    cola[finalCola++] = i;
                }
            }
        }
        System.out.println("FIN");
    }

    public void dijkstra(String nodoInicio) {
        int inicio = obtenerIndice(nodoInicio);
        if (inicio == -1) return;

        System.out.println("\n>>> Dijkstra desde " + nodoInicio);
        int[] distancias = new int[MAX_NODOS];
        boolean[] visitados = new boolean[MAX_NODOS];

        for (int i = 0; i < cantidadNodos; i++) distancias[i] = INFINITO;
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
                    if (distancias[u] + peso < distancias[v]) distancias[v] = distancias[u] + peso;
                }
            }
        }

        for (int i = 0; i < cantidadNodos; i++) {
            String d = (distancias[i] == INFINITO) ? "Inalcanzable" : "" + distancias[i];
            System.out.println(nombres[i] + "\t Costo: " + d);
        }
    }

    public static void main(String[] args) {
        GrafoArtesanal g = new GrafoArtesanal();
        // IMPORTANTE: EL NOMBRE DEBE SER EXACTO AL DE TU ARCHIVO
        g.cargarCSV("maestro (2).csv"); 
        g.bfs("P1");
        g.dijkstra("P1");
    }
}    