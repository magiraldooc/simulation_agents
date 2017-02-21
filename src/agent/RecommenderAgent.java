/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import agent.behaviour.RecommenderBehaviour;
import jade.core.Agent;

/**
 *
 * @author magir
 */
public class RecommenderAgent extends Agent{

    @Override
    protected void setup() {
        
        RecommenderBehaviour recommenderBehaviour = new RecommenderBehaviour(this);
        addBehaviour(recommenderBehaviour);
        
        super.setup();
    }

    @Override
    protected void takeDown() {
        super.takeDown();
    }
    
    
    
}
