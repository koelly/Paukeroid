package de.koelly.pauker;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import android.util.Log;

import de.koelly.pauker.PaukerDataSet;
import de.koelly.pauker.PaukerHandler;


public class PaukerProvider {
	
    ArrayList <PaukerDataSet> data;
    PaukerHandler myPaukerHandler  = new PaukerHandler();
    String description;
    
	
	public PaukerProvider(){
	}
	
	public ArrayList<PaukerDataSet> getData(String _path){
    	try {
    		SAXParserFactory spf = SAXParserFactory.newInstance();    		
			SAXParser sp = spf.newSAXParser();
			
			XMLReader xr = sp.getXMLReader();
        	PaukerHandler myPaukerHandler = new PaukerHandler();
        	xr.setContentHandler(myPaukerHandler);
        	
        	FileInputStream fis = new FileInputStream(_path);
        	xr.parse(new InputSource(fis));
            fis.close();
            
            //data = null;
            data = myPaukerHandler.getParsedData();
            description = myPaukerHandler.getDescription();

		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		} catch (SAXException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		

        return data;	
	}
	
	public PaukerProvider(URL _url){
		//TODO Make this work :)
	}
	
	String getDescription(){
		//String description = myPaukerHandler.getDescription();
		Log.d("PaukerProvider:", "Description: "+description);
		return description;
	}

}
