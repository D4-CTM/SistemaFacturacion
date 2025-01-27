package com.facturacion.frontend;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.UserManager;
import com.facturacion.backend.UserManager.UserManagerException;
import com.facturacion.frontend.InternalClasses.IndexCard;

import java.io.IOException;
import java.awt.Dimension;
import java.awt.Insets;
import java.awt.Toolkit;

public class MainFrame extends JFrame {
    
    private final Dimension getUsableSpaceDimension() {
        setVisible(true);
        pack();
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        Insets frameInsets = getInsets();

        setVisible(false);
        return new Dimension(
            screenDimension.width - insets.left - insets.right - frameInsets.left - frameInsets.right,
            screenDimension.height - insets.top - insets.bottom - frameInsets.top - frameInsets.bottom
        );
    }

    public MainFrame(String FrameName) throws IOException, UserManagerException {
        final SQLConnection sql = new SQLConnection("restaurante", "restaurante4312");
        final UserManager userManager = new UserManager();
        SwingUtilities.invokeLater(() -> {
            final Dimension frameSize = getUsableSpaceDimension();
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            IndexCard indexCard = new IndexCard();
            indexCard.setSize(frameSize);

            indexCard.add(new LogInScene(indexCard, userManager, frameSize), "LogIn");
            indexCard.add(new MenuScene(indexCard, sql, frameSize), "InventoryMenu");
            indexCard.show("LogIn");
            
            add(indexCard);
            pack();
            setVisible(true);
            setResizable(false);
        });
    }

}