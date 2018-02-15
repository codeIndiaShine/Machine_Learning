package com.machinelearning.parser;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

public class ParserThread implements Runnable {

	File[] files = null;
	File file = null;

	public ParserThread(File file) {
		this.file = file;
	}

	@Override
	public void run() {

		if (file != null) {
			System.out.println(Thread.currentThread().getName() + " Start. Command = " + file);
			try {
				try {
					Files.copy(Paths.get(file.toURI()),
							Paths.get(new File("E:\\Documents\\resume_processed\\" + file.getName()).toURI()),
							StandardCopyOption.COPY_ATTRIBUTES);
				} catch (IOException e) {
					//
				}
				new CustomParser().parseDocumentCustomTika(file);
				new CustomParser().parseDocumentDefaultTika(file);

			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				file = null;
				files = null;

			}
			System.out.println(Thread.currentThread().getName() + " End.");
		}
	}

}
