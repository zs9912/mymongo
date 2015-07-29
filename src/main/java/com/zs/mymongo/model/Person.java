/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zs.mymongo.model;

/**
 *
 * http://www.journaldev.com/4011/mongodb-java-servlet-web-application-example-tutorial
 */
public class Person {
    // id will be used for primary key in MongoDB
    // We could use ObjectId, but I am keeping it
    // independent of MongoDB API classes
    private String id;
 
    private String name;
 
    private String country;
 
    public String getName() {
        return name;
    }
 
    public void setName(String name) {
        this.name = name;
    }
 
    public String getCountry() {
        return country;
    }
 
    public void setCountry(String country) {
        this.country = country;
    }
 
    public String getId() {
        return id;
    }
 
    public void setId(String id) {
        this.id = id;
    }
}
