/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import agent.behaviour.ControllerBehaviour;
import jade.core.Agent;

/**
 *
 * @author magir
 */
public class ControllerAgent extends Agent{

    @Override
    protected void setup() {
        ControllerBehaviour behavior = new ControllerBehaviour(this);
        addBehaviour(behavior);
        
        super.setup();
    }

    @Override
    protected void takeDown() {
        
        System.out.println("Agente bajado");
    
    }
    
}
