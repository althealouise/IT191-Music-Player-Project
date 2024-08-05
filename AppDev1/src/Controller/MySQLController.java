/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package Controller;

import Controller.MySQLConnector;
import Model.Music;
import java.beans.PropertyVetoException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 *
 * @author boxro
 */
public class MySQLController {
    
    Connection myConn = null;
    Statement myStmt = null;
    ResultSet myRes = null;
    
   public ArrayList<Music> showSongList() throws SQLException, IOException{
   
       ArrayList<Music> music = new ArrayList<Music>();
       try{
           myConn = MySQLConnector.getInstance().getConnection();
           myStmt = myConn.createStatement();
           String qry = "SELECT * FROM musicbackend.database;";
           myRes = myStmt.executeQuery(qry);
           System.out.println(qry);
           while(myRes.next()){
               music.add(new Music(myRes.getInt("id"),myRes.getString("title"),myRes.getString("artist"),myRes.getBinaryStream("art"),myRes.getString("lyrics"),myRes.getString("timelength")));
           
       }
       }
       catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());
       }
       catch (PropertyVetoException ex) { 
            Logger.getLogger(MySQLController.class.getName()).log(Level.SEVERE, null, ex);
        }
        myConn.close();
        return music;
        }
   
   public ArrayList<Music> selectSong(String table) throws SQLException, IOException, PropertyVetoException{
   
       ArrayList<Music> music = new ArrayList<Music>();
       try{
           myConn = MySQLConnector.getInstance().getConnection();
           myStmt = myConn.createStatement();
           String qry = table;
           myRes = myStmt.executeQuery(qry);
           while(myRes.next()){
               music.add(new Music(myRes.getInt("id"),myRes.getString("title"),myRes.getString("artist"),myRes.getBinaryStream("art"),myRes.getString("lyrics"),myRes.getString("timelength"),myRes.getBinaryStream("file")));
           }
            System.out.println("\nLoad finished!");
       }
       catch(SQLException ex){
            JOptionPane.showMessageDialog(null, ex.getMessage());           
       }
       myConn.close();
       return music;
   }

 
}