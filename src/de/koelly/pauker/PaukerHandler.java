package de.koelly.pauker;

import java.util.ArrayList;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import android.util.Log;

	public class PaukerHandler extends DefaultHandler {
		
        private boolean lesson = false;
        private boolean description = false;
        private boolean batch = false;
        private boolean card = false;
        private boolean frontSide = false;
        private boolean frontText = false;
        private boolean reverseSide = false;
        private boolean reverseText = false;
        
        // Manche Pauker Dateien benutzen das "text" Tag, andere nicht.
        // Sollte es nicht benutzt werden, wird hier prophylaktisch
        // auf TRUE gesetzt, damit der Inhalt des Tags auch gespiechert wird
        private boolean text = true;
        
        private String t_lesson;
        private String t_description = "";
        private String t_frontText;
        private String t_backText;
        
        private StringBuilder sb_description = new StringBuilder();
         
        ArrayList <PaukerDataSet> data = new ArrayList<PaukerDataSet>();
        
	    public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
	    	if (localName.equals("Lesson")) {
                this.lesson = true;
	    	}else if (localName.equals("Description")) {
                this.description = true;
	    	}else if (localName.equals("Card")) {
                this.card = true;
	    	}else if (localName.equals("FrontSide")) {
                this.frontSide = true;
	    	}else if (localName.equals("ReverseSide") || localName.equals("BackSide")) {
                this.reverseSide = true;
	    	}else if (localName.equals("Text")) {
                this.text = true;
	    	}
	    }

    
	    public void endElement(String namespaceURI, String localName, String qName) throws SAXException {
	    	if (localName.equals("Lesson")) {
                this.lesson = false;
	    	} else if (localName.equals("Description")) {
                this.description = false;
	    	}else if (localName.equals("Batch")) {
                this.batch = false;
	    	}else if (localName.equals("Card")) {
                this.card = false;
                /*
                 * Neue Karte beginnt danach
                 */
                data.add(new PaukerDataSet(this.t_lesson, null , this.t_frontText, this.t_backText));
                
	    	}else if (localName.equals("FrontSide")) {
                this.frontSide = false;
	    	}else if (localName.equals("FrontText")) {
                this.frontText = false;
	    	}else if (localName.equals("ReverseSide") || localName.equals("BackSide")) {
                this.reverseSide = false;
	    	}else if (localName.equals("ReverseText")) {
	    		this.reverseText = false;
	    	}else if (localName.equals("Text")) {
	    		this.text = false;
	    	}
	    }

		/** Gets be called on the following structure:
         * <tag>characters</tag> */
        @Override
        public void characters(char ch[], int start, int length) {
        	 if(this.description){
        		 String tmp = new String(ch, start, length);
                 sb_description.append(tmp);
        	 }

        	 if(this.frontSide && this.text){
        		 String tmp = new String(ch, start, length);
        		 this.t_frontText = tmp.toString();
        	 }
        	 
        	 if(this.reverseSide && this.text){
        		 this.t_backText = new String(ch, start, length).toString();
        	 }

        	 
        }
        
        @Override
        public void startDocument() throws SAXException {
        	 Log.d("startDocument: ","Aufgerufen");
        }
 
        @Override
        public void endDocument() throws SAXException {
                // Do some finishing work if needed
        	Log.d("Description: ", ""+sb_description.toString());
        }


		public ArrayList<PaukerDataSet> getParsedData() {
			 return data;
		}
		
		public String getDescription(){
			Log.d("getDescription: ", sb_description.toString());
			return sb_description.toString();
		}
}
