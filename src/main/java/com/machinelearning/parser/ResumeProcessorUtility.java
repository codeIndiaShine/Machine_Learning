package com.machinelearning.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ResumeProcessorUtility {

	static ExecutorService executor = Executors.newFixedThreadPool(20);
	
	public static void main(String[] args) throws InterruptedException, ExecutionException, IOException {
		// TODO Auto-generated method stub
		

		 File unprocessedFolder = new File("E:\\Documents\\resume_unprocessed");

		 File[] files = null;
	        if (unprocessedFolder.isDirectory()) {
	    		files = unprocessedFolder.listFiles();
	    	}
		
		for (File file : files) {
			Runnable worker = new ParserThread(file);
			executor.execute(worker);
		}
		
		executor.shutdown();
		while (!executor.isTerminated()) {
		}
		
		Thread.sleep(4000);
		for(File file : files){
			try {
				Files.delete(Paths.get(file.toURI()));
			} catch (IOException e) {
				// TODO Auto-generated catch block
			}
		}
		
		System.out.println("Finished all threads");
	}
	
	
	/**to add a shutdown hook
	 * 
	 */
	public static void callShutdown(){
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		        executor.shutdown();
		        try {
					if (!executor.awaitTermination(120, TimeUnit.SECONDS)) { //optional *
					    System.out.println("Executor did not terminate in the specified time."); //optional *
					    List<Runnable> droppedTasks = executor.shutdownNow(); //optional **
					    System.out.println("Executor was abruptly shut down. " + droppedTasks.size() + " tasks will not be executed."); //optional **
					}
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		    }
		});
	}

}
