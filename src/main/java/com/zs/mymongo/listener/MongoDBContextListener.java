/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zs.mymongo.listener;

import java.net.UnknownHostException;
 
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
 
import com.mongodb.MongoClient;
/**
 * MongoClient is thread safe and internally manages it’s own connection pool. 
 * Best practice is to create an instance of it and reuse it.
 */
@WebListener
public class MongoDBContextListener implements ServletContextListener{
    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        MongoClient mongo = (MongoClient) sce.getServletContext()
                            .getAttribute("MONGO_CLIENT");
        mongo.close();
        System.out.println("MongoClient closed successfully");
    }
 
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        try {
            ServletContext ctx = sce.getServletContext();
            MongoClient mongo = new MongoClient(
                    ctx.getInitParameter("MONGODB_HOST"), 
                    Integer.parseInt(ctx.getInitParameter("MONGODB_PORT")));
            System.out.println("MongoClient initialized successfully");
            sce.getServletContext().setAttribute("MONGO_CLIENT", mongo);
        } catch (Exception e) {//UnknownHostException is wrong
            throw new RuntimeException("MongoClient init failed");
        }
    }
 
}
