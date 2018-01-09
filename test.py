from sklearn.neighbors import KNeighborsClassifier
from sklearn.feature_extraction.text import CountVectorizer
from sklearn.cluster import KMeans

#dataset
corpus=['Skill','skills','educate','education']
print("Dataaset")
print(corpus)

#extracting features of datasets
bv=CountVectorizer(analyzer='char_wb', ngram_range=(1,5))
x=bv.fit_transform(corpus).toarray()

#clustering dataset
km = KMeans(n_clusters=2, init='k-means++', max_iter=100, n_init=1)
km.fit(x)
km.predict(x)
print("Assigned clusters to datasets")
print(km.labels_)

#assigning clusters as a class
neigh=KNeighborsClassifier(n_neighbors=3)
neigh.fit(x,km.labels_)

#testing data
test_data=['Educational Background','Skill sets']
print("Testing data")
print(test_data)

#feature extraction of test data
test=bv.transform(test_data).toarray()

#predicting class of the test data 
print("Assigned class to the testing data")
print(neigh.predict(test))

input("\nPress Enter to exit...")