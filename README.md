#  PSKMAIL revival

## Motivation
My goal is to use pskmail because it is for long distance mailing without relais.
I do not want to make packet radio via uhf/vhf. Long goal : write a singlepage app with VUE
which does psk mailing. This is a running project.  Thanks to Rein Couperus for is great program
which is now the starting point.
If anybody could explain how arq works or howto implement or perhaps recommend libraries. Please contact  me.

## Equipment
Yaesu FT891 + digirig dr891 Modem + Circo Mazzonis Stealth loop antenna + MAC M1 Computer

## Installation 
FLRIG and FLDIGI   compiled on the Mac or installed with brew.

Important : The build in driver for the Silicon lab chip of the modem is updated so
you have to install the new driver from the silicon Labs page [DownloadPage](https://www.silabs.com/developer-tools/usb-to-uart-bridge-vcp-drivers?tab=downloads)
Use it it does not work because of the security
Settings of the mac ---> go to the security and priviacy settings scroll down and allow the 
Mac to use the driver (it is recognized with the first use doubleclick the driver in the programms dir)

Start flrig go to config and choose /dev/cu.SLAB_USBtoUART7  or similar.
Tune your Tranceiver to some frequency, it works when the frequency is shown in flrig.
If it works start fldigi  goto configure -> Rig-Control -> flrig  Enable flig xcvr control with fldigi as client

Now test it by makeing some QSO's  for example with PSK31.

##  javapskmail
The goal here is to get javapskmail to run on mac m1 make some email connections and transform it in the
long run to a more modern platform.
First step is to get it run on mac m1.
The original sources I got from https://gitlab.com/g7vrd/pskmail/-/tree/master/javapskmail   thanks for saving them !
The problems are : 
1. The program is written around 2009 with the former java version.
2. The program uses native code (jni) to get GPS Data from the serial port.
For the Mac there are no jni libs at all (2024) for any port. So I faked the serialport in the code.
The consequence is that APRS and GPS does not work - but I want mail.....

So if you want to fiddle around (no programming) :
Install openjdk 8  on your maschine in your local user dir.
git clone this project
Goto the javapsk mail dir and change the javahome in the start.sh file to the location of your javajdk. Just installed.
With ./start.sh pskmail should pop up.

##  Problems
There are no settings for psk mail in fldigi like it is described in this document (perhaps I have a to modern
version - I do not know....)

I only get chaos letters back from fldigi (see screenshot), but I hear that something happens when I want to connect.

I do not understand the communication flow nor ARQ.

On the raspberry PI5 it does not run properly, because fldigi slows down the computer, despite there are 4GB
free space RAM left from 8GB.

As the document [Pskmail nutzen und betreiben von Martin Rost](https://www.maroki.de/pub/technology/pskmail/pskmail-Anleitung.pdf) 
shows on page 39 fldiki needs psk settings, but there is no
psk Tab any more in fldigi (see second picture down)

## Programming 
###  Solving the first problem
From the origin codebase I see Netbeans was used -> so I installed netbeans, I think it is needed because of
the form files.

I simply faked the serialport.java


##  Pictures

FLDIGI and JavaPSKMail running 

![FLDigi And JavaPSKMail](doku/PSKmailAndFldigi01.png)


Missing Settings for PSKMail in FLdigi

![FLDigi And JavaPSKMail](doku/fldigi_settings01.png)



