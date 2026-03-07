package src;

/**
 * Representa una Red de Interacción Proteína-Proteína (PPI - Protein-Protein Interaction).
 * Esta clase implementa un grafo no dirigido utilizando una combinación de una 
 * lista vinculada de nodos y una matriz de adyacencia para optimizar la velocidad 
 * de consulta de interacciones.
 * * <p>La estructura permite modelar complejos biológicos donde cada proteína es un nodo
 * y cada interacción física o funcional es una arista con un peso específico.</p>
 * * 
 * * @author Juan B
 * @version 1.0
 */
public class GrafoPPI {

    /** * TDA encargado de almacenar y gestionar la colección de proteínas (nodos) del grafo. 
     */
    private ListaProteina nodos;

    /** * Matriz de adyacencia donde el valor en [i][j] representa el peso de la 
     * interacción entre la proteína i y la j. Un valor de 0 indica ausencia de conexión. 
     */
    private int[][] matrizAdy;

    /** * Contador de proteínas actualmente registradas en el grafo. 
     */
    private int numVertices;

    /** * Capacidad máxima permitida para la matriz de adyacencia en esta implementación. 
     */
    private static final int MAX_PROTEINAS = 50;

    /**
     * Constructor por defecto. Inicializa la lista de proteínas y reserva 
     * el espacio en memoria para la matriz de adyacencia según la capacidad máxima definida.
     */
    public GrafoPPI() {
        this.nodos = new ListaProteina();
        this.matrizAdy = new int[MAX_PROTEINAS][MAX_PROTEINAS];
        this.numVertices = 0;
    }

    /**
     * Añade una nueva proteína a la red biológica siempre que no se supere la capacidad máxima.
     * * @param p El objeto {@link NodoProteina} que representa la proteína a integrar.
     */
    public void agregarProteina(NodoProteina p) {
        if (numVertices < MAX_PROTEINAS) {
            nodos.agregar(p);
            numVertices++;
        }
    }

    /**
     * Registra una interacción (arista) bidireccional entre dos proteínas existentes.
     * Actualiza la matriz de adyacencia de forma simétrica utilizando los índices internos de los nodos.
     * * @param a Proteína de origen.
     * @param b Proteína de destino.
     * @param peso Intensidad o costo de la interacción (utilizado en análisis de rutas metabólicas).
     */
    public void agregarInteraccion(NodoProteina a, NodoProteina b, int peso) {
        matrizAdy[a.getIndice()][b.getIndice()] = peso;
        matrizAdy[b.getIndice()][a.getIndice()] = peso;
    }

    /**
     * Verifica si existe una conexión directa entre dos proteínas identificadas por su índice.
     * * @param a Índice de la primera proteína.
     * @param b Índice de la segunda proteína.
     * @return {@code true} si existe una arista con peso distinto de cero; {@code false} en caso contrario.
     */
    public boolean existeArista(int a, int b) {
        return matrizAdy[a][b] != 0;
    }

    /**
     * Retorna el número total de proteínas registradas en el grafo.
     * * @return Cantidad de vértices activos.
     */
    public int getNumVertices() { 
        return numVertices; 
    }

    /**
     * Obtiene el peso de la interacción entre dos proteínas específicas.
     * * @param i Índice de la proteína A.
     * @param j Índice de la proteína B.
     * @return El valor del peso almacenado en la matriz de adyacencia.
     */
    public int getPeso(int i, int j) { 
        return matrizAdy[i][j]; 
    }

    /**
     * Proporciona acceso a la lista interna de proteínas para su iteración o búsqueda.
     * * @return La instancia de {@link ListaProteina} asociada al grafo.
     */
    public ListaProteina getNodos() {
        return nodos; 
    }
}