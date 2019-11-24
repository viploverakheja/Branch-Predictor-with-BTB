import java.lang.String;
import java.lang.Math;
import java.util.Date;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
public class gshareBTBPredictor{
		int indexbits;
		int address;
		int missprediction;
		int numberofpredictions;
		int indexvalue;
		Integer indexsize;
		String branch_predict;
		String cache_branch_predict;
		int BTBSize;
		int BTBAssoc;
		int gsharebits;
		Integer GHR = 0;
		public CacheSimulation cache;
		int numberofbranchmissBTBandTaken= 0;
		int nubmerofpredictionsfrombranchpredictor=0;
		int misspredictionfrombranchpredictor=0;
		int BTBmisses=0;
		public ArrayList<IndexTable> IndexTable = new ArrayList<IndexTable>();
		
	public gshareBTBPredictor(int indexbits , int gsharebits, int BTBAssoc, int BTBSize ){
			
			this.indexbits = indexbits;
			this.BTBSize = BTBSize;
			this.gsharebits = gsharebits;
			this.BTBAssoc = BTBAssoc;
			cache = new CacheSimulation(4,BTBAssoc,BTBSize);
			this.indexsize = (int) Math.pow((double) 2, (double) indexbits);
			for (int i=0; i < indexsize; i++)
			{ IndexTable.add(new IndexTable(i,"gshare"));}
				
		}
		
		public void gshareCachePrediction(int address, String prediction){
			numberofpredictions++;
			int indexforcache = cache.extractIndex(address);
			if(cache.checkifcachehasvalue(address) == true){
				nubmerofpredictionsfrombranchpredictor++;
				gshareprediction(address,prediction);
				cache.updateLRUcounter(address,indexforcache);
				
			}
			else{
				
				BTBmisses++;
				cache_branch_predict = "n";
				//updateghsare(prediction);
				cache.readmissupdate(address,indexforcache);
			if(prediction.equals("t")){
					numberofbranchmissBTBandTaken++;
					//System.out.println(">>>>enterherer" + numberofbranchmissBTBandTaken );
			}
			}
			
		}
		public void gshareprediction(int address, String prediction){
		//numberofpredictions++;
		indexvalue = extractindexbits (address , indexbits);
		int finalindexvalue = (int)indexafterXOR(indexvalue);
		
		for (IndexTable data : IndexTable){
			  if(data.getindex() == finalindexvalue){
					if(data.getcounter() >= 2)
					{
						branch_predict ="t";
						
						if(prediction.equals ("n"))
						{
							if(data.getcounter() > 0)
							data.setcounter((data.getcounter()-1));
							else
							data.setcounter(0);	
						}	
						else {
							if(data.getcounter() < 3)
							data.setcounter((data.getcounter() + 1));
							else
							data.setcounter(3);	
						}
					}
					
					else {
						branch_predict = "n";
						if(prediction.equals ("n"))
						{
							if(data.getcounter() > 0)
							data.setcounter((data.getcounter()-1));
							else
							data.setcounter(0);	
						}	
						else {
							if(data.getcounter() < 3)
							data.setcounter((data.getcounter() + 1));
							else
							data.setcounter(3);	
						}
					}
			  }
					
			}
			updateghsare(prediction);
			//System.out.println("GHR>>>" + GHR+">>>address"+Integer.toHexString(address) );
			if(!branch_predict.equals(prediction)){
						misspredictionfrombranchpredictor++;
					}
	}
			
			public int extractindexbits(int address, int indexbits){
			int value = 0;
			int index = 0;
			int extractedindex;
			//int mask = ~0;
			switch(indexbits){
				case 0 : index = value;
				default : {
					int mask = (int) Math.pow ((double) 2, (double)indexbits) -1;
					int temporaryaddress = address >> 2;
					extractedindex = temporaryaddress & mask;
				}
			}
			
			return extractedindex;
		}
	public void updateghsare (String prediction){
		GHR = GHR >> 1; 
		if(prediction.equals("t")){
		GHR = (GHR)^((int)(Math.pow(2,gsharebits-1))); }
	}	
	
	public int indexafterXOR (int indexvalue){
		int temporraryindex = 0;
		int tempmask = (1 << (indexbits - gsharebits ))-1;
		String valuetobeappended = Integer.toString(indexvalue & tempmask);
		temporraryindex = indexvalue >> (indexbits - gsharebits);
		int XORvalue = (GHR ^ temporraryindex);
		int finalindex = (XORvalue<<(indexbits - gsharebits)) + (indexvalue & tempmask);
		return finalindex;
	}
	
	public void printingthetable (){
			System.out.println ("size of BTB:	 "+ BTBSize);
			System.out.println ("number of branches:	 "+numberofpredictions);
			System.out.println ("number of predictions from branch predictor:	"+nubmerofpredictionsfrombranchpredictor);
			System.out.println ("number of mispredictions from branch predictor:	"+misspredictionfrombranchpredictor);
			System.out.println ("number of branches miss in BTB and taken:	 "+numberofbranchmissBTBandTaken);
			System.out.println ("total mispredictions:	"+(misspredictionfrombranchpredictor+numberofbranchmissBTBandTaken));
			float misspredictionrate = (float)((float)(misspredictionfrombranchpredictor+numberofbranchmissBTBandTaken)*100/(float)(numberofpredictions));
			DecimalFormat df = new DecimalFormat("#.##");
			System.out.println("misprediction rate: "+df.format(misspredictionrate)+"%");
			System.out.println("FINAL BTB CONTENTS");
			cache.printingCache();
			System.out.println ("\n");
			System.out.println ("FINAL GSHARE CONTENTS");
			for (IndexTable dataprint : IndexTable){
			System.out.println (dataprint.getindex() + "    " + dataprint.getcounter());
			}
		}
}		