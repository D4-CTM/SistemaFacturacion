package com.facturacion.frontend.InternalClasses;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.border.Border;

public class FrontendElements {
//Fonts
    public static final Font DialogFont = new Font("Roboto", Font.PLAIN, 20);
    public static final Font SystemFont = new Font("Roboto", Font.PLAIN, 30);
    public static final Font OptionsFont = new Font("Roboto", Font.BOLD, 40);
//Borders
    public static final Border DEFAULT_BORDER = BorderFactory.createLineBorder(Color.BLACK, 2);
    public static final Border DELETE_BUTTON_BORDER = BorderFactory.createLineBorder(Color.decode("#512828"), 2);
//General colors
    public static final Color DEFAULT_BG = Color.DARK_GRAY;
    public static final Color DEFAULT_FG = Color.WHITE;
    public static final Color OUTER_BG = Color.LIGHT_GRAY;
    public static final Color OUTER_FG = Color.BLACK;
//Colors for the buttons - to combine with the general color scheme
    public static final Color BUTTON_BG = OUTER_BG;
    public static final Color BUTTON_FG = OUTER_FG;
//Delete button colors
    public static final Color DELETE_BUTTON_FG = Color.WHITE;
    public static final Color DELETE_BUTTON_BG = Color.decode("#EC3832");
//Search bar colors - to combine with the general color scheme
    public static final Color FOCUSED_SEARCH_BAR = OUTER_FG;
    public static final Color UNFOCUSED_SEARCH_BAR = DEFAULT_BG;
//Ingredient color scheme - to combine with the general color scheme
    public static final Color INGREDIENT_PANEL_BG = Color.LIGHT_GRAY;
    public static final Color INGREDIENT_PANEL_HEADER_BG = Color.WHITE;
    public static final Color SELECTED_INGREDIENT_PANEL_FG = Color.BLACK;
    public static final Color UNSELECTED_INGREDIENT_PANEL_FG = Color.DARK_GRAY;
}