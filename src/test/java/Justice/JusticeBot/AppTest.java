package Justice.JusticeBot;

import java.awt.List;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

import org.xml.sax.* ;
import org.xml.sax.helpers.DefaultHandler;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     * @throws IOException 
     * @throws ParserConfigurationException 
     * @throws SAXException 
     */
    public void testApp() throws IOException, ParserConfigurationException, SAXException
    {
    	//"https://gelbooru.com/index.php?page=dapi&s=post&q=index&limit=100&api_key=235c8c190f950e9e32959d3d302ef11724aa7edfe9101be85ca8ef8af50ecf01&user_id=425026"
    	//"https://rule34.xxx/?page=dapi&s=post&q=index&limit=100"
    	URL url = new URL("https://rule34.xxx/?page=dapi&s=post&q=index&limit=100");
		HttpURLConnection httpConnection = (HttpURLConnection) url.openConnection();

		httpConnection.setRequestMethod("GET");
		httpConnection.setRequestProperty("Accept", "application/xml");

		InputStream xml = httpConnection.getInputStream();
		System.out.println(xml);
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document doc = db.parse(xml);
		doc.getDocumentElement().normalize();

		System.out.println(doc.getDocumentElement().getNodeName());
		
		NodeList nodeList = doc.getDocumentElement().getChildNodes();
		
		Node node = nodeList.item(0);
		
		NamedNodeMap nodeMap = node.getAttributes();
		
		System.out.println(nodeMap.getNamedItem("file_url"));
		
        assertTrue( true );
    }
}
