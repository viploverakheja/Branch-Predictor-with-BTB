import java.lang.String;
import java.lang.Math;
import java.util.Date;
import java.io.*;
import java.text.DecimalFormat;
public class CacheSimulation{
		int n=1;
		int Cacheblocksize;
		int Cacheassociativity;
		int CacheSize;
		
		int readhit =0;
		int writehit =0;
		int readmiss =0;
		int writemiss = 0;
		int writebackcount =0;
		int readcount =0;
		int writecount =0;
		
		int blockoffsetbits;
		int numberofsets;
		int indexbits;
		int tagbits;
		int Tagsize;
		int setindex;
		int address;
		boolean checkvalue;
		
		private frame[] newframe = new frame[1];
		
			public CacheSimulation(int Cacheblocksize,int Cacheassociativity, int CacheSize){
				this.Cacheblocksize =  Cacheblocksize;
				this.Cacheassociativity = Cacheassociativity;
				this.CacheSize = CacheSize;
				this.numberofsets = this.CacheSize/ (this.Cacheassociativity * this.Cacheblocksize);
				this.indexbits = (int) logof2((this.CacheSize)/(this.Cacheassociativity * this.Cacheblocksize));
				this.blockoffsetbits = (int) logof2(this.Cacheblocksize);
				this.tagbits = 24- this.indexbits - this.blockoffsetbits;
			//	System.out.println("numberofsets->"+numberofsets+"indexbits->"+indexbits+"blockoffsetbits->  "+blockoffsetbits);
				this.newframe = new frame[numberofsets];
				for (int i=0; i<this.numberofsets ; i++){
					newframe[i] = new frame( Cacheassociativity , i ,tagbits);
				}
				
			}
		public void creatingspaceforaddress(int address){
			int index = extractIndex(address);
			//System.out.println("--->"+index +"--->"+newframe[index]);
			boolean checkfull = newframe[index].framefull();			
			if(checkfull){
				
				node writeback = newframe[index].LRUfix();
				//System.out.println("---> checkfull = true"+writeback);
				boolean dirtybit = writeback.isDirty();
				//writebackcount++;
				if(dirtybit){
					writebackcount++;
					node temp = new node(writeback.getaddress());
					Integer tag = temp.getTag(blockoffsetbits);
					//	System.out.println("Tag - > "+tag);
				}
				
			}
			else{
			//	System.out.println("---> checkfull = false");
				newframe[index].LRUshift();
			}
			
		}
		public void readmemory (int address){
			readcount++;
			int index = extractIndex(address);
			checkvalue = checkifcachehasvalue(address);
			if(checkvalue){
				updateLRUcounter(address,index);
				/*readhit++; 
				//System.out.println("ReadHit"); 
				node readfix = newframe[index].LRUreset(address);
				node fix = new node(readfix.getaddress());
				fix.setDirty(false);
				fix.setValid(true);//update made*/
				 
			}
			else {
				//System.out.println("adding new address");
				readmiss++;
			//	System.out.println("Readmiss"); 
				readmissupdate(address,index);
				/*creatingspaceforaddress(address);
				node temp = new node(address);
				node temp2= new node(temp.getaddress());
				temp2.setDirty(false);
				temp2.setValid(true); //update made
				newframe[index].addnewaddress(temp2.getaddress(),false);
				return temp2;*/
			}
			
		}
					
		
		
		
		
		public int extractIndex(int address){
				int value=0; 
				int index =0;
				switch (indexbits){
					case 0 : index =value;
					default : {int masking = (int) Math.pow((double)2,(double) indexbits)-1; 
							int temporaryaddress = address>>blockoffsetbits;
							index = temporaryaddress & masking; 
						}				
				}
				return index;			
			}
		public int extractTag (node tagnode) {
				int masking = (~(int)(Math.pow((double)2, (double)blockoffsetbits)-1));
				int tagvalueofnode = tagnode.getaddress();
				return tagvalueofnode;			
			}
	
	public int logof2(int value){
		Integer y = (int) (Math.log(value)/Math.log(2));
		return y;
	}
	 public void printingCache(){
		for(int k=0;k<numberofsets;k++){
			System.out.print("\nset "+k+ " :     ");
			newframe[k].printthedirtybit();
		}		
	}
	public boolean checkifcachehasvalue(int address){
				int temp = extractIndex (address);
				//System.out.println("index value ?? "+ temp);
				boolean hit = newframe[temp].hit(address);
				return hit;	
			}
			

		
		public void updateLRUcounter(int address, int index){
			readhit++; 
				//System.out.println("ReadHit"); 
				node readfix = newframe[index].LRUreset(address);
				node fix = new node(readfix.getaddress());
				fix.setDirty(false);
				fix.setValid(true);//update made
				//return fix; 
		}
		public void readmissupdate(int address,int index){
			creatingspaceforaddress(address);
				node temp = new node(address);
				node temp2= new node(temp.getaddress());
				temp2.setDirty(false);
				temp2.setValid(true); //update made
				newframe[index].addnewaddress(temp2.getaddress(),false);
				//return temp2;
		}
		
}
