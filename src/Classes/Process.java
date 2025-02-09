package Classes;

import DataStructures.ListaSimple;
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
    private ListaSimple listaListos;
    private ListaSimple listaBloqueados;
    private Semaphore semaphore; // Semáforo para sincronización
    private int duracion;
    private String cpuName;

    public Process(int id, String name, int totalInstructions, boolean cpuBound, boolean ioBound, int ciclosExcepcion,
            ListaSimple listaListos, ListaSimple listaBloqueados) {
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

    }

    @Override
    public void run() {
        try {
            while (programCounter < totalInstructions && status != ProcessStatus.BLOCKED) {
                semaphore.acquire(); // Adquirir el semáforo antes de acceder a recursos compartidos

                System.out.println( name + " ejecutando instrucción " + programCounter + " en el cpu:" + cpuName);
                listaListos.printlist();
                Thread.sleep(500);

                programCounter++;

                semaphore.release(); // Liberar el semáforo después de ejecutar la instrucción

                if (ioBound && programCounter % ciclosExcepcion == 0) {
                    status = ProcessStatus.BLOCKED;
                    System.out.println("Proceso " + name + " en espera de E/S");

                    semaphore.release(); // Liberar el semáforo antes de bloquearse

                    // Simular tiempo de espera de E/S en un hilo separado
                    new Thread(() -> {
                        try {
                            Thread.sleep(2000); // Simular tiempo de espera de E/S
                            System.out.println("E/S del proceso" + name + " listo");
                            semaphore.acquire(); // Adquirir el semáforo antes de modificar la lista
                            listaListos.addProcess(this);
                            status = ProcessStatus.READY;
                            semaphore.release(); // Liberar el semáforo después de modificar la lista
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }).start();

                    return; // Volver al inicio del bucle para que el CPU tome el siguiente proceso
                }
            }

            if (programCounter == totalInstructions) {
                status = ProcessStatus.FINISHED;
                listaListos.RemoveProcess(this);
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

}
