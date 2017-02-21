/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent.behaviour;

import agent.StudentAgent;
import agent.model.LOEvaluation;
import agent.model.LOM;
import agent.model.Student;
import agent.model.StudentList;
import agent.utils.Utils;
import com.google.gson.Gson;
import jade.core.AID;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 *
 * @author magir
 */
public class ReceiveProfileBehaviour extends CyclicBehaviour{

    private StudentAgent studentAgent;
    
    public ReceiveProfileBehaviour(Agent a, StudentAgent agent) {
        super(a);
        myAgent = a;
        studentAgent = agent;
    }
    
    @Override
    public void action() {
        Utils utils = new Utils();
        ACLMessage receivedMessage = utils.receiveMessage(myAgent, null);
        
        Gson gson = new Gson();
        
        if(receivedMessage != null){
            if(receivedMessage.getPerformative() == ACLMessage.REQUEST){
                
                if(utils.getMessage(receivedMessage.getContent()).
                        equals("profile")){
                    
                    System.out.println("1. Recibir perfil del estudiante");
                    
                    Student student = gson.fromJson(
                            utils.getJsonObject(receivedMessage.getContent()),
                            Student.class);
                    
                    System.out.println(student.getId() + ": Hola");
                    
                    studentAgent.setAge(student.getAge());
                    studentAgent.setDisability(student.getDisability());
                    
                }else if(utils.getMessage(receivedMessage.getContent()).
                        equals("evaluate lo")){
                    
                    System.out.println("2. Recibir objetos recomendados");
                    
                    LOEvaluation loEvaluation = gson.fromJson(
                            utils.getJsonObject(receivedMessage.getContent()),
                            LOEvaluation.class);
                    
                    for(LOM lom : loEvaluation.getRecommendedLO().getLOList()){
                        
                        if(loEvaluation.getStudent().getDisability().equals("visual nula")){
                            
                            if(lom.getAuditory().toLowerCase().equals("voz") &&
                                    lom.getHearingAlternative().toLowerCase().equals("si")){
                                loEvaluation.getAcceptedLO().addLO(lom);
                            }else if(lom.getInteractivityLevel().toLowerCase().equals("very low") ||
                                    lom.getInteractivityLevel().toLowerCase().equals("low") ||
                                    lom.getInteractivityLevel().toLowerCase().equals("medium")){
                                loEvaluation.getAcceptedLO().addLO(lom);
                            }else if(lom.getFormat().toLowerCase().equals("audio") ||
                                    lom.getFormat().toLowerCase().equals("video")){
                                loEvaluation.getAcceptedLO().addLO(lom);
                            }
                            
                        }else if(loEvaluation.getStudent().getDisability().equals("visual baja")){
                            
                            if((lom.getAuditory().toLowerCase().equals("voz") &&
                                    lom.getHearingAlternative().toLowerCase().equals("si")) ||
                                    (lom.getTextual().toLowerCase().equals("si") &&
                                    lom.getTextualAlternative().toLowerCase().equals("si"))){
                                loEvaluation.getAcceptedLO().addLO(lom);
                            }else if(lom.getInteractivityLevel().toLowerCase().equals("low") ||
                                    lom.getInteractivityLevel().toLowerCase().equals("medium") ||
                                    lom.getInteractivityLevel().toLowerCase().equals("high")){
                                loEvaluation.getAcceptedLO().addLO(lom);
                            }else if(lom.getFormat().toLowerCase().equals("audio") ||
                                    lom.getFormat().toLowerCase().equals("video") ||
                                    lom.getFormat().toLowerCase().equals("texto")){
                                loEvaluation.getAcceptedLO().addLO(lom);
                            }

                        }else if(loEvaluation.getStudent().getDisability().equals("auditivo nulo") ||
                                loEvaluation.getStudent().getDisability().equals("auditivo bajo")){
                            
                            if(lom.getSignLanguage().toLowerCase().equals("si")){
                                loEvaluation.getAcceptedLO().addLO(lom);
                            }else if(lom.getInteractivityLevel().toLowerCase().equals("very low") ||
                                    lom.getInteractivityLevel().toLowerCase().equals("low") ||
                                    lom.getInteractivityLevel().toLowerCase().equals("medium")){
                                loEvaluation.getAcceptedLO().addLO(lom);
                            }else if(lom.getTextual().toLowerCase().equals("si") &&
                                    lom.getTextualAlternative().toLowerCase().equals("si")){
                                loEvaluation.getAcceptedLO().addLO(lom);
                            }else if(lom.getFormat().toLowerCase().equals("texto") ||
                                    lom.getFormat().toLowerCase().equals("imagen") ||
                                    lom.getFormat().toLowerCase().equals("aplicacion")){
                                loEvaluation.getAcceptedLO().addLO(lom);
                            }
                        }
                    }
                    
                    String json = gson.toJson(loEvaluation);
                    
                    utils.sendMessage(myAgent,
                            new AID("ControllerAgent", AID.ISLOCALNAME),
                            "obj;" + json + ";" + "msg;" + "evaluated lo",
                            ACLMessage.REQUEST);
                    
                }
            }
        }
    }
    
    
}
