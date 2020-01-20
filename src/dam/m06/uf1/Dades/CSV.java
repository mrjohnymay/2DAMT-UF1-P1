/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dam.m06.uf1.Dades;

import dam.m06.uf1.Aplicacio.Model.Equip;
import dam.m06.uf1.Aplicacio.Model.Equips;
import dam.m06.uf1.Aplicacio.Model.Jugador;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manel
 */
public class CSV {

    public static void exportaEquipsACSV(File fitx, Equips dades) throws DadesException {
        try {
            FileWriter fitxerW = new FileWriter(fitx, false);//Creem el objecte per escriure un arxiu amb l'arxiu que li passem per paràmetre i li passem true per a que sobreescriui l'arxiu.
            for (Equip equip : dades.getEquips()) {//Recorrem els equips i per cada equip fem una línia amb les seves dades
                fitxerW.write(equip.getId() + "," + equip.getNombre() + "," + equip.getEstadio() + "," + equip.getPoblacion() + "," + equip.getProvincia() + "," + equip.getCp() + System.getProperty("line.separator"));//Passem les dades per l'arxiu amb un salt de linia.
            }
            fitxerW.close();//Tanquem el fitxer
        } catch (IOException ex) {
            Logger.getLogger(CSV.class.getName()).log(Level.SEVERE, null, ex);//Agafem la excepció i la passem al log
        }
    }

    public static void exportaJugadorsACSV(File fitx, Equips dades) throws DadesException {
        try {
            FileWriter fitxerW = new FileWriter(fitx, false);//Creem el objecte per escriure un arxiu amb l'arxiu que li passem per paràmetre i li passem true per a que sobreescriui l'arxiu

            for (Equip equip : dades.getEquips()) {//Recorrem els equips i per cada equip recorrem els jugadors
                for (Jugador jug : equip.getJugadors()) {//Recorrem els jugadors i per cada jugador fem una línia
                    fitxerW.write(jug.getId() + "," + jug.getIdEquip() + "," + jug.getNombre() + "," + jug.getDorsal() + "," + jug.getEdad() + "," + jug.getCp() + System.getProperty("line.separator"));//Passem les dades per l'arxiu amb un salt de linia.
                }
            }
            fitxerW.close();//Tanquem el fitxer
        } catch (IOException ex) {
            Logger.getLogger(CSV.class.getName()).log(Level.SEVERE, null, ex);//Agafem la excepció i la passem al log
        }
    }
}
