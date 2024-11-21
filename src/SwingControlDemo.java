import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import com.formdev.flatlaf.FlatDarculaLaf;
import com.formdev.flatlaf.FlatLaf;

import java.net.MalformedURLException;
import java.util.Collections;
import java.net.URL;


public class SwingControlDemo implements ActionListener {
    private JFrame mainFrame;
    private JLabel statusLabel;
    private JLabel label1;
    private JLabel label2;
    private JPanel controlPanel;
    private JPanel controlPanel2;
    private JPanel controlPanel3;
    private JPanel controlPanel4;
    private JPanel controlPanel5;
    private JPanel controlPanel6;
    public static JTextArea linkInput; //typing area
    public static JTextArea output;
    public static JTextArea title;
    public static JTextArea temperature;
    public static JTextArea info;

    private int WIDTH=400;
    private int HEIGHT=600;



    public SwingControlDemo() {
        prepareGUI();
    }

    public static void main(String[] args) throws MalformedURLException {
        FlatDarculaLaf.setup();
        FlatLaf.setGlobalExtraDefaults(Collections.singletonMap("@accentColor", "#40E0D0"));
        UIManager.put("Button.arc", 20); // Round buttons
        UIManager.put("Label.arc", 20);
        UIManager.put("Component.focusColor", Color.CYAN); // Cyan focus color
        UIManager.put("Panel.background", Color.DARK_GRAY); // Dark gray
        UIManager.put("TextField.focusedBackground", "#40E0D0");
        UIManager.put("ScrollBar.thumb", new Color(0x40E0D0)); // Turquoise for thumb

        SwingControlDemo swingControlDemo = new SwingControlDemo();
        swingControlDemo.showEventDemo();


    }

    private void prepareGUI() {
        mainFrame = new JFrame("Weather");
        mainFrame.setSize(WIDTH, HEIGHT);
        mainFrame.setLayout(new GridLayout(3, 1));

        controlPanel = new JPanel();
        controlPanel.setLayout(new GridLayout(3, 1));

        controlPanel2 = new JPanel();
        controlPanel2.setLayout(new GridLayout(2, 1));

        controlPanel3 = new JPanel();
        controlPanel3.setLayout(new GridLayout(1, 2));

        controlPanel4 = new JPanel();
        controlPanel4.setLayout(new GridLayout(1, 1));

        controlPanel5 = new JPanel();
        controlPanel5.setLayout(new GridLayout(1, 1));

        controlPanel6 = new JPanel();
        controlPanel6.setLayout(new GridLayout(1, 2));

        linkInput = new JTextArea();
        linkInput.setBounds(50, 5, WIDTH-100, 100);

        output = new JTextArea();
        title = new JTextArea();
        temperature = new JTextArea();
        info = new JTextArea();


        mainFrame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent windowEvent) {
                System.exit(0);
            }
        });
        mainFrame.setVisible(true);
    }

    private void showEventDemo() throws MalformedURLException {


        //output.setBounds(50, 1, WIDTH-100, 100);
        output.setLineWrap(true);  // Wrap lines
        output.setWrapStyleWord(true);  //  Wrap at word boundaries


        title.setFont(new Font("Arial", Font.BOLD, 48));
        temperature.setFont(new Font("Arial", Font.BOLD, 60));
        info.setFont(new Font("Serif", Font.PLAIN, 20));

        JButton button1 = new JButton("Search");
        JButton button2 = new JButton("Button 2");
        JButton button3 = new JButton("Button 3");
        JButton button4 = new JButton("Button 4");
        JButton button5 = new JButton("Button 5");

        JScrollPane scrollPane = new JScrollPane(output);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);

        button1.setActionCommand("Start");
        button1.addActionListener(this);

        JLabel label1 = new JLabel("Search for a City:", JLabel.CENTER);
        label2 = new JLabel();
        JLabel label3 = new JLabel("60°");
        label3.setFont(new Font("Arial", Font.BOLD, 48));
        JLabel label4 = new JLabel("<html>high temp: " + temperature + "&#176;C<br/>low temp: 60&#176;C<br/>wind:</html>");
        JLabel label5 = new JLabel ("low tempt");
        JLabel label6 = new JLabel("      ");
        label6.setFont(new Font("Arial", Font.BOLD, 30));
        //JLabel image = new JLabel(imageIcon);




       /* mainFrame.add(button1);
        mainFrame.add(button2);
        mainFrame.add(button3);
        mainFrame.add(button4);*/
        mainFrame.add(controlPanel);
        mainFrame.add(controlPanel2);

        controlPanel.add(label1);
        controlPanel.add(linkInput);
        controlPanel.add(button1);

        controlPanel2.add(controlPanel5);
        controlPanel5.add(title);


        controlPanel2.add(controlPanel6);
        controlPanel6.add(controlPanel3);
        controlPanel6.add(controlPanel4);

        //set image

        controlPanel3.add(label2);

        controlPanel3.add(temperature);
        controlPanel4.add(info);

        mainFrame.add(output);
        output.append("test");


        //controlPanel2.add(label2);
        //controlPanel2.add(output);

        mainFrame.setVisible(true);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if ("Start".equals(e.getActionCommand())) {
            output.setText("");
            temperature.setText("");
            info.setText("");
            title.setText("");
            ReadJson ReadJson = new ReadJson();

            //create image
            URL imageUrl = null;
            try {
                imageUrl = new URL(ReadJson.iconLink);
            } catch (MalformedURLException ex) {
                throw new RuntimeException(ex);
            }
            ImageIcon icon = new ImageIcon(imageUrl);
            label2.setIcon(icon);
        }

    }
    public static String getLinkInput() {
        return linkInput.getText();
    }



    private class ButtonClickListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            String command = e.getActionCommand();


        }
    }
    public static void errorMessage() {
        output.append("Your URL is invalid. Please check for typos and try again."+ "\n");
        output.append("Tip: ensure that your URL begins with https://www...");
    }

    //generic method to write to any j text area
    public static void writeTo(JTextArea destination, String parameter) {
        destination.append(parameter);
    }
}