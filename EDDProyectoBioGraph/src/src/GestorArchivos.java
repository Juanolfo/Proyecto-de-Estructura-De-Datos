package src;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */


/**
 *
 * @author Juan B
 */
import java.io.File; // file se permite para manejo de archivos, no afecta la logica del TDA.

public class GestorArchivos {
    public GrafoPPI cargarGrafo(File ruta) {
        return new GrafoPPI(); 
    }

    public boolean guardarGrafo(File ruta, GrafoPPI grafo) {
        return true;
    }
}

