package com.facturacion.frontend;

import javax.swing.JFrame;
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
        pack();
        setVisible(true);
        Dimension screenDimension = Toolkit.getDefaultToolkit().getScreenSize();
        Insets insets = Toolkit.getDefaultToolkit().getScreenInsets(getGraphicsConfiguration());
        Insets frameInsets = getInsets();

        return new Dimension(
            screenDimension.width - insets.left - insets.right - frameInsets.left - frameInsets.right,
            screenDimension.height - insets.top - insets.bottom - frameInsets.top - frameInsets.bottom
        );
    }

    public MainFrame(String FrameName) throws IOException, UserManagerException {
        final SQLConnection sql = new SQLConnection("joush", "Delcids4312");
        final UserManager userManager = new UserManager();
        final Dimension frameSize = getUsableSpaceDimension();
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setVisible(true);
        setSize(frameSize);
        setResizable(false);

        IndexCard indexCard = new IndexCard();

        indexCard.add(new LogIn(indexCard, userManager, frameSize), "LogIn");
        indexCard.add(new InventoryMenu(indexCard, sql, frameSize), "InventoryMenu");

        indexCard.show("LogIn");
        add(indexCard);
    }

}