package com.ooredoo.saleswebservices;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import  java.sql.*; 
import java.util.Properties;


@Path("/ServicesCA")
public class ServicesCA {
     private int  qte_vendues []= new int [20];      private int  numCli []= new int [20];
	private int  codes[] = new int [20];             private int  nbCli []= new int [20];
	private float  prix[] = new float [20]; 	private float  ca[] = new float [20];
	private String  region[] = new String [20]; private String  libelle[] = new String [20];
	private String  adresse[] = new String [20];


	@GET
	 @Path("/Pdt/a1/{v_a1}/m1/{v_m1}/j1/{v_j1}/a2/{v_a2}/m2/{v_m2}/j2/{v_j2}")
    @Produces(MediaType.TEXT_XML)
    public String Categ(@PathParam("v_a1") int v_a1 , @PathParam("v_m1") int v_m1,@PathParam("v_j1") int v_j1 , @PathParam("v_a2") int v_a2,@PathParam("v_m2") int v_m2 , @PathParam("v_j2") int v_j2) {
   	  String rst; ResultSet res = null;int i = 0;boolean exit=false;
    	 try {
  		   Statement st= connexionBD().createStatement();
  	
  		   res=st.executeQuery("select sum(nb_articles),produit.libelle,prix from vente,produit where produit.code=vente.pro_code and date_vente between STR_TO_DATE('"+v_a1+","+v_m1+","+v_j1+"','%Y,%m,%d') and STR_TO_DATE('"+v_a2+","+v_m2+","+v_j2+"','%Y,%m,%d') group by (libelle)");
  		
  		  while(res.next())
  		   {
  			 
  			   System.out.println(" \nle libelle:\n"+res.getString(2));
  			   System.out.println(" \nla qtevendue:\n"+res.getInt(1));

  		    ca[i]=res.getInt(1)*res.getFloat(3)/1000; libelle[i]=res.getString(2); prix[i]=res.getFloat(3);
  			i++;
  		  }
  		
  		int p,j,max; float tmp;
		for(p=0;p<ca.length-1;p++)
		{
			max=p;
			for(j=p+1;j<ca.length;j++)	
				if(ca[j]>ca[max])
					max=j;
				
			if(max!=p)
			{
				tmp=ca[p];
				ca[p]=ca[max];
				ca[max]=tmp;
			}
		}
  		  } catch (SQLException e) {
  		   e.printStackTrace();
  		  }      
        rst =  "<Service1>"
        		+ "<a1>" + v_a1 + "</a1>"  + "<m1>" + v_m1 + "</m1>" + "<j1>" + v_j1 + "</j1>" + "<a2>" + v_a2 + "</a2>" + "<m2>" + v_m2 + "</m2>" + "<j2>" + v_j2 + "</j2>";
                for (int j =0 ; j < 5 ; j++) {
        	rst+="<unite>";
          rst += "<libelle>" + libelle[j] + "</libelle>"
             	+ "<ca>" + ca[j]+ "</ca>";//+ "<qte_vendue>" + qte_vendues[j] + "</qte_vendue>";
           rst+="</unite>";
           }
         rst += "</Service1>";
         
         return rst ;
      }
 /*****************************  *********************************************/
	@GET
	 @Path("/Pdt/{cat}/{gouv}/{adresse}/a1/{v_a1}/m1/{v_m1}/j1/{v_j1}/a2/{v_a2}/m2/{v_m2}/j2/{v_j2}")
   @Produces(MediaType.TEXT_XML)
   public String pdt_cate(@PathParam("cat") String cat,@PathParam("gouv") String gouv,@PathParam("adresse") String adresse,@PathParam("v_a1") int v_a1 , @PathParam("v_m1") int v_m1,@PathParam("v_j1") int v_j1 , @PathParam("v_a2") int v_a2,@PathParam("v_m2") int v_m2 , @PathParam("v_j2") int v_j2) {
  	  String rst; ResultSet res = null;int i = 0;boolean exit=false;
   	 try {
 		   Statement st= connexionBD().createStatement();
 // res=st.executeQuery("select sum(nb_articles),p.libelle,prix from vente v,produit p,categorie c,pointvente pv where pv.gouvernorat=IFNULL('"+gouv+"',pv.gouvernorat) and c.libelle = IFNULL('"+cat+"',c.libelle) and pv.adresse=IFNULL('"+adresse+"',pv.adresse) and v.poi_code=pv.code and c.code=p.cat_cod  and p.code=v.pro_code and date_vente between STR_TO_DATE('"+v_a1+","+v_m1+","+v_j1+"','%Y,%m,%d') and STR_TO_DATE('"+v_a2+","+v_m2+","+v_j2+"','%Y,%m,%d') group by (libelle)");
  res=st.executeQuery("select sum(nb_articles),p.libelle,prix from vente v,produit p,categorie c,pointvente pv where v.poi_code=pv.code and c.code=p.cat_cod  and p.code=v.pro_code and  pv.gouvernorat= IFNULL("+gouv+",pv.gouvernorat) and c.libelle = IFNULL("+cat+",c.libelle) and pv.adresse=IFNULL("+adresse+",pv.adresse) and date_vente between STR_TO_DATE('"+v_a1+","+v_m1+","+v_j1+"','%Y,%m,%d') and STR_TO_DATE('"+v_a2+","+v_m2+","+v_j2+"','%Y,%m,%d') group by (libelle)");

 		  while(res.next())//&&(exit==false)) 
 		   {			 
 			   System.out.println(" \nla qtevendue:\n"+res.getInt(1));

 		    qte_vendues[i]=res.getInt(1);  prix[i]=res.getFloat(3);libelle[i]=res.getString(2);
 			i++;
 				//if(!res.next()) exit = true ;
 		  }
 		 System.out.print("i"+i);
 		  } catch (SQLException e) {
 		   // TODO Auto-generated catch block
 		   e.printStackTrace();
 		  }
   
     
       rst =  "<Service1>"
       + "<categorie>" + cat + "</categorie>" + "<gouvernorat>"+gouv+"</gouvernorat>"+"<adresse>"+adresse+"</adresse>";
       for (int j =0 ; j < i ; j++) {
    	   rst+="<unite>";
         rst += "<libelle>" + libelle[j] + "</libelle>"
            	+ "<ca>" + prix[j]*qte_vendues[j] + "</ca>";//+ "<qte_vendue>" + qte_vendues[j] + "</qte_vendue>";
rst+="</unite>";
          }
        rst += "</Service1>";
        
        return rst ;
     }
	
	@GET
    @Path("/CA_Par_Categorie_Temps/{cat}/{temps}/{valeur}")
     @Produces(MediaType.TEXT_XML)
     public String CA_Par_Categorie_Temps(@PathParam("cat") String cat,@PathParam("temps") String temps,@PathParam("valeur") String valeur) {
   	  String rst; ResultSet res = null;int i = 0;boolean exit=false;
   	 try {
 		   Statement st= connexionBD().createStatement();
 		   
 		  if(temps.equals("annee"))
 	  		   res=st.executeQuery("select sum(nb_articles),p.code,prix from vente v,produit p,categorie c where c.code=p.cat_cod and c.libelle='"+cat+"' and p.code=v.code and substring(date_vente,1,4)="+valeur+" group by (code)");
 	  		 if(temps.equals("jour"))
 	    		   res=st.executeQuery("select sum(nb_articles),p.code,prix from vente v,produit p ,categorie c where c.code=p.cat_cod and c.libelle='"+cat+"' and p.code=v.code and substring(date_vente,8,9)="+valeur+" group by (code)");
 	  		if(temps.equals("mois"))
 	 		   res=st.executeQuery("select sum(nb_articles),p.code,prix from vente v,produit p,categorie c where c.code=p.cat_cod and c.libelle='"+cat+"' and p.code=v.code and substring(date_vente,6,7)="+valeur+" group by (code)");
 	 		
		  while(res.next())//&&(exit==false))
 		   {
 			 
 			   System.out.println(" \nle code:\n"+res.getInt(2));
 			   System.out.println(" \nla qtevendue:\n"+res.getInt(1));

 		    qte_vendues[i]=res.getInt(1); codes[i]=res.getInt(2); prix[i]=res.getFloat(3);
 			i++;
 				//if(!res.next()) exit = true ;
 		  }
 		 
 		  } catch (SQLException e) {
 		   // TODO Auto-generated catch block
 		   e.printStackTrace();
 		  }
   
     
       rst =  "<Service3>"
       + "<temps>" + temps + "</temps>" 
       + "<categorie>" + cat+ "</categorie>" ;

       for (int j =0 ; j < i ; j++) {
         rst += "<code>" + codes[j] + "</code>"
            	+ "<ca>" + prix[j]*qte_vendues[j] + "</ca>";//+ "<qte_vendue>" + qte_vendues[j] + "</qte_vendue>";

          }
        rst += "</Service3>";
        
        return rst ;     }

	
	@GET
    @Path("/CA_Par_Region_Pdt_Temps/{pdt}/{temps}/{valeur}")
     @Produces(MediaType.TEXT_XML)
     public String CA_Par_Region_Pdt_Temps(@PathParam("pdt") String pdt,@PathParam("temps") String temps,@PathParam("valeur") String valeur) {
   	  String rst; ResultSet res = null;int i = 0;boolean exit=false;
   	 try {
 		   Statement st= connexionBD().createStatement();
 		   
 		  if(temps.equals("annee"))
 	  		   res=st.executeQuery("select sum(nb_articles),region,prix from produit p,vente v,pointvente pv where p.code=v.code and p.code_pv=pv.code and p.libelle='"+pdt+"' and substring(date_vente,1,4)="+valeur+" group by (region)");
 	  		 if(temps.equals("jour"))
 	    		   res=st.executeQuery("select sum(nb_articles),region,prix from produit p ,vente v,pointvente pv where p.code=v.code and p.code_pv=pv.code and p.libelle='"+pdt+"' and substring(date_vente,8,9)="+valeur+" group by (region)");
 	  		if(temps.equals("mois"))
 	 		   res=st.executeQuery("select sum(nb_articles),region,prix from produit p,vente v,pointvente pv where p.code=v.code and p.code_pv=pv.code and p.libelle='"+pdt+"' and substring(date_vente,6,7)="+valeur+" group by (region)");
 	 		
		  while(res.next())//&&(exit==false))
 		   {
 			 
 			   System.out.println(" \nla qtevendue:\n"+res.getInt(1));

 		    qte_vendues[i]=res.getInt(1); prix[i]=res.getFloat(3);region[i]=res.getString(2);
 			i++;
 				//if(!res.next()) exit = true ;
 		  }
 		 
 		  } catch (SQLException e) {
 		   // TODO Auto-generated catch block
 		   e.printStackTrace();
 		  }
   
     
       rst =  "<Service4>"
       + "<temps>" + valeur + "</temps>" 
       + "<produit>" + pdt+ "</produit>" ;

       for (int j =0 ; j < i ; j++) {
         rst += "<region>" + region[j] + "</region>"
            	+ "<ca>" + prix[j]*qte_vendues[j] + "</ca>";//+ "<qte_vendue>" + qte_vendues[j] + "</qte_vendue>";

          }
        rst += "</Service4>";
        
        return rst ;
     }
	
	@GET
    @Path("/CA_Par_Client/{temps}/{valeur}")
     @Produces(MediaType.TEXT_XML)
     public String CA_Par_Client(@PathParam("temps") String temps,@PathParam("valeur") String valeur) {
   	  String rst; ResultSet res = null;int i = 0;
   	 try {
 		   Statement st= connexionBD().createStatement();
 		   if(temps.equals("annee"))
 		   res=st.executeQuery("select sum(nb_articles),c.msisdn,prix from vente v,produit p,client c where c.msisdn=v.msisdn and p.code=v.code and substring(date_vente,1,4)="+valeur+" group by (msisdn)");
		
 		  if(temps.equals("mois"))
 	 		   res=st.executeQuery("select sum(nb_articles),c.msisdn,prix from vente v,produit p,client c where c.msisdn=v.msisdn and p.code=v.code and substring(date_vente,6,7)="+valeur+" group by (msisdn)");
 			
 		 if(temps.equals("jour"))
 	 		   res=st.executeQuery("select sum(nb_articles),c.msisdn,prix from vente v,produit p,client c where c.msisdn=v.msisdn and p.code=v.code and substring(date_vente,9,10)="+valeur+" group by (msisdn)");
 			
 		  while(res.next())
 		   { 			 
 			   System.out.println(" \nmsisdn:\n"+res.getInt(2));
 			   System.out.println(" \nla qtevendue:\n"+res.getInt(1));

 		    qte_vendues[i]=res.getInt(1); numCli[i]=res.getInt(2); prix[i]=res.getFloat(3);
 			i++;
 				//if(!res.next()) exit = true ;
 		  }
 		 
 		  } catch (SQLException e) {
 		   // TODO Auto-generated catch block
 		   e.printStackTrace();
 		  }
   
     
       rst =  "<Service5>"
       + "<temps>" + valeur + "</temps>" ;
       for (int j =0 ; j < i ; j++) {
         rst += "<numcli>" + numCli[j] + "</numcli>"
            	+ "<ca>" + prix[j]*qte_vendues[j] + "</ca>";//+ "<qte_vendue>" + qte_vendues[j] + "</qte_vendue>";

          }
        rst += "</Service5>";
        
        return rst ;
     }
	
	@GET
    @Path("/PointVenteLesMoinsFrequentes/{region}")
     @Produces(MediaType.TEXT_XML)
     public String PointVenteLesMoinsFrequentes(@PathParam("region") String temps,@PathParam("valeur") String valeur) {
   	  String rst; ResultSet res = null;int i = 0;
   	 try {
 		   Statement st= connexionBD().createStatement();
 		   res=st.executeQuery("select count(msisdn) as nb_cli,adresse from pointvente p , vente v where p.code=v.pro_code and region = '"+region+"' and substring(date_vente,1,4) between substring(sysdate(),1,4)and ( substring(sysdate(),1,4) - 3 ) group by(adresse)");
 		 	
 		  while(res.next())
 		   { 			 
 			   System.out.println(" \nadresse:\n"+res.getString(2));
 		    nbCli[i]=res.getInt(1); adresse[i]=res.getString(2);
 			i++;
 		  }
 		 
 		  } catch (SQLException e) {
 		   // TODO Auto-generated catch block
 		   e.printStackTrace();
 		  }
   
     
       rst =  "<Service6>"
       + "<temps>" + valeur + "</temps>" ;
       for (int j =0 ; j < i ; j++) {
         rst += "<adresse>" + adresse[j] + "</adresse>"
            	+ "<nbCli>" + nbCli[j] + "</nbCli>";
          }
        rst += "</Service6>";
        
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
