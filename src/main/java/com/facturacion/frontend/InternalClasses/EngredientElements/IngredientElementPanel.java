package com.facturacion.frontend.InternalClasses.EngredientElements;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

import com.facturacion.frontend.InternalClasses.FrontendElements;

public class IngredientElementPanel extends JPanel {
    public int id;

    // sizes
    // id - 0.1 | name - 0.5 | price - 0.3 | removeObjBTN - height
    public IngredientElementPanel(Object Id, Object name, Object quantity, boolean removable, Dimension panelSize) {
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        setSize(panelSize);

        try {
            id = Integer.parseInt(Id.toString());
        } catch (Exception e) {
            id = -1;
        }

        if (removable) {
            setBackground(FrontendElements.OUTER_BG);
        } else setBackground(FrontendElements.INGREDIENT_PANEL_HEADER_BG);

        final int usableWidth = panelSize.width - panelSize.height;
        
        final int idWidth = (int) (usableWidth * 0.1);
        final JLabel idLBL = new JLabel(Id.toString());
        idLBL.setPreferredSize(new Dimension(idWidth, panelSize.height));
        idLBL.setOpaque(false);
        add(idLBL);

        final int nameWidth = (int) (usableWidth * 0.5);
        final JLabel nameLBL = new JLabel(name.toString());
        nameLBL.setPreferredSize(new Dimension(nameWidth, panelSize.height));
        nameLBL.setOpaque(false);
        add(nameLBL);

        final int quantityWidth = (int) (usableWidth * 0.4);
        final JLabel quantityLBL = new JLabel(quantity.toString());
        quantityLBL.setPreferredSize(new Dimension(quantityWidth, panelSize.height));
        quantityLBL.setOpaque(false);
        add(quantityLBL);

        if (removable) {
            final JButton removeBTN = new JButton();
            removeBTN.setPreferredSize(new Dimension(panelSize.height, panelSize.height));
            removeBTN.setForeground(FrontendElements.DELETE_BUTTON_FG);
            removeBTN.setBackground(FrontendElements.DELETE_BUTTON_BG);
            removeBTN.setBorder(FrontendElements.DELETE_BUTTON_BORDER);
            add(removeBTN);
        } else {
            final JPanel extraPNL = new JPanel();
            extraPNL.setPreferredSize(new Dimension(panelSize.height, panelSize.height));
            extraPNL.setBackground(FrontendElements.OUTER_BG);
            add(extraPNL);
        }


        for (final Component component : getComponents()) {
            component.setForeground(FrontendElements.SELECTED_INGREDIENT_PANEL_FG);
            if (component instanceof JLabel LBL) {
                LBL.setFont(FrontendElements.SystemFont);
                LBL.setBorder(FrontendElements.DEFAULT_BORDER);
                LBL.setHorizontalAlignment(JLabel.CENTER);
            }
        }
    }

}