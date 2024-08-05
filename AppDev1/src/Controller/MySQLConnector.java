/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;
import java.beans.PropertyVetoException;
import java.io.IOException;
import java.sql.*;
import org.apache.commons.dbcp2.BasicDataSource;

/**
 *
 * @OriginalAuthor - @drgimatt
 * MySQLConnector - Responsible for handling the connections between the Application 
 * and the SQL Database.
 *
 */
public class MySQLConnector {
    String url = "snboots.ddns.net:3310/musicbackend";
    String address = "jdbc:mysql://" + url ;
    String username = "admin";
    String password = "titingkayad";
    
    private static MySQLConnector instance;
    private BasicDataSource dataSource;
    
    /**
     * Sets the Pooled Connection parameters for the connection to the SQL Server.
     */    
    
    public MySQLConnector()throws IOException, SQLException, PropertyVetoException{

            dataSource = new BasicDataSource();
            dataSource.setUrl(address);
            dataSource.setUsername(username);
            dataSource.setPassword(password);
 
            dataSource.setMinIdle(10);
            dataSource.setMaxIdle(20);
            dataSource.setMaxOpenPreparedStatements(50);
  
    }

    /**
     * Creates a single instance of the connection to prevent degradation.
     */ 
    
    public static MySQLConnector getInstance() throws IOException, SQLException, PropertyVetoException {
        if (instance == null) {
            instance = new MySQLConnector();
            return instance;
        } else {
            return instance;
        }
    }

    /**
     * Creates the connection the server using the supplied parameters.
     */     
    
    public Connection getConnection() throws SQLException {
        return this.dataSource.getConnection();
    }    
    
}
