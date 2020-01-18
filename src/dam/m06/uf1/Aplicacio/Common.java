/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dam.m06.uf1.Aplicacio;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import nu.xom.Document;
import nu.xom.ParsingException;
/**
 *
 * @author manel
 */
public class Common {

    public static String retornaTempsCiutat(String url) throws AplicacioException {
        String ret = "";
        try {
            nu.xom.Builder analitzador = new nu.xom.Builder();
            Document doc = analitzador.build(url);
            nu.xom.Element arrel = doc.getRootElement();
            nu.xom.Element prediccion = arrel.getFirstChildElement("prediccion");
            
            for (nu.xom.Element e : prediccion.getChildElements("ciudad")) {
                ret += "Ciutat: " + e.getFirstChildElement("nombre").getValue() + " Temperatures min/max: " + e.getFirstChildElement("minima").getValue() + ";" + e.getFirstChildElement("maxima").getValue() + "\n";
            }
        } catch (ParsingException ex) {
            Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(Common.class.getName()).log(Level.SEVERE, null, ex);
        }
        return ret;
    }

}
