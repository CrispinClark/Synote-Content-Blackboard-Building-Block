package gdp18.synote;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.w3c.dom.bootstrap.DOMImplementationRegistry;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSOutput;
import org.w3c.dom.ls.LSParser;
import org.w3c.dom.ls.LSSerializer;

import blackboard.platform.plugin.PlugInUtil;

public class Settings {
	private String synoteURL = "svm-tk1g11-gdp18.ecs.soton.ac.uk:3005";
    private String sharedKey = "test secret";
    private int jwtExpirySeconds = 10;
    
    public Settings(){
    	try {
            // Get path to plugin config directory.
            File configDir = PlugInUtil.getConfigDirectory(Utils.vendorID,
                    Utils.pluginHandle);
            File settingsFile = new File(configDir, "settings.xml");

            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();

            if(settingsFile.exists())
            {
                // Parse XML
                Document settingsDocument = db.parse(settingsFile);

                // Walk document tree and set corresponding setting values.
                readSettings(settingsDocument);
            }
        }
        catch(Exception e)
        {
            // Utils has static reference to instance of Settings, so can't use Utils.log() here
            e.printStackTrace();
        }
    }
    
    public String getSynoteURL()
    {
        return synoteURL;
    }
    
    public void setSynoteURL(String instanceName){
    	this.synoteURL = instanceName;
    	save();
    }
    
    public String getSharedKey(){
    	return sharedKey;
    }
    
    public void setSharedKey(String sharedKey){
    	this.sharedKey = sharedKey;
    	save();
    }
    
    public int getJWTExpirySeconds()
    {
    	return jwtExpirySeconds;
    }
    
    public void setJWTExpirySeconds(int expire){
    	this.jwtExpirySeconds = expire;
    	save();
    }    
    
 // Serialize current settings to XML settings file in config directory.
    private void save()
    {
        try
        {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document settingsDocument = db.newDocument();

            Element docElem = settingsDocument.createElement("config");
            settingsDocument.appendChild(docElem);

            Element synoteURLElem = settingsDocument.createElement("synoteURL");
            synoteURLElem.setAttribute("name", synoteURL);
            docElem.appendChild(synoteURLElem);

            Element sharedKeyElem = settingsDocument.createElement("sharedKey");
            sharedKeyElem.setAttribute("name", sharedKey);
            docElem.appendChild(sharedKeyElem);
            
            Element expirySecondsElem = settingsDocument.createElement("expirySeconds");
            expirySecondsElem.setAttribute("name", String.valueOf(jwtExpirySeconds));
            docElem.appendChild(expirySecondsElem);
            
            File configDir = PlugInUtil.getConfigDirectory(Utils.vendorID, Utils.pluginHandle);
            File settingsFile = new File(configDir, "settings.xml");

            FileOutputStream outStream = new FileOutputStream(settingsFile);
            
            DOMImplementationRegistry registry = DOMImplementationRegistry.newInstance();    
            DOMImplementationLS impl = (DOMImplementationLS) registry.getDOMImplementation("XML 3.0 LS 3.0");
            LSParser parser = impl.createLSParser(
                    DOMImplementationLS.MODE_SYNCHRONOUS,
                    "http://www.w3.org/TR/REC-xml");
            LSSerializer serializer = impl.createLSSerializer();
            LSOutput output = impl.createLSOutput();
            output.setEncoding("UTF-8");
            output.setByteStream(outStream);
            serializer.write(settingsDocument, output);
        }
        catch(Exception e)
        {
            Utils.log(e, "Error saving settings.");
        }
    }
    
 // Walk document tree and set corresponding setting values.
    private void readSettings(Document settingsDocument)
    {
        Element docElem = settingsDocument.getDocumentElement();

        NodeList synoteURLNodes = docElem.getElementsByTagName("synoteURL");
        if(synoteURLNodes.getLength() != 0)
        {
            Element synoteURLElem = (Element)synoteURLNodes.item(0);
            this.synoteURL = synoteURLElem.getAttribute("name");
        }

        NodeList sharedKeyNodes = docElem.getElementsByTagName("sharedKey");
        if(sharedKeyNodes.getLength() != 0)
        {
            Element sharedKeyElem = (Element)sharedKeyNodes.item(0);
            this.sharedKey = sharedKeyElem.getAttribute("name");
        }
        
        NodeList expirySecondsNodes = docElem.getElementsByTagName("expirySeconds");
        if(expirySecondsNodes.getLength() != 0)
        {
            Element expirySecondsElem = (Element)expirySecondsNodes.item(0);
            this.jwtExpirySeconds = Integer.parseInt(expirySecondsElem.getAttribute("name"));
        }
    }
}
