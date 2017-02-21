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
public class SearchLO {
    
    private String searchString;
    
    private LOList loList;
    
    private LOList nullVisionList;
    
    private LOList lowVisionList;
    
    private LOList nullAuralList;
    
    private LOList lowAuralList;

    public SearchLO() {
        this.loList = new LOList();
        this.nullVisionList = new LOList();
        this.lowVisionList = new LOList();
        this.nullAuralList = new LOList();
        this.lowAuralList = new LOList();
    }

    public String getSearchString() {
        return searchString;
    }

    public void setSearchString(String searchString) {
        this.searchString = searchString;
    }

    public LOList getLoList() {
        return loList;
    }

    public void setLoList(LOList loList) {
        this.loList = loList;
    }

    public LOList getNullVisionList() {
        return nullVisionList;
    }

    public void setNullVisionList(LOList nullVisionList) {
        this.nullVisionList = nullVisionList;
    }

    public LOList getLowVisionList() {
        return lowVisionList;
    }

    public void setLowVisionList(LOList lowVisionList) {
        this.lowVisionList = lowVisionList;
    }

    public LOList getNullAuralList() {
        return nullAuralList;
    }

    public void setNullAuralList(LOList nullAuralList) {
        this.nullAuralList = nullAuralList;
    }

    public LOList getLowAuralList() {
        return lowAuralList;
    }

    public void setLowAuralList(LOList lowAuralList) {
        this.lowAuralList = lowAuralList;
    }
    
}
