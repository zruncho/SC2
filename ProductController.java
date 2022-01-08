package com.cryptophonecall.controller;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.Map;
import java.util.Arrays;

import jakarta.servlet.RequestDispatcher;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.cryptophonecall.dao.ProductDAO;
import com.cryptophonecall.model.Product;

public class ProductController extends HttpServlet {
   private static final long serialVersionUID = 1L;
   private ProductDAO dao;
   
   
   public ProductController() {
       super();
       dao = new ProductDAO();
       
   }

  
   protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       Product prod = new Product();
       
       //List<String> ml = new ArrayList<>(Arrays.asList("uuid","name","price","off","pic","prom","shopid","type","flag"));
       
       try {
    	   Enumeration enParams = request.getParameterNames();
    	   while( enParams.hasMoreElements()) {    
    		  String x = enParams.nextElement().toString();
    		  //System.out.println("x8");
    		  //System.out.println(x);
    		  //System.out.println(request.getParameter(x));
    		  if(x.equals("action"))continue;
    		  
    		   Field mf = null;
    		   mf =  prod.getClass().getDeclaredField(x);
    		   mf.setAccessible(true);
    		   
    		   switch (x) {
    		   	
    		   case ("flag") : {
    			   	mf.set(prod, Integer.parseInt(request.getParameter(x)));
    			   	break;
    		    }
    		   
    		   case ("price") :{
    			   mf.set(prod,Double.parseDouble(request.getParameter(x)));
    		       break;
    		   }
    		
    		   case ("per") :{
    			   mf.set(prod,Integer.parseInt(request.getParameter(x)));
    		       break;
    		   }
    		
    		   
    		   
    		   default: { 
    			   mf.set(prod, request.getParameter(x));
    		   }
    		   }
    	  }
       }catch(Exception e) {e.printStackTrace();}
       
       
       	
       
       String action = request.getParameter("action");
       JSONObject json =new JSONObject();
       
       		
       		if (action.equalsIgnoreCase("delete")){
           
           		if(dao.delProd(prod.getUuid())){
           			json.put("info", "success");
                 } else {
            	   json.put("info", "failed");
                 }
           }
       	   else if (action.equalsIgnoreCase("add")){
              if (dao.checkProd( prod.getName(),prod.getUuid())) {
               		json.put("info", "exists");
               		response.setContentType("application/json");
               		response.setCharacterEncoding("UTF-8");
               		response.getWriter().write(json.toString());
               		return; 	
             	}
               
          		if(dao.addProd(prod)){
           			json.put("info", "success");
                 } else {
            	   json.put("info", "failed");
                 }
       	   }
       	 
       	 else if (action.equalsIgnoreCase("getuu")){
             Product UUProd = dao.getUUProd(prod.getUuid());
             System.out.println("uu:"+UUProd.name+UUProd.uuid);
             if ( UUProd != null) {
            	 JSONObject ro = new JSONObject();
        		 JSONArray ma = new JSONArray();
        		 
        		 ma.add(Prod2JO(UUProd));
  		    	 
        		 ro.put("type", "products");
        		 ro.put("arr", ma);
        			
        		 response.setContentType("application/json");
        		 response.setCharacterEncoding("UTF-8");
        		 response.getWriter().write(ro.toJSONString());
        		 return; 
             	}
             }
       		
             else if (action.equalsIgnoreCase("getusernumbers")) {
       		    List<Product> prods = dao.getProds(prod.getName(),prod.getType(), prod.getPrice(), prod.getFlag());
       		    
       		    JSONObject ro = new JSONObject();
       		    JSONArray ma = new JSONArray();
       		    
       		    for (Product mprod : prods){
 		    	 ma.add(Prod2JO(mprod));
 		    	}
       			ro.put("type", "products");
       			ro.put("arr", ma);
       			
 		     response.setContentType("application/json");
             response.setCharacterEncoding("UTF-8");
             response.getWriter().write(ro.toJSONString());
 		     return; 
 		  
 		}
    	else {
       	   	   json.put("info", "unknown");
           }
       	
           
       
       response.setContentType("application/json");
       response.setCharacterEncoding("UTF-8");
       response.getWriter().write(json.toString());
     }
   
   private JSONObject Prod2JO(Product UUProd) {	          
		JSONObject myentry = new JSONObject();
	
		 Field[] ml = UUProd.getClass().getDeclaredFields();
		 for(Field x: ml) {
	 		try {
	 		 Field mf =  x;
	          mf.setAccessible(true);
	          myentry.put(mf.getName(), mf.get(UUProd));
	          }catch(Exception e) {e.printStackTrace();}
		  }
		 
		return myentry;
   }
}
  
   
