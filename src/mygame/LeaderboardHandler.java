package mygame;

import java.io.File;
import javax.swing.JOptionPane;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

public final class LeaderboardHandler {
    
    public void addScore(String playerName, int Score){
        File f = new File(System.getProperty("user.dir") + "\\leaderboard.xml");
        checkFile(f);
        
        try{
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(f);
            
            Element rootElement = doc.getDocumentElement();
            
            Element player = doc.createElement("player");
            rootElement.appendChild(player);
            int tmp = doc.getElementsByTagName("player").getLength();
            player.setAttribute("id", String.valueOf(doc.getElementsByTagName("player").getLength()));
            
            // Add elements to Player
            Element name = doc.createElement("name");
            Element score = doc.createElement("score");
            name.appendChild(doc.createTextNode(playerName));
            score.appendChild(doc.createTextNode(String.valueOf(Score)));
            player.appendChild(name);
            player.appendChild(score);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(f);
            
            transformer.transform(source, result);
            System.out.println("File saved!");
        }
        catch(Exception ex) {
            if (deleteFile(f)){
                addScore(playerName, Score);
            }
        }
    }
    private void checkFile(File f){
        if (!f.exists()){
            createFile(f);
        }
    }
    private void createFile(File f){
        try {
            f.createNewFile();
            
            DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docFactory.newDocumentBuilder();
            
            // add Root elements
            Document doc = docBuilder.newDocument();
            Element rootElement = doc.createElement("leaderboard");
            doc.appendChild(rootElement);
            
            // Add Player element (child of root element "Leaderboard")
            Element player = doc.createElement("player");
            rootElement.appendChild(player);
            player.setAttribute("id", String.valueOf(rootElement.getElementsByTagName("player").getLength()));
            // Add elements to Player
            Element name = doc.createElement("name");
            Element score = doc.createElement("score");
            name.appendChild(doc.createTextNode("Bob de Haan"));
            score.appendChild(doc.createTextNode("8"));
            player.appendChild(name);
            player.appendChild(score);
            
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();
            DOMSource source = new DOMSource(doc);
            StreamResult result = new StreamResult(f);
            
            transformer.transform(source, result);
            System.out.println("File saved!");
        } 
        catch (Exception ex) {
            System.out.println(ex.getMessage());
            deleteFile(f);
        }
    }
    private boolean deleteFile(File f){
        int dialogResult = JOptionPane.showConfirmDialog(null, "There was a problem with file: " + f.getName() + "/r/nDo you want to delete and recreate it?");
        if (dialogResult == JOptionPane.YES_OPTION){
            //TODO: delete file
            checkFile(f);
            return true;
        }
        return false;
    }
}
