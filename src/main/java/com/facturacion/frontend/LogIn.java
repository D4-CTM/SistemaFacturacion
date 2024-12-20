package com.facturacion.frontend;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

import com.facturacion.backend.UserManager;
import com.facturacion.frontend.BaseAbstractClasses.ResizablePanel;

public class LogIn extends ResizablePanel{
    private final UserManager userManager;    

    public LogIn(JPanel _cardPanel, CardLayout _cardLayout, Dimension minDimension, UserManager _userManager) {
        super(_cardPanel, _cardLayout, minDimension);
        userManager = _userManager;
        setName("Log In");
        
        setBackground(Color.LIGHT_GRAY);
    }



    @Override
    protected void resizeElements() {

    }
    
    //GUI ELEMENTS
    
    //GUI ELEMENTS
}
