package com.facturacion.frontend.MenuOptions.PlateElements;

import java.awt.Dimension;
import java.util.LinkedList;

import javax.swing.BoxLayout;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollBar;
import javax.swing.JScrollPane;

import com.facturacion.backend.SQLConnection;
import com.facturacion.backend.RestaurantItems.Items;
import com.facturacion.backend.RestaurantItems.Plate;
import com.facturacion.frontend.InternalClasses.FrontendElements;
import com.facturacion.frontend.InternalClasses.OptionsHeader;

public class PlateScene extends JPanel {
    final PlateDialog plateDialog;
    final Dimension platePNLSize;
    final SQLConnection sql;

    public PlateScene(SQLConnection _sql, Dimension sceneSize) {
        final int scrollBarWidth = 15;
        platePNLSize = new Dimension(sceneSize.width - scrollBarWidth, (int) (sceneSize.height * 0.1));
        sql = _sql;

        plateDialog = new PlateDialog(sql);
        final int headerHeight = (int) (sceneSize.height * 0.075);
        final int scrollPaneHeight = (int) (sceneSize.height * (1 - (0.075 + 0.075 + 0.1)));
        final int width = sceneSize.width;

        setBackground(FrontendElements.OUTER_BG);
        setSize(sceneSize);
        setLayout(null);

        final Dimension headerDMSN = new Dimension(width, headerHeight);
        final OptionsHeader header = new OptionsHeader(sql, headerDMSN, Items.Plate, plateDialog, this);
        add(header);

        final PlatePanel platePanel = new PlatePanel(platePNLSize);
        platePanel.setLocation(0, headerHeight);
        add(platePanel);

        platePNL = new JPanel();
        platePNL.setBackground(getBackground());
        platePNL.setLayout(new BoxLayout(platePNL, BoxLayout.Y_AXIS));

        final JScrollPane scrollPane = new JScrollPane(platePNL);
        scrollPane.setPreferredSize(new Dimension(sceneSize.width, scrollPaneHeight));
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        scrollPane.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        final JScrollBar scrollBar = scrollPane.getVerticalScrollBar();
        scrollBar.setPreferredSize(new Dimension(scrollBarWidth, scrollPaneHeight));
        scrollPane.setVerticalScrollBar(scrollBar);

        scrollPane.setLocation(0, platePanel.getY() + platePanel.getHeight());
        scrollPane.setSize(sceneSize.width, scrollPaneHeight);
        add(scrollPane);

        comboBox = new JComboBox<>(createComboBoxModel());
        comboBox.setFont(FrontendElements.DialogFont);
        comboBox.setSize((int) (headerDMSN.width/4), (int) (headerDMSN.height * 0.75));
        comboBox.setLocation(sceneSize.width/2 - comboBox.getWidth()/2, sceneSize.height - headerDMSN.height + ((int) (headerDMSN.height * 0.25)/2));
        comboBox.addActionListener(event -> {
            showElements(sql.getPlatesAt(comboBox.getSelectedIndex()));
        });
        add(comboBox);


        final JButton previousBTN = new JButton();
        previousBTN.setSize(headerDMSN.height, headerDMSN.height);
        previousBTN.setLocation(0, sceneSize.height - headerDMSN.height);
        previousBTN.addActionListener(event -> {
            int page = comboBox.getSelectedIndex();
            if (page == 0) {
                JOptionPane.showMessageDialog(null, "No puede retroceder, no hay mas elementos previos.", "Error", JOptionPane.ERROR_MESSAGE);
                return ;
            }

            comboBox.setSelectedIndex(page - 1);
        });
        add(previousBTN);

        final JButton nextBTN = new JButton();
        nextBTN.setSize(headerDMSN.height, headerDMSN.height);
        nextBTN.setLocation(sceneSize.width - headerDMSN.height, sceneSize.height - headerDMSN.height);
        nextBTN.addActionListener(event -> {
            int page = comboBox.getSelectedIndex();
            if (page == comboBox.getItemCount() - 1) {
                JOptionPane.showMessageDialog(null, "No puede avanzar, no hay mas elementos.", "Error", JOptionPane.ERROR_MESSAGE);
                return ;
            }

            comboBox.setSelectedIndex(page + 1);
        });
        add(nextBTN);

        showElements(sql.getCurrentPlates());
    }

    public void showElements(LinkedList<Plate> list) {
        if (list == null) {
            return;
        }
        platePNL.removeAll();

        PlatePanel platePanel;
        for (final Plate plate : list) {
            platePanel = new PlatePanel(plate, this, platePNLSize);
            platePanel.setMaximumSize(platePNLSize);
            platePNL.add(platePanel);
        }
        platePNL.revalidate();
        platePNL.repaint();
    }


    private DefaultComboBoxModel<String> createComboBoxModel() {
        final DefaultComboBoxModel<String> comboBoxModel = new DefaultComboBoxModel<>();
        for (int i = 0; i <= sql.getPageCount(Items.Plate); i++) {
            comboBoxModel.addElement("pagina: " + (i + 1));
        }
        return comboBoxModel;
    }

    public void removePlate(Plate plate) {
        if (sql.deleteElement(plate)) {
            updateComboBox();
        } else JOptionPane.showMessageDialog(null, "Ha ocurrido un error al intentar eliminar el plato. Por favor asegurese de contar con una conexion de internet estable.", "eliminar plato", JOptionPane.WARNING_MESSAGE);
    }

    public void modifyPlate(Plate plate) {
        plateDialog.modifyPlate(plate);
    }

    public void updateComboBox() {
        int selectedPage = comboBox.getSelectedIndex();
        
        comboBox.setModel(createComboBoxModel());
        comboBox.setSelectedIndex(selectedPage);
        
    }
    
    private final JComboBox<String> comboBox;
    private final JPanel platePNL;
}
