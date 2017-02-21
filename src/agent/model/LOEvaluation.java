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
public class LOEvaluation {
    
    private LOList completLO;
    
    private LOList recommendedLO;
    
    private LOList acceptedLO;
    
    private Student student;

    public LOEvaluation() {
        completLO = new LOList();
        recommendedLO = new LOList();
        acceptedLO = new LOList();
    }

    public LOList getCompletLO() {
        return completLO;
    }

    public void setCompletLO(LOList completLO) {
        this.completLO = completLO;
    }

    public LOList getRecommendedLO() {
        return recommendedLO;
    }

    public void setRecommendedLO(LOList recommendedLO) {
        this.recommendedLO = recommendedLO;
    }

    public LOList getAcceptedLO() {
        return acceptedLO;
    }

    public void setAcceptedLO(LOList acceptedLO) {
        this.acceptedLO = acceptedLO;
    }

    public Student getStudent() {
        return student;
    }

    public void setStudent(Student student) {
        this.student = student;
    }
    
}
