import cv2
from subprocess import call
import numpy as np
from time import time

#TODO
offset_from_center = 1

#TODO Get the right numbers
#F=(w*d)/P
#F is the camera foucus
#w is the actual width of the object
#d is the distance to it
#P is the percived radius
object_width_apperant = 100
known_distance=1
object_width=1
foucus = object_width_apperant*known_distance/object_width 
def find_distance(foucus,  actual_size,  apperant_width):
    return foucus*actual_size/apperant_width

cap = cv2.VideoCapture(0)

#set the exposure
cap.set(15, .5)
while(cap.isOpened()):
    cur_time = time()
    a, frame = cap.read()
    
    # Convert BGR to HSV
    hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)

    # Threshold the HSV image to get only red colors
    mask1 = cv2.inRange(hsv, np.array([0,70,50]), np.array([10,255,255]))
    mask2 = cv2.inRange(hsv, np.array([170,70,50]), np.array([180,255,255]))
    
    #merge the mask
    mask = mask1 | mask2
    
    #Save the image to a file and reread it to make it grayscale. There has to be a better way of doing this
    cv2.imwrite("current_frame.png", mask)
    gray = cv2.imread("current_frame.png", 0)
    for i in range(2):
        gray = cv2.medianBlur(gray, 5)
    circles = cv2.HoughCircles(gray,cv2.cv.CV_HOUGH_GRADIENT,1.5,30,param1=30,param2=25,minRadius=10,maxRadius=300)
    if circles is not None:
        circles = np.uint32(np.around(circles))
        largest = 0
        x = 0
        y = 0
        for i in circles[0,:]:
    
            
            # draw the outer circle
            cv2.circle(frame,(i[0],i[1]),i[2],(0,255,0),2)
            # draw the center of the circle
            cv2.circle(frame,(i[0],i[1]),2,(0,0,255),3)
            size = i[2]
            print size
            
            #TODO make sure that the points dont move to far to quickly
            if size>largest:
                largest = size
                x = i[0]
                y = i[1]
        print find_distance(foucus, object_width, largest)        
    cv2.imshow('gray scale', gray)
    cv2.imshow('frame',frame)
    
    k = cv2.waitKey(5) & 0xFF
    if k == ord('q'):
        break
    print time()-cur_time
cv2.destroyAllWindows()
#call(["rm", "current_frame.png" ])


