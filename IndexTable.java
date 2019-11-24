import java.lang.String;
import java.lang.Math;
import java.util.Date;
import java.io.*;
import java.text.DecimalFormat;
import java.util.Scanner;
import java.util.List;
public class IndexTable{
		Integer Counter;
		Integer index;
		public IndexTable(Integer index , String predictor){
			if (predictor.equals("hybrid")){
				this.index = index;
				this.Counter = 1;
			}	
			else{
				this.index = index;
				this.Counter = 2;
			}				
		}
		public int getcounter(){
			return (int) this.Counter;
		}
		public void setcounter(int countervalue){
			this.Counter = countervalue;
		}
		public int getindex(){
			return (int) this.index;
		}
}