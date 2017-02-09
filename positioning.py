import sys
import getopt
from Decimal import *
import math
import time

sys.path.append('/home/pi/Downloads/RTIMULib/Linux/python')
import RTIMU

SETTINGS_FILE = "/home/pi/Downloads/RTIMULib/Linux/python/RTIMULib"

print("Using settings file " + SETTINGS_FILE + ".ini")
if not os.path.exists(SETTINGS_FILE + ".ini"):
  print("Settings file does not exist, will be created")

s = RTIMU.Settings(SETTINGS_FILE)

imu = RTIMU.RTIMU(s)

if (not imu.IMUInit()):
    print("IMU Init Failed")
    sys.exit(1)
else:
    print("IMU Init Succeeded")

imu.setSlerpPower(0.02)
imu.setGyroEnable(True)
imu.setAccelEnable(True)


poll_interval = imu.IMUGetPollInterval()

getcontext().prec = 10

refresh_rate = decimal(poll_interval)
time_between = refresh_rate/decimal('1000')
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
    data= imu.getIMUData()
    rpy=data["fusionPose"]
    return math.degrees(rpy[2])

#Method to return the acceleration form the accelerometer 
def getAccelerations():
    x,y,_ = imu.getFusionData()
    
    x_accel_tmp = x
    y_accel_tmp = y
    theta = getTheta()
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
    

def main():
    while(True):
        calculatePosition()
        time.sleep(float(time_between))
    

    
    
