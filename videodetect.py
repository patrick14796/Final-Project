# This file work with Frames Comming from the drone!!!
from cProfile import label
from turtle import color
import cv2
from matplotlib.pyplot import box
import numpy as np
import pickle

net = cv2.dnn.readNet('bee_final.weights','bee.cfg')
classes = ["Bee"]


cap = cv2.VideoCapture("maze.MP4")
#To run on rtmp server comment line 92 and add this
# cap = cv2.VideoCapture("ur rtmp url")
# example: cap = cv2.VideoCapture("rtmp://ip:port/live/password")
#for image i need to use this below sentence
#img = cv2.imread("IMG_8330.jpg")
numberboxes = 0
sumofconfidence = 0
cord = []
while True:
	_, img = cap.read()
	if not _:
		break
	height, width, _=img.shape
	blob = cv2.dnn.blobFromImage(img, 1/255, (224, 224), (0,0,0), swapRB= True, crop=False)

	# for b in blob:
	# 	for n, img_blob in enumerate(b):
	# 		cv2.imshow(str(n), img_blob)

	net.setInput(blob)
	output_layer_names = net.getUnconnectedOutLayersNames()
	layerOutputs = net.forward(output_layer_names)


	boxes= []
	confidences = []
	class_ids = []
	for output in layerOutputs:
		for detection in output:
			scores =detection[5:]
			class_id = np.argmax(scores)
			confidence = scores[class_id]
			if confidence > 0.3:
				center_x = int(detection[0]*width)
				center_y = int(detection[1]*height)
				w = int(detection[2]*width)
				h = int(detection[3]*height)

				x = int(center_x - w/2)
				y = int(center_y - h/2)

				boxes.append([x,y,w,h])
				confidences.append((float(confidence)))
				class_ids.append(class_ids)
				numberboxes += len(boxes)
				for x in confidences:
					sumofconfidence += x

	#print(len(boxes))
	if(str(boxes) != "[]"):
		cord.append(boxes[0])
		#f.write(str(boxes[0])+",")
		#print(str(boxes[0]) + ",")
	indexes = cv2.dnn.NMSBoxes(boxes, confidences, 0.3, 0.2)
	#print(indexes.flatten())

	font = cv2.FONT_HERSHEY_SIMPLEX 
	colors = np.random.uniform(0,0,255)

	if len(indexes)>0:
		for i in indexes.flatten():
			x, y, w, h = boxes[i]
			label = str(classes)
			confidence = str(round(confidences[i],2))
			color = colors[i]
			cv2.rectangle(img, (x,y), (x+w, y+h), (0,0,255), 2)
			cv2.putText(img, "bee" + " " + confidence, (x, y-10), font, 1, (0,0,0), 1)
		


	imS = cv2.resize(img, (1080, 1080))
	cv2.imshow("Image",imS)
	#cv2.namedWindow("output", cv2.WINDOW_NORMAL)
	key =cv2.waitKey(1)
	if key == 27:
		break

print("Number of Boxes: " + str(numberboxes))
print("sum of confidences: " + str(sumofconfidence))
if(numberboxes != 0):
	print("Detection percentages: " + str (sumofconfidence / numberboxes ) + "%")
else:
	print("There Are no Detection of Bee in this Video..")	
cap.release()
cv2.destroyAllWindows()
fptr = open("cord", "wb")  # open file in write binary mode
pickle.dump(cord, fptr)  # dump list data into file 
fptr.close()  # close file pointer



#Operation Steps
#Run this code by the command: "py videodetect.py && plot.py" -> and get "cord.txt" file.
# After That you will recieve plot of the Bee Path

