package com.facturacion.frontend.BaseAbstractClasses;

import java.awt.CardLayout;
import java.awt.Dimension;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import javax.swing.JPanel;

public abstract class ResizablePanel extends JPanel {
    private final CardLayout cardLayout;
    private final JPanel cardPanel;
    
    public ResizablePanel(JPanel _cardPanel, CardLayout _cardLayout, Dimension minDimension) {
        cardLayout = _cardLayout;
        cardPanel = _cardPanel;

        this.addComponentListener(new ComponentAdapter() {
            @Override
            public void componentResized(ComponentEvent e) {
                resizeElements();
            }
        });

        setPreferredSize(minDimension);
    }

    protected void showThis() {cardLayout.show(cardPanel, getName()); }
    protected void changeTo(String panelName) { cardLayout.show(cardPanel, panelName); }

    protected abstract void resizeElements();
}
