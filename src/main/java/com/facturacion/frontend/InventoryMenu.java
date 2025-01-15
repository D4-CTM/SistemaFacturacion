package com.facturacion.frontend;

import javax.swing.JLabel;
import javax.swing.JPanel;

import java.awt.Dimension;

import com.facturacion.backend.SQLConnection;
import com.facturacion.frontend.InternalClasses.FrontendElements;
import com.facturacion.frontend.InternalClasses.IndexCard;
import com.facturacion.frontend.InternalClasses.MenuButtons;

public class InventoryMenu extends JPanel{
    private final float sidePanelPercentage = 0.2f;

    public InventoryMenu(IndexCard _indexCard, SQLConnection _sql, Dimension frameSize) {
        setPreferredSize(frameSize);
        setLayout(null);
        
        final int sidePanelWidth = (int) (frameSize.width * sidePanelPercentage);
        final JPanel sidePanel = new JPanel();
        sidePanel.setSize(sidePanelWidth, frameSize.height);
        sidePanel.setBackground(FrontendElements.DEFAULT_BG);
        add(sidePanel);

        final IndexCard indexPanel = new IndexCard();
        indexPanel.setSize((int) (frameSize.width * (1 - sidePanelPercentage)), frameSize.height);
        indexPanel.setLocation(sidePanelWidth, 0);
        add(indexPanel);

        final int sidePanelOptionsHeight = (int) (frameSize.height/12);
        final Dimension optionsDimension = new Dimension(sidePanelWidth, sidePanelOptionsHeight);
        final JLabel companyLogo = new JLabel();
        companyLogo.setPreferredSize(optionsDimension);
        sidePanel.add(companyLogo);

        final MenuButtons inventoryOption = new MenuButtons(indexPanel, optionsDimension, "Inventario");
        sidePanel.add(inventoryOption);

        final MenuButtons platesOption = new MenuButtons(indexPanel, optionsDimension, "Menu");
        sidePanel.add(platesOption);

        final MenuButtons reportsOption = new MenuButtons(indexPanel, optionsDimension, "Reportes");
        sidePanel.add(reportsOption);

        final JPanel spacer = new JPanel();
        spacer.setPreferredSize(new Dimension(sidePanelWidth, sidePanelOptionsHeight * 6));
        spacer.setOpaque(false);
        sidePanel.add(spacer);

        final MenuButtons returnOption = new MenuButtons(_indexCard, optionsDimension, "Regresar");
        sidePanel.add(returnOption);
    }

}
