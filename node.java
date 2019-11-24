import java.lang.String;
import java.lang.Math;
import java.util.Date;
import java.io.*;
import java.text.DecimalFormat;
public class node{
	boolean dirtybit;
	boolean validbit;
	Integer blocksize;
	Integer associativity;
	Integer address;
	Integer count;
	
	public node(Integer address){
		if(address == null){
			this.address = 0;
			this.dirtybit = false;
			this.validbit = false;
			this.count = 0;
		}
		else{
			this.address = address;
			this.dirtybit= false;
			this.validbit = true;
			
		}
	}
	
	public boolean isDirty(){
		return this.dirtybit;
	}
	
	public void setDirty(boolean dirtyvalue){
		this.dirtybit = dirtyvalue;
	}
	public Integer getTag(int tagbits){
		//System.out.println("insidetagbits" + this.address);
		return (int) this.address>>>(24-tagbits);
	}
	public int getaddress(){
		return (int) this.address;
	}
	public boolean checkValid(){
		return this.validbit;
	}
	
	public void setAddress(int addressLFU){
			this.address = addressLFU;
	}
	public void setValid(boolean validvalue){
		this.validbit = validvalue;
	}
////////////////////////////////////////////////////////////////////////////////////////////////
	public int getCount(){
		System.out.println("===>enter here==>" +this.count);
			return (int) this.count;		
		}
	public void setCount(int count){
			 this.count = count;	
		}
}


