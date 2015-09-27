package com.virginia.cs.cs4720androidproject;

public class Card {

    private String name;
    private String expansion;
    private String language;
    private int conditionIndex;


    public Card(String name){
        this.name = name;
        this.expansion = "";
        this.conditionIndex = 0;
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
}
