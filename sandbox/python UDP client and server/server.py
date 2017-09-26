import socket  #networking library
s = socket.socket(socket.AF_INET, socket.SOCK_DGRAM)  #AF_INET = ip address, SOCK_DGRAM = UDP
s.bind(('',5000)) #accept from any IP address, port 5000
s.setblocking(0) #means we don't want to wait for a packet if there's none present
data ='' #good practice to clear any variables
#address = ''
while True:
    try:
        data,address = s.recvfrom(10000) #data will fill with the payload, address will fill with
                                         #the sender IP and return port
    except socket.error: #woo!  no data, business as usual
        pass
    else:                #oh wait, there is data, do something
        print "recv:", data  #and that something is just print it out

