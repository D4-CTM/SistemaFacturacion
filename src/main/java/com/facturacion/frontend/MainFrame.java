package com.facturacion.frontend;

import javax.swing.JFrame;
import javax.swing.JPanel;
import java.awt.Dimension;
import java.awt.CardLayout;

public class MainFrame extends JFrame {
    private final Dimension minDimension = new Dimension(600, 600);
    private final CardLayout cardLayout = new CardLayout();
    
    public MainFrame(String FrameName) {
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        cardPanel = new JPanel(cardLayout);
        cardPanel.setPreferredSize(minDimension);
        setTitle(FrameName);

        add(cardPanel);
        pack();
        setMinimumSize(getSize());
        setLocationRelativeTo(null);
    }

    // Gui elements
    private final JPanel cardPanel;
}