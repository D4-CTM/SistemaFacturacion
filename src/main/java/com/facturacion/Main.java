package com.facturacion;
import java.io.IOException;

import com.facturacion.backend.UserManager.UserManagerException;
import com.facturacion.frontend.MainFrame;

public class Main {
    public static void main(String[] args) throws IOException, UserManagerException {
        new MainFrame("Sistema de caja").setVisible(true);;
    }
}