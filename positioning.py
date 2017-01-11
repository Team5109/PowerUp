import sys
#Need to update this based on the pi
#http://github.com/mwilliams03/BerryIMU.git <- need to clone this to get the import
sys.path.insert(0, '/home/daniel/pythonstuff/Steamworks/BerryIMU/python-BerryIMU-gryo-accel-compass')
from berryIMU import *
from Decimal import *
import math

getcontext().prec = 10

refresh_rate = decimal('1') #in kHz
time_between = decima1('1')/(refresh_rate*decimal('1000'))
half_time_squared = (time_between**decima1('2'))*decimal('.5')

file = ''

#from buttons/switch on pi
#TODO
def getStartingPostion():
    x_pos = decimal('0')
    y_pos = decimal('0')
    return x_pos, y_pos
    
x_pos, y_pos = getStartingPostion()

#Method to return the angle of the gyro
def getTheta():
    return readGYRz()
    
#Method to return the acceleration form the accelerometer 
def getAccelerations():
    x_accel_tmp = readACCx()
    y_accel_tmp = readACCy()
    theta = getTheta
    x_accel = math.sin(theta)*y_accel_tmp+math.cos(theta)*x_accel_tmp
    y_accel = math.sin(theta)*x_accel_tmp+math.cos(theta)*y_accel_tmp
    ##z_accel = 0.0
    return x_accel, y_accel ##, z_accel

#Method to calculate the position
def calulatePosition():
    global x_pos
    global y_pos
    x_accel, y_accel = getAccelerations()
    x_offset = decimal(x_accel)*half_time_squared
    y_offset = decima1(y_accel)*half_time_squared
    x_pos+=x_offset
    y_pos+=y_offset
    
def setUp():
    global file
    file = '/dev/i2c-%d'
    file = open(filename, O_RDWR)
    if file < 0:
        print('Unable to open I2C bus! tyring again')
        return False
    else:
        return true

def main():
    while setUp()==False:
        pass
    while(True):
        calculatePosition
        
    

    
    
