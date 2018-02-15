package com.machinelearning.parser;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.rendering.PDFRenderer;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.parser.pdf.PDFParser;
import org.apache.tika.parser.pdf.PDFParserConfig;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;

public class CustomParser {

	
	private String returnFileType(File file) throws IOException {
		Tika tika = new Tika();

		String documentType = tika.detect(file);

		return documentType;
	}

	/**
	 * Method to perform segmentation and OCR on all file types
	 * 
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException
	 * @throws TesseractException
	 */
	public void parseDocumentDefaultTika(File file)
			throws IOException, SAXException, TikaException, TesseractException {

		Parser parser = new AutoDetectParser();

		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(file);

		ParseContext context = new ParseContext();
		
		parser.parse(inputstream, handler, metadata, context);
		
		inputstream.close();
		File newMetaDataFile = new File("E:\\Documents\\metadata\\metadata" + file.getName() + ".txt");
		FileOutputStream fos = new FileOutputStream(newMetaDataFile);
		for (String name : metadata.names()) {
			String str = name + " : " + metadata.get(name);

			fos.write(str.getBytes());
		}
		fos.close();

		String content = null;
		if (new CustomParser().returnFileType(file).contains("pdf")) {
			/*parser = new PDFParser();

			new PropertyReader("pdfParser.properties", "/parserconfiguration/");

			FileInputStream fs = PropertyReader.in;
			PDFParserConfig pdfParserConfig = new PDFParserConfig(fs);

			((PDFParser) parser).setPDFParserConfig(pdfParserConfig);*/

			Tesseract tesseract = new Tesseract();
			tesseract.setLanguage("eng");
			tesseract.setPageSegMode(2);
			tesseract.setOcrEngineMode(2);
			//tesseract.setHocr(true);
			
			
			PDDocument document = PDDocument.load(file);
			int noOfPages = document.getNumberOfPages();

			int i = 0;
			while (i < noOfPages) {
				showImageWithSegments(i, tesseract, document, file);
				i++;
			}

			document.close();
			content = tesseract.doOCR(file);

			//fs.close();
		} else {
			content = handler.toString();
		}

		File newContentFile = new File(
				"E:\\Documents\\Processed_txt_resume\\resumeContentinTxt" + file.getName() + ".txt");
		FileOutputStream fosContent = new FileOutputStream(newContentFile);

		String str = content;

		fosContent.write(str.getBytes());

		fosContent.close();
		
	}

	/**
	 * method to parse word document and give output in both HTML and string
	 * format
	 * 
	 * @param file
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException
	 */
	public void parseDocumentCustomTika(File file) throws IOException, SAXException, TikaException {
		ContentHandlerCustom contentHandlerCustom = new ContentHandlerCustom(file);
		String content = null;

		File newContentFile = null;
		
		FileOutputStream fosContent = null;
		String str = content;

		content = contentHandlerCustom.parseToHTML();
		

		newContentFile = new File("E:\\Documents\\Processed_txt_resume\\parseToHTML" + file.getName() + ".html");
		fosContent = new FileOutputStream(newContentFile);

		str = content.replaceAll("\t", "");;

		fosContent.write(str.getBytes());

		fosContent.close();
		
		content = new DOMParser().parseHTMLfromDOM(newContentFile);
		
		newContentFile = new File("E:\\Documents\\Processed_txt_resume\\parsedHTML" + file.getName() + ".txt");
		fosContent = new FileOutputStream(newContentFile);

		str = content;

		fosContent.write(str.getBytes());

		fosContent.close();
	}

	/**
	 * This method will convert every page into image with highlighted segments.
	 * 
	 * @param pageNumber
	 * @param tesseract
	 * @param document
	 * @throws IOException
	 * @throws TesseractException
	 */
	public void showImageWithSegments(int pageNumber, Tesseract tesseract, PDDocument document, File file)
			throws IOException, TesseractException {
		PDFRenderer renderer = new PDFRenderer(document);
		BufferedImage image = renderer.renderImage(pageNumber);

		List<Rectangle> segmentList = tesseract.getSegmentedRegions(image, 0);

		for (Rectangle rect : segmentList) {
			Graphics2D g2D = image.createGraphics();
			g2D.setColor(Color.BLACK);
			g2D.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());

		}

		ImageIO.write(image, "JPEG", new File("E:\\Documents\\segmentedImages\\sampleResumePDFtoImage" + pageNumber
				+ "+" + file.getName() + ".JPEG"));
	}
}
