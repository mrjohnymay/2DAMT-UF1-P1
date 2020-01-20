/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dam.m06.uf1.Aplicacio;

import dam.m06.uf1.Dades.DadesException;
import dam.m06.uf1.Dades.XML;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author manel
 */
public class Common {

    public static String retornaTempsCiutat(String url) throws AplicacioException {
        String ret = "";

        try {
            ret = XML.readRemoteXML(url);//Llegim les dades amb la funció
        } catch (DadesException ex) {//Agafem les excepcions que venen de DADES
            Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);//Mostrem les excepcions per el log
        }

        return ret;
    }
}
