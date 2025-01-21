package com.facturacion.frontend.InternalClasses.EngredientElements;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JPanel;

import com.facturacion.backend.SQLConnection;

public class IngredientListPanel extends JPanel {
    
    public IngredientListPanel(SQLConnection sql, Dimension listDimension) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        final Dimension ingredientDimension = new Dimension(listDimension.width, (int) (listDimension.height * 0.1));
        final IngredientElementPanel ingredientElementPanel = new IngredientElementPanel("ID", "NOMBRE", "CANTIDAD", false, ingredientDimension);
        add(ingredientElementPanel);
    }
}
