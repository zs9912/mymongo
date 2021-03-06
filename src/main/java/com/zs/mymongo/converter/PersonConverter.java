/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zs.mymongo.converter;

import org.bson.types.ObjectId;
 
import com.zs.mymongo.model.Person;

import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DBObject;
/**
 *converting Person object to MongoDB DBObject and vice versa
 */
public class PersonConverter {
    // convert Person Object to MongoDB DBObject
    // take special note of converting id String to ObjectId
    public static DBObject toDBObject(Person p) {
 
        BasicDBObjectBuilder builder = BasicDBObjectBuilder.start()
                .append("name", p.getName()).append("country", p.getCountry());
        if (p.getId() != null)
            builder = builder.append("_id", new ObjectId(p.getId()));
        return builder.get();
    }
 
    // convert DBObject Object to Person
    // take special note of converting ObjectId to String
    public static Person toPerson(DBObject doc) {
        Person p = new Person();
        p.setName((String) doc.get("name"));
        p.setCountry((String) doc.get("country"));
        ObjectId id = (ObjectId) doc.get("_id");
        p.setId(id.toString());
        return p;
 
    }
}
