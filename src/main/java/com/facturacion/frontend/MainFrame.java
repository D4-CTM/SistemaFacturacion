package com.facturacion.frontend;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.facturacion.backend.UserManager;
import com.facturacion.backend.UserManager.UserManagerException;

import java.awt.Dimension;
import java.io.IOException;
import java.awt.CardLayout;

public class MainFrame extends JFrame {
    private final Dimension minDimension = new Dimension(600, 600);
    private final CardLayout cardLayout = new CardLayout();
    
    public MainFrame(String FrameName) throws IOException, UserManagerException {
        UserManager userManager = new UserManager();

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