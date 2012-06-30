package anjelloatoz.blippAR;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

public class XMLHandler extends DefaultHandler{
	Boolean currentElement = false;
	String currentValue = null;
	public static SitesList sitesList = null;
	
	public static SitesList getSitesList() {
		return sitesList;
	}
	
	public static void setSitesList(SitesList sitesList) {
		XMLHandler.sitesList = sitesList;
	}
	
	@Override
	public void startElement(String uri, String localName, String qName,
	Attributes attributes) throws SAXException {

	currentElement = true;

	if (localName.equals("maintag")){
	/** Start */
		sitesList = new SitesList();
	}
	else if (localName.equals("website")){
	/** Get attribute value */
		String attr = attributes.getValue("category");
		sitesList.setCategory(attr);
	}

	}
	
	@Override
	public void endElement(String uri, String localName, String qName)
	throws SAXException{
		currentElement = false;
		/** set value */
		
		if (localName.equalsIgnoreCase("name"))
			sitesList.setName(currentValue);
		
		else if (localName.equalsIgnoreCase("website"))
			sitesList.setWebsite(currentValue);
		}
	
	@Override
	public void characters(char[] ch, int start, int length)
	throws SAXException{
		if (currentElement){
			currentValue = new String(ch, start, length);
			currentElement = false;
		}
	}
}
