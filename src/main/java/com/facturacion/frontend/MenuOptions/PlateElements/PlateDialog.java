package com.facturacion.frontend.MenuOptions.PlateElements;

import java.awt.Component;
import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.RestaurantItems.*;
import com.facturacion.frontend.InternalClasses.FrontendElements;

public class PlateDialog {
    private final LinkedList<String> recipeIngredientsList;
    private final Dimension recipeIngredientDMSN;
    private final SQLConnection sql;
    private Plate plate = null;

    public PlateDialog(SQLConnection _sql) {
        recipeIngredientsList = new LinkedList<>();
        sql = _sql;

        final Dimension dialogDMSN = new Dimension(400, 600);
        final JPanel platePanel = new JPanel();
        platePanel.setBackground(FrontendElements.DEFAULT_BG);
        platePanel.setForeground(FrontendElements.DEFAULT_FG);
        platePanel.setFont(FrontendElements.DialogFont);
        platePanel.setPreferredSize(dialogDMSN);
        platePanel.setLayout(null);

        final JLabel nameLBL = new JLabel("Nombre:");
        nameLBL.setForeground(platePanel.getForeground());
        nameLBL.setFont(platePanel.getFont());
        nameLBL.setLocation(10, 10);
        nameLBL.setSize(125, 60);
        platePanel.add(nameLBL);

        nameFLD = new JTextField();
        nameFLD.setFont(platePanel.getFont());
        nameFLD.setLocation(nameLBL.getX() + nameLBL.getWidth() + 10, nameLBL.getY());
        nameFLD.setSize(245, nameLBL.getHeight());
        platePanel.add(nameFLD);

        final JLabel priceLBL = new JLabel("precio: ");
        priceLBL.setForeground(platePanel.getForeground());
        priceLBL.setFont(platePanel.getFont());
        priceLBL.setLocation(10, nameLBL.getY() + nameLBL.getHeight() + 10);
        priceLBL.setSize(nameLBL.getSize());
        platePanel.add(priceLBL);

        priceSPNR = new JSpinner(new SpinnerNumberModel(100.00, 1.00, 999999999.99, 0.5));
        priceSPNR.setFont(platePanel.getFont());
        priceSPNR.setLocation(nameFLD.getX(), priceLBL.getY());
        priceSPNR.setSize(nameFLD.getSize());
        platePanel.add(priceSPNR);

        final JButton cancelBTN = new JButton("Regresar");
        cancelBTN.setFont(platePanel.getFont());
        cancelBTN.setLocation(10, dialogDMSN.height - 85);
        cancelBTN.setSize(125, 75);
        platePanel.add(cancelBTN);

        acceptBTN = new JButton();
        acceptBTN.setFont(platePanel.getFont());
        acceptBTN.setLocation(dialogDMSN.width - 10 - cancelBTN.getWidth(), cancelBTN.getY());
        acceptBTN.setSize(cancelBTN.getSize());
        platePanel.add(acceptBTN);

        final int scrollPaneHeight = acceptBTN.getY() - (priceLBL.getY() + priceLBL.getHeight() + 20);
        final int scrollPaneWidth = dialogDMSN.width - 20;
        final int scrollBarWidth = 15;
        final int recipeIngredientHeigth = scrollPaneHeight/5;
        recipeIngredientDMSN = new Dimension(scrollPaneWidth - scrollBarWidth, recipeIngredientHeigth);

        final RecipeIngredientPanel recipeIngredientHeader = new RecipeIngredientPanel(this, sql, recipeIngredientDMSN);
        recipeIngredientHeader.setLocation(10, priceLBL.getY() + priceLBL.getHeight() + 10);
        platePanel.add(recipeIngredientHeader);

        recipeIngredientPanel = new JPanel();
        recipeIngredientPanel.setLayout(new BoxLayout(recipeIngredientPanel, BoxLayout.Y_AXIS));
        recipeIngredientPanel.setBackground(platePanel.getBackground());

        final JScrollPane scrollPane = new JScrollPane(recipeIngredientPanel);
        final JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setPreferredSize(new Dimension(scrollBarWidth, scrollPaneHeight));
        scrollBar.setUnitIncrement(5);
        
        scrollPane.setVerticalScrollBar(scrollBar);
        scrollPane.setSize(scrollPaneWidth, scrollPaneHeight - recipeIngredientHeigth);
        scrollPane.setLocation(10, recipeIngredientHeader.getY() + recipeIngredientHeader.getHeight());
        platePanel.add(scrollPane);
        
        plateDialog = new JDialog((JFrame) null, "Platillos", true);
        plateDialog.add(platePanel);
        plateDialog.pack();
        
        plateDialog.setLocationRelativeTo(null);
        cancelBTN.addActionListener(event -> {
            plateDialog.dispose();
        });

        acceptBTN.addActionListener(event -> {
            if (nameFLD.getText().isBlank()) {
                JOptionPane.showMessageDialog(null, "Por favor ingrese el nombre del platillo.", "Datos faltantes", JOptionPane.INFORMATION_MESSAGE);
                return ;
            }

            if (plate == null) {
                Plate plate =new Plate(nameFLD.getText(), Float.parseFloat(priceSPNR.getValue().toString()));
                LinkedList<Float> quantityList = filterIngredientesToInsert();

                if (sql.insertPlate(plate, recipeIngredientsList, quantityList)) {
                    JOptionPane.showMessageDialog(null, "¡Se ha agregado el platillo exitosamente!", "Platillo", JOptionPane.INFORMATION_MESSAGE);
                    plateDialog.setVisible(false);;
                } else JOptionPane.showMessageDialog(null, "¡Ha ocurrido un error agregando el platillo! Por favor revise lo siguiente:\n- Revise su conexion a internet, en caso de haberse perdido intente de nuevo mas tarde.\n- Ya existe un platillo con dicho nombre, por favor intente con un nombre distinto.", "Platillo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (sql.modifyElement(plate)) {
                    JOptionPane.showMessageDialog(null, "¡Se ha modificado el platillo exitosamente!", "Platillo", JOptionPane.INFORMATION_MESSAGE);
                    plateDialog.setVisible(false);;
                } else JOptionPane.showMessageDialog(null, "¡Ha ocurrido un error modificando el platillo! Por favor revise lo siguiente:\n- Revise su conexion a internet, en caso de haberse perdido intente de nuevo mas tarde.\n- Ya existe un platillo con dicho nombre, por favor intente con un nombre distinto.", "Platillo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public LinkedList<String> getIngredientsNames() {
        LinkedList<String> ingredientsNames = sql.getIngredientsNames();
        if (ingredientsNames == null) return null;

        for (final String removedIngredient : recipeIngredientsList) {
            ingredientsNames.remove(removedIngredient);
        }

        return ingredientsNames;
    }

    public void reeplaceOnList(String oldName, String newName) {
        recipeIngredientsList.remove(oldName);
        recipeIngredientsList.add(newName);
    }

    public void removeFromRecipePanel(final RecipeIngredientPanel rip) {
        recipeIngredientPanel.remove(rip);
    }

    public void addToRecipePanel(final RecipeIngredientPanel rip) {
        recipeIngredientPanel.add(rip);
        recipeIngredientsList.add(rip.getIngredientName());
        recipeIngredientPanel.revalidate();
        recipeIngredientPanel.repaint();
    }

    public void modifyPlate(Plate _plate) {
        plate = _plate;
        nameFLD.setText(plate.name);
        acceptBTN.setText("Modificar");
        priceSPNR.setValue(_plate.price);
        recipeIngredientPanel.removeAll();
        recipeIngredientsList.clear();
        plateDialog.setVisible(true);
    }
    
    public void createPlate() {
        plate = null;
        nameFLD.setText("");
        priceSPNR.setValue(100);
        acceptBTN.setText("Agregar");
        recipeIngredientPanel.removeAll();
        recipeIngredientsList.clear();
        plateDialog.setVisible(true);
    }

    private LinkedList<Float> filterIngredientesToInsert() {
        LinkedList<Float> floatList = new LinkedList<>();
        for (final Component component : recipeIngredientPanel.getComponents()) {
            if (component instanceof RecipeIngredientPanel rip) {
                if (!rip.isActive() && recipeIngredientsList.contains(rip.getIngredientName())) {
                    recipeIngredientsList.remove(rip.getIngredientName());
                } else {
                    floatList.add(rip.getPrice());
                }
            }
        }
        return floatList;
    }

    private final JButton acceptBTN;
    private final JTextField nameFLD;
    private final JSpinner priceSPNR;
    private final JDialog plateDialog;
    private final JPanel recipeIngredientPanel;
}
