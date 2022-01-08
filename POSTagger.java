package com.cryptophonecall.sa;


import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import opennlp.tools.cmdline.postag.POSModelLoader;
import opennlp.tools.postag.POSModel;
import opennlp.tools.postag.POSSample;
import opennlp.tools.postag.POSTaggerME;
import opennlp.tools.tokenize.WhitespaceTokenizer;

public class POSTagger {
	POSModel model;
	public POSTagger(){
	
		model = new POSModelLoader().load(new File("/opt/apache-tomcat-10.0.12/webapps/SCRUDU2/WEB-INF/classes/en-pos-maxent.bin"));
	 	
	}
	
	
    public Map getPos(String input) throws Exception{

        //Loading Parts of speech-maxent model
       // System.setProperty("org.xml.sax.driver", "org.xmlpull.v1.sax2.Driver");

       //InputStream ms= new FileInputStream("/opt/apache-tomcat-10.0.12/webapps/SCRUDU2/WEB-INF/classes/en-pos-maxent.bin");
       // FileDescriptor fileDescriptor = assetFileDescriptor.getFileDescriptor();
       // FileInputStream stream = new FileInputStream(fileDescriptor);
    	//System.setProperty("org.xml.sax.driver","org.xmlpull.v1.sax2.Driver");
       
      // POSModel model = new POSModelLoader().load(new File("/opt/apache-tomcat-10.0.12/webapps/SCRUDU2/WEB-INF/classes/en-pos-maxent.bin"));
      // POSModel model = new POSModel(ms);

        //Creating an object of WhitespaceTokenizer class
        WhitespaceTokenizer whitespaceTokenizer= WhitespaceTokenizer.INSTANCE;

        //Tokenizing the sentence
       
        String sentence = input;
        String[] tokens = whitespaceTokenizer.tokenize(sentence);

        //Instantiating POSTaggerME class
        POSTaggerME tagger = new POSTaggerME(model);

        //Generating tags
        String[] tags = tagger.tag(tokens);

        //Instantiating the POSSample class
   //     POSSample sample = new POSSample(tokens, tags);
        

        //Probabilities for each tag of the last tagged sentence.
 //       double [] probs = tagger.probs();
        
        //Printing the probabilities
        Map<String,String> mm = new TreeMap<>();
       
        for(int i = 0; i<tags.length; i++){
        	switch(tags[i])
            {
            	case "JJ":
            	{
            		if(mm.containsKey("a"))
        	 
            			mm.replace("a", tokens[i].replaceAll("\\W",""));
            		else
            			mm.put("a", tokens[i].replaceAll("\\W",""));
            		break;
            	}
            	case "VBG":
                case ".":
            	case "NN":
            	{
            	  if(!tokens[i].replaceAll("\\W","").equals("please") && !tokens[i].replaceAll("\\W","").equals("thank") && !tokens[i].replaceAll("\\W","").equals("you"))
            		{
            		  if(mm.containsKey("n") )
            		    {
            			 mm.replace("n", tokens[i].replaceAll("\\W",""));
            			}
            		  else
            			mm.put("n", tokens[i].replaceAll("\\W",""));
            		}
            		break;
            	}
            	case "NNS":
            	{
            		if(!tokens[i].replaceAll("\\W","").equals("thanks"))
            		{
            			if(mm.containsKey("ns"))
		
            				mm.replace("ns", tokens[i].replaceAll("\\W",""));
            			else
            				mm.put("ns", tokens[i].replaceAll("\\W",""));
            		}
            		break;
            	}
            }
           System.out.println(tags[i]);
           System.out.println(tokens[i]);
           
        }
        return mm;
    }
}