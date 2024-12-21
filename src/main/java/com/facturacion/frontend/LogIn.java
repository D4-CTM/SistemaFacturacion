package com.facturacion.frontend;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.io.FileNotFoundException;
import java.io.IOException;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import com.facturacion.backend.UserManager;
import com.facturacion.backend.UserManager.UserManagerException;
import com.facturacion.frontend.BaseAbstractClasses.ResizablePanel;

public class LogIn extends ResizablePanel{
    private final UserManager userManager;    

    public LogIn(JPanel _cardPanel, CardLayout _cardLayout, UserManager _userManager) {
        super(_cardPanel, _cardLayout);
        userManager = _userManager;
        setLayout(new GridBagLayout());
        setName("Log In");
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.weightx = 1.0;
        gbc.weighty = 1.0;
        gbc.fill = GridBagConstraints.NONE;
        gbc.anchor = GridBagConstraints.CENTER;
        infoPanel = new JPanel();
        infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
        infoPanel.setBackground(Color.lightGray);
        add(infoPanel, gbc);

        final JPanel logoPNL = new JPanel();
        logoPNL.setLayout(new BoxLayout(logoPNL, BoxLayout.X_AXIS));
        logoPNL.setOpaque(false);
        logoPNL.add(Box.createGlue());
        logoPNL.add(new JLabel("[ICON]"));
        logoPNL.add(Box.createGlue());
        infoPanel.add(logoPNL);

        final JPanel usernamePNL = new JPanel();
        usernamePNL.setLayout(new GridBagLayout());
        usernamePNL.setOpaque(false);
        
        gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.anchor = GridBagConstraints.CENTER;
        gbc.gridx = 0;
        gbc.gridy = 0;

        usernameLBL = new JLabel("Nombre de usuario:");
        usernamePNL.add(usernameLBL, gbc);

        usernameFLD = new JTextField();
        gbc.gridy = 1;
        usernamePNL.add(usernameFLD, gbc);
        infoPanel.add(usernamePNL);

        final JPanel passwordPNL = new JPanel();
        passwordPNL.setLayout(new GridBagLayout());
        passwordPNL.setOpaque(false);

        passwordLBL = new JLabel("Contraseña:");
        gbc.gridy = 0;
        passwordPNL.add(passwordLBL, gbc);

        passwordFLD = new JPasswordField();
        gbc.gridy = 1;
        passwordPNL.add(passwordFLD, gbc);
        infoPanel.add(passwordPNL);
        
        final JPanel buttonsPNL = new JPanel();
        buttonsPNL.setLayout(new BoxLayout(buttonsPNL, BoxLayout.X_AXIS));
        buttonsPNL.add(Box.createGlue());
        cancelBTN = new JButton();
        cancelBTN.setText("Cancelar");

        buttonsPNL.add(cancelBTN);
        buttonsPNL.add(Box.createGlue());
        acceptBTN = new JButton();
        acceptBTN.setText("Aceptar");

        acceptBTN.addActionListener((ActionEvent e) -> {
            String username = usernameFLD.getText();
            String password = new String(passwordFLD.getPassword());
            try {
                if (userManager.logIn(username, password)) {
                    
                }
            } catch (FileNotFoundException e1) {
                e1.printStackTrace();
            } catch (IOException e1) {
                e1.printStackTrace();
            } catch (UserManagerException message) {
                JOptionPane.showMessageDialog(this, message.getMessage(), "ERROR", JOptionPane.ERROR_MESSAGE);
            }
        });

        buttonsPNL.add(acceptBTN);
        buttonsPNL.add(Box.createGlue());
        buttonsPNL.setOpaque(false);
        infoPanel.add(buttonsPNL);

        setBackground(Color.darkGray);
        showThis();
    }

    @Override
    protected void resizeElements() {
        final int infoPanelHeight = (int) (getSize().getHeight() * 0.8);
        final int infoPanelWidth = (int) ((infoPanelHeight > getSize().getWidth()*0.8) ? getSize().getWidth() * 0.8 : infoPanelHeight);
        infoPanel.setPreferredSize(new Dimension(infoPanelWidth, infoPanelHeight));
        
        final Dimension innerPanelsDimension = new Dimension(infoPanelWidth, infoPanelHeight/4);
        for (final Component panel : infoPanel.getComponents()) {
            if (panel instanceof JPanel) panel.setPreferredSize(innerPanelsDimension);
        }

        final Font font = new Font("Arial", Font.PLAIN, (int) (infoPanelWidth * 0.04));

        final int txtFieldsWidth = (int) (infoPanelWidth * 0.8);
        final int txtFieldsHeight = (int) ((infoPanelHeight/4) * 0.25);
        final Dimension txtFieldsDimension = new Dimension(txtFieldsWidth, txtFieldsHeight);
        usernameLBL.setFont(font);
        usernameLBL.setPreferredSize(txtFieldsDimension);
        usernameFLD.setFont(font);
        usernameFLD.setPreferredSize(txtFieldsDimension);
        passwordLBL.setFont(font);
        passwordLBL.setPreferredSize(txtFieldsDimension);
        passwordFLD.setFont(font);
        passwordFLD.setPreferredSize(txtFieldsDimension);

        final int btnHeight = (int) ((infoPanelHeight/4));
        final int btnWidth = (int) ((infoPanelWidth/3));
        final Dimension btnDimension = new Dimension(btnWidth, btnHeight);
        cancelBTN.setFont(font);
        cancelBTN.setPreferredSize(btnDimension);
        acceptBTN.setFont(font);
        acceptBTN.setPreferredSize(btnDimension);

        revalidate();
        repaint();
    }

    //GUI ELEMENTS
    final JButton cancelBTN;
    final JButton acceptBTN;
    final JLabel usernameLBL;
    final JLabel passwordLBL;
    final JTextField usernameFLD;
    final JPasswordField passwordFLD;
    final JPanel infoPanel;
    //GUI ELEMENTS
}
