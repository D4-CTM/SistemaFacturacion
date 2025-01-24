package com.facturacion.frontend.MenuOptions.EngredientElements;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
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

public class InventoryScene extends JPanel {
    private final IngredientPane ingredientPane;
    private final SQLConnection sql;
    
    public InventoryScene(SQLConnection _sql, Dimension containerSize) {
        ingredientPane = new IngredientPane(_sql);
        sql = _sql;

        final int scrollPaneHeight = (int) (containerSize.height * (1 - (0.075 + 0.075 + 0.1)));  
        final int scrollBarWidth = 15;

        ingredientDimension = new Dimension(containerSize.width - (scrollBarWidth + 4), (int) (containerSize.height * 0.1));
        final Dimension headerDimension = new Dimension(containerSize.width, (int) (containerSize.getHeight() * 0.075));
        final Dimension scrollPaneDimension = new Dimension(containerSize.width, scrollPaneHeight);
        setBackground(FrontendElements.OUTER_BG);
        setLayout(null);

        final OptionsHeader header = new OptionsHeader(sql, headerDimension, Items.Ingredient, ingredientPane, this);
        add(header);

        final IngredientElementPanel ingredientHeader = new IngredientElementPanel(null, false, ingredientDimension, sql, this);
        ingredientHeader.setLocation(0, headerDimension.height);
        add(ingredientHeader);

        ingredientPanel = new JPanel();
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
        
        comboBox = new JComboBox<>(createComboBoxModel());
        comboBox.setFont(FrontendElements.DialogFont);
        comboBox.setSize((int) (headerDimension.width/4), (int) (headerDimension.height * 0.75));
        comboBox.setLocation(containerSize.width/2 - comboBox.getWidth()/2, containerSize.height - headerDimension.height + ((int) (headerDimension.height * 0.25)/2));
        comboBox.addActionListener(event -> {
            showElements(sql.getIngredientsAt(comboBox.getSelectedIndex()));
        });
        add(comboBox);

        final JButton previousBTN = new JButton();
        previousBTN.setSize(headerDimension.height, headerDimension.height);
        previousBTN.setLocation(0, containerSize.height - headerDimension.height);
        previousBTN.addActionListener(event -> {
            int page = comboBox.getSelectedIndex();
            if (page == 0) {
                JOptionPane.showMessageDialog(null, "No puede retroceder, no hay mas elementos previos.", "Error", JOptionPane.ERROR_MESSAGE);
                return ;
            }

            comboBox.setSelectedIndex(page - 1);
        });
        add(previousBTN);

        final JButton nextBTN = new JButton();
        nextBTN.setSize(headerDimension.height, headerDimension.height);
        nextBTN.setLocation(containerSize.width - headerDimension.height, containerSize.height - headerDimension.height);
        nextBTN.addActionListener(event -> {
            int page = comboBox.getSelectedIndex();
            if (page == comboBox.getItemCount() - 1) {
                JOptionPane.showMessageDialog(null, "No puede avanzar, no hay mas elementos.", "Error", JOptionPane.ERROR_MESSAGE);
                return ;
            }

            comboBox.setSelectedIndex(page + 1);
        });
        add(nextBTN);

        showElements(sql.getCurrentIngredients());
    }

    public void updateComboBox() {
        int selectedPage = comboBox.getSelectedIndex();
        
        comboBox.setModel(createComboBoxModel());
        comboBox.setSelectedIndex(selectedPage);
    }

    public void modifyIngredient(Ingredient ingredient) {
        ingredientPane.modifyIngredient(ingredient);
    }

    private DefaultComboBoxModel<String> createComboBoxModel() {
        final DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        for (int i = 0; i <= sql.getPageCount(Items.Ingredient); i++) {
            comboBoxModel.addElement("pagina: " + (i + 1));
        }
        return comboBoxModel;
    }

    public void showElements(LinkedList<Ingredient> list) {
        if (list == null) {
            return;
        }
        ingredientPanel.removeAll();

        IngredientElementPanel ingredientElementPanel;
        for (final Ingredient ingredient : list) {
            ingredientElementPanel = new IngredientElementPanel(ingredient, true, ingredientDimension, sql, this);
            ingredientElementPanel.setMaximumSize(ingredientDimension);
            ingredientPanel.add(ingredientElementPanel);
        }
        ingredientPanel.revalidate();
        ingredientPanel.repaint();
    }

    private final JPanel ingredientPanel;
    private final Dimension ingredientDimension;
    private final JComboBox<String> comboBox;
}
