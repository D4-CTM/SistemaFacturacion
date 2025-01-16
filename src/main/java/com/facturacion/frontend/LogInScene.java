package com.facturacion.frontend;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.facturacion.backend.UserManager;
import com.facturacion.backend.UserManager.UserManagerException;
import com.facturacion.frontend.InternalClasses.FrontendElements;
import com.facturacion.frontend.InternalClasses.IndexCard;

public class LogInScene extends JPanel{
    private final IndexCard indexCard;
    private final UserManager userManager;

    public LogInScene(IndexCard _indexCard, UserManager _userManager, Dimension frameSize) {
        userManager = _userManager;
        indexCard = _indexCard;

        setBackground(FrontendElements.OUTER_BG);
        setPreferredSize(frameSize);
        setLayout(null);
        
        final JPanel innerPanel = new JPanel(null);
        final int innerPanelSize = (int) (frameSize.height * 0.75);
        final Point innerPanelPoint = new Point(frameSize.width/2 - innerPanelSize/2, frameSize.height/2 - innerPanelSize/2);
        innerPanel.setLocation(innerPanelPoint);
        innerPanel.setSize(innerPanelSize, innerPanelSize);
        innerPanel.setBackground(FrontendElements.DEFAULT_BG);

        final int thirdOfInnerPanel = (int) (innerPanelSize/3);
        JLabel companyLogo = new JLabel();
        companyLogo.setLocation(innerPanelSize/2 - thirdOfInnerPanel/2, 0);
        companyLogo.setSize(thirdOfInnerPanel, thirdOfInnerPanel);
        companyLogo.setBorder(BorderFactory.createLineBorder(Color.white));
        innerPanel.add(companyLogo);

        final JLabel usernameTXT = new JLabel("Nombre de usuario:");
        usernameTXT.setLocation(40, thirdOfInnerPanel + thirdOfInnerPanel/4);
        usernameTXT.setSize(innerPanelSize - 80, thirdOfInnerPanel - (int) (thirdOfInnerPanel * 0.75));
        usernameTXT.setFont(FrontendElements.SystemFont);
        usernameTXT.setForeground(FrontendElements.DEFAULT_FG);
        innerPanel.add(usernameTXT);

        final JTextField usernameFLD = new JTextField();
        usernameFLD.setLocation(usernameTXT.getX(), usernameTXT.getY() + usernameTXT.getHeight());
        usernameFLD.setSize(usernameTXT.getSize());
        usernameFLD.setFont(FrontendElements.SystemFont);
        innerPanel.add(usernameFLD);

        final JLabel passwordTXT = new JLabel("Contraseña:");
        passwordTXT.setLocation(40, (2*thirdOfInnerPanel) - thirdOfInnerPanel/6);
        passwordTXT.setSize(usernameFLD.getSize());
        passwordTXT.setFont(FrontendElements.SystemFont);
        passwordTXT.setForeground(FrontendElements.DEFAULT_FG);
        innerPanel.add(passwordTXT);

        final JPasswordField passwordFLD = new JPasswordField();
        passwordFLD.setLocation(passwordTXT.getX(), passwordTXT.getY() + passwordTXT.getHeight());
        passwordFLD.setSize(passwordTXT.getSize());
        passwordFLD.setFont(FrontendElements.SystemFont);
        innerPanel.add(passwordFLD);        

        final JButton cancelBTN = new JButton("Cancelar");
        cancelBTN.setSize(thirdOfInnerPanel, (int) (thirdOfInnerPanel * 0.30));
        cancelBTN.setLocation(40, innerPanelSize - cancelBTN.getHeight() - 50);
        cancelBTN.setFont(FrontendElements.SystemFont);
        innerPanel.add(cancelBTN);

        final JButton acceptBTN = new JButton("Acceder");
        acceptBTN.setSize(cancelBTN.getSize());
        acceptBTN.setLocation(innerPanelSize - acceptBTN.getWidth() - 50, cancelBTN.getY());
        acceptBTN.setFont(FrontendElements.SystemFont);
        acceptBTN.addActionListener(event -> {
            String username = usernameFLD.getText();
            String password = new String(passwordFLD.getPassword());

            try {
                if (userManager.logIn(username, password)) {
                    indexCard.show("InventoryMenu");
                }
            } catch (UserManagerException e) {
                JOptionPane.showMessageDialog(this, e.getMessage(), "Inicio de sesion", JOptionPane.ERROR_MESSAGE);
            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "No se ha podido acceder al archivo, por favor asegurese que la carpeta 'Reportes' exista,\ncaso contrario, por favor reinicie el programa.",  "Inicio de sesion", JOptionPane.ERROR_MESSAGE);
            }
        });
        innerPanel.add(acceptBTN);

        add(innerPanel);
    }

}
