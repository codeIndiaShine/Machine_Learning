import re
from sklearn.neighbors import KNeighborsClassifier
from sklearn.feature_extraction.text import CountVectorizer
import numpy as np
import string
import glob
import json
import subprocess
import multithread_alt_pythonscript
import os
from multiprocessing.dummy import Pool

files=glob.glob("E:\\Documents\\Processed_txt_resume\\parsedHTML*.txt")



def script(filename):
	
	file=open(filename,'r')
	filename=filename[44:]
	filename=filename[:-4]
	
	data=file.read().splitlines()
	file.close()
	os.remove("E:\\Documents\\Processed_txt_resume\\parsedHTML"+filename+".txt")


	corpus=[]
	y=[]
	raw_corpus=[]
	training_files=glob.glob(r"E:\\Documents\\python\\training_data\\*.txt")
	for training_file in training_files:
		training_fp=open(training_file,'r')
		training_file=training_file[37:]
		training_file=training_file[:-4]
		for line in training_fp:
			raw_corpus.append(line)
			y.append(training_file)
		training_fp.close()
		
		
	
	for i in raw_corpus:
		i=i.rstrip("\n")
		corpus.append(i)
	
	heading_category=[]
	headings=[]

	bv=CountVectorizer(analyzer='char_wb', ngram_range=(1,10))
	x=bv.fit_transform(corpus).toarray()
	

	neigh=KNeighborsClassifier(n_neighbors=3)
	neigh.fit(x,y)
	
	fp=open(r"E:\\Documents\\python_output\\categorized_resume_"+filename+".txt","w")
	str=""
	flag=0
	flag2=0
	for i in data:
		test=bv.transform([i]).toarray()
		a=neigh.kneighbors(test)
		flag=0
		for j in a[0]:
			for k in j:
				if k < 4.6:
					flag=1
			
			
		
			
		
			
		if flag==1:
			headings.append(i)
			temp=np.array_str(neigh.predict(test))
			temp=temp.lstrip("['")
			temp=temp.rstrip("']")
			training_fp=open(r"E:\\Documents\\python\\training_data\\"+temp+".txt","a+")
			training_fp.write("\n")
			training_fp.write(i)
			training_fp.close()
			heading_category.append(temp)
			str+="$$<heading>"
			str+=i+np.array_str(neigh.predict(test))
			str+="$$<content>"
		elif 3<len(i)<31:
			
			z=i
			i=i.split(" ")
			test1=bv.transform(i).toarray()
			c=neigh.kneighbors(test1)
			flag2=0
			for j in c[0]:
				for k in j:
					if k < 4.6:
						flag2=1
		
		
			if flag2==1:
				headings.append(z)
				temp=np.array_str(neigh.predict(test))
				temp=temp.lstrip("['")
				temp=temp.rstrip("']")
				training_fp=open(r"E:\\Documents\\python\\training_data\\"+temp+".txt","a+")
				training_fp.write("\n")
				training_fp.write(z)
				training_fp.close()
				heading_category.append(temp)
				str+="$$<heading>\n"
				str+=z+np.array_str(neigh.predict(test))
				str+="$$<content>\n"
			else:
				str+=z+"\n"
		else:
			str+=i+"\n"
		
			
	
	
	
	i=0
	counter=counter1=counter2=0
	for l in heading_category:
		if "skill_sets" in l:
			counter1=1
		if "educational_details" in l:
			counter2=1
		if "professional_experience" or "career_highlights_and_objectives" in l:
			counter=1
	
	if (counter+counter1+counter2)<3:
		print("From other script")
		print(heading_category)
		print(filename)
		fp.close()
		
		multithread_alt_pythonscript.alt_script(filename)
	else:
		json_formatting={}
		key='Resume name'
		value=filename
		fp1=open(r"E:\\Documents\\Processed_txt_resume\\resumeContentinTxt"+filename+".txt","r")
	
		raw_resume=fp1.readlines()
		num_lines=len(raw_resume)
	
		while(i < num_lines):
			k=raw_resume[i]
			flag3=0
		
			for l in headings:
				if k.find(l) !=-1:
					li=l
					flag3=1
					break
		
			if flag3==1:
				json_formatting.update({key:value})
				value=''
				j=headings.index(li)
			
				key=heading_category[j]
				if key in json_formatting.keys():
					value=json_formatting[key]
			
			else:
			
				value+=raw_resume[i]
			
		
			i=i+1
	
		json_formatting.update({key:value})
		print("Done")

	
		fp.write(json.dumps(json_formatting))
	
	
	
		fp.close()
		fp1.close()
		os.remove("E:\\Documents\\Processed_txt_resume\\resumeContentinTxt"+filename+".txt")
		
	
	
test_pool = Pool(10)
test_pool.map(script,files)
test_pool.close()
test_pool.join()
