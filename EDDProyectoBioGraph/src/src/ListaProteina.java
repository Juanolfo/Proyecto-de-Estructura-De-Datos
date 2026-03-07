package src;

/**
 * Implementación de una lista simplemente enlazada para objetos {@link NodoProteina}.
 * Gestiona la colección de nodos de la red sin usar librerías externas.
 * * 
 * * @author Juan B
 */
public class ListaProteina {
    private NodoLista inicio;
    private int tamano;

    private class NodoLista {
        NodoProteina dato;
        NodoLista siguiente;
        NodoLista(NodoProteina dato) { this.dato = dato; }
    }

    /**
     * Agrega una nueva proteína al final de la lista enlazada.
     * @param p El {@link NodoProteina} a integrar.
     */
    public void agregar(NodoProteina p) {
        NodoLista nuevo = new NodoLista(p);
        if (inicio == null) inicio = nuevo;
        else {
            NodoLista temp = inicio;
            while (temp.siguiente != null) temp = temp.siguiente;
            temp.siguiente = nuevo;
        }
        tamano++;
    }

    public int getTamano() { return tamano; }
    
    public NodoProteina get(int indice) {
        NodoLista temp = inicio;
        for (int i = 0; i < indice; i++) temp = temp.siguiente;
        return temp.dato;
    }

    /**
     * Elimina una proteína de la lista buscando por su nombre único.
     * @param nombre ID de la proteína a remover.
     */
    public void eliminar(String nombre) {
        if (inicio == null) return;
        if (inicio.dato.getNombre().equals(nombre)) {
            inicio = inicio.siguiente;
            tamano--;
            return;
        }
        NodoLista temp = inicio;
        while (temp.siguiente != null && !temp.siguiente.dato.getNombre().equals(nombre)) {
            temp = temp.siguiente;
        }
        if (temp.siguiente != null) {
            temp.siguiente = temp.siguiente.siguiente;
            tamano--;
        }
    }
}