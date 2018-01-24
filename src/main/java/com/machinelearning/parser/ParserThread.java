package com.machinelearning.parser;

import java.io.File;
import java.io.IOException;

import org.apache.tika.exception.TikaException;
import org.xml.sax.SAXException;

public class ParserThread implements Runnable {
	  
    private File file;
    
    public ParserThread(File file){
        this.file = file;
    }

    @Override
    public void run() {
        System.out.println(Thread.currentThread().getName()+" Start. Command = "+file);
        try {
			new CustomParser().parseDocumentCustomTika(file);
			//new CustomParser().parseDocumentDefaultTika(file);
		} catch (IOException | SAXException | TikaException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
        System.out.println(Thread.currentThread().getName()+" End.");
    }

    @Override
    public String toString(){
        return this.file.getName();
    }
}
