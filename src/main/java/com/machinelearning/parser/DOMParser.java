package com.machinelearning.parser;

import java.io.File;
import java.io.IOException;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.select.Elements;

public class DOMParser {

	public String parseHTMLfromDOM(File input) throws IOException {

		Document doc = Jsoup.parse(input, "UTF-8");

		String content = "";

		//fetching elements in <table> tag
		Elements tableElements = doc.getElementsByTag("table");

		for (Element element : tableElements) {
			Node node = element.childNode(0).childNode(0);
			if (node.childNodes().size() == 1) {
				content += "\n" + node.childNode(0).childNode(0);
			}
		}

		//remove elements in <table> tag from the DOM
		doc.getElementsByTag("table").remove();

		Elements body = doc.getElementsByTag("body");

		Elements elements = body.get(0).children();

		// fetching heading from h1, h2, h3, h4, h5, h6 tags
		for (Element element : elements) {

			Elements hElements = element.getElementsByTag("h1");
			if (hElements.hasText()) {
				content += "\n" + hElements.text();
				hElements.remove();
			}

			hElements = element.getElementsByTag("h2");
			if (hElements.hasText()) {
				content += "\n" + hElements.text();
				hElements.remove();

			}

			hElements = element.getElementsByTag("h3");
			if (hElements.hasText()) {
				content += "\n" + hElements.text();
				hElements.remove();

			}

			hElements = element.getElementsByTag("h4");
			if (hElements.hasText()) {
				content += "\n" + hElements.text();
				hElements.remove();

			}

			hElements = element.getElementsByTag("h5");
			if (hElements.hasText()) {
				content += "\n" + hElements.text();
				hElements.remove();

			}

			hElements = element.getElementsByTag("h6");
			if (hElements.hasText()) {
				content += "\n" + hElements.text();
				hElements.remove();

			}

			// fetching heading from bold tags <p><b></b></p>
			Elements bElements = element.getElementsByTag("b");
			
			//tagName can be <p> or <pre>
			if (bElements.hasText() && bElements.parents().first().tagName().contains("p")) {

				int indexToRemove = -1;
				boolean foundAnELementToRemove = false;
				for (Node node : bElements.parents().first().childNodes()) {
					// System.out.println("\n*********\n"+node.toString());
					indexToRemove++;
					if (node.toString().equals(" ")) {
						foundAnELementToRemove = true;
						break;
					} else if (null == getText(node)) {
						foundAnELementToRemove = true;
						break;
					}
				}

				if (foundAnELementToRemove)
					bElements.parents().first().childNode(indexToRemove).remove();

				if (bElements.parents().size() > 0 && bElements.parents().first().childNodes().size() == 1) {
					Node node = bElements.parents().first().childNode(0);

					content += "\n" + getText(node);
					try{
					bElements.remove();
					}catch (Exception e) {
						// TODO: handle exception
					}
				}
			}

		}

		//fectching headings in capital and those with first letter capital 
		Elements capitalElements = doc.getElementsByTag("p");

		// fetch heading within <p></p> and no further child nodes. Also all
		// Capital letters checked
		for (Element element1 : capitalElements) {
			if (element1.childNodes().size() == 1) {
				Node node = element1.childNode(0);
				String nodeStrValue = getText(node);
				if (null != nodeStrValue) {
					if (nodeStrValue.equals(nodeStrValue.toUpperCase())) {
						content += "\n" + getText(node);
						element1.remove();
					} else if (nodeStrValue.substring(0, 1).equals(nodeStrValue.substring(0, 1).toUpperCase())) {
						content += "\n" + getText(node);
						element1.remove();

					}
				}
			}
		}

		return content;
	}

	/**return the node value in string with length >4 and <40 criteria or return null
	 * @param node
	 * @return
	 */
	public String getText(Node node) {
		String nodeForText = null;
		while (null != node.childNodes() && node.childNodes().size() > 0) {
			node = node.childNode(0);			
		}
		
		nodeForText = node.toString().trim().length() < 4 || node.toString().trim().length() > 40 ? null
					: node.toString().trim();
		
		return nodeForText;
	}
}
