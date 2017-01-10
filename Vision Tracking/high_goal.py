import cv2
import numpy as np
def find_center(img):
    
    #finding the contors
    ret,thresh = cv2.threshold(img,127,255,0)
    contours, hierarchy = cv2.findContours(thresh,cv2.RETR_TREE,cv2.CHAIN_APPROX_SIMPLE)
    
    #Array to be filled with the center points of all the contors
    center_x = []
    center_y = []
    
    #Loop to find all the center points of the contors
    for cnt in contours:
        M = cv2.moments(cnt)
        cx = int(M['m10']/M['m00'])
        cy = int(M['m01']/M['m00'])
        center_x.append(cx)
        center_y.append(cy)
    
    #averaging the points to find a center
    avgx= (center_x[0]+center_x[1])/2
    avgy= (center_y[0]+center_y[1])/2
    return avgx, avgy
    
def main():
    cap = cv2.VideoCapture(0)
    while(cap.isOpened()):
        
        #cur_time = time()
        
        #getting the current frame so it can be maipulated
        a, frame = cap.read()
        x_center, y_center = frame.shape[:2]
        x_center/=2
        y_center/=2
        #print x_center
        # Convert BGR to HSV
        hsv = cv2.cvtColor(frame, cv2.COLOR_BGR2HSV)
        
        #adding some blurs to decrease noise and increase accuracy
        hsv = cv2.GaussianBlur(hsv,(5,5),0)
        hsv = cv2.bilateralFilter(hsv,9,75,75)
        
        # Threshold the HSV image to get only green colors
        range = 20
        mask = cv2.inRange(hsv, np.array([65-range,50,50]), np.array([65+range,255,255]))
        
        #Save and re-read the frame to it can be loaded in grey scale
        cv2.imwrite("current_frame.png", mask)
        gray = cv2.imread("current_frame.png", 0)
        
        #Tring to find the center
        #if no rectangele, then the program doesn't crash
        try:
            avgx, avgy = find_center(gray)
            #print avgx
            
            #put a circle on the frame
            cv2.circles(frame, (avgx, avgy), 4, (0, 0, 2555), 2)
            
            range = 20
            
            #Form the string to be outputted from where the target is
            temp = ''
            if avgy > range+y_center:
                temp+='high, '
            elif avgy < y_center-range:
                temp+='low, '
            if avgx > range+x_center:
                temp+='right, '
            elif avgx < x_center-range:
                temp+='left, '
            if len(temp)==0:
                temp = 'centered'
            print temp
            
            #cv2.imshow('frame',frame)
            
        except:
            print "target not found"
            
main()
