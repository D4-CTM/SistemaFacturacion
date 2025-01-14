package com.facturacion.frontend;

import javax.swing.JPanel;
import java.awt.CardLayout;

import com.facturacion.backend.SQLConnection;
import com.facturacion.frontend.BaseAbstractClasses.ResizablePanel;

public class Inventory extends ResizablePanel{
    private final SQLConnection sql;

    public Inventory(JPanel _cardPanel, CardLayout _cardLayout, SQLConnection _sql) {
        super(_cardPanel, _cardLayout);
        sql = _sql;

        
    }

    @Override
    protected void resizeElements() {

    }
    


}
