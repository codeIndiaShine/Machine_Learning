package com.machinelearning.parser;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class ResumeProcessorUtility {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		ExecutorService executor = Executors.newFixedThreadPool(10);
		
		for(File file : new CustomParser().getFileList()){
			Runnable worker = new ParserThread(file);
            executor.execute(worker);
		}
        executor.shutdown();
        while (!executor.isTerminated()) {
        }
        System.out.println("Finished all threads");
	}

}
