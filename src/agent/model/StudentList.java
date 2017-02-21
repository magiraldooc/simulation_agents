/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package agent.model;

import java.util.ArrayList;

/**
 *
 * @author magir
 */
public class StudentList {
    
    private ArrayList<Student> studentList;

    public StudentList() {
        this.studentList = new ArrayList<>();
    }

    public ArrayList<Student> getStudentList() {
        return studentList;
    }

    public void setStudentList(ArrayList<Student> studentList) {
        this.studentList = studentList;
    }
    
    public void addStudent(Student student) {
        this.studentList.add(student);
    }

    @Override
    public String toString() {
        return studentList.toString();
    }
    
}
