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
	alt_file.close()
	
	alt_corpus=[]
	alt_y=[]
	alt_raw_corpus=[]
	alt_training_files=glob.glob(r"E:\\Documents\\python\\training_data\\*.txt")
	for alt_training_file in alt_training_files:
		alt_training_fp=open(alt_training_file,'r')
		alt_training_file=alt_training_file[37:]
		alt_training_file=alt_training_file[:-4]
		for alt_line in alt_training_fp:
			alt_raw_corpus.append(alt_line)
			alt_y.append(alt_training_file)
		alt_training_fp.close()
		

	
	for alt_i in alt_raw_corpus:
		alt_i=alt_i.rstrip("\n")
		alt_corpus.append(alt_i)
	
	alt_heading_category=[]
	alt_headings=[]
	
	
	
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
			alt_headings.append(alt_i)
			alt_temp=np.array_str(alt_neigh.predict(alt_test))
			alt_temp=alt_temp.lstrip("['")
			alt_temp=alt_temp.rstrip("\\n']")
			alt_training_fp=open(r"E:\\Documents\\python\\training_data\\"+alt_temp+".txt","a+")
			alt_training_fp.write("\n")
			alt_training_fp.write(alt_i)
			alt_training_fp.close()
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
				alt_headings.append(alt_z)
				alt_temp=np.array_str(alt_neigh.predict(alt_test))
				alt_temp=alt_temp.lstrip("['")
				alt_temp=alt_temp.rstrip("\\n']")
				alt_training_fp=open(r"E:\\Documents\\python\\training_data\\"+alt_temp+".txt","a+")
				alt_training_fp.write("\n")
				alt_training_fp.write(alt_z)
				alt_training_fp.close()
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
	os.remove("E:\\Documents\\Processed_txt_resume\\resumeContentinTxt"+filename+".txt")
	
	

