/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.secuotp.model.connection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Zenology
 */
public class ConnectionAgent {
    private static final String DRIVER = "com.mysql.jdbc.Driver";
    private static final String URL = "jdbc:mysql://localhost:3306/secuotp";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "secuotp";
    
    private static Connection instance = null;
    
    public static synchronized Connection getInstance() throws ClassNotFoundException, SQLException{
        if(instance == null){
            Class.forName(DRIVER);
            instance = DriverManager.getConnection(URL, USERNAME, PASSWORD);
        }
        return instance;
    }
}
