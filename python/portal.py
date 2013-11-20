#! /usr/bin/env python

# abre una puerta de casa

import time	# Para manejar las esperas
import RPi.GPIO as GPIO # Para trabajar con las entradas y salidas
import sqlite3 as lite # Libreria para acceder al BD sqlite
import datetime
import sys
from bottle import route, run, template, request

## Declaracion de variables globales##
# Variables hardware
tespera = 1.0 # tiempo de espera para abrir la puerta desde que se ejecuta la orden
topen = 1.0 # tiempo de apertura de la puerta en segundos
pinpuerta = 7 # pin al que esta conectado el circuito de apertura, fisicamente es el 7
pinpulsador = 11 #pin al que esta conectado el boton de llamada. fisicamente es el 11

#Variables POST
IpRpi = '192.168.1.210'
portRpi = 7000

#Usuario y contrasenya para comporbar en base de datos.
con = None
db_name = 'control' #Nombre de la base de datos.

@route('/login', method='POST')
def login():
  username = request.POST.get('user', '')
  password = request.POST.get('password', '')
  try:
    con = lite.connect(db_name) #Abre la conexion a la base de datos.
    print 'Recibiendo datos de Android'
    checar = authentication(con, username, password)
    if checar != None:
      abrepuerta()
      print 'Puerta abierta. Registrando usuario: ' + username
      con = lite.connect(db_name) #Abre la conexion a la base de datos.
      historialPuertaAbierta(con, username)
      print 'Registro insertado en el historial'
    else:
      print 'La puerta no se ha abierto'
  except lite.Error, e:    
    print "Error Login %s:" % e.args[0]    
  finally:    
    if con:
      con.close()

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
    print "Han llamado a la puerta" #Aqui se llama a android
    
# Verifica si el usuario existe en la base de datos
def authentication(conexion, user, password):
    cur = conexion.cursor() #Obtenemos el cursor para manejar las consultas.
    query = 'SELECT u.user FROM users u WHERE u.user = :idUser AND u.password = :idPass'
    cur.execute(query,{"idUser":user,"idPass":password})#Executamos el query con los parametros dados.
    row = cur.fetchone()
    cur.close() #Cerramos el cursor
    if row == None:
      print 'El usuario no existe en BD o la contrasenya es incorrecta ...'
    return row	

# Guarda la fecha, la hora y el usuario que abrio la puerta.
def historialPuertaAbierta(conexion, user):
    cur = conexion.cursor()
    fech_hr = str(datetime.datetime.now())#Obtenemos la fecha actual
    insert = "INSERT INTO input_history(user, date_entry, date_txt) VALUES (:idUser, :fecha, :fecha)"
    cur.execute(insert,{"idUser":user, "fecha":fech_hr})
    conexion.commit()
    cur.close()
    return

def main() :
  print ('Inicializando sistema. Bienvenido')
  configpuerta ()
# Cuando hay un evento en el pinpulsador, se lanza la interrupcion en cualquier parte
# del programa. Se llamara a intrllamada y despues de ser ejecutada se seguira con el
# programa. El bouncetime es para que si llaman 2 veces en menos de medio segundo solo 
# se entre en la interrupcion 1 vez.
  GPIO.add_event_detect(pinpulsador, GPIO.FALLING, callback=intrllamada, bouncetime=500)
# Escribimos los parametros para iniciar el servidor. es la ip de la pi y el puerto
  run(host=IpRpi, port=portRpi)
# Ejecutamos la comprobacion de android-rpi-basededatos
  login()
  GPIO.cleanup() #liberamos los pines de GPIO

#Entramos en main
main()
