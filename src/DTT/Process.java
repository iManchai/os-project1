package DTT;

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
    private ListaProcesos listaProcesos;
    private ListaBloqueados listaBloqueados;
    private Semaphore cpuSemaphore; // Referencia al semáforo del CPU

    public Process(int id, String name, int totalInstructions, boolean cpuBound, boolean ioBound, int ciclosExcepcion, ListaProcesos listaProcesos, ListaBloqueados listaBloqueados, Semaphore cpuSemaphore) {
        this.id = id;
        this.name = name;
        this.programCounter = 0;
        this.status = ProcessStatus.READY;
        this.totalInstructions = totalInstructions;
        this.cpuBound = cpuBound;
        this.ioBound = ioBound;
        this.ciclosExcepcion = ciclosExcepcion;
        this.listaProcesos = listaProcesos;
        this.listaBloqueados = listaBloqueados;
        /// faltan las prioridades para las politicas de planificacion
        this.cpuSemaphore = cpuSemaphore; // Asignamos el semáforo
    }

@Override
public void run() {
    try {
        while (programCounter < totalInstructions && status != ProcessStatus.BLOCKED) {
            cpuSemaphore.acquire();  // Adquirir el semáforo para acceder al CPU

            System.out.println("Proceso " + name + " ejecutando instrucción " + programCounter);
            status = ProcessStatus.RUNNING;

            programCounter++;

            // Verificar si el proceso debe bloquearse por E/S
            if (ioBound && programCounter % ciclosExcepcion == 0) {
                status = ProcessStatus.BLOCKED;
                System.out.println("Proceso " + name + " en espera de E/S");

                // Mover el proceso a la lista de bloqueados
                listaProcesos.removerProceso(this);
                listaBloqueados.agregarProceso(this);

                cpuSemaphore.release();  // Liberar el semáforo antes de bloquearse

                // Simular la operación de E/S (bloqueo)
                Thread.sleep(1000);  // Simular tiempo de espera de E/S

                // Volver a la lista de procesos listos después de la E/S
                listaBloqueados.removerProceso(this);
                listaProcesos.agregarProceso(this);
                status = ProcessStatus.READY;  // Cambiar el estado a READY

                continue;  // Continuar con la siguiente iteración del bucle
            }

            cpuSemaphore.release();  // Liberar el semáforo después de ejecutar una instrucción
            Thread.sleep(100);  // Pequeña pausa para simular el tiempo de ejecución
        }

        // Si el proceso terminó todas sus instrucciones
        if (programCounter >= totalInstructions) {
            status = ProcessStatus.FINISHED;
            listaProcesos.removerProceso(this);
            System.out.println("Proceso " + name + " finalizado.");
        }
    } catch (InterruptedException e) {
        e.printStackTrace();
    }
}
    // Getters y Setters
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

    public ListaProcesos getListaProcesos() {
        return listaProcesos;
    }

    public void setListaProcesos(ListaProcesos listaProcesos) {
        this.listaProcesos = listaProcesos;
    }

    public ListaBloqueados getListaBloqueados() {
        return listaBloqueados;
    }

    public void setListaBloqueados(ListaBloqueados listaBloqueados) {
        this.listaBloqueados = listaBloqueados;
    }

    public Semaphore getCpuSemaphore() {
        return cpuSemaphore;
    }

    public void setCpuSemaphore(Semaphore cpuSemaphore) {
        this.cpuSemaphore = cpuSemaphore;
    }

    public enum ProcessStatus {
        READY, RUNNING, BLOCKED, FINISHED
    }

}
