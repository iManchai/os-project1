package Classes;

import DataStructures.ListaSimple;
import DataStructures.Nodo;
import java.util.concurrent.Semaphore;
import Interfaz.InterfazInicial;

public class Process extends Thread {

    private int id;
    private String name;
    private int programCounter;
    private ProcessStatus status;
    private int totalInstructions;
    private boolean cpuBound;
    private boolean ioBound;
    private int ciclosExcepcion;
    private ListaSimple listaListos;
    private ListaSimple listaBloqueados;
    private Semaphore semaphore; // Semáforo para sincronización
    private int duracion;
    private String cpuName;
    private int velocidadReloj;
    private int ciclosES;
    private InterfazInicial interfaz;
    private int cpu;
    private int tiempoEjecucionRR;
    private int tiempoEnCPU;
    private int tiempoEspera;

    public Process(int id, String name, int totalInstructions, boolean cpuBound, boolean ioBound, int ciclosExcepcion,
            ListaSimple listaListos, ListaSimple listaBloqueados, int velocidadReloj, int cicloES, InterfazInicial interfaz, int cpu, int tiempoEjecucionRR, int tiempoEnCPU) {
        this.id = id;
        this.name = name;
        this.programCounter = 0;
        this.status = ProcessStatus.READY;
        this.totalInstructions = totalInstructions;
        this.cpuBound = cpuBound;
        this.ioBound = ioBound;
        this.ciclosExcepcion = ciclosExcepcion;
        this.listaListos = listaListos;
        this.listaBloqueados = listaBloqueados;
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
    }

    public Process(int id, String name, int totalInstructions, boolean cpuBound, boolean ioBound,
            ListaSimple listaListos, ListaSimple listaBloqueados, int velocidadReloj, InterfazInicial interfaz, int cpu, int tiempoEjecucionRR, int tiempoEnCPU) {
        this.id = id;
        this.name = name;
        this.programCounter = 0;
        this.status = ProcessStatus.READY;
        this.totalInstructions = totalInstructions;
        this.cpuBound = cpuBound;
        this.ioBound = ioBound;
        this.listaListos = listaListos;
        this.listaBloqueados = listaBloqueados;
        this.semaphore = semaphore; // Semáforo compartido
        this.duracion = totalInstructions - programCounter;
        this.cpuName = cpuName;
        this.velocidadReloj = velocidadReloj;
        this.interfaz = interfaz;
        this.cpu = cpu;
        this.tiempoEjecucionRR = tiempoEjecucionRR;
        this.tiempoEnCPU = tiempoEnCPU;
        this.tiempoEspera = tiempoEspera;
    }

    @Override
    public void run() {

        interfaz.actulizarTablaBorrar(interfaz.getModeloTablaListos(), id);

        try {

            Os os = new Os(0, "Os", 3, velocidadReloj, interfaz, cpu);
            os.setSemaphore(semaphore);
            while (programCounter < totalInstructions && status != ProcessStatus.BLOCKED) {

                /////Actulizar la grafica
                if (programCounter == 0) {

                    int utlizacionSistema = interfaz.getUtilizacionSistema();
                    utlizacionSistema++;
                    interfaz.setUtilizacionSistema(utlizacionSistema);
                    interfaz.getDataset().addValue(utlizacionSistema, "Ejecutando proceso", String.valueOf(interfaz.getContadorGlobal()));

                }

                //////crear metodo para SRT
               
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
                        interfaz.actualizarIntefazCrear(interfaz.getModeloTablaListos(), id, name, programCounter, status.name(), totalInstructions);

                        ///actualizar grafica
                        int utlizacionSistema = interfaz.getUtilizacionSistema();
                        utlizacionSistema--;
                        interfaz.setUtilizacionSistema(utlizacionSistema);
                        interfaz.getDataset().addValue(utlizacionSistema, "Ejecutando proceso", String.valueOf(interfaz.getContadorGlobal()));

                        return;

                    }

                }

                semaphore.acquire();

                interfaz.actualizarIntefaz(interfaz.getModeloTablaListos(), id, name, programCounter, status.name(), totalInstructions);

                if (tiempoEjecucionRR == 5 && tiempoEnCPU == 5) {
                    status = ProcessStatus.READY;
                    tiempoEnCPU = 0;
                    listaListos.addProcess(this);
                    System.out.println("Proceso " + name + " sale del CPU (Round Robin)" + cpuName);
                    interfaz.actualizarIntefazCrear(interfaz.getModeloTablaListos(), id, name, programCounter, status.name(), totalInstructions);
                    semaphore.release();

                    os.start();
                    os.join();

                    ///actualizar grafica
                    int utlizacionSistema = interfaz.getUtilizacionSistema();
                    utlizacionSistema--;
                    interfaz.setUtilizacionSistema(utlizacionSistema);
                    interfaz.getDataset().addValue(utlizacionSistema, "Ejecutando proceso", String.valueOf(interfaz.getContadorGlobal()));

                    return;
                }

                interfaz.actualizarInterfazCPU(cpu, String.valueOf(id), name, status.name(), String.valueOf(programCounter), String.valueOf(totalInstructions));

                System.out.println(name + " ejecutando instrucción " + programCounter + " en el cpu:" + cpuName);
                listaListos.printlist("listos");
                listaBloqueados.printlist("bloqueados");
                System.out.println("siguiente iteración-------------->");
                Thread.sleep(velocidadReloj);

                tiempoEnCPU++;
                programCounter++;

                semaphore.release(); // Liberar el semáforo

                if (ioBound && programCounter % ciclosExcepcion == 0) {
                    status = ProcessStatus.BLOCKED;

                    ///actualizar grafica
                    int utlizacionSistema = interfaz.getUtilizacionSistema();
                    utlizacionSistema--;
                    interfaz.setUtilizacionSistema(utlizacionSistema);
                    interfaz.getDataset().addValue(utlizacionSistema, "Ejecutando proceso", String.valueOf(interfaz.getContadorGlobal()));

                    tiempoEnCPU = 0;
                    listaBloqueados.addProcess(this);
                    interfaz.actualizarIntefazCrear(interfaz.getModeloTablaBloqueados(), id, name, programCounter, status.name(), totalInstructions);
                    System.out.println("Proceso " + name + " en espera de E/S");

                    os.start();

                    Thread ioThread = new Thread(() -> { // Hilo para E/S
                        try {
                            Thread.sleep(velocidadReloj * ciclosES);
                            System.out.println("E/S del proceso " + name + " listo");
                            semaphore.acquire();
                            listaBloqueados.RemoveProcess(this);
                            interfaz.actulizarTablaBorrar(interfaz.getModeloTablaBloqueados(), id);
                            status = ProcessStatus.READY;
                            interfaz.actualizarIntefazCrear(interfaz.getModeloTablaListos(), id, name, programCounter, status.name(), totalInstructions);
                            listaListos.addProcess(this);
                            semaphore.release();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });

                    os.join();

                    ioThread.start();

                    return; // Salir del método run()
                }
            }

            if (programCounter == totalInstructions) {
                status = ProcessStatus.FINISHED;
                interfaz.actualizarIntefazCrear(interfaz.getModeloTablaFinalizadoSistema(), id, name, programCounter, status.name(), totalInstructions);
                interfaz.AgregarListaFinalizadosCpu(cpu, id, name, programCounter, name, totalInstructions);

                ///actualizar grafica
                int utlizacionSistema = interfaz.getUtilizacionSistema();
                utlizacionSistema--;
                interfaz.setUtilizacionSistema(utlizacionSistema);
                interfaz.getDataset().addValue(utlizacionSistema, "Ejecutando proceso", String.valueOf(interfaz.getContadorGlobal()));
                
                
                
                listaListos.RemoveProcess(this);

                System.out.println("Proceso " + name + " finalizado.");
                os.start();
                os.join();

                return;
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }
    }

    // Getters y setters
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

}
