/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dam.m06.uf1.Aplicacio;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author manel
 */
public class DriverMySql {
    
    // Per seguretat aquestes dades s'haurien de llegir d'un fitxer xifrat o similar
    String bd = "lliga_futbol";
    String usuari = "root";
    String password = "12345678";
    
    Connection conn = null;

    public Connection getConnection() {
        return conn;
    }
    
    public DriverMySql() throws AplicacioException {
        
        
        this.conn = ConnectarBD();
    }
    
        
    /**
    * Connecta a una BD mysql i gestiona la connexió
    *
    * @return objecte Connection
    * @throws SQLException 
    */
    private Connection ConnectarBD() throws AplicacioException
    {
        Connection ret = null;
        
        
        try {
            ret =  DriverManager.getConnection("jdbc:mysql://localhost:3306/"+bd+"?useUnicode=true&"
                    + "useJDBCCompliantTimezoneShift=true&"   
                    + "useLegacyDatetimeCode=false&serverTimezone=UTC", usuari, password);
        } catch (SQLException ex) {
            throw new AplicacioException("Error ConnectarBD: " + ex.toString());
        }
       
        
        return ret;
    }
    
    public void closeConnection() throws AplicacioException
    {
        try {
            this.conn.close();
        } catch (SQLException ex) {
            throw new AplicacioException("Error inicialitzant connexió: " + ex.toString());
        }
    }
}
