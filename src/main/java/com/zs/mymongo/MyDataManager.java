/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zs.mymongo;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.result.UpdateResult;
import com.mongodb.client.result.DeleteResult;
import com.mongodb.client.AggregateIterable;

import org.bson.Document;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Locale;

import static java.util.Arrays.asList;
import static com.mongodb.client.model.Filters.*;

/**
 *
 * @author szhang
 * example from https://docs.mongodb.org/getting-started/java/query/
 * 
 * use command line(do not use mongo.exe)
 * cd C:\path\to\mongodb\bin
 * mongoimport --db test --collection restaurants --drop --file C:\path\to\primer-dataset.json
 */
public class MyDataManager {    
    private MongoClient mongoClient = null;
    private MongoDatabase db = null;
    
    MyDataManager(){
        mongoClient = new MongoClient();
        db = mongoClient.getDatabase("test");     
    }
    
    public static void main(String[] args){
        MyDataManager dm = new MyDataManager();
        
        //dm.createDocument();
        
        //dm.getAllDocuments();
        //dm.getDocuments();
        //dm.getDocumentsGreaterThan();
        //dm.getDocumentsLessThan();
        //dm.getDocumentsAnd();
        //dm.getDocumentsOr();
        //dm.getDocumentsSorted();
        
        //dm.updateDocument();
        //dm.updateMultipleDocuments();        
        //dm.replaceDocument();
        
        //dm.deleteDocuments();        
        //dm.deleteAll();
        //dm.dropCollection();
    }   
    
    
    public void createDocument(){
        try{
            DateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", Locale.ENGLISH);
            db.getCollection("restaurants").insertOne(
                new Document("address",
                            new Document()
                                    .append("street", "2 Avenue")
                                    .append("zipcode", "10075")
                                    .append("building", "1480")
                                    .append("coord", asList(-73.9557413, 40.7720266)))
                            .append("borough", "Manhattan")
                            .append("cuisine", "Italian")
                            .append("grades", asList(
                                    new Document()
                                            .append("date", format.parse("2014-10-01T00:00:00Z"))
                                            .append("grade", "A")
                                            .append("score", 11),
                                    new Document()
                                            .append("date", format.parse("2014-01-16T00:00:00Z"))
                                            .append("grade", "B")
                                            .append("score", 17)))
                            .append("name", "Vella")
                            .append("restaurant_id", "41704620")
            );
        }catch(ParseException e){
            
        }
    }
    
    public FindIterable<Document> getAllDocuments(){
        FindIterable<Document> iterable = db.getCollection("restaurants").find();
        iterable.forEach(new Block<Document>() {
            @Override
            public void apply(final Document document) {
                System.out.println(document);
            }
        });
        return iterable;
    }
    
    public FindIterable<Document> getDocuments(){
        //FindIterable<Document> iterable = db.getCollection("restaurants").find(new Document("borough", "Manhattan"));
        //FindIterable<Document> iterable = db.getCollection("restaurants").find(eq("borough","Manhattan"));
        //FindIterable<Document> iterable = db.getCollection("restaurants").find(new Document("address.zipcode","10075"));
        FindIterable<Document> iterable = db.getCollection("restaurants").find(new Document("cuisine","Category To Be Determined"));
        
        iterable.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document){
                System.out.println(document);
            }
        });
        return iterable;
    }
    
    public FindIterable<Document> getDocumentsGreaterThan(){
        FindIterable<Document> iterable 
        = db.getCollection("restaurants").find(new Document("grades.score", new Document("$gt", 60)));
        
        //db.getCollection("restaurants").find(gt("grades.score", 60));
        
        iterable.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document){
                System.out.println(document);
            }
        });
        return iterable;
    }
    
    public FindIterable<Document> getDocumentsLessThan(){
        FindIterable<Document> iterable = db.getCollection("restaurants").find(lt("grades.score", 10));
        iterable.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document){
                System.out.println(document);
            }
        });
        return iterable;
    }
    
    public FindIterable<Document> getDocumentsAnd(){
        FindIterable<Document> iterable 
            = db.getCollection("restaurants").find(new Document("cuisine", "Italian").append("address.zipcode", "10075"));
        //db.getCollection("restaurants").find(and(eq("cuisine", "Italian"), eq("address.zipcode", "10075")));
        iterable.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document){
                System.out.println(document);
            }
        });
        return iterable;
    }
    
    public FindIterable<Document> getDocumentsOr(){
        FindIterable<Document> iterable 
            = db.getCollection("restaurants").find(new Document("$or", asList(new Document("cuisine","Italian"), new Document("address.zipcode", "10075"))));
        //db.getCollection("restaurants").find(or(eq("cruisin","Italian"), eq("address.zipcode", "10075")));
        
        iterable.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document){
                System.out.println(document);
            }
        });
        return iterable;
    }
    
    public FindIterable<Document> getDocumentsSorted(){
        FindIterable<Document> iterable = db.getCollection("restaurants").find().sort(new Document("borough",1).append("address.zipcode", 1));
        //db.getCollection("restaurants").find().sort(ascending("borough","address.zipcode"));
        iterable.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document){
                System.out.println(document);
            }
        });
        return iterable;
    }
    
    /*
    The following operation updates the first document with name equal to "Juni", 
    using the $set operator to update the cuisine field and the $currentDate operator to 
    update the lastModified field with the current date.
    */
    public void updateDocument(){
        UpdateResult result = db.getCollection("restaurants")
        .updateOne(new Document("name", "Juni"), new Document("$set", new Document("cuisine", "American (New)"))
                                                     .append("$currentDate", new Document("LastModified", true)));
        long count = result.getModifiedCount();
        System.out.println("affected "+ count + " row(s).");
    }
    
    /*
    The following operation updates all documents that have address.zipcode field equal to "10016", 
    setting the cuisine field to "Category To Be Determined" and the lastModified field to the current date.
    */
    public void updateMultipleDocuments(){
        UpdateResult result 
        = db.getCollection("restaurants").updateMany(new Document("address.zipcode", "10016").append("cuisine", "Other"), 
          new Document("$set", 
                            new Document("cuisine", "Category To Be Determined"))
                                        .append("$currentDate", new Document("lastModified", true)));
        long count = result.getModifiedCount();
        System.out.println("affected "+count+" row(s).");
    }
    
    public void replaceDocument(){
        UpdateResult result = db.getCollection("restaurants").replaceOne(new Document("restaurant_id", "41704620"),
        new Document("address",
                new Document()
                        .append("street", "2 Avenue")
                        .append("zipcode", "10075")
                        .append("building", "1480")
                        .append("coord", asList(-73.9557413, 40.7720266)))
                .append("name", "Vella 2"));
        
        long count = result.getModifiedCount();
        System.out.println("updated "+count+ " row(s).");
    }
    
    public void deleteDocuments(){
        DeleteResult result = db.getCollection("restaurants").deleteMany(new Document("borough", "Manhattan"));
        long count = result.getDeletedCount();
        System.out.println("deleted "+count+" row(s).");
    }
    
    public void deleteAll(){
        db.getCollection("restaurants").deleteMany(new Document());
    }
    
    public void dropCollection(){
        db.getCollection("restaurants").drop();
    }
    
    public AggregateIterable<Document> getDocumentsGroupBy(){
        AggregateIterable<Document> iterable = db.getCollection("restaurants").aggregate(asList(new Document("$group", 
        new Document("_id","$borough").append("count", new Document("$sum",1)))));
        iterable.forEach(new Block<Document>(){
            @Override
            public void apply(final Document document){
                System.out.println(document.toJson());
            }
        });
        return iterable;
    } 
}