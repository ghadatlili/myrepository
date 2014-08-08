package com.ooredoo.saleswebservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import  java.sql.*; 
import java.util.Properties;


@Path("/ServicesPDT")
public class ServicesPDT {
     private String categ []= new String [20];      private int  numCli []= new int [20];
	private int  codes[] = new int [20];             private int  nbCli []= new int [20];
	private float  prix[] = new float [20]; 	private float  ca[] = new float [20];
	private String  region[] = new String [20]; private String libelle[] = new String [20];
	private String  adresse[] = new String [20];


	@GET
     @Path("/Categ/a1/{v_a1}/m1/{v_m1}/j1/{v_j1}/a2/{v_a2}/m2/{v_m2}/j2/{v_j2}")
      @Produces(MediaType.TEXT_XML)
      public String Categ(@PathParam("v_a1") int v_a1 , @PathParam("v_m1") int v_m1,@PathParam("v_j1") int v_j1 , @PathParam("v_a2") int v_a2,@PathParam("v_m2") int v_m2 , @PathParam("v_j2") int v_j2) {
    	  String rst; ResultSet res = null;int i = 0;boolean exit=false;
    	 try {
  		   Statement st= connexionBD().createStatement();
res=st.executeQuery("select distinct(c.libelle) from categorie c ,vente v,produit p where c.code=p.cat_cod and p.code=v.pro_code and date_vente between STR_TO_DATE('"+v_a1+","+v_m1+","+v_j1+"','%Y,%m,%d') and STR_TO_DATE('"+v_a2+","+v_m2+","+v_j2+"','%Y,%m,%d')");

  		   while(res.next())
  		   {  			 
  			   System.out.println(" \nla categ:\n"+res.getString(1));

  		    categ[i]=res.getString(1);
  			i++;
  		  }
  		
  		  } catch (SQLException e) {
  		   e.printStackTrace();
  		  }      
        rst =  "<Service1>"
        + "<a1>" + v_a1 + "</a1>"  + "<m1>" + v_m1 + "</m1>" + "<j1>" + v_j1 + "</j1>" + "<a2>" + v_a2 + "</a2>" + "<m2>" + v_m2 + "</m2>" + "<j2>" + v_j2 + "</j2>";
        for (int j =0 ; j<categ.length; j++) {
        	if(categ[j] != null)
            	{rst+="<unite>";

          rst += "<categ>" + categ[j] + "</categ>";
      	rst+="</unite>";
            	}
           }
         rst += "</Service1>";
         
         return rst ;
      }

	@GET
    @Path("/Ptvente/{gouv}/a1/{v_a1}/m1/{v_m1}/j1/{v_j1}/a2/{v_a2}/m2/{v_m2}/j2/{v_j2}")
     @Produces(MediaType.TEXT_XML)
     public String Ptvente(@PathParam("gouv") String gouv , @PathParam("v_a1") int v_a1 , @PathParam("v_m1") int v_m1,@PathParam("v_j1") int v_j1 , @PathParam("v_a2") int v_a2,@PathParam("v_m2") int v_m2 , @PathParam("v_j2") int v_j2) {
   	  String rst; ResultSet res = null;int i = 0;
   	 try {
 		   Statement st= connexionBD().createStatement();
res=st.executeQuery("select distinct(p.adresse) from pointvente p , vente v where p.code=v.poi_code and p.gouvernorat = '"+gouv+"' and date_vente between STR_TO_DATE('"+v_a1+","+v_m1+","+v_j1+"','%Y,%m,%d') and STR_TO_DATE('"+v_a2+","+v_m2+","+v_j2+"','%Y,%m,%d')");

 		   while(res.next())
 		   {  			 
 			   System.out.println(" \nptvente:\n"+res.getString(1));

 		    adresse[i]=res.getString(1);
 			i++;
 		  }
 		
 		  } catch (SQLException e) {
 		   e.printStackTrace();
 		  }      
   	 
   	        
       rst =  "<Service1>"
   + "<gouv>" + gouv + "</gouv>" + "<a1>" + v_a1 + "</a1>"  + "<m1>" + v_m1 + "</m1>" + "<j1>" + v_j1 + "</j1>" + "<a2>" + v_a2 + "</a2>" + "<m2>" + v_m2 + "</m2>" + "<j2>" + v_j2 + "</j2>";
       for (int j =0 ; j<adresse.length; j++) {
       	if(adresse[j] != null)
           	{rst+="<unite>";

         rst += "<adresse>" + adresse[j] + "</adresse>";
     	rst+="</unite>";
           	}
          }
        rst += "</Service1>";
        
        return rst ;
     }

	
	
	@GET
    @Path("/Pdt/cat/{cat}")
     @Produces(MediaType.TEXT_XML)
     public String Pdt(@PathParam("cat") String cat) {
   	  String rst; ResultSet res = null;int i = 0;boolean exit=false;
   	 try {
 		   Statement st= connexionBD().createStatement();
res=st.executeQuery("select p.libelle from categorie c,produit p where c.code=p.cat_cod and c.libelle = '"+cat+"'");

 		   while(res.next())
 		   {  			 
 			   System.out.println(" \nles lib:\n"+res.getString(1));

 		    libelle[i]=res.getString(1);
 			i++;
 		  }
 		
 		  } catch (SQLException e) {
 		   e.printStackTrace();
 		  }      
       rst =  "<Service2>"
       + "<cat>" + cat + "</cat>";
       for (int j =0 ; j<libelle.length; j++) {
       	if(libelle[j] != null)
       	{
    	   rst+= "<unite>";
         rst += "<libelle>" + libelle[j] + "</libelle>";
         rst+= "</unite>";
       	}
          }
        rst += "</Service2>";
        
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