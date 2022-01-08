package com.cryptophonecall.dao;

import java.sql.*;
import java.util.*;

import javax.sql.DataSource;

import com.cryptophonecall.model.Product;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
 

public class ProductDAO {

	public DataSource dataSource ;
	
	public ProductDAO(){
		try {
			Context initContext  = new InitialContext();
			Context envContext  = (Context)initContext.lookup("java:/comp/env");
			dataSource = (DataSource)envContext.lookup("jdbc/defaultdb");
		}catch(Exception e) {
			e.printStackTrace();
		}
	}
    
    public Connection getConnection() {
    
   	    Connection connection = null;
     
    	try {
             // Get DataSource
             connection =  dataSource.getConnection();
        
    	 } catch (Exception e) {
             e.printStackTrace();
         }
        return connection;
    	
    }
 
    public String getTn(String name) {
    	return "a_prod" ; //name.charAt(0) + "_prod"; 
    }
    
    
    public Boolean addProd(Product prod) {
    	Connection connection = null; 
    	try {
            connection = getConnection();
        	
        	PreparedStatement preparedStatement = connection.prepareStatement("insert into a_prod (uuid, name, price, off, pic, prom, shopid, type, owner, per) values (?,?,?,?,?,?,?,?,?,?)");
            
//        	preparedStatement.setString(1, getTn(prod.getName()));
        	preparedStatement.setString(1, prod.getUuid());
            preparedStatement.setString(2, prod.getName());
            preparedStatement.setString(3, prod.getPrice().toString());
            preparedStatement.setString(4, prod.getOff());
            preparedStatement.setString(5, prod.getPic());
            preparedStatement.setString(6, prod.getProm());
            preparedStatement.setString(7, "3");
            preparedStatement.setString(8, prod.getType());
            preparedStatement.setString(9, prod.getOwner());
            preparedStatement.setString(10, prod.getPer().toString());
            
            System.out.println(preparedStatement.toString());
            
            preparedStatement.executeUpdate();
            
            
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        } finally {
        	try {
        		connection.close();
        		}catch(Exception e) {
        			e.printStackTrace();
        		}
        }
        return true;
    }
 
    
    
    
    public Boolean checkProd(String name, String number) {
  	  Connection connection = null;
    	try {
    		//select uuid from ? where name=? and uuid=?
          connection = dataSource.getConnection();
    	  PreparedStatement preparedStatement = connection.prepareStatement("select uuid from a_prod where name=? and uuid=?");
          
    	 // preparedStatement.setString(1, "a_prod");  
    	  preparedStatement.setString(1, name);
          preparedStatement.setString(2, number);
          
          ResultSet rs = preparedStatement.executeQuery();

          if (rs.isBeforeFirst() ) {    
              return true; 
          }
      } catch (SQLException e) {
          e.printStackTrace();
      }finally {
       	try {
      		connection.close();
      		}catch(Exception e) {
      			e.printStackTrace();
      		}
        }

     return false;
  }
    

    public Product getUUProd(String number) {
    	  Connection connection = null;
      	try {
      		//select uuid from ? where name=? and uuid=?
            connection = dataSource.getConnection();
      	  PreparedStatement preparedStatement = connection.prepareStatement("select * from a_prod where uuid=?");
            
      	 // preparedStatement.setString(1, "a_prod");  
      	  preparedStatement.setString(1, number);
            
            ResultSet rs = preparedStatement.executeQuery();

            if (rs.isBeforeFirst() ) {    
            	rs.next();
            	return setProd(rs); 
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
         	try {
        		connection.close();
        		}catch(Exception e) {
        			e.printStackTrace();
        		}
          }

       return null;
    }

    
    
    
    
    public Boolean delProd(String uuid) {
    	Connection connection = null; 
    	try {
    		connection = getConnection();
    		
    		PreparedStatement preparedStatement = connection.prepareStatement("delete from a_prod where uuid=?");
            // Parameters start with 1
            preparedStatement.setString(1,uuid);
            preparedStatement.executeUpdate();
 
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }finally {
        	try {
        		connection.close();
        		}catch(Exception e) {
        			e.printStackTrace();
        		}
        }
        return true;
    }
      public List<Product> getProds(String sname, String stype, Double price, Integer flag) {
    	  Connection connection = null;
    	  List<Product> prods = new LinkedList<>();
    	  try {
    		  connection = getConnection();
    		  PreparedStatement preparedStatement = null; 
    
    		  String pstr = "select * from a_prod where ";
    		  String estr = "";
    				     				  
    		  if (stype != null)
    			  estr = "type like ? ";
    		  else if (sname != null)
    			  estr = "name like ? ";
    		  else if(sname != null && stype != null)
    			  estr = "name like ? and type like ? "; 
 
    		  if(flag.equals(0))
    			  estr = estr + ";";
    		  else if(flag.equals(1))
    	    	 estr = estr + "and price <= ? ;";
    	     else if(flag.equals(2))
    	    	 estr = estr + "and price >= ? ;";
    	     
    	      String mstr = pstr + estr;
    		  preparedStatement = connection.prepareStatement(mstr);
    		  
    		  
    		//  preparedStatement.setString(1, "a_prod"); //getTn(sname));
    		  
    		  Boolean fl1=true; 
    		  if(sname != null && stype != null)
    		  {
    			  preparedStatement.setString(1, "%"+sname+"%");
			      preparedStatement.setString(2, "%"+stype+"%");
			      fl1=true;
    		  }
    		  else if (sname != null)
    		  {
    			  preparedStatement.setString(1, "%"+sname+"%");
    			  fl1=false;
    		  }
       		  else if (stype != null)
         	  {
       			 preparedStatement.setString(1, "%"+stype+"%");
       			 fl1=false;
         	  }
      		  if(!flag.equals(0))
    		    preparedStatement.setString(fl1?3:2, price.toString());
    		   
    		  
    		  ResultSet rs = preparedStatement.executeQuery();
    		  
              while(rs.next()) {    
                  prods.add(setProd(rs));
                 }
            
    	  } catch (SQLException e) {
              e.printStackTrace();
          }finally {
        	try {
        		connection.close();
        		}catch(Exception e) {
        			e.printStackTrace();
        		}
        
        }
         return prods;
      }
      
      
      
      Product setProd(ResultSet rs){
    	
    	  Product prod = null; 	
    	 
    	  try {
    	  prod = new Product();
          prod.setUuid(rs.getString("uuid"));
          prod.setName(rs.getString("name"));
          prod.setPrice(rs.getDouble("price"));
          prod.setOff(rs.getString("off"));
          prod.setPic(rs.getString("pic"));
          prod.setProm(rs.getString("prom"));
          prod.setShopid(rs.getString("shopid"));
          prod.setType(rs.getString("type"));
          prod.setOwner(rs.getString("owner"));
          prod.setPer(rs.getInt("per"));
	  	} catch (SQLException e) {
	  		e.printStackTrace();
	  	}
          return prod;
    	  
      }
      
      
      
}