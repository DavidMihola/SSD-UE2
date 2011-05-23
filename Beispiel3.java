/**
 * Semistrukturierte Daten - SS 2011
 * Uebungsbeispiel 3
 */

import api.*;

import java.io.File;

import org.xml.sax.SAXException;

import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import org.xml.sax.InputSource;

import org.xml.sax.Attributes;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import javax.xml.transform.dom.DOMSource;

import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;

import org.xml.sax.helpers.XMLFilterImpl;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;

// org.xml.sax.SAXParseException;


public class Beispiel3 {
    public static void main(String[] args) throws Exception {
        // Argumentueberpruefung
        if (args.length != 3) {
            System.err.println("Usage: java Beispiel3 <MEMORY XML-FILE> "
                    + "<EMPTY XML-FILE FOR FILTERING> "
                    + "<EMPTY XML-FILE FOR CHECKING>");
            System.exit(1);
        }

        String xmlInput = args[0];
        String filteredXML = args[1];
        String checkedXML = args[2];

        Beispiel3 beispiel = new Beispiel3();

        beispiel.sax(xmlInput, filteredXML);
        beispiel.dom(filteredXML, checkedXML);
    }

    private Beispiel3() throws Exception {
    }

	class MyFilter extends XMLFilterImpl {

		private boolean deletePairs = false;
		private boolean insidePlayerElement = false;

		public void startElement(String namespaceUri,
					 String localName,
                        		 String qualifiedName,
					 Attributes attributes)
                         		 throws SAXException {
			boolean showTag = true;

			if (localName.equals("player")) {
				insidePlayerElement = true;
			} else if (localName.equals("uncovered-pairs") && !insidePlayerElement) {
				deletePairs = true;				
				showTag = false;
			} else if (localName.equals("covered-pairs")) {
				deletePairs = true;
				showTag = false;
			} else if (localName.equals("pair") && deletePairs) {
				showTag = false;
			}
			
			if (showTag) {
				super.startElement(namespaceUri, localName,
						   qualifiedName, attributes);
			}
		}

		public void endElement(String namespaceUri,
		                       String localName,
		                       String qualifiedName)
			               throws SAXException {
			boolean showTag = true;

			if (localName.equals("player")) {
				insidePlayerElement = false;
			} else if (localName.equals("uncovered-pairs") && !insidePlayerElement) {
				deletePairs = false;
				showTag = false;
			} else if (localName.equals("covered-pairs")) {
				deletePairs = false;
				showTag = false;
			} else if (localName.equals("pair") && deletePairs) {
				showTag = false;
			}

			if (showTag) {
				super.endElement(namespaceUri, localName,
						 qualifiedName);
			}
		
		}		
	}
    /**
     * Vervollstaendigen Sie die Methode. Der Name des XML-Files, welches
     * verarbeitet werden soll, wird mittels Parameter "memoryXML" uebergeben.
     *
     * Verwenden Sie fuer die Loesung dieser Teilaufgabe einen SAX-Prozessor. 
     * Der gefilterte und bearbeitete InputStream soll mittels eines
     * Transformers auf das File "filteredMemoryXML" geschrieben werden.
     */
    private void sax(String memoryXML, String filteredMemoryXML)
            throws Exception {
	
	XMLReader parser = XMLReaderFactory.createXMLReader();

	MyFilter filter = new MyFilter();
	filter.setParent(parser);

	InputSource inpSrc = new InputSource(memoryXML);
	SAXSource saxSrc = new SAXSource(filter, inpSrc);
	StreamResult strRes = new StreamResult(filteredMemoryXML);

	TransformerFactory factory = TransformerFactory.newInstance();
	Transformer transformer = factory.newTransformer();

	transformer.transform(saxSrc, strRes);

    }
	
    /**
     * Vervollstaendigen Sie die Methode. Der Name des XML-Files, welches
     * verarbeitet werden soll, wird mittels Parameter "filteredMemoryXML"
     * uebergeben.
     *
     * Verwenden Sie fuer die Loesung dieser Teilaufgabe einen DOM-Baum. Das 
     * manipulierte Document soll mittels eines Transformers in leserlicher
     * Form in das File "checkedMemoryXML" ausgegeben werden.
     */
    private void dom(String filteredMemoryXML, String checkedMemoryXML)
            throws Exception {

	DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

	//factory.setValidating(true);
	factory.setNamespaceAware(true);
	factory.setIgnoringComments(true);
	factory.setIgnoringElementContentWhitespace(true);

	DocumentBuilder builder = factory.newDocumentBuilder();

	Document document = builder.parse(new File(filteredMemoryXML));

	Game game = new Game(document);

	// hier muss man jetzt noch aus dem game die Paare auslesen und in das
	// DOM einbauen

	DOMSource source = new DOMSource(document);
	StreamResult result = new StreamResult(checkedMemoryXML);

	TransformerFactory transFactory = TransformerFactory.newInstance();
	Transformer transformer = transFactory.newTransformer();

	transformer.transform(source, result);

	}
}
