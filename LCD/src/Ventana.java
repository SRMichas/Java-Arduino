package src;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTextField;
import javax.swing.JButton;
import javax.swing.JTextArea;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import com.panamahitek.ArduinoException;
import com.panamahitek.PanamaHitek_Arduino;
import jssc.SerialPortException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.SpringLayout;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import java.awt.Font;
import javax.swing.JTabbedPane;

public class Ventana extends JFrame implements ActionListener{
	
	private static final long serialVersionUID = 1L;
	private JButton bntEnviar;
	private JLabel lblCaracteres;
	private JTextArea txaLog;
	private JTextField txfMensaje;
	private JTabbedPane tabbedPane;
	private PanamaHitek_Arduino arduino;
	
	public Ventana() {
		super("Java + Arduino: LCD");
		setResizable(false);
		setSize(500,300);
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		init();
		try
		{
			setDefaultLookAndFeelDecorated(true);
			setDefaultLookAndFeelDecorated(true);
			UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
			SwingUtilities.updateComponentTreeUI( this );			
		}
		catch ( Exception excepcion ) {	excepcion.printStackTrace();	}
		setVisible(true);
	}
	private void init(){
		JPanel panel = new JPanel();
		panel.setBackground(Color.WHITE);
		getContentPane().add(panel, BorderLayout.CENTER);
		SpringLayout sl_panel = new SpringLayout();
		panel.setLayout(sl_panel);
		
		JLabel lbMsg = new JLabel("Texto a ingresar:");
		lbMsg.setFont(new Font("Tahoma", Font.BOLD, 16));
		sl_panel.putConstraint(SpringLayout.NORTH, lbMsg, 39, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, lbMsg, 35, SpringLayout.WEST, panel);
		panel.add(lbMsg);
		
		bntEnviar = new JButton("Enviar");
		bntEnviar.setFont(new Font("Tahoma", Font.PLAIN, 14));
		sl_panel.putConstraint(SpringLayout.EAST, bntEnviar, -87, SpringLayout.EAST, panel);
		bntEnviar.addActionListener(this);
		
		txfMensaje = new JTextField();
		sl_panel.putConstraint(SpringLayout.NORTH, txfMensaje, 19, SpringLayout.SOUTH, lbMsg);
		sl_panel.putConstraint(SpringLayout.WEST, txfMensaje, 47, SpringLayout.WEST, panel);
		sl_panel.putConstraint(SpringLayout.EAST, txfMensaje, 429, SpringLayout.WEST, panel);
		txfMensaje.setFont(new Font("Monospaced", Font.PLAIN, 18));
		txfMensaje.setDocument(new Docummento());
		txfMensaje.addKeyListener(new OyeTec());
		
		panel.add(txfMensaje);
		panel.add(bntEnviar);
		
		lblCaracteres = new JLabel("Caracteres: 0 / 32");
		sl_panel.putConstraint(SpringLayout.SOUTH, txfMensaje, -32, SpringLayout.NORTH, lblCaracteres);
		sl_panel.putConstraint(SpringLayout.NORTH, bntEnviar, 1, SpringLayout.NORTH, lblCaracteres);
		sl_panel.putConstraint(SpringLayout.NORTH, lblCaracteres, 148, SpringLayout.NORTH, panel);
		sl_panel.putConstraint(SpringLayout.WEST, lblCaracteres, 0, SpringLayout.WEST, lbMsg);
		lblCaracteres.setFont(new Font("Tahoma", Font.BOLD, 16));
		panel.add(lblCaracteres);
			
		tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setPreferredSize(new Dimension(0, 120));
		//getContentPane().add(tabbedPane, BorderLayout.SOUTH);
	
		txaLog = new JTextArea();
		txaLog.setEditable(false);
		JScrollPane jsp = new JScrollPane(txaLog);
		tabbedPane.addTab("Log",jsp);
		
		activa();
	}
	private void activa(){
		try {
			arduino = new PanamaHitek_Arduino();
            arduino.arduinoTX("COM4", 9600);
        } catch (ArduinoException ex) {
            Logger.getLogger(Ventana.class.getName()).log(Level.SEVERE, null, ex);
        }
	}
	private void envia(){
		String mensaje = txfMensaje.getText();
			
		if( mensaje.length() != 0){
			try {
				arduino.sendData(mensaje);
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
			String text = getText(0, getLength());
			if( (text.length()+1) <= 32 ){
				super.insertString(offs, str, a);
				int car = txfMensaje.getDocument().getLength();
				lblCaracteres.setText("Caracteres: "+(car)+" / 32");
			}	
		}
		@Override
		public void remove(int offs, int len) throws BadLocationException {
			super.remove(offs, len);
			int car = txfMensaje.getDocument().getLength();
			lblCaracteres.setText("Caracteres: "+(car)+" / 32");
		}
	}
}