package src;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
/**
 *
 * @author Juan B
 */
public class AnalizadorAlgoritmo {

    // 1. Identificación de Hubs (Centralidad de Grado)
    public String obtenerHub(GrafoArtesanal g) {
    if (g.totalNodos == 0) return "Error: Grafo vacío.";

    int maxConexiones = -1;
    int indiceHub = -1;

    for (int i = 0; i < g.totalNodos; i++) {
        int conexionesActuales = 0;
        for (int j = 0; j < g.totalNodos; j++) {
            if (g.matriz[i][j] > 0) conexionesActuales++;
        }

        if (conexionesActuales > maxConexiones) {
            maxConexiones = conexionesActuales;
            indiceHub = i;
        }
    }

    if (indiceHub != -1) {
        // Calculamos la importancia relativa (porcentaje de la red que toca)
        double importancia = (double) maxConexiones / (g.totalNodos - 1) * 100;
        
        return g.nombres[indiceHub] + "\n" +
               "-----------------------------------\n" +
               "• Grado de Centralidad: " + maxConexiones + " conexiones directas.\n" +
               "• Importancia en la Red: " + String.format("%.2f", importancia) + "%\n" +
               "• Rol Biológico: Proteína esencial (Diana Terapéutica).\n" +
               "• Nota: Su eliminación fragmentaría significativamente el complejo.";
    }
    
    return "No se pudo identificar un Hub central.";
}

    // 2. Ruta Metabólica Corta (Dijkstra)
    public String[] dijkstraRuta(GrafoArtesanal g, String inicio, String fin) {
        int s = -1, d = -1;
        for(int i=0; i<g.totalNodos; i++){
            if(g.nombres[i].equals(inicio)) s = i;
            if(g.nombres[i].equals(fin)) d = i;
        }
        if(s == -1 || d == -1) return new String[0];

        int[] dist = new int[g.totalNodos];
        int[] prev = new int[g.totalNodos];
        boolean[] visitado = new boolean[g.totalNodos];

        for(int i=0; i<g.totalNodos; i++) { dist[i] = 99999; prev[i] = -1; }
        dist[s] = 0;

        for(int i=0; i<g.totalNodos; i++) {
            int u = -1;
            for(int j=0; j<g.totalNodos; j++) {
                if(!visitado[j] && (u == -1 || dist[j] < dist[u])) u = j;
            }
            if(dist[u] == 99999) break;
            visitado[u] = true;

            for(int v=0; v<g.totalNodos; v++) {
                if(g.matriz[u][v] > 0 && dist[u] + g.matriz[u][v] < dist[v]) {
                    dist[v] = dist[u] + g.matriz[u][v];
                    prev[v] = u;
                }
            }
        }
        // Reconstrucción artesanal de la ruta
        int count = 0;
        for(int i=d; i != -1; i = prev[i]) count++;
        String[] ruta = new String[count];
        int curr = d;
        for(int i=count-1; i>=0; i--) { ruta[i] = g.nombres[curr]; curr = prev[curr]; }
        return ruta;
    }

    // 3. Detección de Complejos (Componentes Conexos con BFS)
    public int[] identificarComplejos(GrafoArtesanal g) {
        int[] ids = new int[g.totalNodos];
        boolean[] visitado = new boolean[g.totalNodos];
        int idActual = 1;

        for (int i = 0; i < g.totalNodos; i++) {
            if (!visitado[i]) {
                int[] cola = new int[g.totalNodos];
                int frente = 0, fin = 0;
                cola[fin++] = i;
                visitado[i] = true;
                while (frente < fin) {
                    int u = cola[frente++];
                    ids[u] = idActual;
                    for (int v = 0; v < g.totalNodos; v++) {
                        if (g.matriz[u][v] > 0 && !visitado[v]) {
                            visitado[v] = true;
                            cola[fin++] = v;
                        }
                    }
                }
                idActual++;
            }
        }
        return ids;
    }
    public void ejecutarDFS(GrafoArtesanal g, int nodoInicio, boolean[] visitados) {
    visitados[nodoInicio] = true;
    // Aquí podrías imprimir o guardar el recorrido
    for (int v = 0; v < g.totalNodos; v++) {
        if (g.matriz[nodoInicio][v] > 0 && !visitados[v]) {
            ejecutarDFS(g, v, visitados);
        }
    }
}

// Método para el botón DFS que identifica componentes usando profundidad
public int[] identificarComplejosDFS(GrafoArtesanal g) {
        int[] ids = new int[g.totalNodos];
        boolean[] visitados = new boolean[g.totalNodos];
        int idActual = 1;

        for (int i = 0; i < g.totalNodos; i++) {
            if (!visitados[i]) {
                dfsRecursivoGrupo(g, i, visitados, ids, idActual);
                idActual++;
            }
        }
        return ids;
    }

    private void dfsRecursivoGrupo(GrafoArtesanal g, int u, boolean[] visitados, int[] ids, int id) {
        visitados[u] = true;
        ids[u] = id;
        for (int v = 0; v < g.totalNodos; v++) {
            if (g.matriz[u][v] > 0 && !visitados[v]) {
                dfsRecursivoGrupo(g, v, visitados, ids, id);
            }
        }
    }
    public int[] identificarComplejosBFS(GrafoArtesanal g) {
        int[] ids = new int[g.totalNodos];
        boolean[] visitados = new boolean[g.totalNodos];
        int idActual = 1;

        for (int i = 0; i < g.totalNodos; i++) {
            if (!visitados[i]) {
                // Iniciar una nueva exploración de componente
                int[] cola = new int[g.totalNodos];
                int frente = 0, fin = 0;
                
                cola[fin++] = i;
                visitados[i] = true;
                
                while (frente < fin) {
                    int u = cola[frente++];
                    ids[u] = idActual;
                    
                    for (int v = 0; v < g.totalNodos; v++) {
                        if (g.matriz[u][v] > 0 && !visitados[v]) {
                            visitados[v] = true;
                            cola[fin++] = v;
                        }
                    }
                }
                idActual++; // Siguiente complejo encontrado
            }
        }
        return ids;
    }

private void marcarGrupoRecursivo(GrafoArtesanal g, int u, boolean[] visitados, int[] ids, int id) {
    visitados[u] = true;
    ids[u] = id; // Aquí se asigna el número de grupo
    for (int v = 0; v < g.totalNodos; v++) {
        if (g.matriz[u][v] > 0 && !visitados[v]) {
            marcarGrupoRecursivo(g, v, visitados, ids, id);
        }
    }
}
public int[] obtenerRecorridoBFS(GrafoArtesanal g, int inicioIdx) {
    int[] orden = new int[g.totalNodos];
    for(int i=0; i<orden.length; i++) orden[i] = -1; // -1 significa no visitado
    
    boolean[] visitado = new boolean[g.totalNodos];
    int[] cola = new int[g.totalNodos];
    int frente = 0, fin = 0, contador = 0;

    cola[fin++] = inicioIdx;
    visitado[inicioIdx] = true;

    while (frente < fin) {
        int u = cola[frente++];
        orden[u] = contador++; // Guardamos el orden de visita
        
        for (int v = 0; v < g.totalNodos; v++) {
            if (g.matriz[u][v] > 0 && !visitado[v]) {
                visitado[v] = true;
                cola[fin++] = v;
            }
        }
    }
    return orden;
}

// Recorrido DFS desde un nodo específico (Recursivo o con Pila)
public int[] obtenerRecorridoDFS(GrafoArtesanal g, int inicioIdx) {
    int[] orden = new int[g.totalNodos];
    // Inicializamos con -1 para saber qué nodos NO fueron visitados
    for (int i = 0; i < orden.length; i++) orden[i] = -1;
    
    boolean[] visitado = new boolean[g.totalNodos];
    
    // Creamos el arreglo contador ANTES de la llamada
    int[] contador = {0}; 
    
    // Llamamos al método pasando el arreglo
    dfsRecursivo(g, inicioIdx, visitado, orden, contador);
    
    return orden;
}

private void dfsRecursivo(GrafoArtesanal g, int u, boolean[] visitado, int[] orden, int[] contador) {
    visitado[u] = true;
    orden[u] = contador[0]; // Asignamos el número de orden actual
    contador[0]++;          // Incrementamos el valor dentro del arreglo
    
    for (int v = 0; v < g.totalNodos; v++) {
        // Si hay conexión y no ha sido visitado
        if (g.matriz[u][v] > 0 && !visitado[v]) {
            dfsRecursivo(g, v, visitado, orden, contador);
        }
    }
}
    
}

