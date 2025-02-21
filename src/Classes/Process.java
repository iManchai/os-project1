package Classes;

import DataStructures.ListaSimple;
import DataStructures.Nodo;
import java.util.concurrent.Semaphore;
import Interfaz.InterfazInicial;

public class Process extends Thread {

    private int id;
    private String name;
    private int programCounter;
    private int mar;
    private ProcessStatus status;
    private int totalInstructions;
    private boolean cpuBound;
    private boolean ioBound;
    private int ciclosExcepcion;
    private int ciclosES;
    private ListaSimple listaListos;
    private ListaSimple listaBloqueados;
    private ListaSimple listaTotalProcesos;
    private Semaphore semaphore; // Semáforo para sincronización
    private int duracion;
    private String cpuName;
    private int velocidadReloj;
    private InterfazInicial interfaz;
    private int cpu;
    private int tiempoEjecucionRR;
    private int tiempoEnCPU;
    private int tiempoEspera;
    private boolean isRunning;

    // IO bound constructor
    public Process(int id, String name, int totalInstructions, boolean cpuBound, boolean ioBound, int ciclosExcepcion,
            ListaSimple listaListos, ListaSimple listaBloqueados, ListaSimple listaTotalProcesos, int velocidadReloj, int cicloES, InterfazInicial interfaz, int cpu, int tiempoEjecucionRR, int tiempoEnCPU) {
        this.id = id;
        this.name = name;
        this.programCounter = 0;
        this.mar = 0;
        this.status = ProcessStatus.READY;
        this.totalInstructions = totalInstructions;
        this.cpuBound = cpuBound;
        this.ioBound = ioBound;
        this.ciclosExcepcion = ciclosExcepcion;
        this.listaListos = listaListos;
        this.listaBloqueados = listaBloqueados;
        this.listaTotalProcesos = listaTotalProcesos;
        this.semaphore = semaphore; // Semáforo compartido
        this.duracion = totalInstructions - programCounter;
        this.cpuName = cpuName;
        this.velocidadReloj = velocidadReloj;
        this.ciclosES = cicloES;
        this.interfaz = interfaz;
        this.cpu = cpu;
        this.tiempoEjecucionRR = tiempoEjecucionRR;
        this.tiempoEnCPU = tiempoEnCPU;
        this.tiempoEspera = tiempoEspera;
        this.isRunning = isRunning;
    }

    // Cpu bound constructor
    public Process(int id, String name, int totalInstructions, boolean cpuBound, boolean ioBound,
            ListaSimple listaListos, ListaSimple listaBloqueados, ListaSimple listaTotalProcesos, int velocidadReloj, InterfazInicial interfaz, int cpu, int tiempoEjecucionRR, int tiempoEnCPU) {
        this.id = id;
        this.name = name;
        this.programCounter = 0;
        this.mar = 0;
        this.status = ProcessStatus.READY;
        this.totalInstructions = totalInstructions;
        this.cpuBound = cpuBound;
        this.ioBound = ioBound;
        this.listaListos = listaListos;
        this.listaBloqueados = listaBloqueados;
        this.listaTotalProcesos = listaTotalProcesos;
        this.semaphore = semaphore; // Semáforo compartido
        this.duracion = totalInstructions - programCounter;
        this.cpuName = cpuName;
        this.velocidadReloj = velocidadReloj;
        this.interfaz = interfaz;
        this.cpu = cpu;
        this.tiempoEjecucionRR = tiempoEjecucionRR;
        this.tiempoEnCPU = tiempoEnCPU;
        this.tiempoEspera = tiempoEspera;
        this.isRunning = isRunning;

    }

    // Cargar proceso de configuracion constructor
    public Process(int id, String name, int totalInstructions, int programCounter, int mar, boolean cpuBound, boolean ioBound, int ciclosExcepcion,
            ListaSimple listaListos, ListaSimple listaBloqueados, ListaSimple listaTotalProcesos, int velocidadReloj, int cicloES, InterfazInicial interfaz, int cpu, int tiempoEjecucionRR, int tiempoEnCPU) {
        this.id = id;
        this.name = name;
        this.programCounter = programCounter;
        this.mar = mar;
        this.status = ProcessStatus.READY;
        this.totalInstructions = totalInstructions;
        this.cpuBound = cpuBound;
        this.ioBound = ioBound;
        this.ciclosExcepcion = ciclosExcepcion;
        this.listaListos = listaListos;
        this.listaBloqueados = listaBloqueados;
        this.listaTotalProcesos = listaTotalProcesos;
        this.semaphore = semaphore; // Semáforo compartido
        this.duracion = totalInstructions - programCounter;
        this.cpuName = cpuName;
        this.velocidadReloj = velocidadReloj;
        this.ciclosES = cicloES;
        this.interfaz = interfaz;
        this.cpu = cpu;
        this.tiempoEjecucionRR = tiempoEjecucionRR;
        this.tiempoEnCPU = tiempoEnCPU;
        this.isRunning = isRunning;

    }

    @Override
    public void run() {
        
        

        isRunning = true;

        interfaz.actualizarTablasBorrar(interfaz.getModeloTablaListos(), id);

        Thread ioThread = new Thread(() -> { // Hilo para E/S
            try {

                int i = 0;

                while (i < ciclosES) {
                    if (!isRunning) {
                        return;
                        
                    }
                    Thread.sleep((velocidadReloj));
                    i++;

                }

                System.out.println("E/S del proceso " + name + " listo");
                listaBloqueados.RemoveProcess(this);
                interfaz.actualizarTablasBorrar(interfaz.getModeloTablaBloqueados(), id);
                status = ProcessStatus.READY;
                listaListos.addProcess(this);
                interfaz.actualizarTablasAñadir(interfaz.getModeloTablaListos(), id, name, programCounter, mar, (ioBound ? "IO Bound" : "CPU Bound"), status.name(), totalInstructions);

            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });

        Os os = new Os(0, "Os", 3, velocidadReloj, interfaz, cpu, isRunning);
        os.setSemaphore(semaphore);

        try {

            if (programCounter >= 0) {

                interfaz.addValueProcessor(cpu);

            }

            while (programCounter < totalInstructions && status != ProcessStatus.BLOCKED && isRunning ) {

                try {

                    if (interfaz.getPlanificadorEscogido() == "SRT" && !listaListos.isEmpty()) {
                        
                        Process procesoMasCorto = null;
                        Nodo nodoActual = listaListos.getpFirst();

                        while (nodoActual != null) {

                            Process procesoActual = (Process) nodoActual.getInfo();

                            if (procesoActual.getStatus() == Process.ProcessStatus.READY) {
                                if (procesoMasCorto == null || procesoActual.getDuracion() - procesoActual.getProgramCounter() < procesoMasCorto.getDuracion() - procesoMasCorto.getProgramCounter()) {
                                    procesoMasCorto = procesoActual;
                                }
                            }
                            nodoActual = nodoActual.getpNext();
                        }

                        if (this.duracion - this.programCounter > procesoMasCorto.getDuracion() - procesoMasCorto.getProgramCounter()) {

                            status = ProcessStatus.READY;
                            tiempoEnCPU = 0;
                            listaListos.addProcess(this);
                            interfaz.actualizarTablasAñadir(interfaz.getModeloTablaListos(), id, name, programCounter, mar, (ioBound ? "IO Bound" : "CPU Bound"), status.name(), totalInstructions);

                            interfaz.restValueprocessor(cpu);

                            return;

                        }

                    }

                    semaphore.acquire();

                    interfaz.actualizarInterfaz(interfaz.getModeloTablaListos(), id, name, programCounter, status.name(), totalInstructions);

                    if (tiempoEjecucionRR == 5 && tiempoEnCPU != 0 && tiempoEnCPU % 5 == 0) {
                        status = ProcessStatus.READY;
                        tiempoEnCPU = 0;
                        listaListos.addProcess(this);   
                        interfaz.actualizarTablasAñadir(interfaz.getModeloTablaListos(), id, name, programCounter, mar, (ioBound ? "IO Bound" : "CPU Bound"), status.name(), totalInstructions);
                        semaphore.release();

                        interfaz.restValueprocessor(cpu);

                        os.start();
                        os.join();

                        return;
                    }

                    interfaz.actualizarInterfazCPU(cpu, String.valueOf(id), status.name(), String.valueOf(programCounter), String.valueOf(mar), name, String.valueOf(totalInstructions));

                    System.out.println("siguiente iteración-------------->");

                    Thread.sleep(velocidadReloj); // Simular un ciclo de reloj
                    tiempoEnCPU++;
                    programCounter++;
                    mar++;

                    semaphore.release(); // Liberar el semáforo

                    if (ioBound && programCounter % ciclosExcepcion == 0) {
                        status = ProcessStatus.BLOCKED;

                        interfaz.restValueprocessor(cpu);

                        tiempoEnCPU = 0;
                        listaBloqueados.addProcess(this);
                        interfaz.actualizarTablasAñadir(interfaz.getModeloTablaBloqueados(), id, name, programCounter, mar, (ioBound ? "IO Bound" : "CPU Bound"), status.name(), totalInstructions);
//                        System.out.println("Proceso " + name + " en espera de E/S");

                        os.start();

                        ioThread.start();

                        os.join();

                        return; // Salir del método run()
                    }

                    if (programCounter == totalInstructions) {
                        status = ProcessStatus.FINISHED;
                        interfaz.actualizarTablasAñadir(interfaz.getModeloTablaFinalizadoSistema(), id, name, programCounter, mar, (ioBound ? "IO Bound" : "CPU Bound"), status.name(), totalInstructions);
                        interfaz.AgregarListaFinalizadosCpu(cpu, id, name, programCounter, status.name(), totalInstructions, mar, (ioBound ? "IO Bound" : "CPU Bound"));

                        interfaz.restValueprocessor(cpu);

                        listaListos.RemoveProcess(this);
                        listaTotalProcesos.RemoveProcess(this);

                        os.start();
                        os.join();

                        return;
                    }
                } catch (InterruptedException e) {
//                    System.out.println("Proceso " + name + " fue interrumpido.");
                    Thread.currentThread().interrupt(); // Restablece la bandera de interrupción
                    isRunning = false; // Asegúrate de que el bucle se detenga
                    ioThread.interrupt(); // Interrumpe el hilo de E/S
                    os.interrupt(); // Interrumpe el hilo del sistema operativo
                    break; // Sale del bucle
                }
            }
        } finally {
        }
    }

    // Getters y setters\
    public boolean isIsRunning() {
        return isRunning;
    }

    public void setIsRunning(boolean isRunning) {
        this.isRunning = isRunning;
    }

    public void setVelocidadReloj(int velocidadReloj) {
        this.velocidadReloj = velocidadReloj;
    }

    public int getTiempoEspera() {
        return tiempoEspera;
    }

    public void setTiempoEspera(int tiempoEspera) {
        this.tiempoEspera = tiempoEspera;
    }

    public int getTiempoEjecucionRR() {
        return tiempoEjecucionRR;
    }

    public void setTiempoEjecucionRR(int tiempoEjecucionRR) {
        this.tiempoEjecucionRR = tiempoEjecucionRR;
    }

    public int getIdProcess() {
        return id;
    }

    public void setIdProcess(int id) {
        this.id = id;
    }

    public String getNameProcess() {
        return name;
    }

    public void setNameProcess(String name) {
        this.name = name;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }

    public int getMar() {
        return mar;
    }

    public void setMar(int mar) {
        this.mar = mar;
    }

    public ProcessStatus getStatus() {
        return status;
    }

    public void setStatus(ProcessStatus status) {
        this.status = status;
    }

    public int getTotalInstructions() {
        return totalInstructions;
    }

    public void setTotalInstructions(int totalInstructions) {
        this.totalInstructions = totalInstructions;
    }

    public boolean isCpuBound() {
        return cpuBound;
    }

    public void setCpuBound(boolean cpuBound) {
        this.cpuBound = cpuBound;
    }

    public boolean isIoBound() {
        return ioBound;
    }

    public void setIoBound(boolean ioBound) {
        this.ioBound = ioBound;
    }

    public int getCiclosExcepcion() {
        return ciclosExcepcion;
    }

    public void setCiclosExcepcion(int ciclosExcepcion) {
        this.ciclosExcepcion = ciclosExcepcion;
    }

    public ListaSimple getListaProcesos() {
        return listaListos;
    }

    public void setListaProcesos(ListaSimple listaProcesos) {
        this.listaListos = listaProcesos;
    }

    public ListaSimple getListaBloqueados() {
        return listaBloqueados;
    }

    public void setListaBloqueados(ListaSimple listaBloqueados) {
        this.listaBloqueados = listaBloqueados;
    }

    public Semaphore getCpuSemaphore() {
        return semaphore;
    }

    public void setCpuSemaphore(Semaphore cpuSemaphore) {
        this.semaphore = cpuSemaphore;
    }

    public enum ProcessStatus {
        READY, RUNNING, BLOCKED, FINISHED
    }

    public int getDuracion() {
        return duracion;
    }

    public void setDuracion(int duracion) {
        this.duracion = duracion;
    }

    public String getCpuName() {
        return cpuName;
    }

    public void setCpuName(String cpuName) {
        this.cpuName = cpuName;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

    public int getCiclosES() {
        return ciclosES;
    }

}
