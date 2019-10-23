package prueba;
  /*
       In this applet, the user types some text in a JTextArea and presses
       a button.  The applet computes and displays the number of lines
       in the text, the number of words in the text, and the number of
       characters in the text.  A word is defined to be a sequence of
       letters, except that an apostrophe with a letter on each side
       of it is considered to be a letter.  (Thus "can't" is one word,
       not two.)
    */
    
    import java.awt.*;
    import java.awt.event.*;
    import javax.swing.*;
    
    
    public class TextCounterApplet extends JApplet 
                                      implements ActionListener {
    
       JTextArea textInput;     // For the user's input text.
       
       JLabel lineCountLabel;   // For displaying the number of lines.
       JLabel wordCountLabel;   // For displaying the number of words.
       JLabel charCountLabel;   // For displaying the number of chars.
    
    
       public void init() {
          
          setBackground(Color.darkGray);
          getContentPane().setBackground(Color.darkGray);
          
          /* Create the text input area and make sure it has a
             white background. */
          
          textInput = new JTextArea();
          textInput.setBackground(Color.white);
          
          /* Create a panel to hold the button and three display
             labels.  These will be laid out in a GridLayout with
             4 rows and 1 column. */
          
          JPanel south = new JPanel();
          south.setBackground(Color.darkGray);
          south.setLayout( new GridLayout(4,1,2,2) );
          
          /* Create the button, set the applet to listen for
             clicks on the button, and add it to the panel. */
          
          JButton countButton = new JButton("Process the Text");
          countButton.addActionListener(this);
          south.add(countButton);
          
          /* Create each of the labels, set their colors, and
             add them to the panel. */
          
          lineCountLabel = new JLabel("  Number of lines:");
          lineCountLabel.setBackground(Color.white);
          lineCountLabel.setForeground(Color.blue);
          lineCountLabel.setOpaque(true);
          south.add(lineCountLabel);
          
          wordCountLabel = new JLabel("  Number of words:");
          wordCountLabel.setBackground(Color.white);
          wordCountLabel.setForeground(Color.blue);
          wordCountLabel.setOpaque(true);
          south.add(wordCountLabel);
          
          charCountLabel = new JLabel("  Number of chars:");
          charCountLabel.setBackground(Color.white);
          charCountLabel.setForeground(Color.blue);
          charCountLabel.setOpaque(true);
          south.add(charCountLabel);
          
          /* Use a BorderLayout on the applet.  Although a BorderLayout
             is the default, I want one with a vertical gap of two
             pixels, to let the dark gray background color show through. */
             
          getContentPane().setLayout( new BorderLayout(2,2) );
             
          /* The text area is put into a JScrollPane to provide
             scroll bars for the TextArea, and the scroll pane is put in
             the Center position.  The panel that holds the button and
             labels is in the South position.  Note that the text area
             will be sized to fill the space that is left after the
             panel is assigned its preferred height. */
          
          JScrollPane scroller = new JScrollPane( textInput );
          getContentPane().add(scroller, BorderLayout.CENTER);
          getContentPane().add(south, BorderLayout.SOUTH);
          
       } // end init();
       
       
       public Insets getInsets() {
             // Leave a 2-pixel border around the edges of the applet.
          return new Insets(2,2,2,2);
       }
       
    
       public void actionPerformed(ActionEvent evt) {
             // Respond when the user clicks on the button by getting
             // the text from the text input area, counting the number
             // of chars, words, and lines that it contains, and
             // setting the labels to display the data.
           
           String text;  // The user's input from the text area.
           
           int charCt, wordCt, lineCt;  // Char, word, and line counts.
           
           text = textInput.getText();
           
           charCt = text.length();  // The number of characters in the
                                    //    text is just its length.
                                    
           /* Compute the wordCt by counting the number of characters
              in the text that lie at the beginning of a word.  The
              beginning of a word is a letter such that the preceding
              character is not a letter.  This is complicated by two
              things:  If the letter is the first character in the
              text, then it is the beginning of a word.  If the letter
              is preceded by an apostrophe, and the apostrophe is
              preceded by a letter, than its not the first character
              in a word.
           */
           
           wordCt = 0;
           for (int i = 0; i < charCt; i++) {
              boolean startOfWord;  // Is character i the start of a word?
              if ( Character.isLetter(text.charAt(i)) == false )
                 startOfWord = false;  // No.  It's not a letter.
              else if (i == 0)
                 startOfWord = true;   // Yes.  It's a letter at start of text.
              else if ( Character.isLetter(text.charAt(i-1)) )
                 startOfWord = false;  // No.  It's a letter preceded by a letter.
              else if ( text.charAt(i-1) == '\'' && i > 1 
                                   && Character.isLetter(text.charAt(i-2)) )
                 startOfWord = false;  // No.  It's a continuation of a word
                                       //      after an apostrophe.
              else
                 startOfWord = true;   // Yes.  It's a letter preceded by
                                       //       a non-letter.
              if (startOfWord)
                 wordCt++;
           }
           
           /* The number of lines is just one plus the number of times the
              end of line character, '\n', occurs in the text. */
           
           lineCt = 1;
           for (int i = 0; i < charCt; i++) {
              if (text.charAt(i) == '\n')
                 lineCt++;
           }
           
           /* Set the labels to display the data. */
           
           lineCountLabel.setText("  Number of Lines:  " + lineCt);
           wordCountLabel.setText("  Number of Words:  " + wordCt);
           charCountLabel.setText("  Number of Chars:  " + charCt);
    
       }  // end actionPerformed()
    
       
    } // end class TextCounterApplet