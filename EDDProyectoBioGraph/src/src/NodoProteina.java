package src;

/**
 * Representa un nodo individual en la red de interacción proteína-proteína.
 * Esta clase funciona como la entidad básica (vértice) que almacena la identidad 
 * de una proteína y su posición lógica dentro de las estructuras de datos.
 * * <p>Cada nodo contiene un identificador único (nombre) y un índice entero que 
 * facilita el acceso rápido a la matriz de adyacencia.</p>
 * * @author Juan B
 * @version 1.0
 */
public class NodoProteina {
    private String nombre;
    private int indice;

    /**
     * Constructor para instanciar una nueva proteína en el sistema.
     * @param nombre El nombre descriptivo de la proteína (ID biológico).
     * @param indice La posición entera asignada en la matriz del grafo.
     */
    public NodoProteina(String nombre, int indice) {
        this.nombre = nombre;
        this.indice = indice;
    }

    public String getNombre() { return nombre; }
    public int getIndice() { return indice; }
    
    @Override
    public String toString() { return nombre; }
}