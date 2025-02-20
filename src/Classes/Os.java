/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Classes;

import DataStructures.ListaSimple;
import Interfaz.InterfazInicial;
import java.util.concurrent.Semaphore;

/**
 *
 * @author jesusventura
 */
public class Os extends Thread {

    private int id;
    private String name;
    private int programCounter;
    private int mar;
    private int totalInstructions;
    private Semaphore semaphore; // Semáforo para sincronización
    private String cpuName;
    private int velocidadReloj;
    private InterfazInicial interfaz;
    private int cpu;
    private boolean isRunning;

    public Os(int id, String name, int totalInstructions,
            int velocidadReloj, InterfazInicial interfaz, int cpu, boolean isRunning) {
        this.id = id;
        this.name = name;
        this.programCounter = 0;
        this.mar = 0;
        this.totalInstructions = totalInstructions;
        this.semaphore = semaphore; // Semáforo compartido
        this.cpuName = cpuName;
        this.velocidadReloj = velocidadReloj;
        this.interfaz = interfaz;
        this.cpu = cpu;
        this.isRunning = isRunning;

    }

    public void run() {

        try {
            while (programCounter < totalInstructions ) {
                
                
                isRunning = interfaz.isIsRunning();
                
                if (!isRunning) {
                    return;
                    
                }

                semaphore.acquire();

                interfaz.actualizarInterfazCPU(cpu, String.valueOf(id), "RUNNING", String.valueOf(programCounter), String.valueOf(mar), name, String.valueOf(totalInstructions));

                System.out.println(name + " ejecutando instrucción " + programCounter + " en el cpu:" + cpuName);
                System.out.println("siguiente iteración-------------->");
                Thread.sleep(velocidadReloj);

                programCounter++;
                semaphore.release();

            }
            if (programCounter == totalInstructions) {
                System.out.println("Proceso " + name + " finalizado.");
                return;
            }

        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
        }

    }

    public int getIdos() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNameos() {
        return name;
    }

    public void setNameos(String name) {
        this.name = name;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void setProgramCounter(int programCounter) {
        this.programCounter = programCounter;
    }


    public int getTotalInstructions() {
        return totalInstructions;
    }

    public void setTotalInstructions(int totalInstructions) {
        this.totalInstructions = totalInstructions;
    }

    public Semaphore getSemaphore() {
        return semaphore;
    }

    public void setSemaphore(Semaphore semaphore) {
        this.semaphore = semaphore;
    }

    public String getCpuName() {
        return cpuName;
    }

    public void setCpuName(String cpuName) {
        this.cpuName = cpuName;
    }

    public int getVelocidadReloj() {
        return velocidadReloj;
    }

    public void setVelocidadReloj(int velocidadReloj) {
        this.velocidadReloj = velocidadReloj;
    }

    public InterfazInicial getInterfaz() {
        return interfaz;
    }

    public void setInterfaz(InterfazInicial interfaz) {
        this.interfaz = interfaz;
    }

    public int getCpu() {
        return cpu;
    }

    public void setCpu(int cpu) {
        this.cpu = cpu;
    }

}
