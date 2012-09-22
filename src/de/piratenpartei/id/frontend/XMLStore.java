package de.piratenpartei.id.frontend;
/**
 * 
 */


import java.io.File;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

/**
 * SUX
 * @author artus
 *
 */
public class XMLStore implements Store {
	private DocumentBuilderFactory docFactory;
	private DocumentBuilder docBuilder;
	private Document doc;
	
	public XMLStore() {
		docFactory = DocumentBuilderFactory.newInstance();
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {
			e.printStackTrace();
		}

		doc = docBuilder.newDocument();
	}
	/* (non-Javadoc)
	 * @see de.piratenpartei.id.Store#put(java.lang.String, java.lang.Object)
	 */
	@Override
	public void put(String key, Object value) {
		try {
			Element e = doc.createElement(key);
			
			Element rootElement = doc.createElement("company");
			doc.appendChild(rootElement);
 
			// staff elements
			Element staff = doc.createElement("Staff");
			rootElement.appendChild(staff);
 
			// set attribute to staff element
			Attr attr = doc.createAttribute("id");
			attr.setValue("1");
			staff.setAttributeNode(attr);
 
			// shorten way
			// staff.setAttribute("id", "1");
 
			// firstname elements
			Element firstname = doc.createElement("firstname");
			firstname.appendChild(doc.createTextNode("yong"));
			staff.appendChild(firstname);
 
			// lastname elements
			Element lastname = doc.createElement("lastname");
			lastname.appendChild(doc.createTextNode("mook kim"));
			staff.appendChild(lastname);
 
			// nickname elements
			Element nickname = doc.createElement("nickname");
			nickname.appendChild(doc.createTextNode("mkyong"));
			staff.appendChild(nickname);
 
			// salary elements
			Element salary = doc.createElement("salary");
			salary.appendChild(doc.createTextNode("100000"));
			staff.appendChild(salary);
 
			// write the content into xml file
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			Transformer transformer = transformerFactory.newTransformer();
			DOMSource source = new DOMSource(doc);
			StreamResult result = new StreamResult(new File("C:\\file.xml"));
 
			// Output to console for testing
			// StreamResult result = new StreamResult(System.out);
 
			transformer.transform(source, result);
 
			System.out.println("File saved!");
 
		} catch (TransformerException tfe) {
			tfe.printStackTrace();
	  }
	}

	/* (non-Javadoc)
	 * @see de.piratenpartei.id.Store#get(java.lang.String)
	 */
	@Override
	public Object get(String key) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see de.piratenpartei.id.Store#fromString(java.lang.String)
	 */
	@Override
	public void fromString(String s) {
		// TODO Auto-generated method stub

	}
	@Override
	public void put(String key, Store value) {
		// TODO Auto-generated method stub
		
	}

}
