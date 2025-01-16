package com.facturacion.frontend.MenuOptions;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.RestaurantItems.Ingredient;
import com.facturacion.backend.RestaurantItems.Items;
import com.facturacion.frontend.InternalClasses.FrontendElements;
import com.facturacion.frontend.InternalClasses.IndexCard;

public class InventoryScene extends JPanel {
    private final String SEARCH_BAR_DEFAULT_TEXT = "Nombre/Id del ingrediente"; 

    public InventoryScene(IndexCard indexCard, SQLConnection sql, Dimension containerSize) {
        setBackground(FrontendElements.OUTER_BG);
        setSize(containerSize);
        setLayout(null);
        final int headerHeight = (int) (containerSize.height * 0.075);        
        final JButton searchBTN = new JButton();
        searchBTN.setSize(headerHeight, headerHeight);
        add(searchBTN);

        final JTextField searchFLD = new JTextField(SEARCH_BAR_DEFAULT_TEXT);
        searchFLD.setSize((int) (containerSize.width * 0.4), headerHeight);
        searchFLD.setForeground(FrontendElements.UNFOCUSED_SEARCH_BAR);
        searchFLD.setBackground(getBackground());
        searchFLD.setLocation(headerHeight + 15, 0);
        searchFLD.setFont(FrontendElements.SystemFont);
        searchFLD.setOpaque(false);
        searchFLD.setBorder(null);
        add(searchFLD);
        
        final JSeparator searchLine = new JSeparator(JSeparator.HORIZONTAL);
        searchLine.setLocation(searchFLD.getX(), (int) (headerHeight * 0.80));
        searchLine.setSize(searchFLD.getWidth(), 10);
        searchLine.setBackground(FrontendElements.UNFOCUSED_SEARCH_BAR);
        add(searchLine);

        final JButton ingredientBTN = new JButton("Agregar ingrediente");
        ingredientBTN.setSize((int) (containerSize.width * 0.25), headerHeight);
        ingredientBTN.setLocation(containerSize.width - ingredientBTN.getWidth(), 0);       
        ingredientBTN.setFont(FrontendElements.SystemFont);
        add(ingredientBTN);

        searchBTN.addActionListener(event -> {
            String product = searchFLD.getText();
            if (product.equals(SEARCH_BAR_DEFAULT_TEXT) || product.isBlank()) return;

            //If the product contains only numbers we are safe to asume that it's searhing for an id
            if (isNumericOnly(product)) {
                int id = Integer.parseInt(product);

                Object object = sql.fetch(id, Items.Ingredient);
                if (object != null && object instanceof Ingredient ingredient) {
                    System.out.println(ingredient.toString());
                } else System.out.println("wasn't found");

            } else {
                Object object = sql.fetch(product, Items.Ingredient);
                if (object != null && object instanceof Ingredient ingredient) {
                    System.out.println(ingredient.toString());
                } else System.out.println("wasn't found");
            }

        });

        searchFLD.addFocusListener(new FocusListener() {

            @Override
            public void focusGained(FocusEvent e) {
                if (searchFLD.getText().equals(SEARCH_BAR_DEFAULT_TEXT)) {
                    searchFLD.setText("");
                    searchFLD.setForeground(FrontendElements.FOCUSED_SEARCH_BAR);
                    searchLine.setBackground(FrontendElements.FOCUSED_SEARCH_BAR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchFLD.getText().isBlank()) {
                    searchFLD.setText(SEARCH_BAR_DEFAULT_TEXT);
                    searchFLD.setForeground(FrontendElements.UNFOCUSED_SEARCH_BAR);
                    searchLine.setBackground(FrontendElements.UNFOCUSED_SEARCH_BAR);
                }
            }
            
        });

        ingredientBTN.addActionListener(event -> {
            
        });

    }

    //checks if the string contains only numbers.
    private boolean isNumericOnly(String text) {
        for (final char character : text.toCharArray()) {
            if (!Character.isDigit(character)) return false;
        }

        return true;
    }

}
