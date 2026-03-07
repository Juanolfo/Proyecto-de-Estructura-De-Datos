package src;

/**
 * Representa una interacción entre dos proteínas dentro del grafo.
 * Actúa como una arista (edge) que conecta dos nodos de tipo proteína.
 * * @author Juan B
 */
public class AristaInteraccion {
    private NodoProteina origen;
    private NodoProteina destino;
    private int resistencia;

    /**
     * Constructor para una nueva interacción biológica.
     * @param origen Nodo inicial.
     * @param destino Nodo receptor.
     * @param resistencia Peso o afinidad de la interacción.
     */
    public AristaInteraccion(NodoProteina origen, NodoProteina destino, int resistencia) {
        this.origen = origen;
        this.destino = destino;
        this.resistencia = resistencia;
    }

    public int getPeso() { return resistencia; }
    public NodoProteina getConectados() { return destino; }
}