import pickle
from sklearn import tree
def test(b1,b2):

	#decision_tree_pkl_filename = 'decision_tree_classifier_20170212.pkl'


    #return 'hello word'
	# Loading the saved decision tree model pickle
	#decision_tree_model_pkl = open(decision_tree_pkl_filename, 'rb')
	#decision_tree_model = pickle.load(decision_tree_model_pkl)

	#clfX = decision_tree_model.predict([[b1, b2, b3]])
	#return clfX
    #X = [[-62, -61], [-74, -64]]
    X = [[-33, -35 ], [-33, -35 ], [-33, -35 ],[-33, -35 ],[-33, -35 ],[-33, -35 ],[-33, -35 ], [-33, -35 ],[-33, -35 ],
         [-33, -35 ], [-33, -35 ], [-33, -35 ],[-33, -35 ],[-33, -35 ],[-33, -35 ],[-33, -35 ], [-33, -35 ],[-33, -35 ],
         [-26, -38 ], [-26, -38 ], [-26, -38 ],[-26, -38 ],[-26, -38 ],[-26, -38 ],[-26, -38 ], [-26, -38 ],[-26, -38 ],
         [-26, -38 ], [-26, -38 ], [-26, -38 ],[-26, -38 ],[-26, -38 ],[-26, -38 ],[-26, -38 ], [-26, -38 ],[-26, -38 ],]
    #X = [[0, 0], [1, 1]]
    Y = [[1],[1],[1],[1],[1],[1],[1],[1],[1],
         [1],[1],[1],[1],[1],[1],[1],[1],[1],
         [2],[2],[2],[2],[2],[2],[2],[2],[2],
         [2],[2],[2],[2],[2],[2],[2],[2],[2]]
    clf = tree.DecisionTreeClassifier()

    clf = clf.fit(X, Y)
    clfX = clf.predict([[b1, b2]])
    #print(clfX)
    return clfX
