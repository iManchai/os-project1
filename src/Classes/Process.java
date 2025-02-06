package Classes;

import Classes.Cpu;
import DataStructures.ListaBloqueados;
import DataStructures.ListaListos;
import java.util.concurrent.Semaphore;

public class Process extends Thread {

    private int id;
    private String name;
    private int programCounter;
    private ProcessStatus status;
    private int totalInstructions;
    private boolean cpuBound;
    private boolean ioBound;
    private int ciclosExcepcion;
    private ListaListos listaListos;
    private ListaBloqueados listaBloqueados;
    private Semaphore semaphore; // Semáforo para sincronización
    public Cpu cpu;

    public Process(int id, String name, int totalInstructions, boolean cpuBound, boolean ioBound, int ciclosExcepcion,
            ListaListos listaListos, ListaBloqueados listaBloqueados, Semaphore semaphore, Cpu cpu) {
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
        this.cpu = cpu;
    }

    @Override
    public void run() {
        try {
            while (programCounter < totalInstructions && status != ProcessStatus.BLOCKED) {
                semaphore.acquire(); // Adquirir el semáforo antes de acceder a recursos compartidos
                // Ejecutar una instrucción del proceso
                System.out.println("Proceso " + name + " ejecutando instrucción " + programCounter);
                listaListos.imprimirLista();
                Thread.sleep(500);

                programCounter++;

                semaphore.release(); // Liberar el semáforo después de ejecutar la instrucción

                // Simular tiempo de ejecución de la instrucción
                // Verificar si el proceso debe bloquearse por E/S
                if (ioBound && programCounter % ciclosExcepcion == 0) {
                    status = ProcessStatus.BLOCKED;
                    System.out.println("Proceso " + name + " en espera de E/S");

                    // Mover el proceso a la lista de bloqueados
                    listaBloqueados.agregarProceso(this);

                    semaphore.release(); // Liberar el semáforo antes de bloquearse

                    // Simular tiempo de espera de E/S
                    Thread.sleep(2000);

                    // Volver a la lista de procesos listos después de la E/S
                    listaBloqueados.removerProceso(this);
                    listaListos.agregarProceso(this); // Agregar al final de la lista de listos

                    status = ProcessStatus.READY;
                    return; // Volver al inicio del bucle para que el CPU tome el siguiente proceso
                }

            }

            // Si el proceso terminó todas sus instrucciones
            if (programCounter == totalInstructions) {
                status = ProcessStatus.FINISHED;
                listaListos.removerProceso(this);
                System.out.println("Proceso " + name + " finalizado.");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            
                semaphore.release(); // Asegurarse de liberar el semáforo en caso de excepción
            
        }
    }

    // Getters y setters
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

    public ListaListos getListaProcesos() {
        return listaListos;
    }

    public void setListaProcesos(ListaListos listaProcesos) {
        this.listaListos = listaProcesos;
    }

    public ListaBloqueados getListaBloqueados() {
        return listaBloqueados;
    }

    public void setListaBloqueados(ListaBloqueados listaBloqueados) {
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
}
