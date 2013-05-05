package poi.cvrp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Time;
import java.util.Date;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.DataFormat;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;


public class PlotIt {
	String[] headers ;
	String[] datatypes;
	CellStyle[] cellTypes;
	int maxIterations = 0;
	String workBookName ;
	int currentRun = 0;
	Workbook wb;
	Sheet s ;
	int buffer = 2;
	FileOutputStream out = null;
	//FileInputStream in = null;
	int lastRow = 0;
	boolean fileExists;	
	public static void main(String args[]){
		PlotIt plotit = null;
		try{
			String[] headers = {"Iteration","Cost","Time"};
			String[] cellDataFormat = {"0","0.000","hh:mm:ss"};
			
			String workBookName = "CVRP.xls";
			plotit = new PlotIt(headers,cellDataFormat,workBookName);
			plotit.initialize();
			plotit.startSession("CVRP3");
			plotit.setRun(1);
			Object[] data1 = {1.0,898.34, new Time(12,34,01)};
			plotit.plot(data1);
			Object[] data2 = {2.0,780.14, new Time(12,34,01)};
			plotit.plot(data2);
			Object[] data3 = {3.0,727.04, new Time(12,35,01)};
			plotit.plot(data3);
			Object[] data4 = {4.0,717.3, new Time(12,36,01)};
			plotit.plot(data4);

			plotit.setRun(2);
			Object[] data5 = {5.0,784.34, new Time(12,34,01)};
			plotit.plot(data5);
			Object[] data6 = {6.0,678.39, new Time(12,34,01)};
			plotit.plot(data6);
			Object[] data7 = {7.0,650.14, new Time(12,35,01)};
			plotit.plot(data7);
			Object[] data8 = {8.0,649.34, new Time(12,36,01)};
			plotit.plot(data8);
			plotit.logImprovement();
			
			plotit.startSession("CVRP4");
			plotit.setRun(1);
			Object[] data9 = {1.0,814.34, new Time(12,34,01)};
			plotit.plot(data9);
			Object[] data10 = {2.0,624.24, new Time(12,34,01)};
			plotit.plot(data10);
			Object[] data11 = {3.0,604.34, new Time(12,35,01)};
			plotit.plot(data11);
			Object[] data12 = {4.0,599.14, new Time(12,36,01)};
			plotit.plot(data12);

			plotit.setRun(2);
			Object[] data13 = {5.0,879.44, new Time(12,34,01)};
			plotit.plot(data13);
			Object[] data14 = {6.0,749.14, new Time(12,34,01)};
			plotit.plot(data14);
			Object[] data15 = {7.0,734.34, new Time(12,35,01)};
			plotit.plot(data15);
			Object[] data16 = {8.0,714.34, new Time(12,36,01)};
			plotit.plot(data16);
			plotit.logImprovement();
			plotit.close();
			
		}catch(Exception e){
			plotit.close();
			e.printStackTrace();
		}
	}
	
	public void startSession(String sheetName) throws Exception{
		s = wb.getSheet(sheetName);
		if(null==s)
			s = wb.createSheet(sheetName);
		else{
			throw new Exception("Session already exists!");
		}
		currentRun = 0;
	}
	public PlotIt(String[] headers,String[] dataTypes,String workBookName){
		this.headers = headers.clone();
		this.workBookName = workBookName;
		this.datatypes = dataTypes;
		this.cellTypes = new CellStyle[dataTypes.length];
		
	}
	public void close() {
		try{
			if(null!=out){
				wb.write(out);
				out.close();
			}
			//if(null!=in)
				//in.close();
			
		}catch(Exception e){
			e.printStackTrace();
		}
	}
	public void average() throws Exception{
		int headerRow = 0;
		
		Row r = null;
		Cell c = null;
		r = s.getRow(headerRow);
		for(int i = 0 ; i < headers.length ; i++){
			c = r.createCell((headers.length+buffer)*currentRun+i);
			c.setCellValue(headers[i]);
		}

		for(int l = 0 ; l < lastRow ; l++){
			r = s.getRow(l+1);
			if(null==r)
				r = s.createRow(l+1);
				
			for(int i = 0 ; i < datatypes.length ; i++){
				c = r.createCell((headers.length+buffer)*(currentRun)+i);
	            c.setCellStyle(cellTypes[i]);
	            if(datatypes[i].startsWith("0")){
	            	double sum = 0;
	            	for(int j = 0 ; j < currentRun ; j ++ ){
	            		//int k = (headers.length+buffer)*j+i;
	            		sum+=r.getCell((headers.length+buffer)*j+i).getNumericCellValue();
	            	}
	            	c.setCellValue((Double)sum/currentRun);
	            }
			}
			
		}
		
	}
	public void logImprovement() throws Exception{
		int headerRow = 0;
		
		Row r = null;
		Cell c = null;
		r = s.getRow(headerRow);
		for(int i = 0 ; i < headers.length ; i++){
			c = r.createCell((headers.length+buffer)*currentRun+i);
			c.setCellValue(headers[i]);
		}
		Row rnew = null ;
		Row rold = null; 
		for(int l = 0 ; l < lastRow -1 ; l++){
			rnew = s.getRow(l+2);
			if(null==rnew)
				rnew = s.createRow(l+2);
			
			rold = s.getRow(l+1);
			if(null==rold)
				rold = s.createRow(l+2);
			for(int i = 0 ; i < datatypes.length ; i++){
				c = rnew.createCell((headers.length+buffer)*(currentRun)+i);
	            c.setCellStyle(cellTypes[i]);
	            if(datatypes[i].equals("0.000")){
	            	double sum = 0;
	            	for(int j = 0 ; j < currentRun ; j ++ ){
	            		//int k = (headers.length+buffer)*j+i;
	            		double newCost = rnew.getCell((headers.length+buffer)*j+i).getNumericCellValue();
	            		double oldCost = rold.getCell((headers.length+buffer)*j+i).getNumericCellValue();
	            		sum+=Math.log1p((oldCost - newCost)*1000);
	            	}
	            	c.setCellValue((Double)sum/currentRun);
	            }
			}
			
		}
		
	}

	public void plot(Object[] data) throws IOException{
		Row r = null;
		Cell c = null;
		r = s.getRow(lastRow+1);
		if(null==r)
			r = s.createRow(lastRow+1);
			
		for(int i = 0 ; i < data.length ; i++){
			c = r.createCell((headers.length+buffer)*(currentRun-1)+i);
            c.setCellStyle(cellTypes[i]);
            if(datatypes[i].startsWith("0"))
            	c.setCellValue((Double)data[i]);
            else if(datatypes[i].equals("hh:mm:ss"))
            	c.setCellValue((Date)data[i]);
            else
            	c.setCellValue((String)data[i]);
		}
		lastRow++;
	}
	public void initialize() throws IOException{
		
		File directory = new File( "." );
		fileExists = false;
		for( File f : directory.listFiles() )
		{
		  if( f.getName().equals( workBookName ) )
		  {
			  fileExists = true;
		    break;
		  }
		}
		
		File workBookFile = new File(workBookName);
		if(!fileExists){
			workBookFile.createNewFile();
			wb = new HSSFWorkbook();
		}else{
			//in = ;
			wb = new HSSFWorkbook(new FileInputStream(workBookFile));
		}
		// create a new workbook
		out = new FileOutputStream(workBookName);
		
		DataFormat df = wb.createDataFormat();

		for(int i = 0 ; i < datatypes.length ; i++){
			cellTypes[i] = wb.createCellStyle();
			cellTypes[i].setDataFormat(df.getFormat(datatypes[i]));
		}
		
	}
	public void setRun(int run){
		// create headers
		int headerRow = 0;
		
		Row r = null;
		Cell c = null;
		if(run==1)
			r = s.createRow(headerRow);
		else
			r = s.getRow(headerRow);
		for(int i = 0 ; i < headers.length ; i++){
			c = r.createCell((headers.length+buffer)*currentRun+i);
			c.setCellValue(headers[i]);
		}
		currentRun = run;
		lastRow = 0;
	}
}
