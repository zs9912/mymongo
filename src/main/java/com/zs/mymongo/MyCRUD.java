/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zs.mymongo;

import com.mongodb.MongoClient;
import com.mongodb.MongoCredential;
import com.mongodb.client.MongoDatabase;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.WriteResult;

import java.util.List;
import java.util.ArrayList;

import com.zs.mymongo.model.User;
/**
 *
 * http://www.journaldev.com/3963/mongodb-java-crud-example-tutorial
 */
public class MyCRUD {    
   
    public static void main(String[] args){
        //MyCRUD crud = new MyCRUD();
        //crud.createCredential();
        
        User user = createUser();
        DBObject obj = createDBObject(user);
         
        MongoClient mongo = new MongoClient("localhost", 27017);
        DB db = mongo.getDB("journaldev");
         
        DBCollection col = db.getCollection("users");
         
        //create user
        WriteResult result = col.insert(obj);
        System.out.println(result.getUpsertedId());
        System.out.println(result.getN());
        System.out.println(result.isUpdateOfExisting());
        //System.out.println(result.getLastConcern());
         
        //read example
        DBObject queryObj = BasicDBObjectBuilder.start().add("_id", user.getId()).get();
        //Utility for building complex objects.
        //Example: BasicDBObjectBuilder.start().add( "name" , "eliot").add("number" , 17).get()
        
        DBCursor cursor = col.find(queryObj);
        while(cursor.hasNext()){
            System.out.println(cursor.next());
        }
         
        //update example
        user.setName("UserABC");
        obj = createDBObject(user);
        result = col.update(queryObj, obj);
        System.out.println(result.getUpsertedId());
        System.out.println(result.getN());
        System.out.println(result.isUpdateOfExisting());
        //System.out.println(result.getLastConcern());
         
        //delete example
        result = col.remove(queryObj);
        System.out.println(result.getUpsertedId());
        System.out.println(result.getN());
        System.out.println(result.isUpdateOfExisting());
        //System.out.println(result.getLastConcern());
         
        //close resources
        mongo.close();
    }
    
    private void createCredential(){
        MongoCredential auth = MongoCredential.createPlainCredential("userA", "journaldev", "654321".toCharArray());
        MongoCredential testAuth = MongoCredential.createPlainCredential("test", "test", "test".toCharArray());
        List<MongoCredential> auths = new ArrayList();
        auths.add(auth);
        auths.add(testAuth);
    }
    
    private static DBObject createDBObject(User user) {
        BasicDBObjectBuilder docBuilder = BasicDBObjectBuilder.start();
                                 
        docBuilder.append("_id", user.getId());
        docBuilder.append("name", user.getName());
        docBuilder.append("role", user.getRole());
        docBuilder.append("isEmployee", user.isEmployee());
        return docBuilder.get();
    }
 
    private static User createUser() {
        User u = new User();
        u.setId(2);
        u.setName("User123");
        u.setEmployee(true);
        u.setRole("CEO");
        return u;
    }
}
