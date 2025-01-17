package com.facturacion.frontend.MenuOptions;

import javax.swing.JPanel;

import java.awt.Dimension;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.RestaurantItems.Items;
import com.facturacion.frontend.InternalClasses.OptionsHeader;

public class InventoryScene extends JPanel {

    public InventoryScene(SQLConnection sql, Dimension containerSize) {
        final Dimension headerDimension = new Dimension(containerSize.width, (int) (containerSize.getHeight() * 0.075));
        setLayout(null);

        OptionsHeader header = new OptionsHeader(sql, headerDimension, Items.Ingredient);
        add(header);
    }

}
