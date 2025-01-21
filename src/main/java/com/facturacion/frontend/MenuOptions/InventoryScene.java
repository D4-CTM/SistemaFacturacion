package com.facturacion.frontend.MenuOptions;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import java.awt.Dimension;
import java.util.LinkedList;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.RestaurantItems.Ingredient;
import com.facturacion.backend.RestaurantItems.Items;
import com.facturacion.frontend.InternalClasses.FrontendElements;
import com.facturacion.frontend.InternalClasses.OptionsHeader;
import com.facturacion.frontend.InternalClasses.EngredientElements.IngredientElementPanel;

public class InventoryScene extends JPanel {

    public InventoryScene(SQLConnection sql, Dimension containerSize) {
        final int scrollBarWidth = 15;
        final int scrollPaneHeight = (int) (containerSize.height * (1 - (0.075 + 0.075 + 0.1)));  

        final Dimension headerDimension = new Dimension(containerSize.width, (int) (containerSize.getHeight() * 0.075));
        final Dimension ingredientDimension = new Dimension(containerSize.width - (scrollBarWidth + 4), (int) (containerSize.height * 0.1));
        final Dimension scrollPaneDimension = new Dimension(containerSize.width, scrollPaneHeight);
        setBackground(FrontendElements.OUTER_BG);
        setLayout(null);

        final OptionsHeader header = new OptionsHeader(sql, headerDimension, Items.Ingredient);
        add(header);

        final IngredientElementPanel ingredientHeader = new IngredientElementPanel("Id", "Nombre", "Cantidad", false, ingredientDimension);
        ingredientHeader.setLocation(0, headerDimension.height);
        add(ingredientHeader);

        final JPanel ingredientPanel = new JPanel();
        ingredientPanel.setLayout(new BoxLayout(ingredientPanel, BoxLayout.Y_AXIS));
        ingredientPanel.setBackground(FrontendElements.OUTER_BG);

        final JScrollPane scrollPane = new JScrollPane(ingredientPanel);

        final JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setPreferredSize(new Dimension(scrollBarWidth, scrollPaneDimension.height));
        scrollBar.setUnitIncrement(5);

        scrollPane.setVerticalScrollBar(scrollBar);
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setLocation(0, ingredientHeader.getY() + ingredientHeader.getHeight());
        scrollPane.setSize(scrollPaneDimension);
        add(scrollPane);

        showElements(ingredientPanel, ingredientDimension, sql.getNextIngredients());
    }

    private void showElements(JPanel ingredientPanel, Dimension ingredientDimension, LinkedList<Ingredient> list) {
        if (list == null) return;
        ingredientPanel.removeAll();

        for (final Ingredient ingredient : list) {
            ingredientPanel.add(new IngredientElementPanel(ingredient.id, ingredient.name, ingredient.quantity, true, ingredientDimension));
        }
    }

}
