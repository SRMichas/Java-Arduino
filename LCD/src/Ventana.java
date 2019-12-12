package src;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import javax.swing.JPanel;
import java.awt.BorderLayout;
import javax.swing.JTextArea;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import java.awt.Font;
import java.awt.FlowLayout;
import javax.swing.JScrollPane;
import java.awt.Dimension;
import javax.swing.JButton;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import net.miginfocom.swing.MigLayout;

public class Ventana extends JFrame implements ActionListener,SerialPortEventListener{
	
	private JTextArea txfMensaje;
	private JButton btnEnviar;
	private JLabel lbtxtEnviado;
	private PanamaHitek_Arduino arduino;
	private boolean algo = false;
	public Ventana() {
		super("Java + Arduino: LCD");
		setResizable(false);
		setSize(new Dimension(500, 300));
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setLocationRelativeTo(null);
		try
		{
			setDefaultLookAndFeelDecorated(true);
			setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI( this );						
		} catch ( Exception excepcion ) {	excepcion.printStackTrace();	}
		JPanel panel = new JPanel();
		FlowLayout flowLayout = (FlowLayout) panel.getLayout();
		flowLayout.setAlignment(FlowLayout.LEFT);
		getContentPane().add(panel, BorderLayout.NORTH);
		
		JLabel lblNewLabel = new JLabel("    Texto a ingresar:");
		lblNewLabel.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblNewLabel);
		
		txfMensaje = new JTextArea(new Docummento());
		txfMensaje.setFont(new Font("Consolas", Font.PLAIN, 16));
		txfMensaje.setLineWrap(true);
		txfMensaje.setWrapStyleWord(true);
		txfMensaje.addKeyListener(new OyeTec());
		
		JScrollPane scrollPane = new JScrollPane(txfMensaje);
		getContentPane().add(scrollPane, BorderLayout.CENTER);
		
		JPanel panel_1 = new JPanel();
		panel_1.setPreferredSize(new Dimension(10, 50));
		getContentPane().add(panel_1, BorderLayout.SOUTH);
		panel_1.setLayout(new MigLayout("", "[][][][][][][][grow][][][][][]", "[][][][grow][]"));
		
		btnEnviar = new JButton("Enviar");
		btnEnviar.addActionListener(this);
		panel_1.add(btnEnviar, "cell 0 1");
		
		JLabel lblNewLabel_1 = new JLabel("Estatus -> ");
		panel_1.add(lblNewLabel_1, "cell 2 1");
		
		lbtxtEnviado = new JLabel("Nada que enviar");
		panel_1.add(lbtxtEnviado, "cell 3 1 3 1,grow");
		setVisible(true);
		activa();
	}
	private SerialPortEventListener listener = new SerialPortEventListener() {
		@Override
		public void serialEvent(SerialPortEvent arg0) {
			try {
				if( arduino.isMessageAvailable() && algo){
					txfMensaje.setEnabled(true);
					btnEnviar.setEnabled(true);
					lbtxtEnviado.setText("Texto enviado con exito!!!");
					algo = false;
				}
			} catch (SerialPortException | ArduinoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
				lbtxtEnviado.setText("Fallo al enviar el texto");
				txfMensaje.setEnabled(true);
				btnEnviar.setEnabled(true);
			}
		}
	};
	private void activa(){
		try {
			arduino = new PanamaHitek_Arduino();
            arduino.arduinoRXTX("COM4", 9600, this);
        } catch (ArduinoException ex) {
            Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	private void envia(){
		String mensaje = txfMensaje.getText();
			
		if( mensaje.length() != 0){
			try {
				arduino.sendData(mensaje);
				txfMensaje.setText("");
				lbtxtEnviado.setText(mensaje);
				txfMensaje.setEnabled(false);
				btnEnviar.setEnabled(false);
				lbtxtEnviado.setText("Enviando texto...");
				algo = true;
			} catch (ArduinoException ex){
				Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
			} catch (SerialPortException ex){
				Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
			}
			
		}else
			JOptionPane.showMessageDialog(null, "No hay texto para enviar", "LCD", 0);
	}
	@Override
	public void actionPerformed(ActionEvent e) {
			envia();	
	}
	class OyeTec extends KeyAdapter{
		@Override
		public void keyPressed(KeyEvent e) {
			if(e.getKeyCode() == KeyEvent.VK_ENTER){
				envia();
            }
		}
	}
	
	class Docummento extends DefaultStyledDocument{

		private static final long serialVersionUID = 1L;
		@Override
		public void insertString(int offs, String str, AttributeSet a) throws BadLocationException {
			if( !str.contains("\n")){
				super.insertString(offs, str, a);
			}
		}
	}
	@Override
	public void serialEvent(SerialPortEvent arg0) {
		try {
			if( arduino.isMessageAvailable() && algo){
				txfMensaje.setEnabled(true);
				btnEnviar.setEnabled(true);
				lbtxtEnviado.setText("Texto enviado con exito!!!");
			}
		} catch (SerialPortException | ArduinoException e) {
			e.printStackTrace();
			lbtxtEnviado.setText("Fallo al enviar el texto");
			txfMensaje.setEnabled(true);
			btnEnviar.setEnabled(true);
		}		
	}
	public static void main(String[] args) {
		new Ventana();

	}
}
