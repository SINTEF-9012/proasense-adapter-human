# Sensing Architecture - Human adapter
Human sensor adapter for the Sensing Architecture developed in the ProaSense project.

## Requirements
* Internet access.
* Maven
* Internet browser.

## Modules
 * There are six modules in proasense-adapter-human folder, five of them will be used as application, being:
 1. human-inspectionreport
 2. human-maintenancereport
 3. human-materialcertificate
 4. human-materialchange
 5. human-productionplan

## Setup
* cd to proasense-adapter-human folder.
* write "mvn clean install" in command prompt.
* cd to one of the five folders mentioned in previouse section, for example 
  * cd human-inspectionreport
* run by writing "mvn jetty:run" in command prompt.

## User guide
* The server must have been started after the steps above, for every module there is an url. Different modules have different
  url, these url are described below parallel to their modules.
 1. human-inspectionreport    : http://(server address)/human-inspectionreport
 2. human-maintenancereport   : http://(server address)/human-maintenancereport
 3. human-materialcertificate : http://(server address)/human-materialcertificate
 4. human-materialchange      : http://(server address)/human-materialchange
 5. human-productionplan      : http://(server address)/human-productionplan
* By visiting the url, different forms will appear in the browser, depending on the module being used. Fill the form of your  choice and click submit. 
