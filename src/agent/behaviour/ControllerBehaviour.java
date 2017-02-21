/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent.behaviour;

import agent.InterfaceAgent;
import agent.model.LOEvaluation;
import agent.model.SearchLO;
import agent.model.Student;
import agent.model.StudentList;
import agent.utils.Utils;
import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import jade.wrapper.AgentController;
import jade.wrapper.ContainerController;
import jade.wrapper.StaleProxyException;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * En esta clase se implementa un comportamiento ciclico, donde se define que la
 * interfaz del usuario del sma espera recibir un mensaje, el cual publicará en 
 * el área de texto de la iterfaz del chat
 * 
 * @author Mauricio
 */
public class ControllerBehaviour extends CyclicBehaviour{
    
    ArrayList<Student> controllerStudentList;
    
    public ControllerBehaviour(Agent a) {
        super(a);
        myAgent = a;
        controllerStudentList = new ArrayList<>();
    }
    
    @Override
    public void action() {
        Utils utils = new Utils();
        ACLMessage receivedMessage = utils.receiveMessage(myAgent, null);
        
        Gson gson = new Gson();
        
        if(receivedMessage != null){
            
            if(receivedMessage.getPerformative() == ACLMessage.REQUEST){
                
                if(utils.getMessage(receivedMessage.getContent()).
                        equals("create simulation agents")){
                    System.out.println("1. Crear estudiantes simulados");
                    
                    StudentList studentList = gson.fromJson(
                            utils.getJsonObject(receivedMessage.getContent()),
                            StudentList.class);
                    
                    ContainerController container = myAgent.getContainerController();
                    
                    for(Student student : studentList.getStudentList()){
                    
                        long start = new Date().getTime();
                        while(new Date().getTime() - start < 2000L){

                        }
                        
                        String agentName = "Student-" + student.getId();
                        student.setAgentName(agentName);
                        controllerStudentList.add(student);
                        
                        String json = gson.toJson(student);
                        
                        try {
                            AgentController agent = container.createNewAgent(
                                    agentName,
                                    "agent.StudentAgent",
                                    null);
                            agent.start();

                            ACLMessage studentProfileMessage = new ACLMessage(ACLMessage.REQUEST);
                            studentProfileMessage.addReceiver(new AID(agentName, AID.ISLOCALNAME));
                            studentProfileMessage.setContent("obj;" + json + ";" + "msg;" + "student profile");
                            myAgent.send(studentProfileMessage);

                        } catch (StaleProxyException ex) {
                            System.out.println(ex.getMessage());
                        }
                    }
                }else if(utils.getMessage(receivedMessage.getContent()).
                        equals("search lo")){
                    System.out.println("2. Buscar OA y recomendar");
                    
                    SearchLO searchLO = gson.fromJson(
                            utils.getJsonObject(receivedMessage.getContent()),
                            SearchLO.class);
                    
                    ContainerController container = myAgent.getContainerController();
                    
                    String json = gson.toJson(searchLO);
                        
                    String agentName = "RecommenderAgent";

                    try {
                        AgentController agent = container.createNewAgent(
                                agentName,
                                "agent.RecommenderAgent",
                                null);
                        agent.start();

                        ACLMessage searchLOMessage = new ACLMessage(ACLMessage.REQUEST);
                        searchLOMessage.addReceiver(new AID(agentName, AID.ISLOCALNAME));
                        searchLOMessage.setContent("obj;" + json + ";" + "msg;" + "search lo");
                        myAgent.send(searchLOMessage);

                    } catch (StaleProxyException ex) {
                        System.out.println(ex.getMessage());
                    }
                }else if(utils.getMessage(receivedMessage.getContent()).
                        equals("recomended lo")){
                    System.out.println("3. Enviar OA recomendados a evaluar");
                    
                    SearchLO searchLO = gson.fromJson(
                            utils.getJsonObject(receivedMessage.getContent()),
                            SearchLO.class);
                    
                    String jsonLOResult = gson.toJson(searchLO);
                    
                    utils.sendMessage(myAgent,
                            new AID("InterfaceAgent", AID.ISLOCALNAME),
                            "obj;" + jsonLOResult + ";" + "msg;" + "lo result",
                            ACLMessage.REQUEST);
                    
                    if(!controllerStudentList.isEmpty()){
                        
                        for(Student student : controllerStudentList){
                            
                            long start = new Date().getTime();
                            while(new Date().getTime() - start < 1000L){

                            }
                            
                            LOEvaluation loEvaluation = new LOEvaluation();
                            loEvaluation.setStudent(student);
                            loEvaluation.setCompletLO(searchLO.getLoList());
                            
//                            searchLO.setNullVisionList(searchLO.getLoList());
//                            searchLO.setLowVisionList(searchLO.getLoList());
//                            searchLO.setNullAuralList(searchLO.getLoList());
//                            searchLO.setLowAuralList(searchLO.getLoList());
                            
                            if(!searchLO.getNullVisionList().getLOList().isEmpty() &&
                                    student.getDisability().equals("visual nula")){
                                
                                loEvaluation.setRecommendedLO(searchLO.getNullVisionList());
                                
                                String json = gson.toJson(loEvaluation);
                                
                                utils.sendMessage(myAgent,
                                        new AID(student.getAgentName(), AID.ISLOCALNAME),
                                        "obj;" + json + ";" + "msg;" + "evaluate lo",
                                        ACLMessage.REQUEST);
                                
                            }else if(!searchLO.getLowVisionList().getLOList().isEmpty() &&
                                    student.getDisability().equals("visual baja")){
                                
                                loEvaluation.setRecommendedLO(searchLO.getLowVisionList());
                                
                                String json = gson.toJson(loEvaluation);
                                
                                utils.sendMessage(myAgent,
                                        new AID(student.getAgentName(), AID.ISLOCALNAME),
                                        "obj;" + json + ";" + "msg;" + "evaluate lo",
                                        ACLMessage.REQUEST);
                                
                            }else if(!searchLO.getNullAuralList().getLOList().isEmpty() &&
                                    student.getDisability().equals("auditivo nulo")){
                                
                                loEvaluation.setRecommendedLO(searchLO.getNullAuralList());
                                
                                String json = gson.toJson(loEvaluation);
                                
                                utils.sendMessage(myAgent,
                                        new AID(student.getAgentName(), AID.ISLOCALNAME),
                                        "obj;" + json + ";" + "msg;" + "evaluate lo",
                                        ACLMessage.REQUEST);
                                
                            }else if(!searchLO.getLowAuralList().getLOList().isEmpty() &&
                                    student.getDisability().equals("auditivo bajo")){
                                
                                loEvaluation.setRecommendedLO(searchLO.getLowAuralList());
                                
                                String json = gson.toJson(loEvaluation);
                                
                                utils.sendMessage(myAgent,
                                        new AID(student.getAgentName(), AID.ISLOCALNAME),
                                        "obj;" + json + ";" + "msg;" + "evaluate lo",
                                        ACLMessage.REQUEST);
                                
                            }
                            
                        }
                    }
                }else if(utils.getMessage(receivedMessage.getContent()).
                        equals("evaluated lo")){
                    System.out.println("4. OA avaluados");
                    
                    utils.sendMessage(myAgent,
                            new AID("InterfaceAgent", AID.ISLOCALNAME),
                            receivedMessage.getContent(),
                            ACLMessage.REQUEST);
                    
                }
                
            }
        }
    }
}
