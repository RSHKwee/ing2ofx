package zandbak;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class AboutWindowExample {
    public static void main(String[] args) {
        Frame mainFrame = new Frame("Main Window");
        
        Label aboutLabel = new Label("About");
        aboutLabel.setForeground(Color.BLUE);
        aboutLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        aboutLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                openAboutWindow();
            }
        });
        
        mainFrame.add(aboutLabel);
        
        mainFrame.setSize(300, 200);
        mainFrame.setVisible(true);
    }
    
    private static void openAboutWindow() {
        Frame aboutFrame = new Frame("About Window");
        
        Label websiteLabel = new Label("Visit our website");
        websiteLabel.setForeground(Color.BLUE);
        websiteLabel.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        websiteLabel.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                try {
                    Desktop.getDesktop().browse(new URI("https://www.example.com"));
                } catch (IOException | URISyntaxException ex) {
                    ex.printStackTrace();
                }
            }
        });
        
        aboutFrame.add(websiteLabel);
        
        aboutFrame.setSize(300, 200);
        aboutFrame.setVisible(true);
    }
}
