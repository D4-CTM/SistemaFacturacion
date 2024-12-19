package com.facturacion.frontend;

import java.awt.CardLayout;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.facturacion.frontend.BaseAbstractClasses.ResizablePanel;

public class LogIn extends ResizablePanel{

    public LogIn(JPanel _cardPanel, CardLayout _cardLayout, Dimension minDimension) {
        super(_cardPanel, _cardLayout, minDimension);
        setName("Log In");

        
    }

    @Override
    protected void resizeElements() {

    }
    
    //GUI ELEMENTS

}
