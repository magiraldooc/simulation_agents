/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent.behaviour;

import agent.model.LOList;
import agent.model.LOM;
import agent.model.SearchLO;
import agent.utils.Utils;
import com.google.gson.Gson;
import com.sun.org.apache.xalan.internal.xsltc.trax.SAX2DOM;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.OneShotBehaviour;
import jade.lang.acl.ACLMessage;
import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jdom2.Document;
import org.jdom2.Element;
import org.jdom2.JDOMException;
import org.jdom2.input.SAXBuilder;
import org.json.JSONArray;
import org.json.JSONObject;
import sun.net.www.http.HttpClient;

/**
 *
 * @author magir
 */
public class RecommenderBehaviour extends OneShotBehaviour{

    public RecommenderBehaviour(Agent a) {
        
        super(a);
        myAgent = a;
        
    }
    
    @Override
    public void action() {
        
        Utils utils = new Utils();
        ACLMessage receivedMessage = utils.receiveMessage(myAgent, null);
        
        Gson gson = new Gson();
        
        if(receivedMessage != null){
            if(receivedMessage.getPerformative() == ACLMessage.REQUEST){
                
                if(utils.getMessage(receivedMessage.getContent()).
                        equals("search lo")){
                    
                    System.out.println("1. Recuperar OA y recomendar por perfil");
                    
                    SearchLO searchLO = gson.fromJson(
                            utils.getJsonObject(receivedMessage.getContent()),
                            SearchLO.class);
                    
                    try {
                        searchLO.setLoList(this.getLO(searchLO.getSearchString()));
                        
                        System.out.println("Lista objetos: " + 
                                searchLO.getLoList().getLOList().size());
                        
                        /******************************************************/
                        //Sin recomendación
                        searchLO.setNullVisionList(searchLO.getLoList());
                        searchLO.setLowVisionList(searchLO.getLoList());
                        searchLO.setNullAuralList(searchLO.getLoList());
                        searchLO.setLowAuralList(searchLO.getLoList());
                        
                        /******************************************************/
                        //Con recomendación
//                        searchLO = distributeLO(searchLO);
                        
                        String json = gson.toJson(searchLO);

                        utils.sendMessage(myAgent, 
                                new AID("ControllerAgent", AID.ISLOCALNAME), 
                                "obj;" + json + ";" + "msg;" + "recomended lo", 
                                ACLMessage.REQUEST);
                        
                        System.out.println("Visión nula: " + 
                                searchLO.getNullVisionList().getLOList().size());
                        System.out.println("Visión baja: " + 
                                searchLO.getLowVisionList().getLOList().size());
                        System.out.println("Audición nula: " + 
                                searchLO.getNullAuralList().getLOList().size());
                        System.out.println("Audición baja: " + 
                                searchLO.getLowAuralList().getLOList().size());
                        
                        long start = new Date().getTime();
                        while(new Date().getTime() - start < 3000L){

                        }
                        
                        myAgent.doDelete();
                        
                    } catch (UnsupportedEncodingException ex) {
                        Logger.getLogger(RecommenderBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (IOException ex) {
                        Logger.getLogger(RecommenderBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                    } catch (JDOMException ex) {
                        Logger.getLogger(RecommenderBehaviour.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
            }
        }
        
    }
    
    /***
     * Método para realizar petición get al servicio de recuperación de OA
     * 
     * @param searchString
     * @throws MalformedURLException
     * @throws UnsupportedEncodingException
     * @throws IOException 
     */
    private LOList getLO(String searchString) throws MalformedURLException, UnsupportedEncodingException, IOException, JDOMException{
        
        System.out.println("Cadena de busqueda OA: " + searchString);
        URL url = new URL("http://froac.manizales.unal.edu.co/froacn/?raim=" + searchString);
        
        URLConnection connection = url.openConnection();
        
        // Leyendo el resultado
        BufferedReader in = new BufferedReader(new InputStreamReader(
                connection.getInputStream()));
        
        String row;
        LOList loList = new LOList();
        
        while ((row = in.readLine()) != null) {
//            System.out.println("Respuesta servicio busqueda OA:");
            System.out.println(row);
            
            //Json array
            JSONArray jsonArray = new JSONArray(row.substring(1, row.length() - 1));
            
            for(int i = 0; i < jsonArray.length(); i ++){
//                System.out.println(jsonArray.getJSONObject(i).getString("xml"));
                LOM lom = new LOM();
                
                SAXBuilder builder = new SAXBuilder();
                
                Document xmlDocument = 
                        builder.build(new ByteArrayInputStream(
                                jsonArray.getJSONObject(i).getString("xml").getBytes()));
                
                Element root = xmlDocument.getRootElement();
                
                List educational = root.getChildren(
                        "educational", root.getNamespace());
                
                Element educationalChild = (org.jdom2.Element) educational.get(0);
                
                Element resourceType = educationalChild.getChild(
                        "learningresourcetype", educationalChild.getNamespace());
                
                lom.setLearningResourceType(resourceType == null ? "" : resourceType.getValue().trim());
                
                Element interactivityLevel = educationalChild.getChild(
                        "interactivitylevel", educationalChild.getNamespace());
                
                lom.setInteractivityLevel(interactivityLevel == null ? "" : interactivityLevel.getValue().trim());
                
                Element interactivityType = educationalChild.getChild(
                        "interactivitytype", educationalChild.getNamespace());
                
                lom.setInteractivityType(interactivityType == null ? "" : interactivityType.getValue().trim());
                
                /***************************************************************/
                
                List technical = root.getChildren(
                        "technical", root.getNamespace());
                
                Element technicalChild = (org.jdom2.Element) technical.get(0);
                
                Element format = technicalChild.getChild(
                        "format", technicalChild.getNamespace());
                
                lom.setFormat(format == null ? "" : format.getValue().trim());
                
                /***************************************************************/
                
                List accessibility = root.getChildren(
                        "accessibility", root.getNamespace());
                
                Element accessibilityChild = (org.jdom2.Element) accessibility.get(0);
                
                Element presentationModeChild = accessibilityChild.getChild(
                        "presentationmode", accessibilityChild.getNamespace());
                
                Element auditory = presentationModeChild.getChild(
                        "auditory", presentationModeChild.getNamespace());
                
                lom.setAuditory(auditory == null ? "" : auditory.getValue().trim());
                
                Element textual = presentationModeChild.getChild(
                        "textual", presentationModeChild.getNamespace());
                
                lom.setTextual(textual == null ? "" : textual.getValue().trim());
                
                /*****************/
                
                Element adaptationTypeChild = accessibilityChild.getChild(
                        "adaptationtype", accessibilityChild.getNamespace());
                
                Element hearingAlternative = adaptationTypeChild.getChild(
                        "hearingalternative", adaptationTypeChild.getNamespace());
                
                lom.setHearingAlternative(hearingAlternative == null ? "" : hearingAlternative.getValue().trim());
                
                Element textualAlternative = adaptationTypeChild.getChild(
                        "textualalternative", adaptationTypeChild.getNamespace());
                
                lom.setTextualAlternative(textualAlternative == null ? "" : textualAlternative.getValue().trim());
                
                Element signLanguage = adaptationTypeChild.getChild(
                        "signlanguage", adaptationTypeChild.getNamespace());
                
                lom.setSignLanguage(signLanguage == null ? "" : signLanguage.getValue().trim());
                
                loList.addLO(lom);
            }
        }
        
        System.out.println(loList.toString());
        
        return loList;
        
    }
    
    private SearchLO distributeLO(SearchLO searchLO){
        
        LOList nullVisionList = new LOList();
    
        LOList lowVisionList = new LOList();

        LOList nullAuralList = new LOList();

        LOList lowAuralList = new LOList();
        
        for(LOM lom : searchLO.getLoList().getLOList()){
            
            if(
                    (lom.getLearningResourceType().trim().toLowerCase().equals("narrative text") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("lecture") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("audio"))
//                    (lom.getInteractivityLevel().trim().toLowerCase().equals("low") ||
//                    lom.getInteractivityLevel().trim().toLowerCase().equals("very low")) &&
//                    (lom.getInteractivityType().trim().toLowerCase().equals("expositivo") ||
//                    lom.getInteractivityType().trim().toLowerCase().equals("mixto"))
                    ){
                nullVisionList.addLO(lom);
            }
            if(
                    (lom.getLearningResourceType().trim().toLowerCase().equals("narrative text") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("lecture") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("audio"))
//                    (lom.getInteractivityLevel().trim().toLowerCase().equals("medium") ||
//                    lom.getInteractivityLevel().trim().toLowerCase().equals("high")) &&
//                    (lom.getInteractivityType().trim().toLowerCase().equals("expositivo") ||
//                    lom.getInteractivityType().trim().toLowerCase().equals("mixto"))
                    ){
                lowVisionList.addLO(lom);
            }
            if(
                    (lom.getLearningResourceType().trim().toLowerCase().equals("diagram") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("self assessment") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("simulation") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("questionnaire") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("slide") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("exercise"))
//                    (lom.getInteractivityLevel().trim().toLowerCase().equals("low") ||
//                    lom.getInteractivityLevel().trim().toLowerCase().equals("very low") ||
//                    lom.getInteractivityLevel().trim().toLowerCase().equals("medium")) &&
//                    (lom.getInteractivityType().trim().toLowerCase().equals("activo") ||
//                    lom.getInteractivityType().trim().toLowerCase().equals("mixto"))
                    ){
                nullAuralList.addLO(lom);
            }
            if(
                    (lom.getLearningResourceType().trim().toLowerCase().equals("diagram") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("figure") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("graph") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("self assessment") ||
                    lom.getLearningResourceType().trim().toLowerCase().equals("table")||
                    lom.getLearningResourceType().trim().toLowerCase().equals("exercise"))
//                    (lom.getInteractivityLevel().trim().toLowerCase().equals("low") ||
//                    lom.getInteractivityLevel().trim().toLowerCase().equals("very low") ||
//                    lom.getInteractivityLevel().trim().toLowerCase().equals("medium") ||
//                    lom.getInteractivityLevel().trim().toLowerCase().equals("high") ||
//                    lom.getInteractivityLevel().trim().toLowerCase().equals("very high")) &&
//                    (lom.getInteractivityType().trim().toLowerCase().equals("activo") ||
//                    lom.getInteractivityType().trim().toLowerCase().equals("mixto"))
                    ){
                lowAuralList.addLO(lom);
            }
            
        }
        
        searchLO.setNullVisionList(nullVisionList);
        searchLO.setLowVisionList(lowVisionList);
        searchLO.setNullAuralList(nullAuralList);
        searchLO.setLowAuralList(lowAuralList);
        
        return searchLO;
    }
    
}
