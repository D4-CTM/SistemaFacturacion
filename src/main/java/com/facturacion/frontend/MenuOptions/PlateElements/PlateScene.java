package com.facturacion.frontend.MenuOptions.PlateElements;

import java.awt.Dimension;

import javax.swing.JPanel;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.RestaurantItems.Items;
import com.facturacion.frontend.InternalClasses.OptionsHeader;

public class PlateScene extends JPanel {
    
    public PlateScene(SQLConnection sql, Dimension sceneFrame) {
        final PlateDialog plateDialog = new PlateDialog(sql);
        final int headerHeight = (int) (sceneFrame.height * 0.075);
        final int width = sceneFrame.width;

        setSize(sceneFrame);
        setLayout(null);

        final Dimension headerDMSN = new Dimension(width, headerHeight);
        final OptionsHeader header = new OptionsHeader(sql, headerDMSN, Items.Plate, plateDialog, this);
        add(header);
    }

}
