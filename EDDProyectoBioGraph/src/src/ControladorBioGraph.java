package src;

/**
 * Clase controladora principal del sistema BioGraph.
 * Actúa como intermediaria entre la interfaz de usuario (Vista) y la lógica de 
 * negocio (Modelo), gestionando el flujo de datos y la respuesta a eventos.
 * * <p>Esta clase orquestra las operaciones sobre la red de interacción proteína-proteína (PPI),
 * permitiendo la carga de datos, manipulación de nodos y ejecución de análisis.</p>
 * * @author Juan B
 * @version 1.0
 */
public class ControladorBioGraph {
    
    /**
     * El modelo de datos que representa la red de interacciones biológicas.
     */
    private GrafoPPI modelo;
    
    /**
     * El componente encargado de la persistencia y lectura de datos desde archivos externos (CSV).
     */
    private GestorArchivos gestor;

    /**
     * Inicializa los componentes principales del sistema.
     * Crea las instancias necesarias del modelo y del gestor de archivos para 
     * preparar la aplicación antes de su interacción con el usuario.
     */
    public void iniciar() {
        this.modelo = new GrafoPPI();
        this.gestor = new GestorArchivos();
    }

    /**
     * Gestiona el evento de carga de datos.
     * Invoca al {@link GestorArchivos} para leer el repositorio de proteínas y 
     * actualiza el {@link GrafoPPI} con la información recuperada.
     */
    public void eventoCargarArchivo() { 
        /* Lógica para abrir explorador de archivos y poblar el grafo */ 
    }

    /**
     * Gestiona la eliminación de un nodo proteico de la red.
     * Actualiza la estructura del grafo eliminando tanto la proteína seleccionada 
     * como todas sus interacciones (aristas) asociadas.
     */
    public void eventoEliminarProteina() { 
        /* Lógica para remover nodo del modelo y refrescar vista */ 
    }

    /**
     * Ejecuta el análisis de ruta metabólica más corta entre dos puntos.
     * Utiliza el algoritmo de Dijkstra para determinar el camino con menor 
     * resistencia dentro de la red PPI.
     */
    public void eventoCalcularDijkstra() { 
        /* Lógica para obtener parámetros de la vista y ejecutar algoritmo */ 
    }
}