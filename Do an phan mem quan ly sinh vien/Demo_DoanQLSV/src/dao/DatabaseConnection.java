/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dao;

import java.sql.Connection;
import java.sql.DriverManager;

public class DatabaseConnection {
    public static Connection openConnection() {
        Connection conn = null;
        try {
            String url = "jdbc:sqlserver://localhost\\SQLEXPRESS;databaseName=QuanLySinhVienDB;encrypt=true;trustServerCertificate=true;";
            String user = "sa";
            String password = "123456";
            
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            conn = DriverManager.getConnection(url, user, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }
}