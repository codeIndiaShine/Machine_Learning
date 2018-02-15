import re
from sklearn.neighbors import KNeighborsClassifier
from sklearn.feature_extraction.text import CountVectorizer
import numpy as np
import string
import glob
import json
import os



def alt_script(filename):

	alt_file=open(r"E:\\Documents\\Processed_txt_resume\\resumeContentinTxt"+filename+".txt","r")
	
	alt_data=alt_file.read().splitlines()
	
	alt_corpus=[]
	alt_f= open(r"E:\\Documents\\python\\train.txt","a+")
	alt_f.seek(0)
	alt_raw_corpus=alt_f.readlines()
	for alt_i in alt_raw_corpus:
		alt_i=alt_i.rstrip("\n")
		alt_corpus.append(alt_i)
	
	alt_heading_category=[]
	alt_headings=[]
	
	alt_f1= open(r"E:\\Documents\\python\\classes.txt","a+")
	alt_f1.seek(0)
	alt_y = alt_f1.readlines()
	
	
	alt_bv=CountVectorizer(analyzer='char_wb', ngram_range=(1,10))
	alt_x=alt_bv.fit_transform(alt_corpus).toarray()
	
	
	alt_neigh=KNeighborsClassifier(n_neighbors=3)
	alt_neigh.fit(alt_x,alt_y)

	
	alt_fp=open(r"E:\\Documents\\python_output\\categorized_resume_"+filename+".txt","w")
	alt_str=""
	alt_flag=0
	alt_flag2=0
	for alt_i in alt_data:
		alt_test=alt_bv.transform([alt_i]).toarray()
		alt_a=alt_neigh.kneighbors(alt_test)
		alt_flag=0
		for alt_j in alt_a[0]:
			for alt_k in alt_j:
				if alt_k < 4.6:
					alt_flag=1
		
		
		if alt_flag==1:
			alt_f.write("\n")
			alt_f.write(alt_i)
			alt_headings.append(alt_i)
			alt_f1.write("\n")
			alt_temp=np.array_str(alt_neigh.predict(alt_test))
			alt_temp=alt_temp.lstrip("['")
			alt_temp=alt_temp.rstrip("\\n']")
			alt_f1.write(alt_temp)
			alt_heading_category.append(alt_temp)
			alt_str+="$$<heading>"
			alt_str+=alt_i+np.array_str(alt_neigh.predict(alt_test))
			alt_str+="$$<content>"
		elif 3<len(alt_i)<31:
			alt_z=alt_i
			alt_i=alt_i.split(" ")
			alt_test1=alt_bv.transform(alt_i).toarray()
			alt_c=alt_neigh.kneighbors(alt_test1)
			alt_flag2=0
			for alt_j in alt_c[0]:
				for alt_k in alt_j:
					if alt_k < 4.6:
						alt_flag2=1
			
			
			if alt_flag2==1:
				alt_f.write("\n")
				alt_f.write(alt_z)
				alt_headings.append(alt_z)
				alt_f1.write("\n")
				alt_temp=np.array_str(alt_neigh.predict(alt_test))
				alt_temp=alt_temp.lstrip("['")
				alt_temp=alt_temp.rstrip("\\n']")
				alt_f1.write(alt_temp)
				alt_heading_category.append(alt_temp)
				alt_str+="$$<heading>\n"
				alt_str+=alt_z+np.array_str(alt_neigh.predict(alt_test))
				alt_str+="$$<content>\n"
			else:
				alt_str+=alt_z+"\n"
		else:
			alt_str+=alt_i+"\n"
		
			
	
	print("done")
	alt_json_formatting={}
	alt_key='Resume name'
	alt_value=filename
	alt_json_formatting.update({alt_key:alt_value})
	alt_key="Outline"
	alt_value=""
	alt_b=alt_str.split("$$")
	for alt_i in alt_b:
		if "heading" in alt_i:
			alt_json_formatting.update({alt_key:alt_value})
			alt_value=''
			for alt_l in alt_headings:
				if alt_l in alt_i:
					alt_j=alt_headings.index(alt_l)
					alt_key=alt_heading_category[alt_j]
					if alt_key in alt_json_formatting.keys():
						alt_value=alt_json_formatting[alt_key]
			
		elif "content" in alt_i:
			alt_i=alt_i.lstrip("<content>")
			alt_value+=alt_i
		else:
			alt_value+=alt_i
			
			
	alt_json_formatting.update({alt_key:alt_value})	
		

	alt_fp.write(json.dumps(alt_json_formatting))
	alt_fp.close()
	alt_file.close()
	alt_f.close()
	alt_f1.close()
	os.remove("E:\\Documents\\Processed_txt_resume\\resumeContentinTxt"+filename+".txt")
	os.remove("E:\\Documents\\Processed_txt_resume\\parsedHTML"+filename+".txt")
	

