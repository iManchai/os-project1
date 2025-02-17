/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/GUIForms/JFrame.java to edit this template
 */
package Interfaz;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import Classes.Cpu;
import Classes.Process;
import DataStructures.ListaSimple;
import DataStructures.Nodo;
import Planificacion.PlanificadorFCFS;
import Planificacion.PlanificadorHrrn;
import Planificacion.PlanificadorRR;
import Planificacion.PlanificadorSJF;
import Planificacion.PlanificadorSRT;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.awt.BorderLayout;
import java.util.concurrent.Semaphore;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableModel;
import org.jfree.chart.ChartPanel;

/**
 *
 * @author manch
 */
public class InterfazInicial extends javax.swing.JFrame implements Runnable {

    public static boolean isRunning = false; // Estado de la simulacion
    private CpuLabels cpu1Labels; // Etiquetas (Labels) de la interfaz gráfica para mostrar información del CPU 1.
    private CpuLabels cpu2Labels; // Etiquetas (Labels) de la interfaz gráfica para mostrar información del CPU 2.
    private CpuLabels cpu3Labels; // Etiquetas (Labels) de la interfaz gráfica para mostrar información del CPU 3.
    private DefaultTableModel modeloTablaFinalizadoSistema; // Modelo de la tabla para mostrar procesos finalizados en el sistema.
    private DefaultTableModel modeloTablaListos; // Modelo de la tabla para mostrar procesos en estado "Listo" (Ready).
    private DefaultTableModel modeloTablaBloqueados; // Modelo de la tabla para mostrar procesos en estado "Bloqueado" (Blocked).
    private DefaultTableModel modeloTablaFinalizadoSistemaCP1; // Modelo de la tabla para mostrar procesos finalizados en el CPU 1.
    private DefaultTableModel modeloTablaFinalizadoSistemaCP2; // Modelo de la tabla para mostrar procesos finalizados en el CPU 2.
    private DefaultTableModel modeloTablaFinalizadoSistemaCP3; // Modelo de la tabla para mostrar procesos finalizados en el CPU 3.
    private int utilizacionSistema = 0; // Porcentaje de utilización del sistema (puede ser un promedio o valor actual).
    ListaSimple listaListos = new ListaSimple(); // Lista de procesos en estado "Listo" (Ready).
    ListaSimple listaBloqueados = new ListaSimple(); // Lista de procesos en estado "Bloqueado" (Blocked).
    ListaSimple listaTotalProcesos = new ListaSimple(); // Lista de todos los procesos creados en el sistema.
    Semaphore semaphoreList = new Semaphore(1); // Semáforo para controlar el acceso a las listas de procesos.
    int cantidadCpus = 2; // Cantidad de CPUs en el sistema.
    int velocidadReloj = 1000; // Velocidad del reloj de la CPU (en milisegundos).
    int procesosCreados = 1; // Contador del número total de procesos creados en el sistema.
    int contadorGlobal = 0; // Contador global del sistema (Ciclos de reloj).
    String planificadorEscogido = "FCFS"; // Algoritmo de planificación de procesos seleccionado (en este caso, FCFS - First-Come, First-Served).
    Semaphore semaphoreCpu1 = new Semaphore(1); // Semáforo para controlar el acceso al CPU 1.
    Semaphore semaphoreCpu2 = new Semaphore(1); // Semáforo para controlar el acceso al CPU 2.
    Semaphore semaphoreCpu3 = new Semaphore(1); // Semáforo para controlar el acceso al CPU 3.
    Cpu cpu1 = new Cpu(1, listaListos, semaphoreList, semaphoreCpu1, velocidadReloj, this); // Objeto CPU que representa el CPU 1.
    Cpu cpu2 = new Cpu(2, listaListos, semaphoreList, semaphoreCpu2, velocidadReloj, this); // Objeto CPU que representa el CPU 2.
    Cpu cpu3 = new Cpu(3, listaListos, semaphoreList, semaphoreCpu3, velocidadReloj, this); // Objeto CPU que representa el CPU 3.
    DefaultCategoryDataset dataset = new DefaultCategoryDataset(); // Conjunto de datos para el gráfico
    private ChartPanel frame;

    // Getters and Setters
    public int getUtilizacionSistema() {
        return utilizacionSistema;
    }

    public void setUtilizacionSistema(int utilizacionSistema) {
        this.utilizacionSistema = utilizacionSistema;
    }

    public DefaultCategoryDataset getDataset() {
        return dataset;
    }

    public void setDataset(DefaultCategoryDataset dataset) {
        this.dataset = dataset;
    }

    public String getPlanificadorEscogido() {
        return planificadorEscogido;
    }

    public void setPlanificadorEscogido(String planificadorEscogido) {
        this.planificadorEscogido = planificadorEscogido;
    }

    public int getContadorGlobal() {
        return contadorGlobal;
    }

    public void setContadorGlobal(int contadorGlobal) {
        this.contadorGlobal = contadorGlobal;
    }

    public void setIDProcessCPU1(String id) {
        IDProcessCPU1.setText(id);
    }

    public void setEstateProcessCPU1(String text) {
        EstateProcessCPU1.setText(text);
    }

    public void setMarProcessCPU1(String text) {
        MarProcessCPU1.setText(text);
    }

    public void setPcCPU1(String Pc) {
        PcCPU1.setText(Pc);
    }

    public DefaultTableModel getModeloTablaBloqueados() {
        return modeloTablaBloqueados;
    }

    public void setModeloTablaBloqueados(DefaultTableModel modeloTablaBloqueados) {
        this.modeloTablaBloqueados = modeloTablaBloqueados;
    }

    public DefaultTableModel getModeloTablaFinalizadoSistema() {
        return modeloTablaFinalizadoSistema;
    }

    public void setModeloTablaFinalizadoSistema(DefaultTableModel modeloTablaFinalizadoSistema) {
        this.modeloTablaFinalizadoSistema = modeloTablaFinalizadoSistema;
    }

    public DefaultTableModel getModeloTablaListos() {
        return modeloTablaListos;
    }

    public void setModeloTablaListos(DefaultTableModel modeloTablaListos) {
        this.modeloTablaListos = modeloTablaListos;
    }

    JFreeChart chart = ChartFactory.createLineChart(
            "Progreso del Proceso", // Título del gráfico
            "Ciclos", // Etiqueta del eje X
            "Valor", // Etiqueta del eje Y
            dataset, // Dataset
            PlotOrientation.VERTICAL, // Orientación del gráfico
            true, // Incluir leyenda
            true, // Incluir tooltips
            false // Incluir URLs
    );

    // FUNCIONES AUXILIARES // 
    /**
     * Agrega información sobre un proceso finalizado a la tabla correspondiente
     * según el número de CPU.
     *
     * @param cpu El número de CPU (1, 2 o 3) al que se asignó el proceso.
     * @param procesosCreados El número total de procesos creados hasta el
     * momento.
     * @param nombreProceso El nombre del proceso finalizado.
     * @param programCounter El valor final del contador de programa (PC) del
     * proceso.
     * @param status El estado final del proceso (por ejemplo, "Finalizado").
     * @param totalInstructions El número total de instrucciones ejecutadas por
     * el proceso.
     */
    public void AgregarListaFinalizadosCpu(int cpu, int procesosCreados, String nombreProceso, int programCounter, String status, int totalInstructions, int mar, String tipo) {
        // Verifica a qué CPU se le asignó el proceso y actualiza la tabla correspondiente.
        if (cpu == 1) {
            // Actualiza la tabla de procesos finalizados para el CPU 1.
            actualizarTablasAñadir(modeloTablaFinalizadoSistemaCP1, procesosCreados, nombreProceso, programCounter, mar, tipo, status, totalInstructions);
        }
        if (cpu == 2) {
            // Actualiza la tabla de procesos finalizados para el CPU 2.
            actualizarTablasAñadir(modeloTablaFinalizadoSistemaCP2, procesosCreados, nombreProceso, programCounter, mar, tipo, status, totalInstructions);
        }
        if (cpu == 3) {
            // Actualiza la tabla de procesos finalizados para el CPU 3.
            actualizarTablasAñadir(modeloTablaFinalizadoSistemaCP3, procesosCreados, nombreProceso, programCounter, mar, tipo, status, totalInstructions);
        }
    }

    /**
     * Elimina un proceso de la tabla de procesos listos por su ID.
     *
     * @param modeloTablaListos La tabla (modelo) de procesos listos de la cual
     * se va a eliminar el proceso.
     * @param procesosCreados El ID del proceso que se va a eliminar.
     */
    public void actualizarTablasBorrar(DefaultTableModel modeloTablaListos, int procesosCreados) {
        // Itera sobre las filas de la tabla de procesos listos.
        for (int i = 0; i < modeloTablaListos.getRowCount(); i++) {
            // Obtiene el ID del proceso de la fila actual.
            int idProceso = (int) modeloTablaListos.getValueAt(i, 0);

            // Compara el ID del proceso actual con el ID del proceso que se va a eliminar.
            if (idProceso == procesosCreados) {
                // Se encontró el proceso. Elimina la fila correspondiente de la tabla.
                modeloTablaListos.removeRow(i);
                return; // Importante: Salir del método después de eliminar la fila.
            }
        }
    }

    /**
     * Actualiza la tabla de procesos finalizados con la información del
     * proceso. Este método se encarga de añadir una nueva fila a la tabla con
     * los datos del proceso finalizado.
     *
     * @param modeloTabla La tabla (modelo) que se va a actualizar.
     * @param procesosCreados El número total de procesos creados hasta el
     * momento.
     * @param nombreProceso El nombre del proceso finalizado.
     * @param programCounter El valor final del contador de programa (PC) del
     * proceso.
     * @param status El estado final del proceso (por ejemplo, "Finalizado").
     * @param totalInstructions El número total de instrucciones ejecutadas por
     * el proceso.
     */
    public void actualizarTablasAñadir(DefaultTableModel modeloTabla, int procesosCreados, String nombreProceso, int programCounter, int mar, String tipo, String status, int totalInstructions) {
        // No necesitas buscar si ya existe, simplemente añade la nueva fila
        Object[] nuevaFila = new Object[]{
            procesosCreados,
            nombreProceso,
            programCounter,
            mar,
            tipo,
            status,
            totalInstructions
        };
        modeloTabla.addRow(nuevaFila);
    }

    /**
     * Actualiza la información de un proceso en la tabla de procesos listos.
     * Busca el proceso por su ID y actualiza su contador de programa (PC),
     * estado y número total de instrucciones.
     *
     * @param modeloTablaListos La tabla (modelo) de procesos listos que se va a
     * actualizar.
     * @param procesosCreados El ID del proceso que se va a actualizar.
     * @param nombreProceso El nombre del proceso (actualmente no se utiliza,
     * pero se incluye por consistencia).
     * @param programCounter El nuevo valor del contador de programa (PC) del
     * proceso.
     * @param status El nuevo estado del proceso (por ejemplo, "Listo",
     * "Ejecutando", "Bloqueado").
     * @param totalInstructions El nuevo número total de instrucciones
     * ejecutadas por el proceso.
     */
    public void actualizarInterfaz(DefaultTableModel modeloTablaListos, int procesosCreados, String nombreProceso, int programCounter, String status, int totalInstructions) {
        // Itera sobre las filas de la tabla de procesos listos.
        for (int i = 0; i < modeloTablaListos.getRowCount(); i++) {
            // Obtiene el ID del proceso de la fila actual.
            int idProceso = (int) modeloTablaListos.getValueAt(i, 0);

            // Compara el ID del proceso actual con el ID del proceso que se va a actualizar.
            if (idProceso == procesosCreados) {
                // Actualiza el contador de programa (PC).
                modeloTablaListos.setValueAt(programCounter, i, 2);
                // Actualiza el estado del proceso.
                modeloTablaListos.setValueAt(status, i, 3);
                // Actualiza el número total de instrucciones.
                modeloTablaListos.setValueAt(totalInstructions, i, 4);
                return; // Sale del método una vez que se ha actualizado el proceso.
            }
        }
    }

    /**
     * Actualiza la información mostrada en la interfaz gráfica para un CPU
     * específico. Este método se ejecuta en el hilo de eventos de Swing para
     * garantizar la correcta actualización de los componentes de la interfaz.
     *
     * @param cpuId El ID del CPU (1, 2 o 3).
     * @param id El ID del proceso que se está ejecutando en el CPU.
     * @param estado El estado del proceso.
     * @param pc El valor actual del contador de programa (PC).
     * @param name El nombre del proceso.
     * @param longitud La longitud o duración del proceso (puede ser en
     * instrucciones o tiempo).
     */
    public void actualizarInterfazCPU(int cpuId, String id, String estado, String pc, String mar, String name, String longitud) {
        // Obtiene las etiquetas correspondientes al CPU.
        CpuLabels labels = switch (cpuId) {
            case 1 ->
                cpu1Labels;
            case 2 ->
                cpu2Labels;
            case 3 ->
                cpu3Labels;
            default ->
                null; // Manejo de ID de CPU inválido.
        };
        // Actualiza las etiquetas en el hilo de eventos de Swing.
        if (labels != null) {
            CpuLabels finalLabels = labels;
            SwingUtilities.invokeLater(() -> {
                finalLabels.idLabel.setText(id);
                finalLabels.nameLabel.setText(name);
                finalLabels.estateLabel.setText(estado);
                finalLabels.pcLabel.setText(pc);
                finalLabels.marLabel.setText(mar);
                finalLabels.longitud.setText(longitud);

                // Establece el color del texto según el nombre del proceso.
                Color color = (finalLabels.nameLabel.getText().equalsIgnoreCase("OS")
                        || finalLabels.nameLabel.getText().equalsIgnoreCase("system32"))
                        ? Color.CYAN : Color.BLACK;

                finalLabels.idLabel.setForeground(color);
                finalLabels.nameLabel.setForeground(color);
                finalLabels.estateLabel.setForeground(color);
                finalLabels.pcLabel.setForeground(color);
                finalLabels.marLabel.setForeground(color);
                finalLabels.longitud.setForeground(color);
            });
        } else {
            System.err.println("ID de CPU inválido: " + cpuId);
        }
    }

    public static boolean validarCampoEntero(JTextField textField, String nombreCampo) {
        try {
            int valor = Integer.parseInt(textField.getText());
            if (valor > 0) {
                return true;
            } else {
                JOptionPane.showMessageDialog(null, "El campo: " + nombreCampo + " debe ser un entero positivo mayor que 0", "ERROR", JOptionPane.ERROR_MESSAGE);
                return false;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(null, "El campo: " + nombreCampo + " debe ser un entero", "ERROR", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public static boolean validarCampoStringNoVacio(JTextField textField, String nombreCampo) {
        String texto = textField.getText().trim(); // Obtener el texto y eliminar espacios en blanco al inicio y al final

        if (texto.isEmpty()) {
            // Mostrar un mensaje de error o realizar alguna acción (p. ej., cambiar el foco)
            javax.swing.JOptionPane.showMessageDialog(null, "El campo '" + nombreCampo + "' no puede estar vacío.", "Error", javax.swing.JOptionPane.ERROR_MESSAGE);
            textField.requestFocus(); // Opcional: enfocar el campo para que el usuario lo corrija
            return false; // Indica que la validación falló
        }

        return true; // Indica que la validación fue exitosa
    }

    public static boolean validarVelocidadReloj(JTextField textField) {
        int velocidadReloj = Integer.parseInt(textField.getText());

        if (velocidadReloj <= 5000 && velocidadReloj >= 200) {
            return true;
        } else {
            JOptionPane.showMessageDialog(null, "El valor de la velocidad de reloj debe ser entre 200 y 5000", "Error", JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    /**
     * Creates new form InterfazPrincipal
     */
    public InterfazInicial() {
        initComponents();
        modeloTablaFinalizadoSistema = (DefaultTableModel) EndedProcessesSystem.getModel();
        modeloTablaListos = (DefaultTableModel) TablaListaDeListos.getModel();
        modeloTablaBloqueados = (DefaultTableModel) TablaBloqueados.getModel();
        modeloTablaFinalizadoSistemaCP1 = (DefaultTableModel) EndedProcessesCPU1.getModel();
        modeloTablaFinalizadoSistemaCP2 = (DefaultTableModel) EndedProcessesCPU2.getModel();
        modeloTablaFinalizadoSistemaCP3 = (DefaultTableModel) EndedProcessesCPU3.getModel();
        cpu1Labels = new CpuLabels(IDProcessCPU1, NameProcessCPU1, EstateProcessCPU1, PcCPU1, MarProcessCPU1, LongitudProcessCPU1);
        cpu2Labels = new CpuLabels(IDProcessCPU2, NameProcessCPU2, EstateProcessCPU2, PcCPU2, MarProcessCPU2, LongitudProcessCPU2);
        cpu3Labels = new CpuLabels(IDProcessCPU3, NameProcessCPU3, EstateProcessCPU3, PcCPU3, MarProcessCPU3, LongitudProcessCPU3);

        // Setear labels al inicializar el JFrame
        CurrentCpus.setText(Integer.toString(cantidadCpus));
        CurrentCycle.setText(Integer.toString(velocidadReloj));
        GlobalCounter.setText(Integer.toString(contadorGlobal));
        CurrentPolicy.setText(planificadorEscogido);

        frame = new ChartPanel(chart);

        jPanel5.setLayout(new BorderLayout());
        jPanel5.add(frame, BorderLayout.NORTH);

        Thread t = new Thread(this);
        t.start();

    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jPanel1 = new javax.swing.JPanel();
        TituloPrincipal = new javax.swing.JLabel();
        IniciarButton = new javax.swing.JButton();
        CargarButton = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        GuardarConfigButton = new javax.swing.JButton();
        FinalizarSimulacionButton = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JSeparator();
        SeccionProcesos = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator3 = new javax.swing.JSeparator();
        jLabel4 = new javax.swing.JLabel();
        LongitudTextField = new javax.swing.JTextField();
        jLabel5 = new javax.swing.JLabel();
        TipoProcesoSelect = new javax.swing.JComboBox<>();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        CiclosLlamarIOTextField = new javax.swing.JTextField();
        CiclosTerminarIOTextField = new javax.swing.JTextField();
        jLabel11 = new javax.swing.JLabel();
        NombreProcesoTextField = new javax.swing.JTextField();
        jSeparator4 = new javax.swing.JSeparator();
        AgregarProcesoButton = new javax.swing.JButton();
        SeccionConfiguracion = new javax.swing.JPanel();
        jLabel8 = new javax.swing.JLabel();
        jSeparator5 = new javax.swing.JSeparator();
        jLabel9 = new javax.swing.JLabel();
        PoliticaPlanificacionSelect = new javax.swing.JComboBox<>();
        jSeparator6 = new javax.swing.JSeparator();
        jLabel2 = new javax.swing.JLabel();
        CantidadCpuSelect = new javax.swing.JComboBox<>();
        jLabel3 = new javax.swing.JLabel();
        CicloTextField = new javax.swing.JTextField();
        AplicarConfiguraciónButton = new javax.swing.JButton();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jSeparator8 = new javax.swing.JSeparator();
        CurrentCpus = new javax.swing.JLabel();
        CurrentCycle = new javax.swing.JLabel();
        CurrentPolicy = new javax.swing.JLabel();
        SeccionPrincipal = new javax.swing.JPanel();
        CPU1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jPanel6 = new javax.swing.JPanel();
        IdProceso1 = new javax.swing.JLabel();
        NombreProceso1 = new javax.swing.JLabel();
        EstadoProceso1 = new javax.swing.JLabel();
        PC1 = new javax.swing.JLabel();
        MAR1 = new javax.swing.JLabel();
        IDProcessCPU1 = new javax.swing.JLabel();
        NameProcessCPU1 = new javax.swing.JLabel();
        EstateProcessCPU1 = new javax.swing.JLabel();
        MarProcessCPU1 = new javax.swing.JLabel();
        PcCPU1 = new javax.swing.JLabel();
        Longitud1 = new javax.swing.JLabel();
        LongitudProcessCPU1 = new javax.swing.JLabel();
        CPU2 = new javax.swing.JPanel();
        jLabel15 = new javax.swing.JLabel();
        jPanel7 = new javax.swing.JPanel();
        IdProceso2 = new javax.swing.JLabel();
        NombreProceso2 = new javax.swing.JLabel();
        EstadoProceso2 = new javax.swing.JLabel();
        PC2 = new javax.swing.JLabel();
        MAR2 = new javax.swing.JLabel();
        IDProcessCPU2 = new javax.swing.JLabel();
        NameProcessCPU2 = new javax.swing.JLabel();
        EstateProcessCPU2 = new javax.swing.JLabel();
        MarProcessCPU2 = new javax.swing.JLabel();
        PcCPU2 = new javax.swing.JLabel();
        Longitud2 = new javax.swing.JLabel();
        LongitudProcessCPU2 = new javax.swing.JLabel();
        CPU3 = new javax.swing.JPanel();
        jLabel16 = new javax.swing.JLabel();
        jPanel8 = new javax.swing.JPanel();
        IdProceso3 = new javax.swing.JLabel();
        NombreProceso3 = new javax.swing.JLabel();
        EstadoProceso3 = new javax.swing.JLabel();
        PC3 = new javax.swing.JLabel();
        MAR3 = new javax.swing.JLabel();
        MarProcessCPU3 = new javax.swing.JLabel();
        EstateProcessCPU3 = new javax.swing.JLabel();
        NameProcessCPU3 = new javax.swing.JLabel();
        IDProcessCPU3 = new javax.swing.JLabel();
        PcCPU3 = new javax.swing.JLabel();
        Longitud3 = new javax.swing.JLabel();
        LongitudProcessCPU3 = new javax.swing.JLabel();
        jSeparator9 = new javax.swing.JSeparator();
        SeccionListaListos = new javax.swing.JPanel();
        jLabel17 = new javax.swing.JLabel();
        jSeparator10 = new javax.swing.JSeparator();
        jScrollPane9 = new javax.swing.JScrollPane();
        TablaListaDeListos = new javax.swing.JTable();
        SeccionListaBloqueados = new javax.swing.JPanel();
        jLabel18 = new javax.swing.JLabel();
        jSeparator11 = new javax.swing.JSeparator();
        jScrollPane8 = new javax.swing.JScrollPane();
        TablaBloqueados = new javax.swing.JTable();
        jSeparator12 = new javax.swing.JSeparator();
        jLabel19 = new javax.swing.JLabel();
        GlobalCounter = new javax.swing.JLabel();
        TabsProcesosCulminados = new javax.swing.JTabbedPane();
        ProcessEndedSystem = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        EndedProcessesSystem = new javax.swing.JTable();
        ProcessEndedCpu1 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        EndedProcessesCPU1 = new javax.swing.JTable();
        ProcessEndedCpu2 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        EndedProcessesCPU2 = new javax.swing.JTable();
        ProcessEndedCpu3 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        EndedProcessesCPU3 = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        TituloPrincipal.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        TituloPrincipal.setText("PROYECTO SISTEMAS OPERATIVOS #1");
        TituloPrincipal.setToolTipText("");

        IniciarButton.setBackground(new java.awt.Color(91, 204, 20));
        IniciarButton.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        IniciarButton.setForeground(new java.awt.Color(0, 0, 0));
        IniciarButton.setText("INICIAR SIMULACIÓN");
        IniciarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                IniciarButtonActionPerformed(evt);
            }
        });

        CargarButton.setBackground(new java.awt.Color(91, 90, 90));
        CargarButton.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        CargarButton.setForeground(new java.awt.Color(185, 185, 185));
        CargarButton.setText("CARGAR CONFIGURACIÓN");
        CargarButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CargarButtonActionPerformed(evt);
            }
        });

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        GuardarConfigButton.setBackground(new java.awt.Color(0, 139, 252));
        GuardarConfigButton.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        GuardarConfigButton.setForeground(new java.awt.Color(0, 0, 0));
        GuardarConfigButton.setText("GUARDAR CONFIGURACIÓN");
        GuardarConfigButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                GuardarConfigButtonActionPerformed(evt);
            }
        });

        FinalizarSimulacionButton.setBackground(new java.awt.Color(217, 51, 39));
        FinalizarSimulacionButton.setFont(new java.awt.Font("Segoe UI", 1, 10)); // NOI18N
        FinalizarSimulacionButton.setForeground(new java.awt.Color(0, 0, 0));
        FinalizarSimulacionButton.setText("FINALIZAR SIMULACIÓN");
        FinalizarSimulacionButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                FinalizarSimulacionButtonActionPerformed(evt);
            }
        });

        jSeparator7.setOrientation(javax.swing.SwingConstants.VERTICAL);

        SeccionProcesos.setBackground(new java.awt.Color(187, 187, 187));

        jLabel1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(0, 0, 0));
        jLabel1.setText("CREACION DE PROCESOS");

        jLabel4.setForeground(new java.awt.Color(0, 0, 0));
        jLabel4.setText("LONGITUD");

        LongitudTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                LongitudTextFieldActionPerformed(evt);
            }
        });

        jLabel5.setForeground(new java.awt.Color(0, 0, 0));
        jLabel5.setText("TIPO DE PROCESO");

        TipoProcesoSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "IO Bound", "Cpu Bound" }));
        TipoProcesoSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                TipoProcesoSelectActionPerformed(evt);
            }
        });

        jLabel6.setForeground(new java.awt.Color(0, 0, 0));
        jLabel6.setText("CICLOS PARA LLAMAR UNA INSTRUCCIÓN E/S");

        jLabel7.setForeground(new java.awt.Color(0, 0, 0));
        jLabel7.setText("CICLOS PARA COMPLETAR LA INSTRUCCIÓN E/S");

        CiclosLlamarIOTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CiclosLlamarIOTextFieldActionPerformed(evt);
            }
        });

        CiclosTerminarIOTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CiclosTerminarIOTextFieldActionPerformed(evt);
            }
        });

        jLabel11.setForeground(new java.awt.Color(0, 0, 0));
        jLabel11.setText("NOMBRE DEL PROCESO");

        NombreProcesoTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                NombreProcesoTextFieldActionPerformed(evt);
            }
        });

        AgregarProcesoButton.setBackground(new java.awt.Color(0, 139, 252));
        AgregarProcesoButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AgregarProcesoButton.setForeground(new java.awt.Color(0, 0, 0));
        AgregarProcesoButton.setText("AGREGAR PROCESO");
        AgregarProcesoButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AgregarProcesoButtonActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout SeccionProcesosLayout = new javax.swing.GroupLayout(SeccionProcesos);
        SeccionProcesos.setLayout(SeccionProcesosLayout);
        SeccionProcesosLayout.setHorizontalGroup(
            SeccionProcesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SeccionProcesosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SeccionProcesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TipoProcesoSelect, javax.swing.GroupLayout.Alignment.TRAILING, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator3)
                    .addComponent(LongitudTextField)
                    .addComponent(CiclosLlamarIOTextField)
                    .addComponent(CiclosTerminarIOTextField)
                    .addComponent(NombreProcesoTextField)
                    .addGroup(SeccionProcesosLayout.createSequentialGroup()
                        .addGroup(SeccionProcesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel4)
                            .addComponent(jLabel5)
                            .addComponent(jLabel6)
                            .addComponent(jLabel7)
                            .addComponent(jLabel11))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addComponent(jSeparator4, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(SeccionProcesosLayout.createSequentialGroup()
                .addGap(93, 93, 93)
                .addComponent(AgregarProcesoButton)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SeccionProcesosLayout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jLabel1)
                .addGap(77, 77, 77))
        );
        SeccionProcesosLayout.setVerticalGroup(
            SeccionProcesosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SeccionProcesosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 16, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(LongitudTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(TipoProcesoSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel6)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CiclosLlamarIOTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CiclosTerminarIOTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel11)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(NombreProcesoTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator4, javax.swing.GroupLayout.PREFERRED_SIZE, 9, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(AgregarProcesoButton, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(18, Short.MAX_VALUE))
        );

        SeccionConfiguracion.setBackground(new java.awt.Color(187, 187, 187));

        jLabel8.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(0, 0, 0));
        jLabel8.setText("CONFIGURACIÓN DE LA SIMULACIÓN");

        jLabel9.setForeground(new java.awt.Color(0, 0, 0));
        jLabel9.setText("POLITICA DE PLANIFICACIÓN");

        PoliticaPlanificacionSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "FCFS", "SJF", "RR", "SRT", "HRRN" }));
        PoliticaPlanificacionSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                PoliticaPlanificacionSelectActionPerformed(evt);
            }
        });

        jLabel2.setForeground(new java.awt.Color(0, 0, 0));
        jLabel2.setText("CANTIDAD DE CPUS");

        CantidadCpuSelect.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "2", "3" }));
        CantidadCpuSelect.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CantidadCpuSelectActionPerformed(evt);
            }
        });

        jLabel3.setForeground(new java.awt.Color(0, 0, 0));
        jLabel3.setText("DURACIÓN DE CADA CICLO (En milisegundos)");

        CicloTextField.setToolTipText("E.g: 1000");
        CicloTextField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                CicloTextFieldActionPerformed(evt);
            }
        });

        AplicarConfiguraciónButton.setBackground(new java.awt.Color(0, 139, 252));
        AplicarConfiguraciónButton.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        AplicarConfiguraciónButton.setForeground(new java.awt.Color(0, 0, 0));
        AplicarConfiguraciónButton.setText("APLICAR CONFIGURACIÓN");
        AplicarConfiguraciónButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                AplicarConfiguraciónButtonActionPerformed(evt);
            }
        });

        jLabel12.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(0, 0, 0));
        jLabel12.setText("CANTIDAD DE CPUS ACTUALES:");

        jLabel13.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(0, 0, 0));
        jLabel13.setText("VELOCIDAD DE RELOJ ACTUAL:");

        jLabel14.setFont(new java.awt.Font("Segoe UI", 1, 12)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(0, 0, 0));
        jLabel14.setText("POLÍTICA DE PLANIFICACION ESCOGIDA:");

        CurrentCpus.setForeground(new java.awt.Color(0, 0, 0));
        CurrentCpus.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        CurrentCycle.setForeground(new java.awt.Color(0, 0, 0));
        CurrentCycle.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);

        CurrentPolicy.setForeground(new java.awt.Color(0, 0, 0));
        CurrentPolicy.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        CurrentPolicy.setText("none");

        javax.swing.GroupLayout SeccionConfiguracionLayout = new javax.swing.GroupLayout(SeccionConfiguracion);
        SeccionConfiguracion.setLayout(SeccionConfiguracionLayout);
        SeccionConfiguracionLayout.setHorizontalGroup(
            SeccionConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SeccionConfiguracionLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SeccionConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator8, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jSeparator5, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(PoliticaPlanificacionSelect, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator6, javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CantidadCpuSelect, 0, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(CicloTextField)
                    .addGroup(SeccionConfiguracionLayout.createSequentialGroup()
                        .addGroup(SeccionConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel2)
                            .addComponent(jLabel9)
                            .addComponent(jLabel3)
                            .addComponent(jLabel14)
                            .addComponent(CurrentPolicy)
                            .addGroup(SeccionConfiguracionLayout.createSequentialGroup()
                                .addComponent(jLabel12)
                                .addGap(8, 8, 8)
                                .addComponent(CurrentCpus))
                            .addGroup(SeccionConfiguracionLayout.createSequentialGroup()
                                .addComponent(jLabel13)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(CurrentCycle)))
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addContainerGap())
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SeccionConfiguracionLayout.createSequentialGroup()
                .addGap(0, 55, Short.MAX_VALUE)
                .addGroup(SeccionConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SeccionConfiguracionLayout.createSequentialGroup()
                        .addComponent(jLabel8)
                        .addGap(25, 25, 25))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, SeccionConfiguracionLayout.createSequentialGroup()
                        .addComponent(AplicarConfiguraciónButton)
                        .addGap(73, 73, 73))))
        );
        SeccionConfiguracionLayout.setVerticalGroup(
            SeccionConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SeccionConfiguracionLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel8)
                .addGap(2, 2, 2)
                .addComponent(jSeparator8, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(SeccionConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(CurrentCpus))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(SeccionConfiguracionLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel13)
                    .addComponent(CurrentCycle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel14)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CurrentPolicy)
                .addGap(18, 18, Short.MAX_VALUE)
                .addComponent(jSeparator5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CantidadCpuSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(CicloTextField, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jLabel9)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(PoliticaPlanificacionSelect, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jSeparator6, javax.swing.GroupLayout.PREFERRED_SIZE, 5, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(AplicarConfiguraciónButton, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(15, 15, 15))
        );

        SeccionPrincipal.setBackground(new java.awt.Color(187, 187, 187));

        CPU1.setPreferredSize(new java.awt.Dimension(304, 304));

        jLabel10.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(0, 0, 0));
        jLabel10.setText("CPU 1");

        jPanel6.setBackground(new java.awt.Color(107, 107, 107));

        IdProceso1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        IdProceso1.setText("ID PROCESO:");

        NombreProceso1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NombreProceso1.setText("NOMBRE PROCESO:");

        EstadoProceso1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EstadoProceso1.setText("ESTADO PROCESO:");

        PC1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PC1.setText("PC:");

        MAR1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        MAR1.setText("MAR:");

        IDProcessCPU1.setForeground(new java.awt.Color(0, 0, 0));
        IDProcessCPU1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        IDProcessCPU1.setText("NULL");

        NameProcessCPU1.setForeground(new java.awt.Color(0, 0, 0));
        NameProcessCPU1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NameProcessCPU1.setText("NULL");

        EstateProcessCPU1.setForeground(new java.awt.Color(0, 0, 0));
        EstateProcessCPU1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        EstateProcessCPU1.setText("NULL");

        MarProcessCPU1.setForeground(new java.awt.Color(0, 0, 0));
        MarProcessCPU1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MarProcessCPU1.setText("NULL");

        PcCPU1.setForeground(new java.awt.Color(0, 0, 0));
        PcCPU1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        PcCPU1.setText("NULL");

        Longitud1.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Longitud1.setText("LONGITUD:");

        LongitudProcessCPU1.setForeground(new java.awt.Color(0, 0, 0));
        LongitudProcessCPU1.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LongitudProcessCPU1.setText("NULL");

        javax.swing.GroupLayout jPanel6Layout = new javax.swing.GroupLayout(jPanel6);
        jPanel6.setLayout(jPanel6Layout);
        jPanel6Layout.setHorizontalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(IdProceso1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IDProcessCPU1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(NombreProceso1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NameProcessCPU1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(EstadoProceso1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EstateProcessCPU1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(PC1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PcCPU1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(MAR1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MarProcessCPU1))
                    .addGroup(jPanel6Layout.createSequentialGroup()
                        .addComponent(Longitud1)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LongitudProcessCPU1)))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        jPanel6Layout.setVerticalGroup(
            jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel6Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IdProceso1)
                    .addComponent(IDProcessCPU1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NombreProceso1)
                    .addComponent(NameProcessCPU1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EstadoProceso1)
                    .addComponent(EstateProcessCPU1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PC1)
                    .addComponent(PcCPU1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MAR1)
                    .addComponent(MarProcessCPU1))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel6Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Longitud1)
                    .addComponent(LongitudProcessCPU1))
                .addContainerGap(105, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CPU1Layout = new javax.swing.GroupLayout(CPU1);
        CPU1.setLayout(CPU1Layout);
        CPU1Layout.setHorizontalGroup(
            CPU1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CPU1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(CPU1Layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(jLabel10)
                .addContainerGap(122, Short.MAX_VALUE))
        );
        CPU1Layout.setVerticalGroup(
            CPU1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CPU1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel10)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel6, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        CPU2.setPreferredSize(new java.awt.Dimension(304, 304));

        jLabel15.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel15.setForeground(new java.awt.Color(0, 0, 0));
        jLabel15.setText("CPU 2");

        jPanel7.setBackground(new java.awt.Color(107, 107, 107));

        IdProceso2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        IdProceso2.setText("ID PROCESO:");

        NombreProceso2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NombreProceso2.setText("NOMBRE PROCESO:");

        EstadoProceso2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EstadoProceso2.setText("ESTADO PROCESO:");

        PC2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PC2.setText("PC:");

        MAR2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        MAR2.setText("MAR:");

        IDProcessCPU2.setForeground(new java.awt.Color(0, 0, 0));
        IDProcessCPU2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        IDProcessCPU2.setText("NULL");

        NameProcessCPU2.setForeground(new java.awt.Color(0, 0, 0));
        NameProcessCPU2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NameProcessCPU2.setText("NULL");

        EstateProcessCPU2.setForeground(new java.awt.Color(0, 0, 0));
        EstateProcessCPU2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        EstateProcessCPU2.setText("NULL");

        MarProcessCPU2.setForeground(new java.awt.Color(0, 0, 0));
        MarProcessCPU2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MarProcessCPU2.setText("NULL");

        PcCPU2.setForeground(new java.awt.Color(0, 0, 0));
        PcCPU2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        PcCPU2.setText("NULL");

        Longitud2.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Longitud2.setText("LONGITUD:");

        LongitudProcessCPU2.setForeground(new java.awt.Color(0, 0, 0));
        LongitudProcessCPU2.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LongitudProcessCPU2.setText("NULL");

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(IdProceso2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IDProcessCPU2))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(NombreProceso2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(NameProcessCPU2))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(EstadoProceso2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(EstateProcessCPU2))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(PC2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(PcCPU2))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(MAR2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MarProcessCPU2))
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(Longitud2)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LongitudProcessCPU2)))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IdProceso2)
                    .addComponent(IDProcessCPU2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NombreProceso2)
                    .addComponent(NameProcessCPU2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EstadoProceso2)
                    .addComponent(EstateProcessCPU2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PC2)
                    .addComponent(PcCPU2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MAR2)
                    .addComponent(MarProcessCPU2))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Longitud2)
                    .addComponent(LongitudProcessCPU2))
                .addContainerGap(96, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CPU2Layout = new javax.swing.GroupLayout(CPU2);
        CPU2.setLayout(CPU2Layout);
        CPU2Layout.setHorizontalGroup(
            CPU2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CPU2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(CPU2Layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(jLabel15)
                .addContainerGap(122, Short.MAX_VALUE))
        );
        CPU2Layout.setVerticalGroup(
            CPU2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CPU2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel15)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGap(15, 15, 15))
        );

        CPU3.setPreferredSize(new java.awt.Dimension(304, 304));

        jLabel16.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel16.setForeground(new java.awt.Color(0, 0, 0));
        jLabel16.setText("CPU 3");

        jPanel8.setBackground(new java.awt.Color(107, 107, 107));

        IdProceso3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        IdProceso3.setText("ID PROCESO:");

        NombreProceso3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        NombreProceso3.setText("NOMBRE PROCESO:");

        EstadoProceso3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        EstadoProceso3.setText("ESTADO PROCESO:");

        PC3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        PC3.setText("PC:");

        MAR3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        MAR3.setText("MAR:");

        MarProcessCPU3.setForeground(new java.awt.Color(0, 0, 0));
        MarProcessCPU3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MarProcessCPU3.setText("NULL");

        EstateProcessCPU3.setForeground(new java.awt.Color(0, 0, 0));
        EstateProcessCPU3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        EstateProcessCPU3.setText("NULL");

        NameProcessCPU3.setForeground(new java.awt.Color(0, 0, 0));
        NameProcessCPU3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        NameProcessCPU3.setText("NULL");

        IDProcessCPU3.setForeground(new java.awt.Color(0, 0, 0));
        IDProcessCPU3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        IDProcessCPU3.setText("NULL");

        PcCPU3.setForeground(new java.awt.Color(0, 0, 0));
        PcCPU3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        PcCPU3.setText("NULL");

        Longitud3.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        Longitud3.setText("LONGITUD:");

        LongitudProcessCPU3.setForeground(new java.awt.Color(0, 0, 0));
        LongitudProcessCPU3.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        LongitudProcessCPU3.setText("NULL");

        javax.swing.GroupLayout jPanel8Layout = new javax.swing.GroupLayout(jPanel8);
        jPanel8.setLayout(jPanel8Layout);
        jPanel8Layout.setHorizontalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(IdProceso3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(IDProcessCPU3))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(PC3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(PcCPU3))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(MAR3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(MarProcessCPU3))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(EstadoProceso3)
                            .addComponent(NombreProceso3))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(NameProcessCPU3)
                            .addComponent(EstateProcessCPU3)))
                    .addGroup(jPanel8Layout.createSequentialGroup()
                        .addComponent(Longitud3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(LongitudProcessCPU3)))
                .addContainerGap(119, Short.MAX_VALUE))
        );
        jPanel8Layout.setVerticalGroup(
            jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel8Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(IdProceso3)
                    .addComponent(IDProcessCPU3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(NombreProceso3)
                    .addComponent(NameProcessCPU3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(EstadoProceso3)
                    .addComponent(EstateProcessCPU3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(PC3)
                    .addComponent(PcCPU3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(MAR3)
                    .addComponent(MarProcessCPU3))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel8Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(Longitud3)
                    .addComponent(LongitudProcessCPU3))
                .addContainerGap(105, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout CPU3Layout = new javax.swing.GroupLayout(CPU3);
        CPU3.setLayout(CPU3Layout);
        CPU3Layout.setHorizontalGroup(
            CPU3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CPU3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
            .addGroup(CPU3Layout.createSequentialGroup()
                .addGap(127, 127, 127)
                .addComponent(jLabel16)
                .addContainerGap(122, Short.MAX_VALUE))
        );
        CPU3Layout.setVerticalGroup(
            CPU3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(CPU3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel16)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jPanel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        javax.swing.GroupLayout SeccionPrincipalLayout = new javax.swing.GroupLayout(SeccionPrincipal);
        SeccionPrincipal.setLayout(SeccionPrincipalLayout);
        SeccionPrincipalLayout.setHorizontalGroup(
            SeccionPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SeccionPrincipalLayout.createSequentialGroup()
                .addGap(16, 16, 16)
                .addComponent(CPU1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 76, Short.MAX_VALUE)
                .addComponent(CPU2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18))
            .addGroup(SeccionPrincipalLayout.createSequentialGroup()
                .addGap(197, 197, 197)
                .addComponent(CPU3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SeccionPrincipalLayout.setVerticalGroup(
            SeccionPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SeccionPrincipalLayout.createSequentialGroup()
                .addGap(14, 14, 14)
                .addGroup(SeccionPrincipalLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(CPU1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(CPU2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 28, Short.MAX_VALUE)
                .addComponent(CPU3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(23, 23, 23))
        );

        jSeparator9.setOrientation(javax.swing.SwingConstants.VERTICAL);

        SeccionListaListos.setBackground(new java.awt.Color(187, 187, 187));

        jLabel17.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel17.setForeground(new java.awt.Color(0, 0, 0));
        jLabel17.setText("LISTA DE LISTOS");

        TablaListaDeListos.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "PC", "MAR", "Tipo", "Estado", "Longitud"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaListaDeListos.getTableHeader().setReorderingAllowed(false);
        jScrollPane9.setViewportView(TablaListaDeListos);

        javax.swing.GroupLayout SeccionListaListosLayout = new javax.swing.GroupLayout(SeccionListaListos);
        SeccionListaListos.setLayout(SeccionListaListosLayout);
        SeccionListaListosLayout.setHorizontalGroup(
            SeccionListaListosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SeccionListaListosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SeccionListaListosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator10)
                    .addComponent(jScrollPane9))
                .addContainerGap())
            .addGroup(SeccionListaListosLayout.createSequentialGroup()
                .addGap(179, 179, 179)
                .addComponent(jLabel17)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SeccionListaListosLayout.setVerticalGroup(
            SeccionListaListosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SeccionListaListosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel17)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator10, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane9, javax.swing.GroupLayout.PREFERRED_SIZE, 211, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        SeccionListaBloqueados.setBackground(new java.awt.Color(187, 187, 187));
        SeccionListaBloqueados.setPreferredSize(new java.awt.Dimension(304, 328));

        jLabel18.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N
        jLabel18.setForeground(new java.awt.Color(217, 51, 39));
        jLabel18.setText("LISTA DE BLOQUEADOS");

        TablaBloqueados.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "PC", "MAR", "Tipo", "Estado", "Longitud"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        TablaBloqueados.getTableHeader().setReorderingAllowed(false);
        jScrollPane8.setViewportView(TablaBloqueados);

        javax.swing.GroupLayout SeccionListaBloqueadosLayout = new javax.swing.GroupLayout(SeccionListaBloqueados);
        SeccionListaBloqueados.setLayout(SeccionListaBloqueadosLayout);
        SeccionListaBloqueadosLayout.setHorizontalGroup(
            SeccionListaBloqueadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SeccionListaBloqueadosLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(SeccionListaBloqueadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator11)
                    .addComponent(jScrollPane8, javax.swing.GroupLayout.Alignment.TRAILING))
                .addContainerGap())
            .addGroup(SeccionListaBloqueadosLayout.createSequentialGroup()
                .addGap(151, 151, 151)
                .addComponent(jLabel18)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        SeccionListaBloqueadosLayout.setVerticalGroup(
            SeccionListaBloqueadosLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(SeccionListaBloqueadosLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel18)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator11, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane8, javax.swing.GroupLayout.DEFAULT_SIZE, 198, Short.MAX_VALUE)
                .addContainerGap())
        );

        jSeparator12.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jLabel19.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        jLabel19.setText("CONTADOR GLOBAL:");

        GlobalCounter.setFont(new java.awt.Font("Segoe UI", 1, 18)); // NOI18N
        GlobalCounter.setText("0");

        ProcessEndedSystem.setBackground(new java.awt.Color(187, 187, 187));

        EndedProcessesSystem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "PC", "MAR", "Tipo", "Estado", "Longitud"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        EndedProcessesSystem.getTableHeader().setReorderingAllowed(false);
        jScrollPane1.setViewportView(EndedProcessesSystem);

        javax.swing.GroupLayout ProcessEndedSystemLayout = new javax.swing.GroupLayout(ProcessEndedSystem);
        ProcessEndedSystem.setLayout(ProcessEndedSystemLayout);
        ProcessEndedSystemLayout.setHorizontalGroup(
            ProcessEndedSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProcessEndedSystemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1)
                .addContainerGap())
        );
        ProcessEndedSystemLayout.setVerticalGroup(
            ProcessEndedSystemLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProcessEndedSystemLayout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        TabsProcesosCulminados.addTab("SISTEMA", ProcessEndedSystem);

        ProcessEndedCpu1.setBackground(new java.awt.Color(187, 187, 187));

        EndedProcessesCPU1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "PC", "MAR", "Tipo", "Estado", "Longitud"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        EndedProcessesCPU1.getTableHeader().setReorderingAllowed(false);
        jScrollPane2.setViewportView(EndedProcessesCPU1);

        javax.swing.GroupLayout ProcessEndedCpu1Layout = new javax.swing.GroupLayout(ProcessEndedCpu1);
        ProcessEndedCpu1.setLayout(ProcessEndedCpu1Layout);
        ProcessEndedCpu1Layout.setHorizontalGroup(
            ProcessEndedCpu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProcessEndedCpu1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2)
                .addContainerGap())
        );
        ProcessEndedCpu1Layout.setVerticalGroup(
            ProcessEndedCpu1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProcessEndedCpu1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 315, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(8, Short.MAX_VALUE))
        );

        TabsProcesosCulminados.addTab("CPU 1", ProcessEndedCpu1);

        ProcessEndedCpu2.setBackground(new java.awt.Color(187, 187, 187));

        EndedProcessesCPU2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "PC", "MAR", "Tipo", "Estado", "Longitud"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Integer.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        EndedProcessesCPU2.getTableHeader().setReorderingAllowed(false);
        jScrollPane3.setViewportView(EndedProcessesCPU2);

        javax.swing.GroupLayout ProcessEndedCpu2Layout = new javax.swing.GroupLayout(ProcessEndedCpu2);
        ProcessEndedCpu2.setLayout(ProcessEndedCpu2Layout);
        ProcessEndedCpu2Layout.setHorizontalGroup(
            ProcessEndedCpu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProcessEndedCpu2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addContainerGap())
        );
        ProcessEndedCpu2Layout.setVerticalGroup(
            ProcessEndedCpu2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProcessEndedCpu2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TabsProcesosCulminados.addTab("CPU 2", ProcessEndedCpu2);

        ProcessEndedCpu3.setBackground(new java.awt.Color(187, 187, 187));

        EndedProcessesCPU3.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "ID", "Nombre", "PC", "MAR", "Tipo", "Estado", "Longitud"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        EndedProcessesCPU3.getTableHeader().setReorderingAllowed(false);
        EndedProcessesCPU3.setUpdateSelectionOnSort(false);
        jScrollPane4.setViewportView(EndedProcessesCPU3);

        javax.swing.GroupLayout ProcessEndedCpu3Layout = new javax.swing.GroupLayout(ProcessEndedCpu3);
        ProcessEndedCpu3.setLayout(ProcessEndedCpu3Layout);
        ProcessEndedCpu3Layout.setHorizontalGroup(
            ProcessEndedCpu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProcessEndedCpu3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 458, Short.MAX_VALUE)
                .addContainerGap())
        );
        ProcessEndedCpu3Layout.setVerticalGroup(
            ProcessEndedCpu3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(ProcessEndedCpu3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 317, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        TabsProcesosCulminados.addTab("CPU 3", ProcessEndedCpu3);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addComponent(TituloPrincipal)
                                .addGap(5, 5, 5)
                                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(CargarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 163, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(IniciarButton, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(FinalizarSimulacionButton, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(GuardarConfigButton, javax.swing.GroupLayout.PREFERRED_SIZE, 172, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator12, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel19)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(GlobalCounter))
                            .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(SeccionConfiguracion, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(SeccionProcesos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                                .addGap(8, 8, 8)
                                .addComponent(jSeparator7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(SeccionPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jSeparator9, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(SeccionListaListos, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                    .addComponent(SeccionListaBloqueados, javax.swing.GroupLayout.DEFAULT_SIZE, 470, Short.MAX_VALUE)
                                    .addComponent(TabsProcesosCulminados))))
                        .addContainerGap())
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(jSeparator1)
                        .addGap(12, 12, 12))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(TituloPrincipal, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jSeparator2)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                        .addComponent(CargarButton, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(IniciarButton)
                        .addComponent(GuardarConfigButton)
                        .addComponent(FinalizarSimulacionButton))
                    .addComponent(jSeparator12)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel19)
                            .addComponent(GlobalCounter))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 3, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jSeparator9)
                    .addComponent(jSeparator7)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(SeccionConfiguracion, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addComponent(SeccionProcesos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addComponent(SeccionPrincipal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(SeccionListaListos, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(SeccionListaBloqueados, javax.swing.GroupLayout.PREFERRED_SIZE, 245, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                        .addComponent(TabsProcesosCulminados, javax.swing.GroupLayout.PREFERRED_SIZE, 364, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addContainerGap())
        );

        jTabbedPane2.addTab("SIMULACIÓN", jPanel1);

        jPanel5.setBackground(new java.awt.Color(187, 187, 187));
        jPanel5.addContainerListener(new java.awt.event.ContainerAdapter() {
            public void componentAdded(java.awt.event.ContainerEvent evt) {
                jPanel5ComponentAdded(evt);
            }
        });
        jPanel5.addComponentListener(new java.awt.event.ComponentAdapter() {
            public void componentShown(java.awt.event.ComponentEvent evt) {
                jPanel5ComponentShown(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 1561, Short.MAX_VALUE)
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 944, Short.MAX_VALUE)
        );

        jTabbedPane2.addTab("ESTADISTICAS", jPanel5);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jTabbedPane2, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jTabbedPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 979, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 6, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void IniciarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_IniciarButtonActionPerformed
        // TODO add your handling code here:

        try {
            isRunning = true;

            if (cantidadCpus == 3) {
                cpu1.start();
                cpu2.start();
                cpu3.start();
            } else {
                cpu1.start();
                cpu2.start();
            }
        } catch (NumberFormatException ex) {
            System.out.println("Error");
        }

    }//GEN-LAST:event_IniciarButtonActionPerformed

    private void CargarButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CargarButtonActionPerformed
        // TODO add your handling code here:
        /**
         * Buscador de archivos Accede todos los documentos del ordenador
         */
        JFileChooser fileChooser = new JFileChooser();
        String projectRoot = System.getProperty("user.dir");
        fileChooser.setCurrentDirectory(new File(projectRoot));

        fileChooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES); // Si quieres permitir seleccionar directorios

        FileNameExtensionFilter imgFilter = new FileNameExtensionFilter("JSON Files", "json");
        fileChooser.setFileFilter(imgFilter);

        int result = fileChooser.showOpenDialog(this); // 'this' asumiendo que este código está dentro de un componente Swing

        if (result == JFileChooser.APPROVE_OPTION) { // Verifica si el usuario seleccionó un archivo
            File fileName = fileChooser.getSelectedFile();

            if (fileName.isFile()) { // Asegúrate de que es un archivo y no un directorio (si permites ambos)
                try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
                    Gson gson = new Gson();
                    JsonObject json = gson.fromJson(reader, JsonObject.class);

                    procesosCreados = json.get("procesosCreados").getAsInt();
                    cantidadCpus = json.get("cantidadCpus").getAsInt();
                    velocidadReloj = json.get("velocidadReloj").getAsInt();
                    planificadorEscogido = json.get("planificador").getAsString();

                    if ("FCFS".equals(planificadorEscogido)) {
                        cpu1.setPlanificador(new PlanificadorFCFS());
                        cpu2.setPlanificador(new PlanificadorFCFS());
                        cpu3.setPlanificador(new PlanificadorFCFS());
                    } else if ("SJF".equals(planificadorEscogido)) {
                        cpu1.setPlanificador(new PlanificadorSJF());
                        cpu2.setPlanificador(new PlanificadorSJF());
                        cpu3.setPlanificador(new PlanificadorSJF());
                    } else if ("RR".equals(planificadorEscogido)) {
                        cpu1.setPlanificador(new PlanificadorRR());
                        cpu2.setPlanificador(new PlanificadorRR());
                        cpu3.setPlanificador(new PlanificadorRR());
                    } else if ("SRT".equals(planificadorEscogido)) {
                        cpu1.setPlanificador(new PlanificadorSRT());
                        cpu2.setPlanificador(new PlanificadorSRT());
                        cpu3.setPlanificador(new PlanificadorSRT());
                    } else if ("HRRN".equals(planificadorEscogido)) {
                        cpu1.setPlanificador(new PlanificadorHrrn());
                        cpu2.setPlanificador(new PlanificadorHrrn());
                        cpu3.setPlanificador(new PlanificadorHrrn());
                    }

                    JsonArray procesosArray = json.get("procesos_listos").getAsJsonArray();

                    for (JsonElement procesoElement : procesosArray) {
                        JsonObject procesoJson = procesoElement.getAsJsonObject();

                        // Elementos del proceso
                        int id = procesoJson.get("id").getAsInt();
                        String nombre = procesoJson.get("nombre").getAsString();
                        int totalInstructions = procesoJson.get("totalInstructions").getAsInt();
                        int programCounter = procesoJson.get("programCounter").getAsInt();
                        int mar = procesoJson.get("mar").getAsInt();
                        boolean cpuBound = procesoJson.get("cpuBound").getAsBoolean();
                        boolean ioBound = procesoJson.get("ioBound").getAsBoolean();
                        int ciclosExcepcion = procesoJson.get("ciclosExcepcion").getAsInt();
                        int ciclosIO = procesoJson.get("ciclosIO").getAsInt();

                        Process proceso = new Process(id, nombre, totalInstructions, programCounter, mar, cpuBound, ioBound, ciclosExcepcion, listaListos, listaBloqueados,
                                listaTotalProcesos, velocidadReloj, ciclosIO, this, 0, 1, 0);
                        listaListos.addProcess(proceso);

                        if (proceso.isCpuBound()) {
                            actualizarTablasAñadir(modeloTablaListos, id, nombre, programCounter, mar, "CPU Bound", "READY", totalInstructions);
                        } else {
                            actualizarTablasAñadir(modeloTablaListos, id, nombre, programCounter, mar, "IO Bound", "READY", totalInstructions);
                        }
                        
                    }

                    CurrentCpus.setText(Integer.toString(cantidadCpus));
                    CurrentCycle.setText(Integer.toString(velocidadReloj));
                    CurrentPolicy.setText(planificadorEscogido);

                } catch (IOException ex) {
                    JOptionPane.showMessageDialog(this, "Error al leer el archivo: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else if (fileName.isDirectory()) {
                JOptionPane.showMessageDialog(this, "Seleccionaste un directorio.  Por favor, selecciona un archivo JSON.", "Error", JOptionPane.ERROR_MESSAGE);
            }
    }//GEN-LAST:event_CargarButtonActionPerformed
    }

    private void GuardarConfigButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_GuardarConfigButtonActionPerformed
        // TODO add your handling code here:
        try {

            JsonObject json = new JsonObject();
            json.addProperty("procesosCreados", procesosCreados);
            json.addProperty("cantidadCpus", cantidadCpus);
            json.addProperty("velocidadReloj", velocidadReloj);
            json.addProperty("planificador", planificadorEscogido);

            JsonArray procesosArray = new JsonArray();
            for (Nodo nodo = listaTotalProcesos.getpFirst(); nodo != null; nodo = nodo.getpNext()) {
                Process proceso = (Process) nodo.getInfo();
                JsonObject procesoJson = new JsonObject();

                procesoJson.addProperty("id", proceso.getIdProcess());
                procesoJson.addProperty("nombre", proceso.getNameProcess());
                procesoJson.addProperty("totalInstructions", proceso.getTotalInstructions());
                procesoJson.addProperty("mar", proceso.getMar());
                procesoJson.addProperty("status", "READY");
                procesoJson.addProperty("programCounter", proceso.getProgramCounter());
                procesoJson.addProperty("cpuBound", proceso.isCpuBound());
                procesoJson.addProperty("ioBound", proceso.isIoBound());
                procesoJson.addProperty("ciclosExcepcion", proceso.getCiclosExcepcion());
                procesoJson.addProperty("ciclosIO", proceso.getCiclosES());
                procesosArray.add(procesoJson);

            }

            json.add("procesos_listos", procesosArray);

            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            String jsonString = gson.toJson(json);

            try (FileWriter writer = new FileWriter("Configuracion.json")) {
                gson.toJson(json, writer); // Directly write to the file using Gson
                //writer.write(jsonString);  No need to write the string manually
                JOptionPane.showMessageDialog(null, "Configuración guardada con éxito", "Éxito", JOptionPane.INFORMATION_MESSAGE);
            }
        } catch (IOException e) {
            JOptionPane.showMessageDialog(null, "Hubo un error de E/S", "ERROR", JOptionPane.ERROR_MESSAGE);
        }
    }//GEN-LAST:event_GuardarConfigButtonActionPerformed

    private void CantidadCpuSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CantidadCpuSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CantidadCpuSelectActionPerformed

    private void CicloTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CicloTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CicloTextFieldActionPerformed

    private void CiclosTerminarIOTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CiclosTerminarIOTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CiclosTerminarIOTextFieldActionPerformed

    private void CiclosLlamarIOTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_CiclosLlamarIOTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_CiclosLlamarIOTextFieldActionPerformed

    private void AgregarProcesoButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AgregarProcesoButtonActionPerformed
        // TODO add your handling code here:
        if (TipoProcesoSelect.getSelectedItem() == "IO Bound") {
            if (validarCampoEntero(CiclosLlamarIOTextField, "Ciclos para llamar una E/S")
                    && validarCampoEntero(LongitudTextField, "Longitud")
                    && validarCampoEntero(CiclosTerminarIOTextField, "Ciclos para terminar E/S")
                    && validarCampoStringNoVacio(NombreProcesoTextField, "Nombre del proceso")) {

                int cicloLlamarIO = Integer.parseInt(CiclosLlamarIOTextField.getText());
                int cicloTerminarIO = Integer.parseInt(CiclosTerminarIOTextField.getText());
                int longitudProceso = Integer.parseInt(LongitudTextField.getText());
                String nombreProceso = NombreProcesoTextField.getText();

                Process process = new Process(procesosCreados, nombreProceso, longitudProceso, false, true, cicloLlamarIO, listaListos, listaBloqueados,
                        listaTotalProcesos, velocidadReloj, cicloTerminarIO, this, 0, 1, 0);
                listaListos.addProcess(process);
                listaTotalProcesos.addProcess(process);

                // Agregar a la tabla de listos en la interfaz
                Object[] nuevaFila = new Object[]{
                    procesosCreados,
                    nombreProceso,
                    process.getProgramCounter(),
                    process.getMar(),
                    (process.isIoBound() ? "IO Bound" : "CPU Bound"), 
                    process.getStatus().name(),
                    process.getTotalInstructions()

                };
                modeloTablaListos.addRow(nuevaFila);
            }
        } else {
            if (validarCampoStringNoVacio(NombreProcesoTextField, "Nombre del proceso") && validarCampoEntero(LongitudTextField, "Longitud")) {
                int longitudProceso = Integer.parseInt(LongitudTextField.getText());
                String nombreProceso = NombreProcesoTextField.getText();

                Process process = new Process(procesosCreados, nombreProceso, longitudProceso, true, false, listaListos, listaBloqueados,
                        listaTotalProcesos, velocidadReloj, this, 0, 1, 0);
                listaListos.addProcess(process);
                listaTotalProcesos.addProcess(process);

                System.out.println(process.getCiclosExcepcion());

                Object[] nuevaFila = new Object[]{
                    procesosCreados,
                    nombreProceso,
                    process.getProgramCounter(),
                    process.getMar(),
                    (process.isIoBound() ? "IO Bound" : "CPU Bound"), 
                    process.getStatus().name(),
                    process.getTotalInstructions(),};

                modeloTablaListos.addRow(nuevaFila);
            }
        }

        procesosCreados++;
        listaListos.printlist("listos");

    }//GEN-LAST:event_AgregarProcesoButtonActionPerformed

    private void FinalizarSimulacionButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_FinalizarSimulacionButtonActionPerformed
        // TODO add your handling code here:
        isRunning = false;
        contadorGlobal = 0;
        procesosCreados = 0;
    }//GEN-LAST:event_FinalizarSimulacionButtonActionPerformed

    private void AplicarConfiguraciónButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_AplicarConfiguraciónButtonActionPerformed
        // TODO add your handling code here:

        if (validarCampoEntero(CicloTextField, "Duracion de cada ciclo") && validarVelocidadReloj(CicloTextField)) {
            velocidadReloj = Integer.parseInt(CicloTextField.getText());
            cantidadCpus = Integer.parseInt(CantidadCpuSelect.getSelectedItem().toString());
            planificadorEscogido = PoliticaPlanificacionSelect.getSelectedItem().toString();

            CurrentCpus.setText(CantidadCpuSelect.getSelectedItem().toString());
            CurrentCycle.setText(CicloTextField.getText());
            CurrentPolicy.setText(planificadorEscogido);

            if (planificadorEscogido == "FCFS") {
                cpu1.setPlanificador(new PlanificadorFCFS());
                cpu2.setPlanificador(new PlanificadorFCFS());
                cpu3.setPlanificador(new PlanificadorFCFS());

            }
            if (planificadorEscogido == "SJF") {
                cpu1.setPlanificador(new PlanificadorSJF());
                cpu2.setPlanificador(new PlanificadorSJF());
                cpu3.setPlanificador(new PlanificadorSJF());
            }
            if (planificadorEscogido == "RR") {

                cpu1.setPlanificador(new PlanificadorRR());
                cpu2.setPlanificador(new PlanificadorRR());
                cpu3.setPlanificador(new PlanificadorRR());
            }
            if (planificadorEscogido == "SRT") {
                cpu1.setPlanificador(new PlanificadorSRT());
                cpu2.setPlanificador(new PlanificadorSRT());
                cpu3.setPlanificador(new PlanificadorSRT());
            }
            if (planificadorEscogido == "HRRN") {

                cpu1.setPlanificador(new PlanificadorHrrn());
                cpu2.setPlanificador(new PlanificadorHrrn());
                cpu3.setPlanificador(new PlanificadorHrrn());

            }

        }


    }//GEN-LAST:event_AplicarConfiguraciónButtonActionPerformed

    private void LongitudTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_LongitudTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_LongitudTextFieldActionPerformed

    private void NombreProcesoTextFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_NombreProcesoTextFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_NombreProcesoTextFieldActionPerformed

    private void TipoProcesoSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_TipoProcesoSelectActionPerformed
        // TODO add your handling code here:
        if (TipoProcesoSelect.getSelectedItem() == "Cpu Bound") {
            // Desabilitar los campos de ciclos de ejecucion IO y su duracion de ejecucion.
            CiclosLlamarIOTextField.setEnabled(false);
            CiclosTerminarIOTextField.setEnabled(false);
            CiclosLlamarIOTextField.setText("");
            CiclosTerminarIOTextField.setText("");
        } else {
            CiclosLlamarIOTextField.setEnabled(true);
            CiclosTerminarIOTextField.setEnabled(true);
        }
    }//GEN-LAST:event_TipoProcesoSelectActionPerformed

    private void PoliticaPlanificacionSelectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_PoliticaPlanificacionSelectActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_PoliticaPlanificacionSelectActionPerformed

    private void jPanel5ComponentAdded(java.awt.event.ContainerEvent evt) {//GEN-FIRST:event_jPanel5ComponentAdded
        // TODO add your handling code here:


    }//GEN-LAST:event_jPanel5ComponentAdded

    private void jPanel5ComponentShown(java.awt.event.ComponentEvent evt) {//GEN-FIRST:event_jPanel5ComponentShown
        // TODO add your handling code here:
    }//GEN-LAST:event_jPanel5ComponentShown

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {

    }


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton AgregarProcesoButton;
    private javax.swing.JButton AplicarConfiguraciónButton;
    private javax.swing.JPanel CPU1;
    private javax.swing.JPanel CPU2;
    private javax.swing.JPanel CPU3;
    private javax.swing.JComboBox<String> CantidadCpuSelect;
    private javax.swing.JButton CargarButton;
    private javax.swing.JTextField CicloTextField;
    private javax.swing.JTextField CiclosLlamarIOTextField;
    private javax.swing.JTextField CiclosTerminarIOTextField;
    private javax.swing.JLabel CurrentCpus;
    private javax.swing.JLabel CurrentCycle;
    private javax.swing.JLabel CurrentPolicy;
    private javax.swing.JTable EndedProcessesCPU1;
    private javax.swing.JTable EndedProcessesCPU2;
    private javax.swing.JTable EndedProcessesCPU3;
    private javax.swing.JTable EndedProcessesSystem;
    private javax.swing.JLabel EstadoProceso1;
    private javax.swing.JLabel EstadoProceso2;
    private javax.swing.JLabel EstadoProceso3;
    private javax.swing.JLabel EstateProcessCPU1;
    private javax.swing.JLabel EstateProcessCPU2;
    private javax.swing.JLabel EstateProcessCPU3;
    private javax.swing.JButton FinalizarSimulacionButton;
    private javax.swing.JLabel GlobalCounter;
    private javax.swing.JButton GuardarConfigButton;
    private javax.swing.JLabel IDProcessCPU1;
    private javax.swing.JLabel IDProcessCPU2;
    private javax.swing.JLabel IDProcessCPU3;
    private javax.swing.JLabel IdProceso1;
    private javax.swing.JLabel IdProceso2;
    private javax.swing.JLabel IdProceso3;
    private javax.swing.JButton IniciarButton;
    private javax.swing.JLabel Longitud1;
    private javax.swing.JLabel Longitud2;
    private javax.swing.JLabel Longitud3;
    private javax.swing.JLabel LongitudProcessCPU1;
    private javax.swing.JLabel LongitudProcessCPU2;
    private javax.swing.JLabel LongitudProcessCPU3;
    private javax.swing.JTextField LongitudTextField;
    private javax.swing.JLabel MAR1;
    private javax.swing.JLabel MAR2;
    private javax.swing.JLabel MAR3;
    private javax.swing.JLabel MarProcessCPU1;
    private javax.swing.JLabel MarProcessCPU2;
    private javax.swing.JLabel MarProcessCPU3;
    private javax.swing.JLabel NameProcessCPU1;
    private javax.swing.JLabel NameProcessCPU2;
    private javax.swing.JLabel NameProcessCPU3;
    private javax.swing.JLabel NombreProceso1;
    private javax.swing.JLabel NombreProceso2;
    private javax.swing.JLabel NombreProceso3;
    private javax.swing.JTextField NombreProcesoTextField;
    private javax.swing.JLabel PC1;
    private javax.swing.JLabel PC2;
    private javax.swing.JLabel PC3;
    private javax.swing.JLabel PcCPU1;
    private javax.swing.JLabel PcCPU2;
    private javax.swing.JLabel PcCPU3;
    private javax.swing.JComboBox<String> PoliticaPlanificacionSelect;
    private javax.swing.JPanel ProcessEndedCpu1;
    private javax.swing.JPanel ProcessEndedCpu2;
    private javax.swing.JPanel ProcessEndedCpu3;
    private javax.swing.JPanel ProcessEndedSystem;
    private javax.swing.JPanel SeccionConfiguracion;
    private javax.swing.JPanel SeccionListaBloqueados;
    private javax.swing.JPanel SeccionListaListos;
    private javax.swing.JPanel SeccionPrincipal;
    private javax.swing.JPanel SeccionProcesos;
    private javax.swing.JTable TablaBloqueados;
    private javax.swing.JTable TablaListaDeListos;
    private javax.swing.JTabbedPane TabsProcesosCulminados;
    private javax.swing.JComboBox<String> TipoProcesoSelect;
    private javax.swing.JLabel TituloPrincipal;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel6;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JPanel jPanel8;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane8;
    private javax.swing.JScrollPane jScrollPane9;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator10;
    private javax.swing.JSeparator jSeparator11;
    private javax.swing.JSeparator jSeparator12;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSeparator jSeparator3;
    private javax.swing.JSeparator jSeparator4;
    private javax.swing.JSeparator jSeparator5;
    private javax.swing.JSeparator jSeparator6;
    private javax.swing.JSeparator jSeparator7;
    private javax.swing.JSeparator jSeparator8;
    private javax.swing.JSeparator jSeparator9;
    private javax.swing.JTabbedPane jTabbedPane2;
    // End of variables declaration//GEN-END:variables

    @Override
    public void run() {

        while (true) {
            if (isRunning) {
                this.getDataset().addValue(utilizacionSistema, "Ejecutando proceso", String.valueOf(this.getContadorGlobal()));
                CantidadCpuSelect.setEnabled(false);
                FinalizarSimulacionButton.setEnabled(true);
                IniciarButton.setEnabled(false);
                CargarButton.setEnabled(false);
                GuardarConfigButton.setEnabled(true);

                for (Nodo process = listaListos.getpFirst(); process != null; process = process.getpNext()) {
                    Process procesoActual = (Process) process.getInfo();

                    int valorTiempoActual = procesoActual.getTiempoEspera();
                    procesoActual.setTiempoEspera(valorTiempoActual + 1);

                    System.out.println(valorTiempoActual + " del proceso" + procesoActual.getNameProcess());

                }
                try {
                    // MANEJO DEL CONTADOR GLOBAL EN LA INTERFAZ
                    Thread.sleep(velocidadReloj);
                    contadorGlobal++;

                    GlobalCounter.setText(Integer.toString(contadorGlobal));
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    return;
                }
            } else {
                CantidadCpuSelect.setEnabled(true);
                FinalizarSimulacionButton.setEnabled(false);
                IniciarButton.setEnabled(true);
                contadorGlobal = 0;
                GlobalCounter.setText(Integer.toString(contadorGlobal));
                CargarButton.setEnabled(true);
                GuardarConfigButton.setEnabled(false);
            }
        }
    }
}
