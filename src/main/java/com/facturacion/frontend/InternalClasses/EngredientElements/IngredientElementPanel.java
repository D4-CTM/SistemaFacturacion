package com.facturacion.frontend.InternalClasses.EngredientElements;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.RestaurantItems.Ingredient;
import com.facturacion.frontend.InternalClasses.FrontendElements;
import com.facturacion.frontend.MenuOptions.InventoryScene;

public class IngredientElementPanel extends JPanel {
    public int id;

    // sizes
    // id - 0.1 | name - 0.5 | price - 0.3 | removeObjBTN - height
    public IngredientElementPanel(Ingredient ingredient, boolean removable, Dimension panelSize, SQLConnection sql, InventoryScene inventoryScene) {
        setLayout(new FlowLayout(FlowLayout.LEADING, 0, 0));
        setSize(panelSize);

        Object Id = (ingredient == null)? "Id" : ingredient.id;
        Object name = (ingredient == null)? "Nombre" : ingredient.name;
        Object quantity = (ingredient == null)? "Cantidad" : ingredient.quantity + " " + ingredient.unit;

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
            
            removeBTN.addActionListener(event -> {
                if (sql.deleteElement(ingredient)) {
                    inventoryScene.updateComboBox();
                    JOptionPane.showMessageDialog(null, "El ingrediente ha sido eliminado exitosamente", "eliminar ingrediente", JOptionPane.INFORMATION_MESSAGE);
                } else JOptionPane.showMessageDialog(null, "Ha ocurrido un error al intentar eliminar el ingrediente", "eliminar ingrediente", JOptionPane.ERROR_MESSAGE);
            });

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