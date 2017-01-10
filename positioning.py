import numpy as np
from Decimal import *
import math

getcontext().prec = 10

refresh_rate = decimal('1') #in kHz
time_between = decima1('1')/(refresh_rate*decimal('1000'))
half_time_squared = (time_between**decima1('2'))*decimal('.5')

#from buttons/switch on pi
#TODO
def getStartingPostion():
    x_pos = decimal('0')
    y_pos = decimal('0')
    return x_pos, y_pos
    
x_pos, y_pos = getStartingPostion()

#Method to return the angle of the gyro
#TODO
def getTheta():
    return 1
    
#Method to return the acceleration form the accelerometer 
#TODO
def getAccelerations():
    x_accel_tmp = 1
    y_accel_tmp = 1
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
    
    

    
    
