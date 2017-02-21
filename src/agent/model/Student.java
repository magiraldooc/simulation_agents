/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent.model;

/**
 *
 * @author magir
 */
public class Student {
    
    private String id;
    
    private String agentName;
    
    private Integer age;
    
    private String disability;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

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

    public String getAgentName() {
        return agentName;
    }

    public void setAgentName(String agentName) {
        this.agentName = agentName;
    }

    @Override
    public String toString() {
        return "Student{" + "id=" + id + ", agentName=" + agentName + ", age=" + age + ", disability=" + disability + '}';
    }
    
}
