import java.lang.String;
import java.lang.Math;
import java.util.Date;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
public class hybridpredictor{
	public BimodalPredictor Bimodalpredictor2;
	public gsharepredictor gsharepredictor2;
	public ArrayList<IndexTable> IndexTable = new ArrayList<IndexTable>();
	//public ArrayList<IndexTable> BimodalTable = new ArrayList<IndexTable>();
	//public ArrayList<IndexTable> GshareTable = new ArrayList<IndexTable>();
	int programcounterbits;
	int hybrid_gsharebits;
	int hybrid_GHRsize;
	int hybrid_bimodalbits;
	int hybrid_numberofpredictions =0;
	String hybrid_branchpredict;
	String gshare_branch_predict;
	String bimodal_branch_predict;
	int hybrid_misspredictions =0;
	int PCbits;
	int hybrid_index;
	int Bimodal_index;
	int gshare_index;
	int tempgshare_index;
	
	public hybridpredictor (int PCbits, int gsharebits, int GHRsize, int bimodalbits){
		int i;
		this.PCbits = PCbits;
		this.hybrid_gsharebits = gsharebits;
		this.hybrid_GHRsize = GHRsize;
		this.hybrid_bimodalbits = bimodalbits;
		for (i=0; i< ((int) Math.pow((double) 2, (double) PCbits)); i++){
		IndexTable.add(new IndexTable(i,"hybrid"));
		}
		/*for (i=0; i< ((int) Math.pow((double) 2, (double) gsharebitcount)); i++)
			gsharepredictor2.IndexTable.add(new IndexTable(i,"gshare"));
		for (i=0; i< ((int) Math.pow((double) 2, (double) bimodalbitcount)); i++)
			Bimodalpredictor2.IndexTable.add(new Bimodalpredictor2.IndexTable(i,"bimodal"));*/
		
		   Bimodalpredictor2 = new BimodalPredictor(hybrid_bimodalbits);
		   gsharepredictor2 = new gsharepredictor(hybrid_gsharebits,hybrid_GHRsize );
		   
			
	}
	public void hybridprediction(int address, String prediction){
		hybrid_numberofpredictions++;
		hybrid_index = Bimodalpredictor2.extractindexbits(address,PCbits);
		Bimodal_index = Bimodalpredictor2.extractindexbits(address,hybrid_bimodalbits);
		tempgshare_index = gsharepredictor2.extractindexbits(address,hybrid_gsharebits);
		gshare_index = (int)gsharepredictor2.indexafterXOR(tempgshare_index);
		//System.out.println("Bimodal_index>>"+Bimodal_index+ "gshare_index>>"+gshare_index+ "hybrid_index>> "+hybrid_index);
		for(IndexTable datatest: Bimodalpredictor2.IndexTable){
			if(datatest.getindex() == Bimodal_index){
				if(datatest.getcounter()>=2)
					bimodal_branch_predict= "t";
				else bimodal_branch_predict = "n";
			}
			//System.out.println("bimodal_branch_predict>>>"+bimodal_branch_predict);
		}
		for(IndexTable datatest: gsharepredictor2.IndexTable){
			if(datatest.getindex() == gshare_index){
				//System.out.println(">>counter value>>" + datatest.getcounter());
				if(datatest.getcounter()>=2)
					gshare_branch_predict= "t";
				else gshare_branch_predict = "n";
			}
			//System.out.println("gshare_branch_predict>>>"+gshare_branch_predict);
		}
		for (IndexTable datahybrid: IndexTable){
			if(datahybrid.getindex() == hybrid_index){
				if(datahybrid.getcounter()<2){
					Bimodalpredictor2.prediction(address,prediction);
					gsharepredictor2.updateghsare(prediction);
					hybrid_branchpredict = Bimodalpredictor2.branch_predict;
				}
				else{
					gsharepredictor2.gshareprediction(address,prediction);
					hybrid_branchpredict = gsharepredictor2.branch_predict;
				}
				//System.out.println ("bimodal_branch_predict>>>" + bimodal_branch_predict + ">>>>gshare_branch_predict>>>"+gshare_branch_predict );
			if((bimodal_branch_predict.equals(prediction) == true) && (gshare_branch_predict.equals(prediction)== false)){
				
				if(datahybrid.getcounter() > 0)
				{	
			//System.out.println ("enter the bimodal>>>" + hybrid_numberofpredictions );
				datahybrid.setcounter(datahybrid.getcounter() - 1);
				}
				else
					datahybrid.setcounter(0);
			}
			 if((bimodal_branch_predict.equals(prediction) == false) && (gshare_branch_predict.equals(prediction)== true)){
				 
				if(datahybrid.getcounter() < 3)
				datahybrid.setcounter(datahybrid.getcounter() + 1);
				else
					datahybrid.setcounter(3);
			}
			}
		
			
		}
		if(!hybrid_branchpredict.equals(prediction)){
						hybrid_misspredictions++;
					}
	}
	public void printingthehybridtable (){
			System.out.println ("number of predictions:  "+ hybrid_numberofpredictions);
			System.out.println ("number of mispredictions:  "+ hybrid_misspredictions);
			float misspredictionrate = (float)((float)(hybrid_misspredictions)*100/(float)(hybrid_numberofpredictions));
			DecimalFormat df = new DecimalFormat("#.00");
			System.out.println ("misprediction rate: "+df.format(misspredictionrate) +"%");
			System.out.println ("FINAL CHOOSER CONTENTS");
			for (IndexTable datahybrid: IndexTable){
			System.out.println (datahybrid.getindex() + "    " + datahybrid.getcounter());
			}
			System.out.println("FINAL GSHARE CONTENTS");
			for(IndexTable datagshare: gsharepredictor2.IndexTable){
				System.out.println (datagshare.getindex() + "    " + datagshare.getcounter());
			}
			System.out.println("FINAL BIMODAL CONTENTS");
			for(IndexTable databimodal: Bimodalpredictor2.IndexTable){
				System.out.println (databimodal.getindex() + "    " + databimodal.getcounter());
			}
		}
	
}