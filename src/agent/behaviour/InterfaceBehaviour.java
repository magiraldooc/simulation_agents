/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent.behaviour;

import agent.InterfaceAgent;
import agent.model.LOEvaluation;
import agent.model.SearchLO;
import agent.utils.Utils;
import com.google.gson.Gson;
import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;

/**
 * En esta clase se implementa un comportamiento ciclico, donde se define que la
 * interfaz del usuario del sma espera recibir un mensaje, el cual publicará en 
 * el área de texto de la iterfaz del chat
 * 
 * @author Mauricio
 */
public class InterfaceBehaviour extends CyclicBehaviour{
    
    public InterfaceAgent gui; 
    
    public InterfaceBehaviour(Agent a, InterfaceAgent gui) {
        super(a);
        myAgent = a;
        this.gui = gui;
    }
    
    @Override
    public void action() {
        Utils utils = new Utils();
        ACLMessage receivedMessage = utils.receiveMessage(myAgent, null);
        
        Gson gson = new Gson();
        
        if(receivedMessage != null){
            if(receivedMessage.getPerformative() == ACLMessage.REQUEST){
                if(utils.getMessage(receivedMessage.getContent()).
                        equals("evaluated lo")){
                    System.out.println("1. Mostrar resultado estudiantes");
                    
                    LOEvaluation loEvaluation = gson.fromJson(
                            utils.getJsonObject(receivedMessage.getContent()),
                            LOEvaluation.class);
                    
                    gui.gui.showStudentResult(loEvaluation);
                }else if(utils.getMessage(receivedMessage.getContent()).
                        equals("lo result")){
                    System.out.println("2. Mostrar resultado OA buscados");
                    
                    SearchLO searchLO = gson.fromJson(
                            utils.getJsonObject(receivedMessage.getContent()),
                            SearchLO.class);
                    
                    gui.gui.showLOResult(searchLO);
                }
            }
        }
    }
}
