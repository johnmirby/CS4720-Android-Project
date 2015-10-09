package com.virginia.cs.cs4720androidproject;

import android.content.Intent;

public class Card {

    private String name;
    private String expansion;
    private String language;
    private int conditionIndex;
    private String imageFileName;


    public Card(String name){
        this.name = name;
        this.expansion = "";
        this.language = "";
        this.conditionIndex = 0;
        this.imageFileName = "";
    }

    @Override
    public String toString(){
        return this.getName();
    }

    public void setName(String name){
        this.name = name;
    }

    public void setExpansion(String expansion){
        this.expansion = expansion;
    }

    public void setLanguage(String language){
        this.language = language;
    }

    public void setConditionIndex(int index){
        this.conditionIndex = index;
    }

    public void setImageFileName(String imageFileName) { this.imageFileName = imageFileName; }

    public String getName(){
        return this.name;
    }

    public String getExpansion(){
        return this.expansion;
    }

    public String getLanguage(){
        return this.language;
    }

    public int getConditionIndex(){
        return this.conditionIndex;
    }

    public String getImageFileName() { return this.imageFileName; }

    public String toCSV(){
        return this.name + "," + this.expansion + "," + this.language
                + "," + Integer.toString(this.conditionIndex) + "," + this.imageFileName;
    }
}
