package Modelo;

import java.io.Serializable;
import java.util.Random;

public class Reserva implements Serializable{
    private String nombre;
    private int cantidadPersonas;
    private String hora;
    private int numeroMesa;

    public Reserva(String nombre, int cantidadPersonas, String hora) {
        this.nombre = nombre;
        this.cantidadPersonas = cantidadPersonas;
        this.hora = hora;
        // Generar un número de mesa aleatorio entre 1 y 20
        this.numeroMesa = generarNumeroMesa();
    }

    public String getNombre() {
        return nombre;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public void setCantidadPersonas(int cantidadPersonas) {
        this.cantidadPersonas = cantidadPersonas;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public int getNumeroMesa() {
        return numeroMesa;
    }

    private int generarNumeroMesa() {
       
        return (int) (Math.random() * 20) + 1;
    }

    @Override
    public String toString() {
        return "Reserva = " +
                "Nombre: '" + nombre +  
                ", Acompañantes: " + cantidadPersonas +
                ", Hora: '" + hora + '\'' +
                ", Mesa: " + numeroMesa +
                "";
    }
}