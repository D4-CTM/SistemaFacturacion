package com.facturacion.frontend.InternalClasses;

import java.awt.CardLayout;
import javax.swing.JPanel;

public class IndexCard extends JPanel{
    private final CardLayout cardLayout;

    public IndexCard() {
        cardLayout = new CardLayout();
        setLayout(cardLayout);
    }

    public void show(String name) {
        cardLayout.show(this, name);
    }

}