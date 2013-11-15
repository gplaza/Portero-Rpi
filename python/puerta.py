#! /usr/bin/env python

# abre una puerta de casa


import time	# Para manejar las esperas
import RPi.GPIO as GPIO # Para trabajar con las entradas y salidas


## Declaracion de variables globales##
tespera = 1.0 # tiempo de espera para abrir la puerta desde que se ejecuta la orden
topen = 1.0 # tiempo de apertura de la puerta en segundos
pinpuerta = 7 # pin al que esta conectado el circuito de apertura, fisicamente es el 7
pinpulsador = 11 #pin al que esta conectado el boton de llamada. fisicamente es el 11

# configura los puertos fisicos de la raspberry Pi. Se conectara una entrada y una salida.
def configpuerta () :  
  GPIO.setmode(GPIO.BOARD) # El mapeado de los pines es fisicamente como estan en la placa
  GPIO.setup(pinpuerta, GPIO.OUT) # pinpuerta como salida
  GPIO.setup(pinpulsador, GPIO.IN, pull_up_down=GPIO.PUD_UP) # habilita la resistencia PullUp
  #print('circuito configurado')

# activa senyal 3.3 por el pinpuerta. En este caso abrira la puerta de la calle durante topen
def abrepuerta() :  
  time.sleep(tespera)	# Espera topen para abrir
  GPIO.output(pinpuerta, 1) # Activa la senyal a nivel alto
  #print('puerta abierta')
  time.sleep(topen)
  GPIO.output(pinpuerta, 0) #desactiva la senyal y la puerta ya no se puede abrir
  #print('puerta cerrada')

# interrupcion cada vez que se pulsa el boton conectado a pinpulsador
def intrllamada(channel):
    print "Han llamado a la puerta" #Aquí se llama a android

def main() :
  print ('entro en main')
  configpuerta ()
# Cuando hay un evento en el pinpulsador, se lanza la interrupcion en cualquier parte
# del programa. Se llamara a intrllamada y despues de ser ejecutada se seguira con el
# programa. El bouncetime es para que si llaman 2 veces en menos de medio segundo solo 
# se entre en la interrupcion 1 vez.
  GPIO.add_event_detect(pinpulsador, GPIO.FALLING, callback=intrllamada, bouncetime=500)
  while 1:
    print('no se como hacer la interrupcion de abrir puerta abrepuerta() desde android')
    time.sleep(0.1)
  GPIO.cleanup() #liberamos los pines al finalizar el programa
#Entramos en main
main()