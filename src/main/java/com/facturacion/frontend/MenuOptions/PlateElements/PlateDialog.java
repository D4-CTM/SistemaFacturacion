package com.facturacion.frontend.MenuOptions.PlateElements;

import java.awt.Dimension;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.RestaurantItems.*;
import com.facturacion.frontend.InternalClasses.FrontendElements;

public class PlateDialog {
    private Plate plate = null;

    public PlateDialog(SQLConnection sql) {
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

        priceSPNR = new JSpinner(new SpinnerNumberModel(0, 0, 999999999.99, 0.5));
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

        plateDialog = new JDialog((JFrame) null, "Platillos", true);
        plateDialog.add(platePanel);
        plateDialog.pack();
        
        plateDialog.setLocationRelativeTo(null);
        cancelBTN.addActionListener(event -> {
            plateDialog.dispose();
        });

        acceptBTN.addActionListener(event -> {
            if (plate == null) {
                Plate plate =new Plate(nameFLD.getText(), Float.parseFloat(priceSPNR.getValue().toString()));

                if (sql.insertElement(plate)) {
                    JOptionPane.showMessageDialog(null, "¡Se ha agregado el platillo exitosamente!", "Platillo", JOptionPane.INFORMATION_MESSAGE);
                    plateDialog.dispose();
                } else JOptionPane.showMessageDialog(null, "¡Ha ocurrido un error agregando el platillo! Por favor revise lo siguiente:\n- Revise su conexion a internet, en caso de haberse perdido intente de nuevo mas tarde.\n- Ya existe un platillo con dicho nombre, por favor intente con un nombre distinto.", "Platillo", JOptionPane.INFORMATION_MESSAGE);
            } else {
                if (sql.modifyElement(plate)) {
                    JOptionPane.showMessageDialog(null, "¡Se ha modificado el platillo exitosamente!", "Platillo", JOptionPane.INFORMATION_MESSAGE);
                    plateDialog.dispose();
                } else JOptionPane.showMessageDialog(null, "¡Ha ocurrido un error modificando el platillo! Por favor revise lo siguiente:\n- Revise su conexion a internet, en caso de haberse perdido intente de nuevo mas tarde.\n- Ya existe un platillo con dicho nombre, por favor intente con un nombre distinto.", "Platillo", JOptionPane.INFORMATION_MESSAGE);
            }
        });
    }

    public void modifyPlate(Plate _plate) {
        plate = _plate;
        nameFLD.setText(plate.name);
        acceptBTN.setText("Modificar");
        priceSPNR.setValue(_plate.price);
        plateDialog.setVisible(true);
    }
    
    public void createPlate() {
        plate = null;
        nameFLD.setText("");
        acceptBTN.setText("Agregar");
        priceSPNR.setValue(0);
        plateDialog.setVisible(true);
    }

    private final JButton acceptBTN;
    private final JTextField nameFLD;
    private final JSpinner priceSPNR;
    private final JDialog plateDialog;
}
