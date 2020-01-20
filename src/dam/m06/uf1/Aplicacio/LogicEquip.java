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
import dam.m06.uf1.Dades.EquipsBD;
import dam.m06.uf1.Dades.XML;
import dam.m06.uf1.Dades.JugadorsBD;
import java.io.File;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manel
 */
public class LogicEquip {

    /**
     * Retorna tots els equips amb els seus corresponents jugadors
     *
     * @return
     * @throws AplicacioException
     */
    public static ArrayList<Equip> getEquips() throws AplicacioException {
        DriverMySql conn = null;
        ArrayList<Equip> ret = null;

        try {
            conn = new DriverMySql();
            ret = EquipsBD.getEquipsBD(conn.getConnection());
            for (Equip e : ret) {
                e.setJugadors(JugadorsBD.CarregarJugadorsByIdEquip(conn.getConnection(), e.getId()));
            }
            conn.closeConnection();
        } catch (DadesException ex) {
            throw new AplicacioException("Error getEquips: " + ex.toString());
        }

        return ret;
    }

    public static void insertEquip(Equip e) throws AplicacioException {
        DriverMySql conn = null;

        try {

            reglaNegoci1(e);
            reglaNegoci4(e);

            conn = new DriverMySql();

            EquipsBD.insertEquipBD(conn.getConnection(), e);
            conn.closeConnection();

        } catch (DadesException ex) {
            throw new AplicacioException("Error insertEquip: " + ex.toString());
        }
    }

    public static void deleteEquip(Equip e) throws AplicacioException {
        DriverMySql conn = null;

        try {
            conn = new DriverMySql();
            EquipsBD.deleteEquipBD(conn.getConnection(), e);
            conn.closeConnection();
        } catch (DadesException ex) {
            throw new AplicacioException("Error deleteEquip: " + ex.toString());
        }
    }

    public static void deleteAllEquips() throws AplicacioException {
        DriverMySql conn = null;

        try {
            conn = new DriverMySql();
            EquipsBD.deleteAllEquipsBD(conn.getConnection());
            conn.closeConnection();
        } catch (DadesException ex) {
            throw new AplicacioException("Error deleteAllEquips: " + ex.toString());
        }
    }

    public static void modifyEquip(Equip e) throws AplicacioException {
        DriverMySql conn = null;

        try {
            reglaNegoci1(e);
            reglaNegoci4(e);

            conn = new DriverMySql();
            EquipsBD.modifyEquipBD(conn.getConnection(), e);
            conn.closeConnection();
        } catch (DadesException ex) {
            throw new AplicacioException("Error modifyEquip: " + ex.toString());
        }
    }

    /**
     * Un equip pot tenir el codi postal en blanc, pero si el té informat, ha de
     * ser en el format correcte: únicament 5 números.
     *
     * @param e
     * @throws AplicacioException
     */
    private static void reglaNegoci1(Equip e) throws AplicacioException {
        int tmp;

        String txtReglaNegoci = "Un equip ha de tenir el CP informat i en el format correcte: únicament 5 números.";

        try {
            //si és diferent de 5 caracters llavors llencem excepció
            if (e.getCp().length() != 5) {
                throw new AplicacioException(txtReglaNegoci);
            }
            // Si no ho podem passar a numèric, petarà
            tmp = Integer.parseInt(e.getCp());

        } catch (NumberFormatException ex) {
            throw new AplicacioException(txtReglaNegoci);
        }
    }

    /**
     * Un equip ha de tenir un estadi informat diferent de blanc.
     *
     * @param e
     * @throws AplicacioException
     */
    private static void reglaNegoci4(Equip e) throws AplicacioException {
        String txtReglaNegoci = "Un equip ha de tenir un estadi informat diferent de blanc.";

        if ("".equals(e.getEstadio())) {
            throw new AplicacioException(txtReglaNegoci);
        }
    }

    public static Equips carregaDadesDeXML(File fitxer) throws AplicacioException {
        Equips ret = new Equips();//Creem els equips

        try {
            ret = XML.carregaDadesDeXML(fitxer);//Cridem la funció amb el fitxer del que hem de carregar les dades
        } catch (DadesException ex) {//Agafem les excepción
            Logger.getLogger(LogicEquip.class.getName()).log(Level.SEVERE, null, ex);//Passem les excepcions al log
        }

        for (Equip equip : ret.getEquips()) {//Repassem els equips i comprovem les regles de negoci
            reglaNegoci1(equip);
            reglaNegoci4(equip);

            for (Jugador jugador : equip.getJugadors()) {//Recorrem els jugadors i mirem la verificació de les regles de negoci
                LogicJugador.verificaReglesNegoci(jugador);
            }
        }

        return ret;//Passem els equips una vegada comprovats
    }

    public static void exportaDadesToXML(File fitx, Equips dades) throws AplicacioException {
        try {
            XML.exportaDadesAXML(fitx, dades);//Cridem a la funció i li passem l'arxiu i els equips
        } catch (DadesException ex) {//Agafem la excepció
            Logger.getLogger(LogicEquip.class.getName()).log(Level.SEVERE, null, ex);//Els passem per el log
        }
    }

    /**
     * Desa les dades a la BD, tot verificant les regles de negoci
     *
     * @param e
     * @return string amb els errors o string buit si no hi ha errors
     */
    public static String desaDadesBD(Equips e) {
        String errors = "";

        if (e.getEquips() != null && e.getEquips().size() > 0) {
            for (Equip eq : e.getEquips()) {
                try {
                    LogicEquip.insertEquip(eq);
                } catch (AplicacioException ex) {
                    errors += "ID equip: " + eq.getId() + " ; Error: " + ex.toString() + System.getProperty("line.separator");
                }

                if (eq.getJugadors() != null && eq.getJugadors().size() > 0) {
                    for (Jugador j : eq.getJugadors()) {
                        try {
                            LogicJugador.insertJugador(j);
                        } catch (AplicacioException ex) {
                            errors += "ID Jugador: " + eq.getId() + " ; Error: " + ex.toString() + System.getProperty("line.separator");
                        }
                    }
                }
            }
        }

        return errors;
    }

    public static String desaEquipsCSV(File fitx, Equips e) throws AplicacioException {
        String errors = "";//Creem un string d'errors
        //En aquest try catch cridem a la funció de la capa DADES i agafem una excepció si hi ha
        try {
            CSV.exportaEquipsACSV(fitx, e);
        } catch (DadesException ex) {
            errors += ex;//Afegim la excepció a l'estring d'errors
        }
        
        return errors;
    }
}