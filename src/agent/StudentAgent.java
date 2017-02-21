/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent;

import agent.behaviour.ReceiveProfileBehaviour;
import jade.core.Agent;
import jade.core.behaviours.ReceiverBehaviour;

/**
 *
 * @author magir
 */
public class StudentAgent extends Agent{
    
    private Integer age;
    
    private String disability;

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public String getDisability() {
        return disability;
    }

    public void setDisability(String disability) {
        this.disability = disability;
    }
    
    @Override
    protected void setup() {
        
        ReceiveProfileBehaviour behaviour = new ReceiveProfileBehaviour(this, this);
        this.addBehaviour(behaviour);
        
        super.setup();
    }

    @Override
    protected void takeDown() {
        super.takeDown(); //To change body of generated methods, choose Tools | Templates.
    }
    
}
