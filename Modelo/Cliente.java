
package Modelo;
import javax.swing.*;
import javax.swing.plaf.ColorUIResource;

import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.*;

public class Cliente  {
    private static final String SERVIDOR_IP = "localhost";
    private static final int PUERTO = 12345;
    private static ObjectOutputStream salida;
    private static ObjectInputStream entrada;

    public static void main(String[] args) {
        // Configurar el UIManager para los JOptionPane
        UIManager.put("OptionPane.background", new ColorUIResource(106, 75, 13)); 
        UIManager.put("Panel.background", new ColorUIResource(106, 75, 13)); 
        UIManager.put("OptionPane.messageForeground", Color.WHITE); 
        UIManager.put("Button.background", new ColorUIResource(182, 135, 41)); 
        UIManager.put("Button.foreground", Color.WHITE); 

        JFrame frame = new JFrame("Restaurante Sabor Porteño");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLayout(new BorderLayout());
        frame.getContentPane().setBackground(new Color(118, 82, 8)); 

        // Panel para los botones
        JPanel panelBotones = new JPanel(new GridLayout(1, 3));
        panelBotones.setBackground(new Color(118, 82, 8)); 
        frame.add(panelBotones, BorderLayout.SOUTH);

        JButton reservar = new JButton("Reservar");
        reservar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                mostrarInterfazReserva();
            }
        });

        JButton buscar = new JButton("Buscar Reservación");
        buscar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                buscarReservacion();
            }
        });

        JButton modificar = new JButton("Modificar Reservación");
        modificar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                modificarReservacion();
            }
        });

        JButton eliminar = new JButton("Eliminar Reservación");
        eliminar.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                eliminarReservacion();
            }
        });

        
        Color colorBoton = new Color(191, 136, 27);
        reservar.setBackground(colorBoton);
        reservar.setForeground(Color.WHITE);
        buscar.setBackground(colorBoton);
        buscar.setForeground(Color.WHITE);
        modificar.setBackground(colorBoton);
        modificar.setForeground(Color.WHITE);
        eliminar.setBackground(colorBoton);
        eliminar.setForeground(Color.WHITE);

        panelBotones.add(reservar);
        panelBotones.add(buscar);
        panelBotones.add(modificar);
        panelBotones.add(eliminar);

        // Panel para la imagen de fondo
        JPanel panelImagen = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                ImageIcon imagenFondo = new ImageIcon("C:/Users/Lenovo/OneDrive/Documents/ProyectosGit/Tarea2/fotos/caption.jpg");
                g.drawImage(imagenFondo.getImage(), 0, 0, getWidth(), getHeight(), null);
            }
        };
        frame.add(panelImagen, BorderLayout.CENTER);

        // Panel para el título
        JPanel panelTitulo = new JPanel(new FlowLayout(FlowLayout.CENTER));
        JLabel titulo = new JLabel("Restaurante Sabor Porteño");
        titulo.setFont(new Font("Arial", Font.BOLD, 28));
        titulo.setForeground(Color.WHITE);
        panelTitulo.add(titulo);
        panelTitulo.setBackground(new Color(118, 82, 8)); 
        frame.add(panelTitulo, BorderLayout.NORTH);

        frame.setVisible(true);
    }

    private static void mostrarInterfazReserva() {
        JFrame reservaFrame = new JFrame("Reservar mesa");
        reservaFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        reservaFrame.setSize(400, 300);
        reservaFrame.setLayout(new GridLayout(5, 2));
        reservaFrame.getContentPane().setBackground(new Color(118, 82, 8)); 

        JTextField nombreField = new JTextField();
        JTextField personasField = new JTextField();
        JTextField horaField = new JTextField();
        JButton reservarButton = new JButton("Reservar");

        JLabel mensajeLabel = new JLabel();
        mensajeLabel.setForeground(Color.WHITE);
        JLabel nombreLabel = new JLabel("Nombre:");
        nombreLabel.setForeground(Color.WHITE); 
        JLabel cantidadLabel = new JLabel("Cantidad de personas:");
        cantidadLabel.setForeground(Color.WHITE); 
        JLabel horaLabel = new JLabel("Hora:");
        horaLabel.setForeground(Color.WHITE); 

        reservaFrame.add(nombreLabel);
        reservaFrame.add(nombreField);
        reservaFrame.add(cantidadLabel);
        reservaFrame.add(personasField);
        reservaFrame.add(horaLabel);
        reservaFrame.add(horaField);
        reservaFrame.add(new JLabel());
        reservaFrame.add(reservarButton);
        reservaFrame.add(mensajeLabel);

       
        reservarButton.setBackground(new Color(191, 136, 27));
        reservarButton.setForeground(Color.WHITE);

        reservarButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String nombre = nombreField.getText();
                int cantidadPersonas = Integer.parseInt(personasField.getText());
                String hora = horaField.getText();

                try {
                    Socket socket = new Socket(SERVIDOR_IP, PUERTO);
                    salida = new ObjectOutputStream(socket.getOutputStream());
                    entrada = new ObjectInputStream(socket.getInputStream());

                    Reserva reserva = new Reserva(nombre, cantidadPersonas, hora);
                    salida.writeObject(reserva);
                    mensajeLabel.setText("Reserva realizada con éxito.");
                    socket.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        });

        reservaFrame.setVisible(true);
    }

    private static void buscarReservacion() {
        String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre para buscar la reservación:", "Buscar Reservación", JOptionPane.PLAIN_MESSAGE);

        try {
           
            UIManager.put("Button.background", new ColorUIResource(182, 135, 41));

            Socket socket = new Socket(SERVIDOR_IP, PUERTO);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());

            salida.writeObject(nombre);

            Object obj = entrada.readObject();

            if (obj instanceof Reserva) {
                Reserva reserva = (Reserva) obj;
                JOptionPane.showMessageDialog(null, "Reserva encontrada:\n" + reserva, "Resultado de búsqueda", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Reserva no encontrada para el nombre proporcionado.", "Resultado de búsqueda", JOptionPane.ERROR_MESSAGE);
            }

            socket.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }

    private static void modificarReservacion() {
        String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre de la reserva a modificar:", "Modificar Reservación", JOptionPane.PLAIN_MESSAGE);
    int nuevaCantidadPersonas = Integer.parseInt(JOptionPane.showInputDialog(null, "Ingrese la nueva cantidad de personas:", "Modificar Reservación", JOptionPane.PLAIN_MESSAGE));
    String nuevaHora = JOptionPane.showInputDialog(null, "Ingrese la nueva hora:", "Modificar Reservación", JOptionPane.PLAIN_MESSAGE);

    try {
        
        UIManager.put("Button.background", new ColorUIResource(182, 135, 41));

        Socket socket = new Socket(SERVIDOR_IP, PUERTO);
        salida = new ObjectOutputStream(socket.getOutputStream());
        entrada = new ObjectInputStream(socket.getInputStream());

      
        salida.writeObject(nombre);

        Object obj = entrada.readObject();

        if (obj instanceof Reserva) {
            Reserva reservaAntigua = (Reserva) obj;
            Reserva reservaNueva = new Reserva(reservaAntigua.getNombre(), nuevaCantidadPersonas, nuevaHora);

           
            salida.writeObject(reservaNueva);

            // Esperamos una confirmación del servidor
            Object respuesta = entrada.readObject();
            JOptionPane.showMessageDialog(null, "Reservación modificada con éxito.", "Modificar Reservación", JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(null, "Reservación no encontrada para el nombre proporcionado.", "Modificar Reservación", JOptionPane.ERROR_MESSAGE);
        }

       
        salida.close();
        entrada.close();
        socket.close();
    } catch (IOException | ClassNotFoundException ex) {
        ex.printStackTrace();
    }
}

    private static void eliminarReservacion() {
        String nombre = JOptionPane.showInputDialog(null, "Ingrese el nombre para buscar la reservación a eliminar:", "Eliminar Reservación", JOptionPane.PLAIN_MESSAGE);

        try {
            
            UIManager.put("Button.background", new ColorUIResource(182, 135, 41));

            Socket socket = new Socket(SERVIDOR_IP, PUERTO);
            salida = new ObjectOutputStream(socket.getOutputStream());
            entrada = new ObjectInputStream(socket.getInputStream());

           
            salida.writeObject(nombre);

            Object obj = entrada.readObject();

            if (obj instanceof Reserva) {
              
                salida.writeObject(null); 

                JOptionPane.showMessageDialog(null, "Reservación eliminada con éxito.", "Eliminar Reservación", JOptionPane.INFORMATION_MESSAGE);
            } else {
                JOptionPane.showMessageDialog(null, "Reservación no encontrada para el nombre proporcionado.", "Eliminar Reservación", JOptionPane.ERROR_MESSAGE);
            }

            socket.close();
        } catch (IOException | ClassNotFoundException ex) {
            ex.printStackTrace();
        }
    }
}