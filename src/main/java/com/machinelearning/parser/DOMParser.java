package com.machinelearning.parser;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class DOMParser {

	public String parseHTMLfromDOM(File input) throws IOException {

		Document doc = Jsoup.parse(input, "UTF-8");

		String content = "\n**************************************************";
		
		Elements capitalElements = doc.getElementsByTag("p");
		
		//fecth heading within <p></p> and no further child nodes. Also all Capital letters checked  
		for (Element element : capitalElements) {
			if(element.childNodes().size()==1){
				Node node = element.childNode(0);
				if(node.toString().equals(node.toString().toUpperCase())){
					content += "\n" + node.toString();
				}
			}
		}
		
		Elements tableElements = doc.getElementsByTag("table");
		
		for (Element element : tableElements) {
			Node node = element.childNode(0).childNode(0);
			if(node.childNodes().size()==1){
				content += "\n" + node.childNode(0).childNode(0);
			}
		}
		
		doc.getElementsByTag("table").remove();

		Elements body = doc.getElementsByTag("body");
		List<Element> elementList = new ArrayList<>();

		Elements elements = body.get(0).children();

		//fetching heading from h1, h2, h3, h4, h5, h6 tags
		for (Element element : elements) {

			Elements bElements = element.getElementsByTag("h1");
			if (bElements.hasText()){
				content += "\n" + element;
			}
			
			bElements = element.getElementsByTag("h2");
			if (bElements.hasText()){
				content += "\n" + element;
			}
			
			bElements = element.getElementsByTag("h3");
			if (bElements.hasText()){
				content += "\n" + element;
			}
			
			bElements = element.getElementsByTag("h4");
			if (bElements.hasText()){
				content += "\n" + element;
			}
			
			bElements = element.getElementsByTag("h5");
			if (bElements.hasText()){
				content += "\n" + element;
			}
			
			bElements = element.getElementsByTag("h6");
			if (bElements.hasText()){
				content += "\n" + element;
			}
			
			//fetching heading from bold tags <p><b></b></p>
			bElements = element.getElementsByTag("b");			
			if (bElements.hasText() && bElements.parents().first().tagName().equals("p")) {
				
				int	indexToRemove = -1;
				boolean foundAnELementToRemove = false;
				for(Node node : bElements.parents().first().childNodes()){
					//System.out.println("\n*********\n"+node.toString());
					indexToRemove++;
					if(node.toString().equals(" ")){
						foundAnELementToRemove = true;
						break;
					}
				}
				
				if(foundAnELementToRemove)
				bElements.parents().first().childNode(indexToRemove).remove();
				
				if (bElements.parents().first().childNodes().size() == 1) {
					content += "\n" + element;
					
					elementList.add(element);
				}
			}
			
		}
		
		return content;
	}
}
