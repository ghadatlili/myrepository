/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.ooredoo.saleswebservices;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import  java.sql.*; 
import java.util.Properties;


@Path("/ServicesConnexion")
public class ServicesConnexion {


	@GET
	 @Path("/{login}/{password}")
    @Produces(MediaType.TEXT_XML)
    public String Categ(@PathParam("login") String login , @PathParam("password") String password) {
   	  String rst; ResultSet res = null;String fonction = null;
    	 try {
  		   Statement st= connexionBD().createStatement();
  	
  		   res=st.executeQuery("select fonction from user where login='"+login+"' and password='"+password+"'");
  		
  		  while(res.next())
  		   {
  			   System.out.println(" \nla fonction:\n"+res.getString(1));
  			   fonction=res.getString(1);
  		   }
  		
  		  } catch (SQLException e) {
  		   e.printStackTrace();
  		  }      
        rst =  "<Service1>"
        		+ "<login>" + login + "</login>"  + "<password>" + password + "</password>";
        	rst+="<unite>";
          rst += "<fonction>" + fonction+ "</fonction>";
           rst+="</unite>";
          
         rst += "</Service1>";
         
         return rst ;
      }
 


      public static Connection connexionBD( )
 	 {

 		Connection  conn = null ;
 	  try {
 	   Class.forName("com.mysql.jdbc.Driver");
 	   //URL of Oracle database server
 	         String url = "jdbc:mysql://localhost:3306/test";

 	         //properties for creating connection to Oracle database
 	         Properties props = new Properties();
 	         props.setProperty("user", "root");
 	         props.setProperty("password", "");

 	         //creating connection to Oracle database using JDBC
 	          conn = DriverManager.getConnection(url,props);

 	  } catch (Exception e)
 	     {

 	   System.out.println("errrrooooorrrrrr     "+e);
 	   e.printStackTrace();

 	    }

 	 return conn;
 	 }
}
