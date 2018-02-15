import re
from sklearn.neighbors import KNeighborsClassifier
from sklearn.feature_extraction.text import CountVectorizer
import numpy as np
import string


file=open("E:\\Documents\\Processed_txt_resume\\resumeContentinTxtRananjaySingh.pdf.txt",'r')

data=file.read().splitlines()



corpus=['Skill',
'skills',
'Competency',
'educate',
'education',
'Academics Qualification',
'Academical Qualification',
'Experience',
'job experiences',
'Experience',
'Experiences',
'Industrial Experience',
'hobby',
'hobbies',
'Career Objective',
'Careers objects',
'strengths',
'strengths',
'Responsibilities',
'Responsibilities',
'Responsibility',
'internship',
'internships',
'extra-curricular activity',
'extra-curricualtion',
'extra curricular activities',
'personal detail',
'personal data',
'achievments',
'achievement',
'technologies',
'technology',
'technologies',
'Projects',
'Project',
'Ongoing Projects'
]

bv=CountVectorizer(analyzer='char_wb', ngram_range=(1,6))
x=bv.fit_transform(corpus).toarray()
y=['skill class',
'skill class',
'skill class',
'Education class',
'Education class',
'Education class',
'Education class',
'Experience class',
'Experience class',
'Experience class',
'Experience class',
'Experience class',
'Hobby class',
'Hobby class',
'career class',
'career class',
'strength class',
'strength class',
'Responsibility class',
'Responsibility class',
'Responsibility class',
'Experience class',
'Experience class',
'cca class',
'cca class',
'cca class',
'personal detail',
'personal detail',
'achievements class',
'achievements class',
'Tech class',
'Tech class',
'Tech class',
'Proj class',
'Proj class',
'Proj class'
]

neigh=KNeighborsClassifier(n_neighbors=3)
neigh.fit(x,y)

counter=4
fp=open(r"E:\\Documents\\python_output\\"+str(counter)+".txt","a")
str=""
for i in data:
	test=bv.transform([i]).toarray()
	a=neigh.kneighbors(test)
	flag=0
	for j in a[0]:
		for k in j:
			if k<4.6:
				flag=1
			
		
	

	
	if flag==1:
		print(i)
		print(neigh.predict(test))
		str+="$$<heading>"
		str+=i+np.array_str(neigh.predict(test))
		str+="$$<content>"
	else:
		str+=i
	
		

print(str)
input()
b=str.split("$$")
for i in b:
	if "heading" in i:
		fp.write("Heading: ")
		i=i.lstrip("<heading>")
		fp.write(i)
		fp.write("\n")
	elif "content" in i:
		fp.write("Content: ")
		i=i.lstrip("<content>")
		fp.write(i)
		fp.write("\n")
	else:
		fp.write("Overview")
		fp.write(i)
		fp.write("\n")

		



fp.close()
file.close()
input()

