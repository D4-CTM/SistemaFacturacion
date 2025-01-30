package com.facturacion.frontend.MenuOptions.PlateElements;

import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.LinkedList;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.RestaurantItems.RecipeIngredient;
import com.facturacion.frontend.InternalClasses.FrontendElements;

public class RecipeIngredientPanel extends JPanel{
    private boolean active = true;

    public RecipeIngredientPanel(PlateDialog plateDialog, SQLConnection sql, Dimension panelSize) {
        quantitySPNR = null;
        setSize(panelSize.width, panelSize.height);
        setLayout(null);

        setBackground(FrontendElements.ITEM_PANEL_HEADER_BG);
        setForeground(FrontendElements.ITEM_PANEL_HEADER_FG);

        nameLBL = new JLabel("Ingrediente");
        nameLBL.setSize((int) ((panelSize.width - panelSize.height) * 0.4), panelSize.height);
        nameLBL.setBorder(FrontendElements.DEFAULT_BORDER);
        nameLBL.setHorizontalAlignment(JLabel.CENTER);
        nameLBL.setFont(FrontendElements.DialogFont);
        add(nameLBL);
        
        final JLabel quantityLBL = new JLabel("<html><body>Cantidad<br>necesaria</body></html>");
        quantityLBL.setSize((int) ((panelSize.width - panelSize.height) * 0.6), panelSize.height);
        quantityLBL.setBorder(FrontendElements.DEFAULT_BORDER);
        quantityLBL.setHorizontalAlignment(JLabel.CENTER);
        quantityLBL.setFont(FrontendElements.DialogFont);
        quantityLBL.setLocation(nameLBL.getWidth(), 0);
        add(quantityLBL);

        final JButton insertBTN = new JButton();
        insertBTN.setSize(panelSize.height, panelSize.height);        
        insertBTN.setLocation(panelSize.width - panelSize.height, 0);

        insertBTN.addActionListener(event -> {
            LinkedList<String> list = plateDialog.getIngredientsNames();

            if (list == null || list.isEmpty()) {
                JOptionPane.showMessageDialog(null, "¡No se han podido encontrar mas ingredientes para agregar a la lista!");
                return ;
            }

            Object option = JOptionPane.showInputDialog(null, "¿Que ingrediente es necesario para realizar esta receta?", "Elegir ingredients", JOptionPane.INFORMATION_MESSAGE, null, list.toArray(), 0);
            if (option == null) return ;
            String unit = sql.getUnitOf(option.toString());
            
            plateDialog.addToRecipePanel(new RecipeIngredientPanel(plateDialog, option.toString(), 0.1f, unit, panelSize));
        });

        add(insertBTN);
    }

    public RecipeIngredientPanel(PlateDialog plateDialog, String name, float quantityNeeded, String unit, Dimension panelSize) {
        setPreferredSize(panelSize);
        setMaximumSize(panelSize);
        setMinimumSize(panelSize);
        setLayout(null);
        setName(name);
        
        nameLBL = new JLabel(name);
        nameLBL.setSize((int) ((panelSize.width - panelSize.height) * 0.4), panelSize.height);
        nameLBL.setBorder(FrontendElements.DEFAULT_BORDER);
        nameLBL.setHorizontalAlignment(JLabel.CENTER);
        nameLBL.setFont(FrontendElements.DialogFont);
        add(nameLBL);

        
        quantitySPNR = new JSpinner(new SpinnerNumberModel(quantityNeeded, 0.01, 9999999999.99, 0.5));
        quantitySPNR.setSize((int) ((panelSize.width - panelSize.height) * 0.6), panelSize.height);
        quantitySPNR.setBorder(FrontendElements.DEFAULT_BORDER);
        quantitySPNR.setFont(FrontendElements.DialogFont);
        quantitySPNR.setLocation(nameLBL.getWidth(), 0);

        String textExample = "#,##0.00 " + unit;
        JSpinner.NumberEditor editor = new JSpinner.NumberEditor(quantitySPNR, textExample);
        quantitySPNR.setEditor(editor);
        add(quantitySPNR);
        
        final JButton removeBTN = new JButton();
        removeBTN.setSize(panelSize.height - 2, panelSize.height);
        removeBTN.setBackground(FrontendElements.DELETE_BUTTON_BG);
        removeBTN.setBorder(FrontendElements.DELETE_BUTTON_BORDER);
        removeBTN.setLocation(quantitySPNR.getX() + quantitySPNR.getWidth(), 0);
        add(removeBTN);

        removeBTN.addActionListener(event -> {
            if (active) {
                deactivate(removeBTN);
                active = false;
            } else {
                activate(removeBTN);
                active = true;
            }

        });

        addMouseListener(new MouseAdapter() {

            @Override
            public void mouseClicked(MouseEvent arg0) {
                LinkedList<String> list = plateDialog.getIngredientsNames();

                if (list == null || list.isEmpty()) {
                    JOptionPane.showMessageDialog(null, "¡No se han podido encontrar mas ingredientes para modificar esta lista!");
                    return ;
                }
    
                Object option = JOptionPane.showInputDialog(null, "¿Por cual ingrediente desea reemplazarlo?", "Elegir ingredients", JOptionPane.INFORMATION_MESSAGE, null, list.toArray(), 0);
                if (option == null) return ;

                setName(option.toString());
                nameLBL.setText(option.toString());

                String textExample = "#,##0.00 " + plateDialog.getUnitOf(getName());
                JSpinner.NumberEditor editor = new JSpinner.NumberEditor(quantitySPNR, textExample);
                quantitySPNR.setEditor(editor);
            }

            @Override
            public void mouseEntered(MouseEvent arg0) {
                if (active)
                    focusedBG();
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                if (active)
                    unfocusedBG();
            }

        });
        unfocusedBG();

    }

    public RecipeIngredient createRecipeIngredient() {
        return new RecipeIngredient(getPrice(), getName());
    }

    public float getPrice() {
        return Float.parseFloat(quantitySPNR.getValue().toString());
    }

    public boolean isActive() {
        return active;
    }

    private void deactivate(JButton btn) {
        setBackground(FrontendElements.DEACTIVATED_PANEL_BG);
        nameLBL.setForeground(FrontendElements.DEACTIVATED_PANEL_FG);
        quantitySPNR.setForeground(FrontendElements.DEACTIVATED_PANEL_FG);
        quantitySPNR.setBackground(getBackground());
        quantitySPNR.setEnabled(false);

        btn.setBackground(getBackground());
        btn.setBorder(FrontendElements.DEFAULT_BORDER);
    }

    private void activate(JButton btn) {
        unfocusedBG();
        quantitySPNR.setEnabled(true);
        btn.setBackground(FrontendElements.DELETE_BUTTON_BG);
        btn.setBorder(FrontendElements.DELETE_BUTTON_BORDER);
    }

    private void focusedBG() {
        setBackground(FrontendElements.UNSELECTED_ITEM_PANEL_BG);
        nameLBL.setForeground(FrontendElements.UNSELECTED_ITEM_PANEL_FG);
        quantitySPNR.setForeground(FrontendElements.UNSELECTED_ITEM_PANEL_FG);
        quantitySPNR.setBackground(getBackground());
    }

    private void unfocusedBG() {
        setBackground(FrontendElements.SELECTED_ITEM_PANEL_BG);
        nameLBL.setForeground(FrontendElements.SELECTED_ITEM_PANEL_FG);
        quantitySPNR.setForeground(FrontendElements.SELECTED_ITEM_PANEL_FG);
        quantitySPNR.setBackground(getBackground());
    }

    private final JLabel nameLBL;
    private final JSpinner quantitySPNR;
}
