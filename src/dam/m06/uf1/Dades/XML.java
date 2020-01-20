/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dam.m06.uf1.Dades;

import dam.m06.uf1.Aplicacio.LogicEquip;
import dam.m06.uf1.Aplicacio.Model.Equips;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import nu.xom.Document;
import nu.xom.ParsingException;

/**
 *
 * @author manel
 */
public class XML {

    public static void exportaDadesAXML(File fitx, Equips dades) throws DadesException {
        try {
            JAXBContext jaxbContext = JAXBContext.newInstance(Equips.class);//Creem la instància del JAXB

            Marshaller jaxbMarshaller = jaxbContext.createMarshaller();//Creem el marshaller

            jaxbMarshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, Boolean.TRUE);//Li donem format d'XML

            jaxbMarshaller.marshal(dades, fitx);//Utilitzem el marshal amb els equips i el fitxer
        } catch (JAXBException e) {//Agafem les excepcions
            e.printStackTrace();//Mostrem les excepcions
        }
    }

    public static Equips carregaDadesDeXML(File fitx) throws DadesException {
        Equips ret = new Equips();

        try {
            JAXBContext context = JAXBContext.newInstance(Equips.class);//Creem la instància de la classe amb els equips
            Unmarshaller unmarshaller = context.createUnmarshaller();//Creem el unmarshaller
            ret = (Equips) unmarshaller.unmarshal(fitx);//Passem les dades del XML a la classe
        } catch (JAXBException ex) {//Agafem les excepcions
            Logger.getLogger(LogicEquip.class.getName()).log(Level.SEVERE, null, ex);//Mostrem l'excepció per el log
        }

        return ret;
    }

    /**
     * Retorna un String que conté el XML
     *
     * @param txtUrl
     * @return
     * @throws DadesException
     */
    public static String readRemoteXML(String txtUrl) throws DadesException {
        String ret = "";
        
        try {
            nu.xom.Builder analitzador = new nu.xom.Builder();//Instanciem la classe
            Document doc = analitzador.build(txtUrl);//Passem tota la informació de la url i la passem a un Document
            nu.xom.Element arrel = doc.getRootElement();//Creem un Element del element root del Document
            nu.xom.Element prediccion = arrel.getFirstChildElement("prediccion");//Agafem el primer fill

            for (nu.xom.Element e : prediccion.getChildElements("ciudad")) {//Per cada fill agafem l'element
                ret += "Ciutat: " + e.getFirstChildElement("nombre").getValue() + " Temperatures min/max: " + e.getFirstChildElement("minima").getValue() + ";" + e.getFirstChildElement("maxima").getValue() + "\n";//Afegim la informació del document a la string
            }
        } catch (ParsingException ex) {//Agafem l'excepcio per poder obrir la URL
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {//Agafem l'excepció per poder manipular les classes
            Logger.getLogger(XML.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }
}
