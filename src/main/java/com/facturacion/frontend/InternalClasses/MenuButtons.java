package com.facturacion.frontend.InternalClasses;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JLabel;
import javax.swing.JPanel;

public class MenuButtons extends JPanel{
    public MenuButtons(IndexCard indexCard, Dimension buttonSize, String option) {
        final JLabel optionText = new JLabel(option);
        optionText.setFont(FrontendElements.OptionsFont);

        optionText.setVerticalAlignment(JLabel.CENTER);
        optionText.setHorizontalAlignment(JLabel.CENTER);
        
        optionText.addMouseListener(new MouseAdapter() {
            
            @Override
            public void mouseClicked(MouseEvent e) {
                if (!option.equals("Regresar")) {
                    indexCard.show(option);
                } else {
                    indexCard.show("LogIn");
                }
            }

            @Override
            public void mouseEntered(MouseEvent e) {
                activeBTN(optionText);
            }

            @Override
            public void mouseExited(MouseEvent e) {
                inactiveBTN(optionText);
            }
            
        });
        
        optionText.setPreferredSize(buttonSize);
        inactiveBTN(optionText);
        add(optionText);
    }

    private void activeBTN(final JLabel optionText) {
        setBackground(FrontendElements.BUTTON_BG);
        optionText.setForeground(FrontendElements.BUTTON_FG);
    }

    private void inactiveBTN(final JLabel optionText) {
        setBackground(FrontendElements.DEFAULT_BG);
        optionText.setForeground(FrontendElements.DEFAULT_FG);
    }

}
