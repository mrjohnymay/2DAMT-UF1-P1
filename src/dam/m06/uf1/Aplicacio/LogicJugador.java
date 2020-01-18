/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dam.m06.uf1.Aplicacio;

import dam.m06.uf1.Aplicacio.Model.Equip;
import dam.m06.uf1.Aplicacio.Model.Equips;
import dam.m06.uf1.Aplicacio.Model.Jugador;
import dam.m06.uf1.Dades.CSV;
import dam.m06.uf1.Dades.DadesException;
import dam.m06.uf1.Dades.JugadorsBD;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manel
 */
public class LogicJugador {
    
    public static ArrayList<Jugador> getJugadorsByIdEquip(int idEquip) throws AplicacioException
    {
        DriverMySql conn = null;
        ArrayList<Jugador> ret = null;
        
        try {
            conn = new DriverMySql();
            ret = JugadorsBD.CarregarJugadorsByIdEquip(conn.getConnection(), idEquip);
        }catch (DadesException ex) {
            throw new AplicacioException("Error getJugadors: " + ex.toString());
        }
        return ret;
    }
    
    public static ArrayList<Jugador> getJugadors() throws AplicacioException
    {
        DriverMySql conn = null;
        ArrayList<Jugador> ret = null;
        
        try {
            conn = new DriverMySql();
            ret = JugadorsBD.CarregarJugadors(conn.getConnection());
        }catch (DadesException ex) {
            throw new AplicacioException("Error getJugadors: " + ex.toString());
        }
        return ret;
    }
    
    public static void insertJugador(Jugador j) throws AplicacioException
    {
        DriverMySql conn = null;
        
        try {
            verificaReglesNegoci(j);
            
            conn = new DriverMySql();
           
            JugadorsBD.insertJugadorBD(conn.getConnection(), j);
            conn.closeConnection();
            
        } catch (DadesException ex) {
            throw new AplicacioException("Error insertJugador: " + ex.toString());
        }
    }
    
    public static void deleteJugador(Jugador j) throws AplicacioException
    {
        DriverMySql conn = null;
        
        try { 
            conn = new DriverMySql();
            JugadorsBD.eliminarJugador(conn.getConnection(), j);
            conn.closeConnection();
        } catch (DadesException ex) {
            throw new AplicacioException("Error deleteEquip: " + ex.toString());
        }
    }
    
    public static void deleteAllJugadors() throws AplicacioException
    {
        DriverMySql conn = null;
        
        try { 
            conn = new DriverMySql();
            JugadorsBD.eliminarJugadors(conn.getConnection());
            conn.closeConnection();
        } catch (DadesException ex) {
            throw new AplicacioException("Error deleteEquip: " + ex.toString());
        }
    }
    
    public static void modifyJugador(Jugador j) throws AplicacioException
    {
        DriverMySql conn = null;
        
        try {
            
            verificaReglesNegoci(j);
            
            conn = new DriverMySql();
            JugadorsBD.modificarJugador(conn.getConnection(), j);
            conn.closeConnection();
        } catch (AplicacioException | DadesException ex) {
            throw new AplicacioException("Error modifyJugador: " + ex.toString());
        }
    }   
    
    public static void verificaReglesNegoci(Jugador j) throws AplicacioException
    {
         reglaNegoci2(j);
         reglaNegoci3(j);
         reglaNegoci5(j);
    }
    
    /**
     * Si un jugador és menor d’edat no pot tenir un equip assignat
     * @param j
     * @throws AplicacioException 
     */
    private static void reglaNegoci2 (Jugador j) throws AplicacioException
    {
         String txtReglaNegoci2 = "Si un jugador és menor d’edat no pot tenir un dorsal diferent de zero assignat";
                 
        if (j.getEdad() < 18 && j.getDorsal() != 0)
            throw new AplicacioException(txtReglaNegoci2);
    }
    
    private static void reglaNegoci3 (Jugador j) throws AplicacioException
    {
         String txtReglaNegoci3 = "Un dorsal de jugador ha de ser entre 0 i 11";
                 
        if (j.getDorsal()< 0 || j.getDorsal() > 11)
            throw new AplicacioException(txtReglaNegoci3);
    }
    
    private static void reglaNegoci5 (Jugador j) throws AplicacioException
    {
         String txtReglaNegoci5 = "L’edat d’un jugador ha d’estar en un rang d’entre 16 i 30 anys";
                 
        if (j.getEdad() < 16 || j.getEdad() > 30)
            throw new AplicacioException(txtReglaNegoci5);
    }
    
    public static String desaJugadorsCSV(File fitx, Equips e) throws AplicacioException 
    {
        FileWriter fitxerW = null;
        String errors = "";
        try {
            fitxerW = new FileWriter(fitx, false);

            for (Equip equip : e.getEquips()) {
                for (Jugador jug : equip.getJugadors()) {
                    fitxerW.write(jug.getId() + "," + jug.getIdEquip() + "," + jug.getNombre() + "," + jug.getDorsal() + "," + jug.getEdad() + "," + jug.getCp() + System.getProperty("line.separator"));
                }
            }
            fitxerW.close();
        } catch (IOException ex) {
            Logger.getLogger(LogicEquip.class.getName()).log(Level.SEVERE, null, ex);
            errors += ex;
        } finally {
            try {
                fitxerW.close();
            } catch (IOException ex) {
                Logger.getLogger(LogicEquip.class.getName()).log(Level.SEVERE, null, ex);
                errors += ex;
            }
        }
        return errors;
    }
}
