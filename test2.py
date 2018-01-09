from sklearn.neighbors import KNeighborsClassifier
from sklearn.feature_extraction.text import CountVectorizer

#dataset
corpus=['Skill','skills','Competency','educate','education']
print("Dataaset")
print(corpus)

#extracting features of datasets
bv=CountVectorizer(analyzer='char_wb', ngram_range=(1,5))
x=bv.fit_transform(corpus).toarray()

y=[1,1,1,0,0]
print("Classes assigned manually")
print(y)


#assigning y as a class
neigh=KNeighborsClassifier(n_neighbors=3)
neigh.fit(x,y)

#testing data
test_data=['Educational Background','Skill sets','competencies']
print("Testing data")
print(test_data)

#feature extraction of test data
test=bv.transform(test_data).toarray()

#predicting class of the test data 
print("Assigned class to the testing data")
print(neigh.predict(test))

input("\nPress Enter to exit...")