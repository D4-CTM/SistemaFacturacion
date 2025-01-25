package com.facturacion.frontend.MenuOptions.PlateElements;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;

import java.awt.event.MouseEvent;

import com.facturacion.backend.RestaurantItems.Plate;
import com.facturacion.frontend.InternalClasses.FrontendElements;

public class PlatePanel extends JPanel{
    
    public PlatePanel(Dimension panelSize) {
        final int usableWidth = panelSize.width - panelSize.height;
        setSize(usableWidth, panelSize.height);
        setLayout(null);

        final Dimension idDMSN = new Dimension((int) (usableWidth * 0.1), panelSize.height);
        final JLabel idLBL = new JLabel("Id");
        idLBL.setHorizontalAlignment(JLabel.CENTER);
        idLBL.setBorder(FrontendElements.DEFAULT_BORDER);
        idLBL.setSize(idDMSN);
        add(idLBL);

        final Dimension nameDMSN = new Dimension((int) (usableWidth * 0.5), panelSize.height);
        final JLabel nameLBL = new JLabel("Nombre");
        nameLBL.setHorizontalAlignment(JLabel.CENTER);
        nameLBL.setBorder(FrontendElements.DEFAULT_BORDER);
        nameLBL.setSize(nameDMSN);
        nameLBL.setLocation(idLBL.getWidth(), 0);
        add(nameLBL);

        final Dimension priceDMSN = new Dimension((int) (usableWidth * 0.4), panelSize.height);
        final JLabel priceLBL = new JLabel("Precio");
        priceLBL.setHorizontalAlignment(JLabel.CENTER);
        priceLBL.setBorder(FrontendElements.DEFAULT_BORDER);
        priceLBL.setSize(priceDMSN);
        priceLBL.setLocation(nameLBL.getX() + nameLBL.getWidth(), 0);
        add(priceLBL);

        setBackground(FrontendElements.ITEM_PANEL_HEADER_BG);
        for (final Component component : getComponents()) {
            component.setForeground(FrontendElements.ITEM_PANEL_HEADER_FG);
            component.setFont(FrontendElements.SystemFont);
        }
    }

    public PlatePanel(Plate plate, PlateScene plateScene, Dimension panelSize) {
        final int usableWidth = panelSize.width - panelSize.height;
        setPreferredSize(panelSize);
        setMinimumSize(panelSize);
        setLayout(null);

        final Dimension idDMSN = new Dimension((int) (usableWidth * 0.1), panelSize.height);
        final JLabel idLBL = new JLabel(String.valueOf(plate.id));
        idLBL.setHorizontalAlignment(JLabel.CENTER);
        idLBL.setBorder(FrontendElements.DEFAULT_BORDER);
        idLBL.setSize(idDMSN);
        add(idLBL);

        final Dimension nameDMSN = new Dimension((int) (usableWidth * 0.5), panelSize.height);
        final JLabel nameLBL = new JLabel(plate.name);
        nameLBL.setHorizontalAlignment(JLabel.CENTER);
        nameLBL.setBorder(FrontendElements.DEFAULT_BORDER);
        nameLBL.setSize(nameDMSN);
        nameLBL.setLocation(idLBL.getWidth(), 0);
        add(nameLBL);

        final Dimension priceDMSN = new Dimension((int) (usableWidth * 0.4), panelSize.height);
        final JLabel priceLBL = new JLabel("L. " + String.valueOf(plate.price));
        priceLBL.setHorizontalAlignment(JLabel.CENTER);
        priceLBL.setBorder(FrontendElements.DEFAULT_BORDER);
        priceLBL.setSize(priceDMSN);
        priceLBL.setLocation(nameLBL.getX() + nameLBL.getWidth(), 0);
        add(priceLBL);

        final JButton removeBTN = new JButton();
        removeBTN.setBackground(FrontendElements.DELETE_BUTTON_BG);
        removeBTN.setBorder(FrontendElements.DELETE_BUTTON_BORDER);
        removeBTN.setLocation(priceLBL.getX() + priceLBL.getWidth(), 0);
        removeBTN.setSize(panelSize.height, panelSize.height);

        removeBTN.addActionListener(event -> {
            int option = JOptionPane.showConfirmDialog(null, "¿Esta seguro que desea eliminar este plato?", "Eliminar plato", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (option != JOptionPane.YES_OPTION) return;

            plateScene.removePlate(plate);
        });

        addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent arg0) {
                plateScene.modifyPlate(plate);
                revalidate();
                repaint();
            }
    
            @Override
            public void mouseEntered(MouseEvent arg0) {
                focused();
            }

            @Override
            public void mouseExited(MouseEvent arg0) {
                unfocused();
            }
        });

        add(removeBTN);

        unfocused();
        for (final Component component : getComponents()) {
            component.setFont(FrontendElements.SystemFont);
        }
    }

    private void focused() {
        setBackground(FrontendElements.SELECTED_ITEM_PANEL_BG);
        for (final Component component : getComponents()) {
            component.setForeground(FrontendElements.SELECTED_ITEM_PANEL_FG);
        }
    }

    private void unfocused() {
        setBackground(FrontendElements.UNSELECTED_ITEM_PANEL_BG);
        for (final Component component : getComponents()) {
            component.setForeground(FrontendElements.UNSELECTED_ITEM_PANEL_FG);
        }
    }

}
