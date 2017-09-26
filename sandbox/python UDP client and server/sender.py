import socket #network library

UDP_IP = "127.0.0.1"  # target IP (wherever the server is expecting)
UDP_PORT = 5000           #target port
MESSAGE = "Hello, World"
for i in range(0,20):
   MESSAGE+="!";         #each iteration, adding a ! to the end of the string
   print "UDP target IP:", UDP_IP
   print "UDP target port:", UDP_PORT
   print "message:", MESSAGE

   sock = socket.socket(socket.AF_INET, # Internet
                     socket.SOCK_DGRAM) # UDP
   sock.sendto(MESSAGE, (UDP_IP, UDP_PORT)) #the money shot, sends our data

