import serial
import time
f = open('distance.txt','w')
try:
     ser = serial.Serial('/dev/ttyUSB0',9600)
except:
     ser=serial.Serial('/dev/ttyUSB1',9600)
count = 0
max = 25
sum = 0
while True:
    
     if count < max:
         try:
              sum+=int(ser.readline())
         except:
              pass
         count+=1
     if count ==max:
         avg = sum/max
         print avg
         f = open('distance.txt','w')
         f.write(str(avg))
         f.close()
         count=0
         sum=0
