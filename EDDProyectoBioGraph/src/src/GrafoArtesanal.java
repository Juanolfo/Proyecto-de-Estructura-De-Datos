package src;

import java.io.*;

/**
 * Red PPI implementada mediante estructuras de datos básicas (Matriz y Arreglos).
 * Gestiona la lógica de conexiones y persistencia CSV de forma manual.
 * * @author Juan B
 */
public class GrafoArtesanal {
    public static final int MAX = 100;
    public String[] nombres = new String[MAX];
    public int[][] matriz = new int[MAX][MAX];
    public int totalNodos = 0;

    /**
     * Busca el índice de una proteína o la crea si hay espacio disponible.
     * @param nombre Nombre de la proteína.
     * @return Índice en el arreglo o -1 si está lleno.
     */
    public int buscarOCrearIndice(String nombre) {
        for (int i = 0; i < totalNodos; i++) {
            if (nombres[i].equals(nombre)) return i;
        }
        if (totalNodos < MAX) {
            nombres[totalNodos] = nombre;
            return totalNodos++;
        }
        return -1;
    }

    public void conectar(String p1, String p2, int peso) {
        int i = buscarOCrearIndice(p1);
        int j = buscarOCrearIndice(p2);
        if (i != -1 && j != -1) {
            matriz[i][j] = peso;
            matriz[j][i] = peso;
        }
    }

    /**
     * Elimina una proteína y reorganiza la matriz para mantener la integridad.
     * @param nombre Proteína a eliminar.
     */
    public void eliminarProteina(String nombre) {
        int idx = -1;
        for (int i = 0; i < totalNodos; i++) {
            if (nombres[i].equals(nombre)) { idx = i; break; }
        }
        if (idx == -1) return;

        for (int i = idx; i < totalNodos - 1; i++) nombres[i] = nombres[i + 1];
        for (int i = 0; i < totalNodos; i++) {
            for (int j = idx; j < totalNodos - 1; j++) matriz[i][j] = matriz[i][j + 1];
        }
        for (int i = idx; i < totalNodos - 1; i++) {
            for (int j = 0; j < totalNodos; j++) matriz[i][j] = matriz[i + 1][j];
        }
        totalNodos--;
    }

    public void cargarCSV(String ruta) {
        try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] partes = linea.split(",");
                if (partes.length == 3) {
                    conectar(partes[0].trim(), partes[1].trim(), Integer.parseInt(partes[2].trim()));
                }
            }
        } catch (Exception e) {
            System.out.println("Error al cargar: " + e.getMessage());
        }
    }
}