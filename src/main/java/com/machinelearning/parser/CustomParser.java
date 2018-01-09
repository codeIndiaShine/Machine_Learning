package main.java.com.machinelearning.parser;

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

	static File file = new File("E:\\Documents\\sampleResume.pdf");

	private String returnFileType() throws IOException {
		Tika tika = new Tika();

		String documentType = tika.detect(file);

		System.out.println("\ndocumentType :" + documentType);
		return documentType;
	}

	/**Method to perform segmentation and OCR on all file types
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException
	 * @throws TesseractException
	 */
	private void parseDocument() throws IOException, SAXException, TikaException, TesseractException {

		Parser parser = new AutoDetectParser();

		BodyContentHandler handler = new BodyContentHandler();
		Metadata metadata = new Metadata();
		FileInputStream inputstream = new FileInputStream(file);

		ParseContext context = new ParseContext();
		parser.parse(inputstream, handler, metadata, context);
		System.out.println("\nmetadata :" + metadata);

		File newMetaDataFile = new File("E:\\Documents\\metadata.txt");
		FileOutputStream fos = new FileOutputStream(newMetaDataFile);
		for (String name : metadata.names()) {
			System.out.println(name + " : " + metadata.get(name));
			String str = name + " : " + metadata.get(name);

			fos.write(str.getBytes());
		}
		fos.close();

		if (new CustomParser().returnFileType().contains("pdf")) {
			parser = new PDFParser();

			
			new PropertyReader("pdfParser.properties", "/parserconfiguration");
			
			FileInputStream fs = PropertyReader.in;
			PDFParserConfig pdfParserConfig = new PDFParserConfig(fs);

			((PDFParser) parser).setPDFParserConfig(pdfParserConfig);

			Tesseract tesseract = new Tesseract();
			tesseract.setLanguage("eng");
			tesseract.setPageSegMode(2);
			tesseract.setOcrEngineMode(2);

			PDDocument document = PDDocument.load(file);
			int noOfPages = document.getNumberOfPages();

			
			int i =0;
			while (i < noOfPages) {
				showImageWithSegments(i,tesseract,document);
				i++;
			}
			
			document.close();
			String content = tesseract.doOCR(file);

			File newContentFile = new File("E:\\Documents\\resumeContentinTxt.txt");
			FileOutputStream fosContent = new FileOutputStream(newContentFile);

			//System.out.println("\ncontent from tesseract :" + content);
			String str = content;

			fosContent.write(str.getBytes());

			fosContent.close();
		} else {

			System.out.println("\ncontent :" + handler.toString());
		}
	}

	public static void main(String[] args) throws IOException, SAXException, TikaException, TesseractException {
		// TODO Auto-generated method stub
		new CustomParser().parseDocument();
	}

	/**
	 * This method will convert every page into image with highlighted segments.
	 * @param pageNumber
	 * @param tesseract
	 * @param document
	 * @throws IOException
	 * @throws TesseractException
	 */
	public void showImageWithSegments(int pageNumber, Tesseract tesseract, PDDocument document) throws IOException, TesseractException{
		PDFRenderer renderer = new PDFRenderer(document);
		BufferedImage image = renderer.renderImage(pageNumber);

		List<Rectangle> segmentList = tesseract.getSegmentedRegions(image, 0);

		for (Rectangle rect : segmentList) {
			Graphics2D g2D = image.createGraphics();
			g2D.setColor(Color.BLACK);
			g2D.drawRect((int) rect.getX(), (int) rect.getY(), (int) rect.getWidth(), (int) rect.getHeight());

		}
		
		System.out.println(segmentList.size());
		ImageIO.write(image, "JPEG",
				new File("E:\\Documents\\sampleResumePDFtoImage"+pageNumber+".JPEG"));

		
		
	
	}
}
