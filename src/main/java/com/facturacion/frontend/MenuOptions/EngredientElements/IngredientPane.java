package com.facturacion.frontend.MenuOptions.EngredientElements;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.RestaurantItems.Ingredient;
import com.facturacion.backend.RestaurantItems.Items;
import com.facturacion.frontend.InternalClasses.FrontendElements;

public class IngredientPane {
    private final JDialog ingredientDialog;
    private final JPanel ingredientPNL;
    private final JLabel ingredientLBL;
    private final JTextField ingredientNameFLD;
    private final JComboBox<String> ingredientUnitBox;
    private final JLabel ingredientQuantityLBL;
    private final JSpinner ingredientQuantitySPNR;
    private final JButton acceptBTN;

    private int id = -1;

    public IngredientPane(SQLConnection sql) {
        final Dimension paneDimension = new Dimension(390, 190);
        final String[] units = { "LB", "OZ", "G", "KG", "MG", "L", "ML"};

        ingredientPNL = new JPanel();
        ingredientPNL.setBackground(FrontendElements.DEFAULT_BG);
        ingredientPNL.setPreferredSize(paneDimension);
        ingredientPNL.setLayout(null);

        ingredientLBL = new JLabel("Nombre: ", JLabel.RIGHT);
        ingredientLBL.setForeground(FrontendElements.DEFAULT_FG);
        ingredientLBL.setFont(FrontendElements.DialogFont);
        ingredientLBL.setLocation(10, 10);
        ingredientLBL.setSize(125, 50);
        ingredientPNL.add(ingredientLBL);

        ingredientQuantityLBL = new JLabel("Cantidad: ", JLabel.RIGHT);
        ingredientQuantityLBL.setForeground(FrontendElements.DEFAULT_FG);
        ingredientQuantityLBL.setFont(FrontendElements.DialogFont);
        ingredientQuantityLBL.setLocation(10, ingredientLBL.getY() + ingredientLBL.getHeight() + 10);
        ingredientQuantityLBL.setSize(ingredientLBL.getSize());
        ingredientPNL.add(ingredientQuantityLBL);

        ingredientQuantitySPNR = new JSpinner(new SpinnerNumberModel(0, 0, 999999999.99, 0.5));        
        ingredientQuantitySPNR.setFont(FrontendElements.DialogFont);
        ingredientQuantitySPNR.setLocation(ingredientQuantityLBL.getX() + ingredientQuantityLBL.getWidth() + 10, ingredientQuantityLBL.getY());
        ingredientQuantitySPNR.setSize(ingredientLBL.getSize());
        ingredientPNL.add(ingredientQuantitySPNR);

        ingredientUnitBox = new JComboBox<>(units);
        ingredientUnitBox.setFont(FrontendElements.DialogFont);
        ingredientUnitBox.setLocation(ingredientQuantitySPNR.getX() + ingredientQuantitySPNR.getWidth() + 10, ingredientQuantityLBL.getY());
        ingredientUnitBox.setSize(ingredientLBL.getHeight()*2, ingredientLBL.getHeight());
        ingredientPNL.add(ingredientUnitBox);

        ingredientNameFLD = new JTextField();
        ingredientNameFLD.setFont(FrontendElements.DialogFont);
        ingredientNameFLD.setLocation(ingredientQuantitySPNR.getX(), ingredientLBL.getY());
        ingredientNameFLD.setSize(ingredientQuantitySPNR.getWidth() + 10 + ingredientUnitBox.getWidth(), ingredientLBL.getHeight());
        ingredientPNL.add(ingredientNameFLD);

        final JButton cancelBTN = new JButton("cancelar");
        final Dimension buttonDimension = new Dimension((int) paneDimension.getWidth()/3, ingredientLBL.getHeight());
        cancelBTN.setFont(FrontendElements.DialogFont);
        cancelBTN.setLocation(10, ingredientQuantityLBL.getY() + ingredientQuantityLBL.getHeight() + 10);
        cancelBTN.setSize(buttonDimension);
        ingredientPNL.add(cancelBTN);

        acceptBTN = new JButton("agregar");
        acceptBTN.setFont(FrontendElements.DialogFont);
        acceptBTN.setLocation(paneDimension.width - buttonDimension.width - 10, cancelBTN.getY());
        acceptBTN.setSize(buttonDimension);

        ingredientPNL.add(acceptBTN);
        
        ingredientDialog = new JDialog((JFrame) null, "Ingredientes", true);
        ingredientDialog.setResizable(false);
        ingredientDialog.add(ingredientPNL);
        ingredientDialog.pack();

        cancelBTN.addActionListener(event -> {
            ingredientDialog.dispose();
        });

        acceptBTN.addActionListener(event -> {
            updateSQL(sql);
        });
    }

    private void updateSQL(SQLConnection sql) {
        if (ingredientNameFLD.getText().isEmpty()) {
            JOptionPane.showMessageDialog(null, "Por favor ingrese el nombre del producto", "Ingresar nombre del producto", JOptionPane.WARNING_MESSAGE);
            return;
        }

        if (id < 0) {

            Ingredient ingredient = new Ingredient(ingredientNameFLD.getText(), ingredientUnitBox.getSelectedItem().toString(), Float.parseFloat(ingredientQuantitySPNR.getValue().toString()));
            if (sql.insertElement(ingredient)) {
                ingredientDialog.dispose();
                JOptionPane.showMessageDialog(null, "¡Se ha agregado exitosamente el ingrediente¡", "Inserción exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else JOptionPane.showMessageDialog(null, "¡No se ha podido ingresar el elemento en la base de datos!! :os motivos pueden ser:\n- Revise su conexion a internet, en caso de haberse perdido intente de nuevo mas tarde.\n- Ya existe un elemento con dicho nombre, por favor intente con un nombre distinto.", "Error al ingresar ingrediente", JOptionPane.WARNING_MESSAGE);

        } else {
        
            Object obj = sql.fetch(id, Items.Ingredient);
            if (obj == null) {

            }
            Ingredient ingredient = (Ingredient) obj;
            ingredient.name = ingredientNameFLD.getText();
            ingredient.unit = ingredientUnitBox.getSelectedItem().toString();
            ingredient.quantity = Float.parseFloat(ingredientQuantitySPNR.getValue().toString());

            if (sql.modifyElement(ingredient)) {
                ingredientDialog.dispose();
                JOptionPane.showMessageDialog(null, "¡Se ha modificado exitosamente el ingrediente!", "Modificacion exitosa", JOptionPane.INFORMATION_MESSAGE);
            } else JOptionPane.showMessageDialog(null, "¡No se ha podido modificar el ingrediente! Los motivos pueden ser:\n- Revise su conexion a internet, en caso de haberse perdido intente de nuevo mas tarde.\n- Ya existe un elemento con dicho nombre, por favor intente con un nombre distinto.\", \"Error al ingresar ingrediente", "Error al modificar ingrediente", JOptionPane.WARNING_MESSAGE);
        }
    }

    public void modifyIngredient(Ingredient _ingredient) {
        ingredientQuantitySPNR.setValue((double) _ingredient.quantity);
        ingredientNameFLD.setText(_ingredient.name);
        ingredientUnitBox.setSelectedItem(_ingredient.unit);
        ingredientDialog.setLocationRelativeTo(null);
        acceptBTN.setText("modificar");
        id = _ingredient.id;
        ingredientDialog.setVisible(true);
    }

    public void createIngredient() {
        ingredientQuantitySPNR.setValue(0);
        ingredientNameFLD.setText("");
        ingredientUnitBox.setSelectedIndex(0);
        ingredientDialog.setLocationRelativeTo(null);
        acceptBTN.setText("agregar");
        id = -1;
        ingredientDialog.setVisible(true);
    }


}
