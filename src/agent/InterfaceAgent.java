/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import agent.behaviour.InterfaceBehaviour;
import agent.gui.GUI;
import jade.core.Agent;

/**
 *
 * @author magir
 */
public class InterfaceAgent extends Agent{
    
    public GUI gui;

    @Override
    protected void setup() {
        
        gui = new GUI(this);
        gui.setVisible(true);
        
        InterfaceBehaviour behaviour = new InterfaceBehaviour(this, this);
        addBehaviour(behaviour);
        
        super.setup();
        
    }

    @Override
    protected void takeDown() {
        gui.setVisible(false);
        System.out.println("Agente bajado");
    }
    
}
