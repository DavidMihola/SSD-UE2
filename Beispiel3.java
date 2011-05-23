/**
 * Semistrukturierte Daten - SS 2011
 * Uebungsbeispiel 3
 */

import org.xml.sax.XMLReader;

import org.xml.sax.InputSource;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;

import javax.xml.transform.sax.SAXSource;
import javax.xml.transform.stream.StreamResult;


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
	
	InputSource inpSrc = new InputSource(memoryXML);
	SAXSource saxSrc = new SAXSource(inpSrc);
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
	}
}
