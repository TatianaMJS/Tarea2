package Modelo;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.List;

public class ClienteH extends Thread {
    private Socket clienteSocket;
    private List<Reserva> reservas;
    private ObjectOutputStream salida;
    private ObjectInputStream entrada;

    public ClienteH(Socket socket, List<Reserva> reservas) {
        this.clienteSocket = socket;
        this.reservas = reservas;
    }

    @Override
    public void run() {
        try {
            salida = new ObjectOutputStream(clienteSocket.getOutputStream());
            entrada = new ObjectInputStream(clienteSocket.getInputStream());

            Object obj;
            while ((obj = entrada.readObject()) != null) {
                if (obj instanceof Reserva) {
                    Reserva reservacionNueva = (Reserva) obj;
                    Servidor.guardarReserva(reservacionNueva);
                } else if (obj instanceof String) {
                    String nombre = (String) obj;
                    buscarReserva(nombre);
                } else if (obj == null) {
                    eliminarReserva();
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                clienteSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void buscarReserva(String nombre) {
        for (Reserva reserva : reservas) {
            if (reserva.getNombre().equalsIgnoreCase(nombre)) {
                try {
                    salida.writeObject(reserva);
                    return;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        try {
            salida.writeObject(null); // No se encontró la reserva
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarMensaje(Object mensaje) {
        try {
            salida.writeObject(mensaje);
            salida.flush(); // Forzar la escritura del mensaje
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void enviarReserva(Reserva reserva) {
        try {
            salida.writeObject(reserva);
            salida.flush(); // Forzar la escritura de la reserva
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void eliminarReserva() {
        if (!reservas.isEmpty()) {
            reservas.remove(reservas.size() - 1); // Elimina la última reserva de la lista
            System.out.println("Reserva eliminada.");
            Servidor.notificarClientes("Eliminado");
        } else {
            System.out.println("No hay reservas para eliminar.");
        }
    }

    public void modificarReserva(Reserva nuevaReserva) {
        try {
            salida.writeObject(nuevaReserva); // Envía la nueva reserva al servidor para su modificación
            salida.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}