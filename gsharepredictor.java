import java.lang.String;
import java.lang.Math;
import java.util.Date;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
public class gsharepredictor{
	int indexbits;
		int address;
		int missprediction;
		int numberofpredictions;
		int indexvalue;
		Integer indexsize;
		String branch_predict;
		Integer GHR = 0;
		int gsharebits;
		String taken = "t";
		public ArrayList<IndexTable> IndexTable = new ArrayList<IndexTable>();
		
	public gsharepredictor(int indexbits , int gsharebits){
			
			this.indexbits = indexbits;
			this.gsharebits = gsharebits;
			this.indexsize = (int) Math.pow((double) 2, (double) indexbits);
			for (int i=0; i < indexsize; i++)
			{ IndexTable.add(new IndexTable(i,"gshare"));}
			
			
		}
		
	public void gshareprediction(int address, String prediction){
		numberofpredictions++;
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
			if(!branch_predict.equals(prediction)){
						missprediction++;
					}
	}	
	public void printingthegsharetable (){
			System.out.println("number of predictions:  "+ numberofpredictions);
			System.out.println("number of mispredictions:  "+ missprediction);
			float misspredictionrate = (float)((float)(missprediction)*100/(float)(numberofpredictions));
			DecimalFormat df = new DecimalFormat("#.00");
			System.out.println("misprediction rate: "+df.format(misspredictionrate)+"%");
			System.out.println("FINAL GSHARE CONTENTS");
			for (IndexTable dataprint : IndexTable){
				
			System.out.println (dataprint.getindex() + "    " + dataprint.getcounter());
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
					
					//System.out.println("Mask >>> " + mask + " extractedindex>>> " + extractedindex + "address >> " +address);		
				}
			}
			
			return extractedindex;
		}
	public void updateghsare (String prediction){
		GHR = GHR >> 1; // shift over
		
		//int mask2 = 1 << gsharebits;	
		//System.out.println("enterhere>>");
		if(prediction.equals("t")){
			//System.out.println(">>>entertheloop");
		GHR = (GHR)^((int)(Math.pow(2,gsharebits-1))); }
		//System.out.println(">>>GHR value" + GHR);
	}	
	
	public int indexafterXOR (int indexvalue){
		int temporraryindex = 0;
		int tempmask = (1 << (indexbits - gsharebits ))-1;
		String valuetobeappended = Integer.toString(indexvalue & tempmask);
		temporraryindex = indexvalue >> (indexbits - gsharebits);
		//System.out.println(">>temporraryindex" + Integer.toBinaryString(temporraryindex) + ">>tempmask" + Integer.toBinaryString(tempmask));
		int XORvalue = (GHR ^ temporraryindex);
		int finalindex = (XORvalue<<(indexbits - gsharebits)) + (indexvalue & tempmask);
		//System.out.println(">>>>returned index" + (Integer.toBinaryString(finalindex))+ ">>appended value "+ valuetobeappended);		
		return finalindex;
	}
}