package prueba;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JFrame;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;


public class TestEditor extends JFrame implements DocumentListener {

	private static final long serialVersionUID = 7840185264770309196L;
	private JTextArea textArea;	
	private JTextField txt_number_off_characters;
	
	private TestEditor() {
		super("Test");
		setDefaultCloseOperation(EXIT_ON_CLOSE);		
		textArea = new JTextArea();
		Font f = new Font("Courier", Font.PLAIN, 12);
		textArea.setFont(f);
		LimitedRowLengthDocument myDoc = new LimitedRowLengthDocument(textArea,16);		
		textArea.setDocument(myDoc);
		myDoc.addDocumentListener(this);		
		textArea.setLineWrap(true);
		textArea.setWrapStyleWord(true);
		txt_number_off_characters = new JTextField();
				
		BorderLayout bl = new BorderLayout();
		this.setLayout(bl);
		
		this.setMinimumSize(new Dimension(400, 250));
		this.setPreferredSize(new Dimension(400, 250));
		
		add(txt_number_off_characters, BorderLayout.SOUTH);
		add(new JScrollPane(textArea), BorderLayout.CENTER);
		
		pack();
		setVisible(true);

	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		new TestEditor();
	}
	
	public class LimitedRowLengthDocument extends DefaultStyledDocument {

		private static final long serialVersionUID = -2059737037274062490L;

	    private static final String EOL = "\n";
		
	    private int max;
	    private JTextArea ta = null;

	    public LimitedRowLengthDocument(JTextArea ta, int max) {
	    	this.ta = ta;
	    	this.max = max;
	    }

	    public void insertString(int offs, String str, AttributeSet attribute) throws BadLocationException { 
	    	
	    	int actRow = ta.getLineOfOffset(offs);
	    	int rowBeginn = ta.getLineStartOffset(actRow);
	    	int rowEnd = ta.getLineEndOffset(actRow);
	      	int referenceValue = 0;
	      	int iaux = 0;
	      	String saux = "";
	      	boolean inserta = true;
	      	/*if (!EOL.equals(str) || occurs(getText(0, getLength()), EOL) < 2 - 1) {
	      		if (str.length() > 1) {
		    		referenceValue = (rowEnd + str.length()) - rowBeginn;
		    		if( str.length() > 32)
		    			str = str.substring(0, 32);
		    		else
		    			str = str.substring(0,getLength());
		    	} else {
		    		referenceValue = rowEnd - rowBeginn;
		    	}*/

		    	if (str.length() > 1) {
		    		referenceValue = (rowEnd + str.length()) - rowBeginn;
		    	} else {
		    		referenceValue = rowEnd - rowBeginn;
		    	}
		    	if (referenceValue >= max) {
		        	if (str.length() > 1) {        		
		        		StringBuffer str_buff = new StringBuffer();
		        		for (int i=0; i<str.length(); i++) {
		        			if (i >= max) {
		        				/*int ocs = occurs(str_buff.toString(), EOL);
		        				if(  ocs < 1)*/
		        					str_buff.append(EOL);
		        				/*else
		        					inserta = false;*/
		        				str_buff.append(str.charAt(i));
		        				
		        				saux += str.substring(iaux,i);
		        				str = str.substring(i, str.length());
		        				/*iaux += i;
		        				if( iaux < (max*2))
		        					i = 0;
		        				else{
		        					str = saux;
		        					break;}*/
		        			} else {
		        				str_buff.append(str.charAt(i));
		        			}
		        		}
		        		/*int ocs = occurs(str_buff.toString(), EOL);
		        		if( ocs < 1)*/
		        			str = str_buff + EOL;
		        		
		        	} else {
		        			/*String text = getText(0, getLength());
		        			int ocs = occurs(text, EOL);
		        			if( ocs < 1)*/
		        				str = EOL + str;
		        			/*else
		        				inserta = false;*/
		        	}    		
		    	}
		    		if( inserta /*&& iaux <= 10*/){
		    			super.insertString(offs, str, attribute);
		    		}
	      	//}
	      	
	    	
	    }
	    public int occurs(String str, String subStr) 
	    {    
	      int occurrences = 0;
	      int fromIndex = 0;

	      while (fromIndex > -1) 
	      {    
	        fromIndex = str.indexOf(subStr, occurrences == 0 ? fromIndex : fromIndex + subStr.length());
	        if (fromIndex > -1) 
	        {    
	          occurrences++;
	        }
	      }

	      return occurrences;
	    }
	}


	public void changedUpdate(DocumentEvent e) {
	}


	public void insertUpdate(DocumentEvent e) {		
		final Document doc = e.getDocument();
		txt_number_off_characters.setText("Textlength: " + String.valueOf(doc.getLength()));		
	}


	public void removeUpdate(DocumentEvent e) {
		Document doc = e.getDocument();
		txt_number_off_characters.setText("Textlength: " + String.valueOf(doc.getLength()));
	}

}