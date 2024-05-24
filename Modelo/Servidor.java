package Modelo;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class Servidor {
    private static final int PUERTO = 12345;
    private static List<ClienteH> clientesConectados = new ArrayList<>();
    private static List<Reserva> reservas = new ArrayList<>();

    public static void main(String[] args) {
        try (ServerSocket servidor = new ServerSocket(PUERTO)) {
            System.out.println("Servidor iniciado. Esperando conexiones...");

            while (true) {
                Socket cliente = servidor.accept();
                System.out.println("Cliente conectado desde " + cliente.getInetAddress());

                ClienteH clienteHilo = new ClienteH(cliente, reservas);
                clientesConectados.add(clienteHilo);
                clienteHilo.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static synchronized void modificarReserva(Reserva nuevaReserva) {
        boolean encontrada = false;
        for (int i = 0; i < reservas.size(); i++) {
            Reserva reserva = reservas.get(i);
            if (reserva.getNombre().equalsIgnoreCase(nuevaReserva.getNombre())) {
                reservas.set(i, nuevaReserva);
                System.out.println("Reserva modificada y guardada: " + nuevaReserva);
                encontrada = true;
                break;
            }
        }
        if (!encontrada) {
            System.out.println("No se encontró la reserva para modificar.");
            return;
        }
        
        notificarClientes("Modificado");
        // Enviar la reserva modificada al cliente
        for (ClienteH cliente : clientesConectados) {
            cliente.enviarReserva(nuevaReserva);
        }
    }

    public static synchronized void eliminarReserva(String nombreReserva) {
        for (Iterator<Reserva> iterator = reservas.iterator(); iterator.hasNext();) {
            Reserva reserva = iterator.next();
            if (reserva.getNombre().equalsIgnoreCase(nombreReserva)) {
                iterator.remove();
                System.out.println("Reserva eliminada: " + reserva);
                notificarClientes("Eliminado");
                return;
            }
        }
        System.out.println("No se encontró la reserva para eliminar.");
    }

    public static synchronized void guardarReserva(Reserva reserva) {
        reservas.add(reserva);
        System.out.println("Reserva guardada: " + reserva);
        notificarClientes("Guardado");
    }

    public static synchronized void notificarClientes(Object mensaje) {
        for (Iterator<ClienteH> iterator = clientesConectados.iterator(); iterator.hasNext();) {
            ClienteH cliente = iterator.next();
            cliente.enviarMensaje(mensaje);
        }
    }
}