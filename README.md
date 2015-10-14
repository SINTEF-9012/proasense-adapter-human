# Sensing Architecture - Human adapter
Human sensor adapter for the Sensing Architecture developed in the ProaSense project.

## Requirements
* Internet access.
* Maven
* Internet browser.

## Modules
 * There are six modules in proasense-adapter-human folder, five of them are will be used as application, being.
 1. human-inspectionreport
 2. human-maintenancereport
 3. human-materialcertificate
 4. human-materialchange
 5. human-productionplan

## Setup
* cd to proasense-adapter-human folder.
* mvn clean install
* cd to one of the five folders mentioned in previouse section, for example 
  * cd human-inspectionreport
* mvn package
* mvn tomcat7:run-war

## User guide
* the server must have been started after the steps above, for every module there is an url. Different modules have different
  url, these url are described below parallel to their modules.
 1. human-inspectionreport    : http://localhost:8080/InspectionReportForm.html
 2. human-maintenancereport   : http://localhost:8080/MaintenanceReport.html
 3. human-materialcertificate : http://localhost:8080/materialCertificateForm.html
 4. human-materialchange      : http://localhost:8080/materialChangeForm.html
 5. human-productionplan      : http://localhost:8080/productionPlanForm.html
