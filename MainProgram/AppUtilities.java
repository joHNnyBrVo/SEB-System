package MainProgram;

import com.formdev.flatlaf.FlatClientProperties;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.FontFormatException;
import java.io.File;
import java.io.IOException;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class AppUtilities {
    
    // Custom Font Style
    public void customFontStyle(Component component, String fontstyle, float fontSize) throws IOException {
        try {
            File fontStyle = new File("src/resources/fonts/" + fontstyle);
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontStyle).deriveFont(fontSize);
            component.setFont(font);
        } catch (FontFormatException | IOException e) {
            e.printStackTrace();
        }
    }

    // Hover Menu Button
    public void setHoverMenuBtn(JPanel panel) {
        panel.setBackground(Color.decode("#1E282C"));
    }

    // Reset Hover Menu Button
    public void resetHoverMenuBtn(JPanel panel) {
        panel.setBackground(Color.decode("#222D32"));
    }
    
    //Change multiple panel 
    public void setTabPanel(JPanel tabpanel, boolean isVisible){
        tabpanel.setVisible(isVisible);
    }
    
    //Placeholder Method
    public void textPlaceholders(JTextField text, String placeholder) {
        text.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, placeholder);
    }
    
    public void setupComboBoxPlaceholder(JComboBox<String> comboBox, String placeholder) {
        comboBox.insertItemAt(placeholder, 0);
        comboBox.setSelectedIndex(0);
        comboBox.setForeground(Color.GRAY);

        comboBox.addActionListener(e -> {
            if (comboBox.getSelectedIndex() == 0) {
                comboBox.setForeground(Color.GRAY);
            } else {
                comboBox.setForeground(Color.BLACK);
            }
        });
    }
    
    public void setHoverBtn(JButton button, String hoverColor) {
        button.setBackground(Color.decode(hoverColor));
    }

    // Reset Hover Menu Button
    public void resetHoverBtn(JButton button, String defaultColor) {
        button.setBackground(Color.decode(defaultColor));
    }
    
    //Placeholder
    public void initializePlaceholders(JTextField textfield){
        textfield.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Search here...");
    }
}
