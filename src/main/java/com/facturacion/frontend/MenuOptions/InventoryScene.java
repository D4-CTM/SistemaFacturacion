package com.facturacion.frontend.MenuOptions;

import javax.swing.JPanel;

import java.awt.Dimension;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.RestaurantItems.Items;
import com.facturacion.frontend.InternalClasses.OptionsHeader;
import com.facturacion.frontend.InternalClasses.EngredientElements.IngredientListPanel;

public class InventoryScene extends JPanel {

    public InventoryScene(SQLConnection sql, Dimension containerSize) {
        final Dimension headerDimension = new Dimension(containerSize.width, (int) (containerSize.getHeight() * 0.075));
        setLayout(null);

        final OptionsHeader header = new OptionsHeader(sql, headerDimension, Items.Ingredient);
        add(header);

        final Dimension ingredientListSpace = new Dimension(containerSize.width, containerSize.height - headerDimension.height);
        final IngredientListPanel ingredientListPanel = new IngredientListPanel(sql, ingredientListSpace);
        ingredientListPanel.setSize(ingredientListSpace);
        ingredientListPanel.setLocation(0, header.getHeight());
        add(ingredientListPanel);

        

    }

}
