import java.lang.String;
import java.lang.Math;
import java.util.Date;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
public class BimodalPredictor{
		int indexbits;
		int address;
		int missprediction;
		int numberofpredictions;
		int indexvalue;
		Integer indexsize;
		String branch_predict;
		public ArrayList<IndexTable> IndexTable = new ArrayList<IndexTable>();
		
	public BimodalPredictor(int indexbits ){
			
			this.indexbits = indexbits;
			this.indexsize = (int) Math.pow((double) 2, (double) indexbits);
			for (int i=0; i < indexsize; i++)
			{ IndexTable.add(new IndexTable(i,"bimodal"));}
				
			
		}
		public void prediction (int address, String prediction){
			indexvalue = extractindexbits(address, indexbits);
			//System.out.println("Index table creation ++++" + indexvalue );
			numberofpredictions++;
			for (IndexTable data : IndexTable){
				//System.out.println("Index table creation" + data.getindex() + " " + data.getcounter() );
			  if(data.getindex() == indexvalue){
					//System.out.println("enter here>>" + data.getindex() + " counter value>>" + data.getcounter() );
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
			if(!branch_predict.equals(prediction)){
						missprediction++;
					}
					
			//System.out.println("Index table creation >>>>" + missprediction);
		}
		public void printingthetable (){
			System.out.println("number of predictions:  "+ numberofpredictions);
			System.out.println("number of misPredictions:  "+ missprediction);
			float misspredictionrate = (float)((float)(missprediction)*100/(float)(numberofpredictions));
			DecimalFormat df = new DecimalFormat("#.00");
			System.out.println("misprediction rate: "+df.format(misspredictionrate)+"%");
			System.out.println("FINAL BIMODAL CONTENTS");
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
			//System.out.println("bit value >> "+Integer.toBinaryString(extractedindex));
			return extractedindex;
		}
}