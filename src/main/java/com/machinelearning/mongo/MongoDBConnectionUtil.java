package com.machinelearning.mongo;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.concurrent.ThreadLocalRandom;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.parser.ParseContext;
import org.apache.tika.parser.Parser;
import org.apache.tika.sax.BodyContentHandler;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.xml.sax.SAXException;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;

public class MongoDBConnectionUtil {

	DB db = null;
	File jsonFolder = null;
	DBObject document = null;
	DBCollection tablex = null;

	/**
	 * @throws UnknownHostException
	 */
	public MongoDBConnectionUtil() throws UnknownHostException {
		MongoClient mongo = new MongoClient("localhost", 27017);

		db = mongo.getDB("local");

		jsonFolder = new File("E:\\Documents\\python_output");

		// specifies the collection
		tablex = db.getCollection("test_collection");
	}

	/**
	 * insert data into the collection
	 * 
	 * @param args
	 * @throws IOException
	 * @throws SAXException
	 * @throws TikaException
	 * @throws ParseException
	 */
	public void insertJSONtoDB() throws IOException, SAXException, TikaException, ParseException {

		for (File jsonFile : jsonFolder.listFiles()) {
			if (jsonFile.length() > 0) {
				FileInputStream inputstream = new FileInputStream(jsonFile);
				Parser parser = new AutoDetectParser();

				BodyContentHandler handler = new BodyContentHandler();
				Metadata metadata = new Metadata();

				ParseContext context = new ParseContext();
				parser.parse(inputstream, handler, metadata, context);

				document = new BasicDBObject();

				document.put("capped", false);
				document.put("autoIndexId", true);
				document.put("filepath", jsonFile.getPath());

				int randomNum = ThreadLocalRandom.current().nextInt(1, 99999 + 1);
				// document.put("max", 10);

				document.put("documentId", randomNum);
				JSONParser jsonParser = new JSONParser();
				try {
					JSONObject jsonObject = (JSONObject) jsonParser.parse(handler.toString());

					HashMap<String, Object> jsonMap = (HashMap<String, Object>) jsonObject.clone();

					document.putAll(jsonMap);
					tablex.insert(document);

					jsonFile.deleteOnExit();
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}finally {
					inputstream.close();
				}
			}
		}
	}

	/**
	 * get All data from the collection
	 * 
	 * @return
	 */
	public DBObject getAllDataFromCollection() {
		DBCursor cursor = tablex.find();

		while (cursor.hasNext()) {
			document = cursor.next();
		}
		return document;
	}

	public static void main(String[] args) {
		try {
			new MongoDBConnectionUtil().insertJSONtoDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
