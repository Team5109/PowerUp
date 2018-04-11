import cv2
import time
import numpy as np
import imutils
import math
import socket
import json


"""
sudo apt-get install python-opencv
sudo apt-get install python-matplotlib
"""


DELAY = 0.02
USE_CAM = 1
IS_FOUND = 0

MORPH = 7
CANNY = 250

_width  = 10000.0
_height = 420.0
_margin = 0.0

UDP_IP = "roboRIO-5109-FRC.local"
UDP_PORT = 5109

if USE_CAM: video_capture = cv2.VideoCapture(0)

corners = np.array(
	[
		[[  		_margin,_margin 			]],
		[[ 			_margin, _height + _margin  ]],
		[[ _width + _margin, _height + _margin  ]],
		[[ _width + _margin, _margin 			]],
	]
)

pts_dst = np.array( corners, np.float32 )

average = 0

while True :

	if USE_CAM :
		ret, rgb = video_capture.read()
	else :
		ret = 1
		rgb = cv2.imread( "opencv.jpg", 1 )

	if ( ret ):
                rgb = cv2.resize(rgb, (500,500))
                hsv = cv2.cvtColor(rgb, cv2.COLOR_BGR2HSV)
                lower_green = np.array([85,68,211])
                upper_green = np.array([101,155,255])
                mask = cv2.inRange(hsv, lower_green, upper_green)
                res = cv2.bitwise_and(rgb,rgb,mask=mask)
        
		gray = cv2.cvtColor( res, cv2.COLOR_BGR2GRAY )
		
		gray = cv2.bilateralFilter( gray, 1, 10, 120 )

		edges  = cv2.Canny( gray, 10, CANNY )

		kernel = cv2.getStructuringElement( cv2.MORPH_RECT, ( MORPH, MORPH ) )
	

		closed = cv2.morphologyEx( edges, cv2.MORPH_CLOSE, kernel )
		contours, h = cv2.findContours( closed, cv2.RETR_EXTERNAL, cv2.CHAIN_APPROX_SIMPLE )
		
		centerx = []

		for cont in contours:

	
			if cv2.contourArea( cont ) > 100 :

				arc_len = cv2.arcLength( cont, True )

				approx = cv2.approxPolyDP( cont, 0.1 * arc_len/2, True )
				approx1 = cv2.approxPolyDP( cont, 0.1 * arc_len, True )

				M = cv2.moments( cont )
                                cX = (int(M["m10"] / M["m00"]))
                                cY = (int(M["m01"] / M["m00"]))
                                CenterX = -250 + cX
                                CenterY = 250 - cY

                                centerx.append(CenterX)

                                
                                

				
				if ( len( approx ) == 4 ):
					IS_FOUND = 1
					M = cv2.moments( cont )
					cX = (int(M["m10"] / M["m00"]))
                                        cY = (int(M["m01"] / M["m00"]))
                                        CenterX = -250 + cX
                                        CenterY = 250 - cY
					center = (int(M["m10"] / M["m00"]), int(M["m01"] / M["m00"]))
					
					cv2.circle(rgb, (average+250, cY), 5, (0, 0, 255), -1)
					
					pts_src = np.array( approx, int)
					text1 = str(approx)
					FirstX = int((text1[3:6]))
					FirstY = int((text1[7:10]))
					SecondX = int((text1[17:20]))
                                        SecondY = int((text1[21:24]))
    					ThirdX = int((text1[31:34]))
					ThirdY = int((text1[35:38]))
					FourthX = int((text1[45:48]))
					FourthY = int((text1[49:52]))
					
				
                                        print (-250 + FirstX,250 - FirstY)
    					print (-250 + SecondX,250 - SecondY)
					print (-250 + ThirdX,250 - ThirdY)
					print (-250 + FourthX,250 - FourthY)
					print ("New set")
									                                     
					                                       
    					cv2.drawContours( rgb, [approx], -1, ( 255, 0, 0 ), 2 )

				else : pass

                average = 0
                for i in range(len(centerx)):
                        average += centerx[i]

                if len(centerx) == 0:
                        centerx.append(0)
                average /= len(centerx)

                print(average)

                angle = average*40/500
		MESSAGE = json.dumps({"Angle": angle, "Keep Driving": True })
		sock = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)
		sock.sendto(MESSAGE.encode('utf-8'), (UDP_IP, UDP_PORT))

	
		cv2.namedWindow( 'rgb', cv2.CV_WINDOW_AUTOSIZE )
		cv2.imshow( 'rgb', rgb )
		

		if cv2.waitKey(27) & 0xFF == ord('q') :
			break

		if cv2.waitKey(99) & 0xFF == ord('c') :
			current = str( time.time() )
			cv2.imwrite( 'ocvi_' + current + '_edges.jpg', edges )
			cv2.imwrite( 'ocvi_' + current + '_gray.jpg', gray )
			cv2.imwrite( 'ocvi_' + current + '_org.jpg', rgb )
			print "Pictures saved"

		time.sleep( DELAY )


if USE_CAM : video_capture.release()
cv2.destroyAllWindows()

# end
