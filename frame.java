import java.lang.String;
import java.lang.Math;
import java.util.Date;
import java.io.*;
import java.text.DecimalFormat;

public class frame{
	
	Integer frameindex;
	Integer setassoc;
	Integer framebits;
	
	public node[] newnode = new node[1];
	
	public frame(int associativity,int index, int numberofbits){
		this.setassoc = associativity;
		this.framebits = numberofbits;
		this.frameindex = index;
		
		this.newnode = new node[setassoc];
		node emptynode = new node(null);
		for(int i=0;i<setassoc;i++){
			newnode[i] = emptynode;
		}
	}
	
	public boolean framefull(){
				
		for(int i=0;i<setassoc;i++){
						
			if(newnode[i].checkValid() == false) return false; //updated
		}
		
		return true;
	}
	
	public boolean hit (int address){
			Integer tagvalue = (new node(address).getTag(framebits));
			//System.out.println("tagvalue->"+tagvalue);
			Integer tag = null;
			if(tagvalue != null){			
			for(int i = 0;i<setassoc;i++)
			{
			//System.out.println("INSIDE FOR LOOP ->" + setassoc + "  "+newnode[i]);
				if(newnode[i]!= null)				
				tag = newnode[i].getTag(framebits);
			//System.out.println("TAG--->"+tag);
			if(tagvalue.equals(tag)) return true;
			}
		}
			return false;
	}
	
	public Integer hitposition (int address){
		Integer tagvalue =  (new node(address).getTag(framebits));
			//System.out.println("TageValue  "+tagvalue);
			Integer tag = null;
			int position=0;
			
			for(int i = 0;i<setassoc;i++){
							
				tag = newnode[i].getTag(framebits);
				//System.out.println("--->"+tag);
			
			if(tag.equals(tagvalue)) { position = i; break; }
			}
			return position;
	}
	public void addnewaddress(int address , boolean dirtybit){
		node newaddressnode = new node(address);
		//System.out.println("enter address mode");
		newaddressnode.setDirty(dirtybit);
		newaddressnode.setCount(0);
		
		newnode[0] = newaddressnode;
	}
	public node LRUreset(int address){
		//System.out.println("LRUResest ---- >");
		int i = hitposition(address);
		//System.out.println("I->>" +i);
		node moving_node =  newnode[i];
		
		for(int j =i ; j>0 ; j--){
		//System.out.println("inside for loop");	
		newnode[j] = newnode[j-1];
		}
		newnode[0] = moving_node; 

		return newnode[0];
	}
	public node LRUfix(){
			node noderemoved = new node(null);
			
			noderemoved = newnode[setassoc -1];
			LRUshift();
		newnode[0]= new node(null);
		return noderemoved; 
	}
	public void LRUshift(){
		for(int i= (setassoc-1);i>0 ;i--){
			newnode[i]= newnode[i-1];
			}
		
	}

	public void LRUfixifnotfull(int address){
		LRUshift();
	}
	
	public void write (boolean dirty, int address){
		node temp = new node(address);		
		temp.setDirty(true);
		newnode[0] = temp;
	}

	public void printthedirtybit(){
			for(int i=0;i<setassoc; i++){
				int tag = newnode[i].getTag(framebits);
				System.out.print(Integer.toHexString(tag));
				/*if(newnode[i].isDirty()){
						System.out.print("D\n");
				}*/
				System.out.print("\t");			
			}
		}

//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
	/*public void addnewaddressLFU(int address , boolean dirtybit, int counter){
				
		node newaddressnodeLFU = new node(address);
		newaddressnodeLFU.setDirty(dirtybit);
		newaddressnodeLFU.setCount(counter + 1);
		newnode[0] = newaddressnodeLFU;
	}*/
	
	/*public void LFUMiss(int address ){
		int	index = (int) getmincounternode();
		int retaincount = newnode[index].getCount();
		System.out.println("RetainCountvalue" + retaincount);
		newnode[index].setAddress(address);
		newnode[index].setCount(retaincount+1);
		System.out.println("count value"+ newnode[index].getCount());
		newnode[index].setDirty(false);
		newnode[index].setValid(true);
	}
	
	public int getmincounternode(){
		int	min = newnode[0].getCount();
		int index = 0;
		for (int i=0;i<setassoc;i++){
			
				if(min > newnode[i].count){
					min = newnode[i].count;
					index = i;
				}
		}
		return index;
	}
	public node LFUhit(int address){
		
		int i = hitposition(address);
		System.out.println("XXXX>>"+i);
		return newnode[i];
	}
	public void LFUshift(){
		for(int i= (setassoc-1);i>0 ;i--){
			newnode[i]= newnode[i-1];
			}
		newnode[0] = new node(null);
	}*/
	
}
	
