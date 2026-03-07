package src;

/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
import org.graphstream.graph.*;
import org.graphstream.graph.implementations.SingleGraph;
import org.graphstream.ui.view.*;
import org.graphstream.ui.swing_viewer.SwingViewer;
import javax.swing.*;
import java.awt.*;
import java.io.*;

/**
 * Interfaz Gráfica Principal (Vista) del sistema BioGraph.
 * Integra la visualización de GraphStream con la lógica de GrafoArtesanal.
 * * 
 * * @author Juan B
 */
public class ventanaprin extends javax.swing.JFrame implements ViewerListener {

    public GrafoArtesanal miGrafo = new GrafoArtesanal();
    private AnalizadorAlgoritmo analizador = new AnalizadorAlgoritmo();
    private Graph graphVisual;
    private String nodoSeleccionado = "";

    public ventanaprin() {
        // Configuración crítica de GraphStream
        System.setProperty("org.graphstream.ui", "swing");
        initComponents();
        this.setLocationRelativeTo(null);
        inicializarGrafoVisual();
        
        // REQUERIMIENTO: Cargar archivo inicial automáticamente
        File inicial = new File("maestro (1).csv");
        if(inicial.exists()) {
            cargarDatosCSV(inicial.getAbsolutePath());
        }
    }
    
    private void inicializarGrafoVisual() {
        graphVisual = new SingleGraph("BioGraph");
        // CSS para visualización profesional
        graphVisual.setAttribute("ui.stylesheet", 
    "node { size: 25px; fill-color: #444; text-alignment: center; text-color: white; text-size: 12; } " +
    "node.hub { size: 40px; stroke-mode: plain; stroke-color: gold; stroke-width: 3px; } " +
    "edge { fill-color: #999; size: 1px; }");

        Viewer viewer = new SwingViewer(graphVisual, Viewer.ThreadingModel.GRAPH_IN_GUI_THREAD);
        viewer.enableAutoLayout();
        View view = viewer.addDefaultView(false);
        
        // Lo insertamos en tu jScrollPane2
        jScrollPane2.setViewportView((Component) view);

        // Habilitar clics en nodos
        ViewerPipe pipe = viewer.newViewerPipe();
        pipe.addViewerListener(this);
        new Thread(() -> {
            while (true) {
                pipe.pump();
                try { Thread.sleep(100); } catch (Exception e) {}
            }
        }).start();
    }
    private void cargarDatosCSV(String ruta) {
    // IMPORTANTE: Resetear la lógica del grafo antes de cargar uno nuevo
    miGrafo = new GrafoArtesanal(); 
    
    try (BufferedReader br = new BufferedReader(new FileReader(ruta))) {
        String linea;
        while ((linea = br.readLine()) != null) {
            if(linea.trim().isEmpty()) continue; // Saltar líneas vacías
            String[] partes = linea.split(",");
            if (partes.length == 3) {
                miGrafo.conectar(partes[0].trim(), partes[1].trim(), Integer.parseInt(partes[2].trim()));
            }
        }
        refrescarGrafo();
    } catch (Exception e) {
        JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + e.getMessage());
    }
}
    private void refrescarGrafo() {
    // 1. Limpiar el grafo visual
    graphVisual.clear();
    
    // 2. Aplicar el estilo base (incluyendo etiquetas de aristas)
    graphVisual.setAttribute("ui.stylesheet", 
        "node { size: 20px; text-size: 12; fill-color: #444; } " +
        "edge { text-size: 14; text-color: #222; text-background-mode: rounded-box; " +
        "text-background-color: #EEE; text-alignment: along; }");

    // 3. PRIMER PASO: Crear TODOS los nodos
    // Esto garantiza que cuando busquemos 'P1' para una arista, ya exista.
    for (int i = 0; i < miGrafo.totalNodos; i++) {
        Node n = graphVisual.addNode(miGrafo.nombres[i]);
        n.setAttribute("ui.label", miGrafo.nombres[i]);
    }

    // 4. SEGUNDO PASO: Obtener componentes para colorear (BFS/DFS)
    int[] componentes = analizador.identificarComplejosDFS(miGrafo);
    String[] coloresPaleta = {"#FF5733", "#33FF57", "#3357FF", "#F333FF", "#FFBD33", "#00FFFF"};

    for (int i = 0; i < miGrafo.totalNodos; i++) {
        Node n = graphVisual.getNode(miGrafo.nombres[i]);
        String color = coloresPaleta[componentes[i] % coloresPaleta.length];
        n.setAttribute("ui.style", "fill-color: " + color + ";");
    }

    // 5. TERCER PASO: Crear las aristas con sus pesos
    for (int i = 0; i < miGrafo.totalNodos; i++) {
        for (int j = i + 1; j < miGrafo.totalNodos; j++) {
            if (miGrafo.matriz[i][j] > 0) {
                // El ID debe ser único para cada arista
                String idArista = miGrafo.nombres[i] + "-" + miGrafo.nombres[j];
                
                // Ahora sí, addEdge no fallará porque los nodos i y j ya existen
                Edge e = graphVisual.addEdge(idArista, miGrafo.nombres[i], miGrafo.nombres[j]);
                
                // Mostrar el peso como etiqueta
                e.setAttribute("ui.label", String.valueOf(miGrafo.matriz[i][j]));
            }
        }
    }
}
    private void rutaVisual(String[] ruta) {
    // 1. Limpiar estilos previos de NODOS
    for (Node n : graphVisual) {
        n.setAttribute("ui.style", "fill-color: #444; stroke-mode: none;");
    }

    // 2. Limpiar estilos previos de ARISTAS (Aquí estaba el error)
    graphVisual.edges().forEach(e -> {
    e.setAttribute("ui.style", "fill-color: #999; size: 1px;"); // <-- AQUÍ
});

    // 3. Resaltar los nodos de la ruta
    for (int i = 0; i < ruta.length; i++) {
        Node n = graphVisual.getNode(ruta[i]);
        if (n != null) {
            n.setAttribute("ui.style", "fill-color: #FF0000; size: 35px; stroke-mode: plain; stroke-color: white; stroke-width: 2px;");
        }
        
        // 4. Resaltar las aristas entre los nodos de la ruta
        if (i < ruta.length - 1) {
            // Buscamos la arista por su ID (origen-destino o destino-origen)
            Edge e = graphVisual.getEdge(ruta[i] + "-" + ruta[i+1]);
            if (e == null) e = graphVisual.getEdge(ruta[i+1] + "-" + ruta[i]);
            
            if (e != null) {
                e.setAttribute("ui.style", "fill-color: #FF0000; size: 5px;"); // <-- AQUÍ
            }
        }
    }
    
}
    private void aplicarColoresPorGrupo(int[] componentes, String algoritmo) {
    // 1. Encontrar cuántos complejos hay
    int maxId = 0;
    for (int id : componentes) if (id > maxId) maxId = id;

    // 2. Definir paleta de colores
    String[] colores = {"#FF5733", "#33FF57", "#3357FF", "#F333FF", "#FFBD33", "#00FFFF", "#8E44AD", "#E67E22"};

    // 3. Aplicar color a cada nodo en el visor
    for (int i = 0; i < miGrafo.totalNodos; i++) {
        Node n = graphVisual.getNode(miGrafo.nombres[i]);
        if (n != null) {
            // El color depende del ID del grupo (mismo grupo = mismo color)
            String color = colores[componentes[i] % colores.length];
            n.setAttribute("ui.style", "fill-color: " + color + "; size: 25px; stroke-mode: plain; stroke-color: white;");
        }
    }
    
    txtResultados.setText("Detección mediante " + algoritmo + ":\n" +
                          "Se han identificado " + maxId + " complejos proteicos independientes.");
}

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollBar1 = new javax.swing.JScrollBar();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextArea1 = new javax.swing.JTextArea();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txtRuta = new javax.swing.JTextField();
        btnSeleccionar = new javax.swing.JButton();
        jScrollPane2 = new javax.swing.JScrollPane();
        txtResultados = new javax.swing.JTextArea();
        jLabel4 = new javax.swing.JLabel();
        eliminar = new javax.swing.JButton();
        hub = new javax.swing.JButton();
        agregar = new javax.swing.JButton();
        djkistra = new javax.swing.JButton();
        jLabel5 = new javax.swing.JLabel();
        bfs = new javax.swing.JButton();
        dfs = new javax.swing.JButton();
        btnGuardar = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();

        jTextArea1.setColumns(20);
        jTextArea1.setRows(5);
        jScrollPane1.setViewportView(jTextArea1);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jLabel2.setText("Archivo CSV:");

        txtRuta.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txtRutaActionPerformed(evt);
            }
        });

        btnSeleccionar.setText("Seleccionar Archivo");
        btnSeleccionar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSeleccionarActionPerformed(evt);
            }
        });

        txtResultados.setEditable(false);
        txtResultados.setColumns(20);
        txtResultados.setRows(5);
        jScrollPane2.setViewportView(txtResultados);

        jLabel4.setText("Modificar Grafo");

        eliminar.setText("Eliminar");
        eliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                eliminarActionPerformed(evt);
            }
        });

        hub.setText("Hub");
        hub.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                hubActionPerformed(evt);
            }
        });

        agregar.setText("Agregar");
        agregar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                agregarActionPerformed(evt);
            }
        });

        djkistra.setText("Djkistra");
        djkistra.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                djkistraActionPerformed(evt);
            }
        });

        jLabel5.setText("Detección de Complejos Proteicos");

        bfs.setText("BFS");
        bfs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                bfsActionPerformed(evt);
            }
        });

        dfs.setText("DFS");
        dfs.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                dfsActionPerformed(evt);
            }
        });

        btnGuardar.setText("Guardar CSV");
        btnGuardar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnGuardarActionPerformed(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Segoe UI", 0, 48)); // NOI18N
        jLabel3.setText("BioGraph");

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(26, 26, 26)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(btnSeleccionar)
                            .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 421, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 209, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGroup(javax.swing.GroupLayout.Alignment.LEADING, layout.createSequentialGroup()
                                    .addComponent(jLabel2)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(jLabel1)
                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, 250, javax.swing.GroupLayout.PREFERRED_SIZE)))))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(47, 47, 47)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(eliminar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(hub))
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(agregar)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(djkistra)))
                        .addGap(64, 64, 64)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addComponent(bfs)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(dfs))
                            .addComponent(jLabel5)
                            .addComponent(btnGuardar))))
                .addContainerGap(81, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(24, 24, 24)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(jLabel2)
                        .addComponent(txtRuta, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jLabel1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnSeleccionar)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel3, javax.swing.GroupLayout.PREFERRED_SIZE, 63, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(28, 28, 28)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(eliminar)
                    .addComponent(hub)
                    .addComponent(bfs)
                    .addComponent(dfs))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(agregar)
                    .addComponent(djkistra)
                    .addComponent(btnGuardar))
                .addGap(18, 18, 18)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 285, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(45, 45, 45))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents
        @Override
    public void buttonReleased(String id) { }

    @Override
    public void viewClosed(String viewName) { }

    @Override
    public void mouseOver(String id) { }

    @Override
    public void mouseLeft(String id) { }
    
    private void txtRutaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txtRutaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txtRutaActionPerformed

    private void btnSeleccionarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSeleccionarActionPerformed
    javax.swing.JFileChooser explorador = new javax.swing.JFileChooser();  
    int seleccion = explorador.showOpenDialog(this);

    if (seleccion == javax.swing.JFileChooser.APPROVE_OPTION) {
        String ruta = explorador.getSelectedFile().getAbsolutePath();
        txtRuta.setText(ruta);
        
        // 1. Cargar los datos en la estructura lógica
        miGrafo.cargarCSV(ruta);
        
        // 2. Dibujar el grafo en el panel visual inmediatamente
        refrescarGrafo(); 

        txtResultados.setText("Éxito: Archivo cargado. \nProteínas detectadas: " + miGrafo.totalNodos);
    }// TODO add your handling code here:
    }//GEN-LAST:event_btnSeleccionarActionPerformed

    private void djkistraActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_djkistraActionPerformed
        // TODO add your handling code here:
        String inicio = JOptionPane.showInputDialog(this, "Proteína de Origen:");
    String fin = JOptionPane.showInputDialog(this, "Proteína de Destino:");
    
    if (inicio != null && fin != null) {
        String[] ruta = analizador.dijkstraRuta(miGrafo, inicio, fin);
        if (ruta.length > 0) {
            String resultado = "Ruta más corta:\n" + String.join(" -> ", ruta);
            txtResultados.setText(resultado);
            // Opcional: Resaltar en el grafo visual
            rutaVisual(ruta); 
        } else {
            txtResultados.setText("No existe conexión entre " + inicio + " y " + fin);
        }
    }
    }//GEN-LAST:event_djkistraActionPerformed

    private void hubActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_hubActionPerformed
        // TODO add your handling code here:
     // 1. Obtener el reporte completo
    String reporteCompleto = analizador.obtenerHub(miGrafo);
    
    // 2. Mostrar todo el texto en el JTextArea
    txtResultados.setText("ANÁLISIS DE PROTEÍNA CRÍTICA\n" + reporteCompleto);
    
    // 3. Extraer solo el primer renglón (el nombre) para resaltar en el mapa
    String nombreHub = reporteCompleto.split("\n")[0].trim();
    
    // 4. Limpiar estilos previos para que el Hub destaque solo
    for (Node n : graphVisual) {
        n.setAttribute("ui.style", "fill-color: #444; size: 20px;");
    }

    // 5. Aplicar el estilo de "Hub" al nodo ganador
    Node nodoEnGrafico = graphVisual.getNode(nombreHub);
    if (nodoEnGrafico != null) {
        // Le aplicamos un estilo "explosivo" para que se note
        nodoEnGrafico.setAttribute("ui.style", 
            "fill-color: gold; " +
            "size: 45px; " +
            "stroke-mode: plain; " +
            "stroke-color: #FF4500; " +
            "stroke-width: 4px; " +
            "text-style: bold; " +
            "text-size: 16;");
        
        // Opcional: Pintar sus conexiones de color fuego para ver su alcance
        nodoEnGrafico.enteringEdges().forEach(e -> e.setAttribute("ui.style", "fill-color: #FF4500; size: 3px;"));
        nodoEnGrafico.leavingEdges().forEach(e -> e.setAttribute("ui.style", "fill-color: #FF4500; size: 3px;"));
    }
    }//GEN-LAST:event_hubActionPerformed

    private void eliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_eliminarActionPerformed
        // TODO add your handling code here:
    if (!nodoSeleccionado.equals("")) {
        int confirm = JOptionPane.showConfirmDialog(this, "¿Eliminar proteína " + nodoSeleccionado + "?");
        if (confirm == JOptionPane.YES_OPTION) {
            miGrafo.eliminarProteina(nodoSeleccionado);
            nodoSeleccionado = "";
            txtResultados.setText("Proteína eliminada.");
            refrescarGrafo();
        }
    } else {
        JOptionPane.showMessageDialog(this, "Seleccione una proteína del grafo primero.");
    }    
    }//GEN-LAST:event_eliminarActionPerformed

    private void agregarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_agregarActionPerformed
        // TODO add your handling code here:
        String p1 = JOptionPane.showInputDialog(this, "Nombre de la nueva proteína:");
    
    if (p1 != null && !p1.isEmpty()) {
        p1 = p1.trim(); // Limpiar espacios accidentales

        // 2. Pedir los destinos separados por comas
        String destinosStr = JOptionPane.showInputDialog(this, 
            "¿A qué proteínas desea conectarla?\n(Escriba los nombres separados por comas, ej: P2, P5, P8)");

        if (destinosStr != null && !destinosStr.isEmpty()) {
            // 3. Pedir el peso de las interacciones
            String pesoStr = JOptionPane.showInputDialog(this, "Peso de la interacción (resistencia):");
            
            try {
                int peso = Integer.parseInt(pesoStr);
                
                // 4. Procesar la lista de destinos
                // Dividimos la cadena por comas
                String[] destinos = destinosStr.split(",");
                int conexionesRealizadas = 0;
                StringBuilder reporte = new StringBuilder("Interacciones agregadas:\n");

                for (String d : destinos) {
                    String nombreDestino = d.trim(); // Quitar espacios alrededor del nombre
                    if (!nombreDestino.isEmpty()) {
                        // El método conectar ya se encarga de buscar o crear los índices
                        miGrafo.conectar(p1, nombreDestino, peso);
                        reporte.append("- ").append(p1).append(" <-> ").append(nombreDestino).append("\n");
                        conexionesRealizadas++;
                    }
                }

                // 5. Actualizar la visualización y el texto de resultados
                refrescarGrafo();
                txtResultados.setText(reporte.toString());
                
                if (conexionesRealizadas == 0) {
                    JOptionPane.showMessageDialog(this, "No se ingresaron destinos válidos.");
                }

            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Error: El peso debe ser un número entero.");
            }
        }
    }
    }//GEN-LAST:event_agregarActionPerformed

    private void dfsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_dfsActionPerformed
        // TODO add your handling code here:
                int[] componentes = analizador.identificarComplejosDFS(miGrafo);
    aplicarColoresPorGrupo(componentes, "DFS");
        
    }//GEN-LAST:event_dfsActionPerformed

    private void bfsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_bfsActionPerformed
        // TODO add your handling code here:
        int[] componentes = analizador.identificarComplejosBFS(miGrafo);
    aplicarColoresPorGrupo(componentes, "BFS");
    
    }//GEN-LAST:event_bfsActionPerformed

    private void btnGuardarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnGuardarActionPerformed
        // TODO add your handling code here:
        if (miGrafo.totalNodos == 0) {
        JOptionPane.showMessageDialog(this, "El grafo está vacío.");
        return;
    }

    javax.swing.JFileChooser guardar = new javax.swing.JFileChooser();
    guardar.setSelectedFile(new File("repositorio_actualizado.csv"));
    int seleccion = guardar.showSaveDialog(this);

    if (seleccion == javax.swing.JFileChooser.APPROVE_OPTION) {
        File archivo = guardar.getSelectedFile();
        
        // Usamos un bloque try-with-resources para asegurar que el archivo se cierre
        try (PrintWriter pw = new PrintWriter(new BufferedWriter(new FileWriter(archivo)))) {
            for (int i = 0; i < miGrafo.totalNodos; i++) {
                for (int j = i + 1; j < miGrafo.totalNodos; j++) {
                    if (miGrafo.matriz[i][j] > 0) {
                        // Formato: Proteina1,Proteina2,Peso
                        pw.println(miGrafo.nombres[i] + "," + miGrafo.nombres[j] + "," + miGrafo.matriz[i][j]);
                    }
                }
            }
            pw.flush(); // Forzar la escritura de datos pendientes
            JOptionPane.showMessageDialog(this, "Archivo guardado exitosamente en:\n" + archivo.getAbsolutePath());
        } catch (IOException e) {
            JOptionPane.showMessageDialog(this, "Error crítico al guardar: " + e.getMessage());
        }
    }
    }//GEN-LAST:event_btnGuardarActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(ventanaprin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(ventanaprin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(ventanaprin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(ventanaprin.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new ventanaprin().setVisible(true);
            }
        });
    }
    @Override
public void buttonPushed(String id) {
    this.nodoSeleccionado = id;
    txtResultados.setText("Proteína seleccionada: " + id);

    // Limpiar estilos previos (resetear a color base)
    for (Node n : graphVisual) {
        n.setAttribute("ui.style", "fill-color: #444; stroke-mode: none;");
    }

    // Resaltar el nodo seleccionado
    Node nodoEnGrafico = graphVisual.getNode(id);
    if (nodoEnGrafico != null) {
        nodoEnGrafico.setAttribute("ui.style", "fill-color: #FF1493; stroke-mode: plain; stroke-color: white; stroke-width: 3px;");
        
        // Opcional: Resaltar también sus vecinos directos
        nodoEnGrafico.neighborNodes().forEach(vecino -> {
            vecino.setAttribute("ui.style", "fill-color: #FF69B4;");
        });
    }
}


    
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton agregar;
    private javax.swing.JButton bfs;
    private javax.swing.JButton btnGuardar;
    private javax.swing.JButton btnSeleccionar;
    private javax.swing.JButton dfs;
    private javax.swing.JButton djkistra;
    private javax.swing.JButton eliminar;
    private javax.swing.JButton hub;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JScrollBar jScrollBar1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JTextArea jTextArea1;
    private javax.swing.JTextArea txtResultados;
    private javax.swing.JTextField txtRuta;
    // End of variables declaration//GEN-END:variables
}
