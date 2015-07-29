/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.zs.mymongo.servlet;

import java.io.IOException;
import java.util.List;
 
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.mongodb.MongoClient;

import com.zs.mymongo.dao.MongoDBPersonDAO;
import com.zs.mymongo.model.Person;

/*
The serialization runtime associates with each serializable class a version number, called a serialVersionUID, 
which is used during deserialization to verify that the sender and receiver of a serialized object have loaded 
classes for that object that are compatible with respect to serialization. 
If the receiver has loaded a class for the object that has a different serialVersionUID than that
of the corresponding sender's class, then deserialization will result in an InvalidClassException. 

A serializable class can declare its own serialVersionUID explicitly by declaring a field named "serialVersionUID" 
that must be static, final, and of type long.

If a serializable class does not explicitly declare a serialVersionUID, then the serialization runtime will
calculate a default serialVersionUID value for that class based on various aspects of the class, 
as described in the Java(TM) Object Serialization Specification.
*/

@WebServlet("/addPerson")
public class AddPersonServlet extends HttpServlet{
    private static final long serialVersionUID = -7060758261496829905L;
 
    protected void doPost(HttpServletRequest request,
            HttpServletResponse response) throws ServletException, IOException {
        
        String name = request.getParameter("name");
        String country = request.getParameter("country");
        
        if ((name == null || name.equals(""))
            || (country == null || country.equals(""))) {
            request.setAttribute("error", "Mandatory Parameters Missing");
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/persons.jsp");
            rd.forward(request, response);
        } else {
            Person p = new Person();
            p.setCountry(country);
            p.setName(name);
            
            MongoClient mongo = (MongoClient) request.getServletContext().getAttribute("MONGO_CLIENT");
            
            MongoDBPersonDAO personDAO = new MongoDBPersonDAO(mongo);
            personDAO.createPerson(p);            
            System.out.println("Person Added Successfully with id="+p.getId());
            
            request.setAttribute("success", "Person Added Successfully");
            List<Person> persons = personDAO.readAllPerson();
            request.setAttribute("persons", persons);
 
            RequestDispatcher rd = getServletContext().getRequestDispatcher("/persons.jsp");
            rd.forward(request, response);
        }
        
    }
}
