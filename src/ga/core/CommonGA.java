package ga.core;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.Properties;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import poi.cvrp.PlotIt;
/**
 * @author Debanjan Bhattacharyya <b.debanjan@gmail.com>
 * This is the common class for any "main" class running a GA solution.
 * Copyright 2013 Academic Free License 3.0
 */
public abstract class CommonGA {
	public static final Logger logger = Logger.getLogger(CommonGA.class.getCanonicalName());
	//public static Logger plotlogger = Logger.getLogger("PlotLogger");

	protected double fractionOfPopulationToDie = 0.5;
	protected double probabilityToCrossOver = 0.7;
	protected double probabilityToMutate = 0.03;
	protected int maxIterations = 15;
	protected int individualChromosomeLength = 20;
	protected int runningPopulationSize = 10;
	protected int optimailFitnessValue = 0;
	protected String mutationNM;
	protected String mutationClassName;
	protected String crossoverNM;
	protected String crossoverClassName;
	protected String selectorNM;
	protected String selectorClassName;
	protected String optimizeNM;
	protected int optimizeOnReset;
	protected int skipSelectionOnReset;
	public static int fourChild;
	protected int resetOnIterations;
	protected static String runIdentifier = "";
	protected PlotIt plotit = null;
	public void setUpLogging()throws Exception{
		Handler fhandler = new FileHandler("logs/" + runIdentifier+".log",true);
		fhandler.setFormatter(new SimpleFormatter());
		fhandler.setLevel(java.util.logging.Level.FINEST);
		logger.setLevel(java.util.logging.Level.FINEST);
		//Logger.global.addHandler(fhandler);
		for (Handler handler : Logger.getLogger("").getHandlers())
			handler.setLevel(Level.SEVERE);
		for (Handler handler : logger.getHandlers())
			logger.removeHandler(handler);

		logger.addHandler(fhandler);
		
	}	
	public void readProperties(String filePath)throws Exception{
		Properties propReader = new Properties();
		InputStream fis = new FileInputStream(new File("properties/" + filePath));
		propReader.load(fis);
		//logger.info(propReader.entrySet().toString());
		probabilityToMutate = Double.valueOf(propReader.getProperty("probabilityToMutate"));
		if(probabilityToMutate>1||probabilityToMutate<0)
				throw new Exception("Invalid value for probabilityToMutate");
		maxIterations = Integer.valueOf(propReader.getProperty("maxIterations"));
		if(maxIterations<0)
			throw new Exception("Invalid value for maxIterations");
		individualChromosomeLength = Integer.valueOf(propReader.getProperty("individualChromosomeLength"));
		if(individualChromosomeLength<0)
			throw new Exception("Invalid value for individualChromosomeLength");
		runningPopulationSize = Integer.valueOf(propReader.getProperty("runningPopulationSize"));
		if(runningPopulationSize<0)
			throw new Exception("Invalid value for runningPopulationSize");
		fractionOfPopulationToDie = Double.valueOf(propReader.getProperty("fractionOfPopulationToDie"));
		if(fractionOfPopulationToDie<0||fractionOfPopulationToDie>1)
			throw new Exception("Invalid value for fractionOfPopulationToDie");
		probabilityToCrossOver = Double.valueOf(propReader.getProperty("probabilityToCrossOver"));
		if(probabilityToCrossOver<0||probabilityToCrossOver>1)
			throw new Exception("Invalid value for probabilityToCrossOver");
		optimailFitnessValue = Integer.valueOf(propReader.getProperty("optimailFitness"));
		if(optimailFitnessValue<0)
			throw new Exception("Invalid value for optimailFitnessValue");


		mutationNM = propReader.getProperty("mutationNM");
		if(mutationNM.equals(""))
			throw new Exception("Invalid value for mutationNM");

		mutationClassName = propReader.getProperty("mutationClassName");
		if(mutationClassName.equals(""))
			throw new Exception("Invalid value for mutationClass");

		crossoverNM = propReader.getProperty("crossoverNM");
		if(crossoverNM.equals(""))
			throw new Exception("Invalid value for crossoverNM");

		crossoverClassName = propReader.getProperty("crossoverClassName");
		if(crossoverClassName.equals(""))
			throw new Exception("Invalid value for crossoverClass");

		selectorNM = propReader.getProperty("selectorNM");
		if(selectorNM.equals(""))
			throw new Exception("Invalid value for selectorNM");

		selectorClassName = propReader.getProperty("selectorClassName");
		if(selectorClassName.equals(""))
			throw new Exception("Invalid value for selectorClass");
		
		//optimizeNM = propReader.getProperty("optimizeNM");
		//if(optimizeNM.equals(""))
			//throw new Exception("Invalid value for optimizeNM");

		resetOnIterations = Integer.valueOf(propReader.getProperty("resetOnIterations"));
		if(resetOnIterations<-1 || resetOnIterations==0)
			throw new Exception("Invalid value for resetOnIterations");
		
		optimizeOnReset = Integer.valueOf(propReader.getProperty("optimizeOnReset"));
		if(optimizeOnReset!=0 && optimizeOnReset!=1)
			throw new Exception("Invalid value for optimizeOnReset");

		skipSelectionOnReset = Integer.valueOf(propReader.getProperty("skipSelectionOnReset"));
		if(skipSelectionOnReset!=0 && skipSelectionOnReset!=1)
			throw new Exception("Invalid value for skipSelectionOnReset");
		
				
		fourChild = Integer.valueOf(propReader.getProperty("fourChild"));
		if(fourChild!=0 && fourChild!=1)
			throw new Exception("Invalid value for fourChild");

		runIdentifier = "Run " 
				+ "-M " + mutationNM + " -C " 
				+ crossoverNM + " -S " 
				+ selectorNM + " -OR " 
				+ (optimizeOnReset==0?"F":"T")  + " -MI " 
				+ maxIterations + " -PC " 
				+ probabilityToCrossOver + " -PM " 
				+ probabilityToMutate + " -RI " 
				+ resetOnIterations + " -FC"
				+ (fourChild==0?" F":" T");
		
		if(null!=plotit)
			plotit.startSession(filePath);

		//plotlogger = Logger.getLogger("PlotLogger_" + System.currentTimeMillis() + "_" +  runIdentifier);
		
		//Handler fhandler = new FileHandler("PlotLogger_" + System.currentTimeMillis() + "_" +  runIdentifier);
		//fhandler.setFormatter(new SimpleFormatter());
		//fhandler.setLevel(java.util.logging.Level.FINEST);
		//plotlogger.setLevel(java.util.logging.Level.FINEST);
		//plotlogger.addHandler(fhandler);
		
	}
	abstract protected void doGA() throws ClassNotFoundException, SecurityException, NoSuchMethodException, IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException;
	
}
