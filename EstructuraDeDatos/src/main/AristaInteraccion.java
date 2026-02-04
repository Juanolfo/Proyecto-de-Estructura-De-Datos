/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package main;

/**
 *
 * @author Juan B
 */
public class AristaInteraccion {
    private NodoProteina origen;
    private NodoProteina destino;
    private int resistencia;

    public AristaInteraccion(NodoProteina origen, NodoProteina destino, int resistencia) {
        this.origen = origen;
        this.destino = destino;
        this.resistencia = resistencia;
    }

    public int getPeso() { return resistencia; }
    public NodoProteina getConectados() { return destino; } // Retorna el destino según el grafo
}
