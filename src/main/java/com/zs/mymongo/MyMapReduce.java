/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zs.mymongo;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MapReduceCommand;
import com.mongodb.MapReduceOutput;
import com.mongodb.Mongo;
/**
 *
 * from http://www.javacodegeeks.com/2012/06/mapreduce-with-mongodb.html
 */
public class MyMapReduce {
    
    public static void main(String[] args){
        Mongo mongo;
        try{
            mongo = new Mongo("localhost", 27017);
            DB db = mongo.getDB("library");
            
            DBCollection books = db.getCollection("books");
            
            BasicDBObject book = new BasicDBObject();
            book.put("name", "Understanding JAVA");
            book.put("pages", 100);
            books.insert(book);
            
            book = new BasicDBObject();
            book.put("name", "Understanding JSON");
            book.put("pages", 200);
            books.insert(book);
            
            book = new BasicDBObject();
            book.put("name", "Understanding XML");
            book.put("pages", 300);
            books.insert(book);
            
            book = new BasicDBObject();
            book.put("name", "Understanding Web Services");
            book.put("pages", 400);
            books.insert(book);
            
            String map = "function() {" +

                            "  var category;" +

                            "  if ( this.pages >= 250 )" +

                            "  category = 'Big Books';" +

                            "  else" +

                            "  category = \"Small Books\";" +

                            "  emit(category, {name: this.name});" +

                            "   };";
            
            String reduce = "function(key, values) {" +

                            "	var sum = 0;" +

                            "	values.forEach(function(doc) {" +

                            "	sum += 1;" +

                            "	});" +

                            "	return {books: sum};" +

                            "	};";
            
            MapReduceCommand cmd = new MapReduceCommand(books, map, reduce, null, MapReduceCommand.OutputType.INLINE, null);
            MapReduceOutput out = books.mapReduce(cmd);
            for(DBObject o : out.results()){
                System.out.println(o.toString());
            }
            
        }catch(Exception e){
            
        }
    }
}
