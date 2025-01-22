package com.facturacion.frontend.InternalClasses;

import java.awt.Dimension;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;

import javax.swing.JButton;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;

import com.facturacion.backend.RestaurantItems.Ingredient;
import com.facturacion.backend.RestaurantItems.Items;
import com.facturacion.frontend.MenuOptions.EngredientElements.IngredientPane;
import com.facturacion.frontend.MenuOptions.EngredientElements.InventoryScene;
import com.facturacion.backend.SQLConnection;

public class OptionsHeader extends JPanel {
    private final String SEARCH_BAR_DEFAULT_TEXT; 

    public OptionsHeader(SQLConnection sql, Dimension headerSize, Items item, Object itemPane, Object inventoryScene) {
        

        SEARCH_BAR_DEFAULT_TEXT = switch (item) {
            case Ingredient -> {
                yield "Nombre/Id del ingrediente";
            }
            
            case Plate -> {
                yield "Nombre/Id del platillo";
            }

            default -> "";
        };

        setBackground(FrontendElements.OUTER_BG);
        setSize(headerSize);
        setLayout(null);
        final int headerHeight = headerSize.height;
        final JButton searchBTN = new JButton();
        searchBTN.setSize(headerHeight, headerHeight);
        add(searchBTN);

        final JTextField searchFLD = new JTextField(SEARCH_BAR_DEFAULT_TEXT);
        searchFLD.setSize((int) (headerSize.width * 0.4), headerHeight);
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

        final JButton ingredientBTN = new JButton("Agregar " + (item == Items.Ingredient ? "ingrediente" : "platillo"));
        ingredientBTN.setSize((int) (headerSize.width * 0.25), headerHeight);
        ingredientBTN.setLocation(headerSize.width - ingredientBTN.getWidth(), 0);       
        ingredientBTN.setFont(FrontendElements.SystemFont);
        add(ingredientBTN);

        searchBTN.addActionListener(event -> {
            String product = searchFLD.getText();
            if (product.equals(SEARCH_BAR_DEFAULT_TEXT) || product.isBlank()) return;

            //If the product contains only numbers we are safe to asume that it's searhing for an id
            if (isNumericOnly(product)) {
                
                if (item == Items.Ingredient) {

                    Ingredient ingredient = searchIngredient(sql, item, Integer.parseInt(product));
                    if (ingredient == null) {
                        JOptionPane.showMessageDialog(null, "No se ha podido encontrar ningun ingrediente con el id: " + product, "No encontrado", JOptionPane.WARNING_MESSAGE);
                        return ;
                    }

                    ((IngredientPane) itemPane).modifyIngredient(ingredient);
                    ((InventoryScene) inventoryScene).updateComboBox();
                }
                
            } else {

                if (item == Items.Ingredient) {

                    Ingredient ingredient = searchIngredient(sql, item, product);
                    if (ingredient == null) {
                        JOptionPane.showMessageDialog(null, "No se ha podido encontrar ningun ingrediente llamado: " + product, "No encontrado", JOptionPane.WARNING_MESSAGE);
                        return ;
                    }
    
                    ((IngredientPane) itemPane).modifyIngredient(ingredient);
                    ((InventoryScene) inventoryScene).updateComboBox();
                }

            }

        });

        searchFLD.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
                if (searchFLD.getText().equals(SEARCH_BAR_DEFAULT_TEXT)) {
                    searchFLD.setText("");
                    searchFLD.setForeground(FrontendElements.FOCUSED_SEARCH_BAR);
                    searchLine.setForeground(FrontendElements.FOCUSED_SEARCH_BAR);
                }
            }

            @Override
            public void focusLost(FocusEvent e) {
                if (searchFLD.getText().isBlank()) {
                    searchFLD.setText(SEARCH_BAR_DEFAULT_TEXT);
                    searchFLD.setForeground(FrontendElements.UNFOCUSED_SEARCH_BAR);
                    searchLine.setForeground(FrontendElements.UNFOCUSED_SEARCH_BAR);
                }
            }
            
        });

        ingredientBTN.addActionListener(event -> {
            if (item == Items.Ingredient) {
                ((IngredientPane) itemPane).createIngredient();
                ((InventoryScene) inventoryScene).updateComboBox();
            }
        });

    }

    private Ingredient searchIngredient(SQLConnection sql, Items searchMode, int id) {
        Object object = sql.fetch(id, Items.Ingredient);
        if (object != null && object instanceof Ingredient ingredient) {
            return ingredient;
        }

        return null;
    }


    private Ingredient searchIngredient(SQLConnection sql, Items searchMode, String name) {
        Object object = sql.fetch(name, Items.Ingredient);
        if (object != null && object instanceof Ingredient ingredient) {
            return ingredient;
        }

        return null;
    }

    //checks if the string contains only numbers.
    private boolean isNumericOnly(String text) {
        for (final char character : text.toCharArray()) {
            if (!Character.isDigit(character)) return false;
        }

        return true;
    }

}
