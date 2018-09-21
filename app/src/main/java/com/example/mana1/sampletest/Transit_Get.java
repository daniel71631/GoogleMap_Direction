package com.example.mana1.sampletest;

public class Transit_Get {

    private String title;
    private int id;

    public Transit_Get(int id, String title){
        this.id=id;
        this.title=title;
    }
    public Transit_Get(String title){
        this.title=title;
    }

    public int getId(){
        return id;
    }
    public void setId(int id){
        this.id=id;
    }
    public String getTitle(){
        return title;
    }
    public void setTitle(String title){
        this.title=title;
    }
}
