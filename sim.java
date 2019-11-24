import java.lang.String;
import java.lang.Math;
import java.util.Date;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.List;
 public class  sim{
	public BimodalPredictor Bimodalpredictor1;
	public gsharepredictor gsharepredictor1;
	public hybridpredictor hybridpredictor1;
	public BimodalBTBPredictor BimodalBTBPredictor1;
	public gshareBTBPredictor gshareBTBPredictor1;
	File tracefile;
	String predictor;
	int PCbits;
	int BTBSize;
	int BTBAssoc;
	int gsharebits;
	int GHRbits;
	String predictionmodel;
	int bimodalbits;
	public static void main(String[] args){
		 System.out.println("COMMAND");
		 sim sim= new sim(args);
	}
		
	 public  sim (String[] args){
		 	 try {
				 predictionmodel = (args[0]);
		if(predictionmodel.equals("bimodal")){
			if(args.length!=5)
			 throw new IllegalArgumentException("Incorrect number of arguments for bimodal : Total number of arguments should be 5");
			  }
		else if (predictionmodel.equals("gshare")){
			if(args.length != 6)
				throw new IllegalArgumentException("Incorrect number of arguments for gshare model: Total number of arguments should be 6");
		}
		else if(predictionmodel.equals("hybrid")){
			if(args.length != 8)
				throw new IllegalArgumentException("Incorrect number of arguments for hybrid model: Total number of arguments should be 8");
		}
				//tracefile = new File(args[0]);
				predictor = (args[0]);
				PCbits = Integer.parseInt(args[1]);
				/*gsharebits = Integer.parseInt(args[2]);
				GHRbits  =  Integer.parseInt(args[3]);
				bimodalbits = Integer.parseInt(args[4]);
				*/BTBSize = Integer.parseInt(args[2]);
				BTBAssoc = Integer.parseInt(args[3]);
				if(predictor.equals("bimodal")){
					 //System.out.println("enter here>>>Bimodal");
					 predictor = (args[0]);
				PCbits = Integer.parseInt(args[1]);
				BTBSize = Integer.parseInt(args[2]);
				BTBAssoc = Integer.parseInt(args[3]);
				if(BTBSize == 0){
					Bimodalpredictor1 = new BimodalPredictor(PCbits);
					System.out.println("./sim bimodal "+PCbits+" "+BTBSize+" "+BTBAssoc+" ./traces/"+args[4]);
				}
				else{
				BimodalBTBPredictor1 = new BimodalBTBPredictor(PCbits,BTBAssoc,BTBSize);
				System.out.println("./sim bimodal "+PCbits+" "+BTBSize+" "+BTBAssoc+" ./traces/"+args[4]);
				}
				}
				if (predictor.equals("gshare")){
				GHRbits  =  Integer.parseInt(args[2]);
				BTBSize = Integer.parseInt(args[3]);
				BTBAssoc = Integer.parseInt(args[4]);
				if(BTBSize == 0){
					gsharepredictor1 = new gsharepredictor(PCbits, GHRbits);
				System.out.println("./sim gshare "+PCbits+" "+GHRbits+" "+BTBSize+" "+BTBAssoc+" ./traces/"+args[5]);
				}
				 else {
					gshareBTBPredictor1 = new gshareBTBPredictor(PCbits, GHRbits,BTBAssoc,BTBSize);
					System.out.println("./sim gshare "+PCbits+" "+GHRbits+" "+BTBSize+" "+BTBAssoc+" ./traces/"+args[5]);
				 } 
				} 
				if(predictor.equals("hybrid"))
				{
					gsharebits = Integer.parseInt(args[2]);
				GHRbits  =  Integer.parseInt(args[3]);
				bimodalbits = Integer.parseInt(args[4]);
			BTBSize = Integer.parseInt(args[5]);
				BTBAssoc = Integer.parseInt(args[6]);
					hybridpredictor1 = new hybridpredictor(PCbits,gsharebits,GHRbits,bimodalbits);
					System.out.println("./sim hybrid "+PCbits+" "+gsharebits+" "+GHRbits+" "+bimodalbits+" "+BTBSize+" "+BTBAssoc+" ./traces/"+args[7]);
				}
			}
				
				
			 catch (NumberFormatException e) {
					throw new IllegalArgumentException("Incorrect input argument format", e);
				}
				
				BufferedReader tracefile = null;
				String tracecurrentline = null;
				try{
				if(predictor.equals("bimodal")){		
				tracefile = new BufferedReader(new FileReader(args[4]));
				}
				else if(predictor.equals("gshare")){
					//System.out.println("enterhere2");
					tracefile = new BufferedReader(new FileReader(args[5]));
				}
				else if(predictor.equals("hybrid")){
					tracefile = new BufferedReader(new FileReader(args[7]));
				}
		}
			catch (IOException ioe){
				System.out.println("Error: File Open failed");
				ioe.printStackTrace();
			}
		try{
			while ((tracecurrentline = tracefile.readLine())!=null){
			if(tracefilemodification(tracecurrentline ,PCbits ,predictor) == false) break;
				}
				
			}
			catch (IOException ioe){
			System.out.println("Error: File cannot be read");
			ioe.printStackTrace();
		}
		System.out.println("OUTPUT");
		 if(predictor.equals("bimodal") && (BTBSize == 0))
		 Bimodalpredictor1.printingthetable(); 
		else if (predictor.equals("gshare") && (BTBSize == 0))
		 gsharepredictor1.printingthegsharetable();
		else if(predictor.equals("hybrid"))
			hybridpredictor1.printingthehybridtable();
		else if(predictor.equals("bimodal") && (BTBSize != 0))
			BimodalBTBPredictor1.printingthetable();
		else if(predictor.equals("gshare") && (BTBSize != 0))
			gshareBTBPredictor1.printingthetable();
		 /*System.out.println("Miss prediction >>>" + BimodalBTBPredictor1.numberofbranchmissBTBandTaken);
		 System.out.println("Miss prediction >>>" + BimodalBTBPredictor1.misspredictionfrombranchpredictor);
	/* hybridpredictor1.printingthehybridtable();
		 hybridpredictor1.Bimodalpredictor2.printingthetable(); 
		 hybridpredictor1.gsharepredictor2.printingthegsharetable();
	*/ }
	public boolean tracefilemodification(String address,int pcbits, String Predictor){
		String[] tracefilesplit = address.split(" ");
		Integer addressvalue = (int) Long.parseLong(tracefilesplit[0],16);
		String Prediction = tracefilesplit[1];
		if(Predictor.equals("bimodal") && (BTBSize == 0)){
		Bimodalpredictor1.prediction(addressvalue,Prediction);} 
	else if (Predictor.equals("bimodal") && (BTBSize != 0)){BimodalBTBPredictor1.CachePrediction(addressvalue,Prediction);}
			//System.out.println("Miss prediction >>>" + Bimodalpredictor1.missprediction);
		
		else if(Predictor.equals("gshare") && (BTBSize == 0)){
			gsharepredictor1.gshareprediction(addressvalue,Prediction);
		}
		else  if(Predictor.equals("gshare") && (BTBSize != 0)){
			gshareBTBPredictor1.gshareCachePrediction(addressvalue,Prediction);
		}
		else if(Predictor.equals("hybrid")){
			hybridpredictor1.hybridprediction(addressvalue,Prediction);
		}
		
		return true;
	}
}