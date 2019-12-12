import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.HeadlessException;
import java.awt.Label;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import jssc.SerialPortException;
/**
 *
 * @author Alberto Loera
 */
public class VentanaPrincipal extends JFrame
{
    ArrayList <String> listaPuertos = new ArrayList <String>();
    PanamaHitek_Arduino arduino = new PanamaHitek_Arduino();
    JPanel panel  = new JPanel();
    boolean motor = false,led = false,conectado = false;
    JButton botonLed = new JButton("LED ON/OFF");
    JButton botonConn = new JButton("CONECTAR BLTH/DESC");
   
    JButton botonMotor = new JButton("MOTOR ON/OFF");
    JTextField lcdText  = new JTextField(25);
    Label contadorDeteclas = new Label();
    int contador = 32;
    public VentanaPrincipal() throws HeadlessException {
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setVisible(true);
        Dimension pantalla = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(pantalla.width - 600, pantalla.height - 200);
        Dimension ventana = getSize();
        setLocation((pantalla.width - ventana.width) / 2, (pantalla.height - ventana.height) / 2);
        try {
            init();
        } catch (ArduinoException ex) {
            Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
        }
        panel.updateUI();
        panel.repaint();
        pack();
    }
    public void init() throws ArduinoException
    {
        contadorDeteclas.setText("32");
        listaPuertos = (ArrayList) arduino.getSerialPorts();
        
        Oyente oyente = new Oyente();
        add(panel);
        panel.setLayout(new FlowLayout(FlowLayout.CENTER,10, 50));
        botonLed.addActionListener(oyente);
        botonConn.addActionListener(oyente);
        botonMotor.addActionListener(oyente);
        lcdText.addKeyListener(oyente);
        panel.add(lcdText);
        panel.add(botonLed);
        
       
        panel.add(botonMotor);
        panel.add(contadorDeteclas);
        panel.add(botonConn);
        for(String s : listaPuertos)
        {
            
            System.out.println(s);
        }
        
       
    }
    public class Oyente implements ActionListener,KeyListener
    {
        public Oyente() {   
        }
        @Override
        public void keyPressed(KeyEvent e) 
        {
            if(e.getKeyCode() == KeyEvent.VK_ENTER)
            {
                try {
                    
                    arduino.sendData(lcdText.getText());
                    
                    
                } catch (ArduinoException ex) 
                {
                    Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                } catch (SerialPortException ex) 
                {
                    Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }    
        }
        @Override
        public void actionPerformed(ActionEvent e) {
            if(e.getSource() == botonLed)
            {
                
                try {
                   led = !led;
                   if(led)
                    arduino.sendData("0");
                   else
                    arduino.sendData("1");
                } catch (ArduinoException ex) {
                    JOptionPane.showMessageDialog(null,"No hay conexion con el dispositivo BT");
                } catch (SerialPortException ex) {
                    Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }  if(e.getSource() == botonMotor)
            {
                
                try {
                   motor = !motor;
                   if(motor)
                    arduino.sendData("2");
                   else
                    arduino.sendData("3");
                    
                } catch (ArduinoException ex) {
                    JOptionPane.showMessageDialog(null,"No hay conexion con el dispositivo BT");
                } catch (SerialPortException ex) {
                    Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            else if(e.getSource() == botonConn)
            {
                try {
                    conexionBluetooth();
                } catch (ArduinoException ex) {
                    Logger.getLogger(VentanaPrincipal.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(null,"Hubo un error al intentar conetar BT, revisa el dispositivo o configuracion");
                }
            }
        }
        @Override
        public void keyTyped(KeyEvent e) 
        {
           if (e.getKeyChar()>=97 && e.getKeyChar()<=122) {
               String cadena  = lcdText.getText();
               if(contador > 0 )   
               {
               contador -=1;
               contadorDeteclas.setText(String.valueOf(contador));
               }
               else {
                   JOptionPane.showMessageDialog(null, "Se ha sobrepasdo los bytes disponibles");
                   String cadena2 = cadena.substring(0,cadena.length()-1);
                   lcdText.setText(cadena2);
            }
        }
           else if(e.getKeyChar() == 8)
           {
             if(contador < 32)
             {
             contador +=1;
             contadorDeteclas.setText(String.valueOf(contador));
             }
           }
        }

        @Override
        public void keyReleased(KeyEvent e) {
            
        }
        
        
    }
    public void conexionBluetooth() throws ArduinoException
    {
        if(!conectado)
        {
        arduino.arduinoTX("COM5", 38400);
        conectado = true;
        JOptionPane.showMessageDialog(null,"Conexion Con exito en el puerto BT");
        }
        else
        {
            arduino.killArduinoConnection();
            conectado = false;
            JOptionPane.showMessageDialog(null,"Desconexion Con exito en el puerto BT");
        }
    }
       
}
