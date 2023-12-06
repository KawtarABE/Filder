# Filder - Smart Agriculture Management System
Fielder 2.0 is a cutting-edge project that addresses the challenges faced by field managers in overseeing multiple fields simultaneously. The manual and individual management of fields can be cumbersome, time-consuming, and prone to human errors. Fielder 2.0 aims to streamline this process by leveraging IoT technology, mobile development tools, and artificial intelligence to enhance decision-making and optimize agricultural operations.

## Table of Contents
* [General Information](#general-information)
* [Technologies Used](#technologies-used)
* [Features](#features)
* [Setup](#setup)
* [Usage](#usage)
* [Screenshots](#screenshots)


## General Information
### Architecture of the project
The project architecture is logically divided into three main layers:
#### 1. Presentation Layer : Android application
- Interface for user interaction, command issuance, and data visualization.
- Controls the state of the solenoid valve for remote irrigation.
#### 2. Data Access Layer : Sensors
- Collects data from field sensors (humidity, temperature, water level) using ESP32.
- Uses MQTT to transmit data reliably and securely to the Raspberry Pi.
#### 3. Control Layer : Raspberry
- Processes collected data before storing it in the database.
- Utilizes Firebase API to send formatted data to Firebase.

<img src="" width="">

This github repository contains the code of the android application

## Technologies Used
[![PHP](https://skillicons.dev/icons?i=php,html,css,js,mysql)](https://skillicons.dev)



## Main Features
### Admin Panel:
- Manage patient/doctor accounts.
- Manage diseases/categories defined in the platform.
- Manage consultations.

### Doctor Dashboard:
- View and Respond to different consultations by category.
- View the profile and various diseases of patients who have submitted consultations.
- View the history of consultations they have responded to and modify responses.

### Patient Interface:
- View public questions from other patients and the responses from doctors.
- Add a consultation and track responses.


## Setup
### Pre-Requisites
To set up this project you should install the following:
- XAMPP (apache, mysql)
- VSCODE (or any other IDE)

### Usage
To use the project you should do the following:
- Add the project to **'htdocs'** folder on the xampp environment.
- Create the database on phpmyadmin panel.
  * patients(**`id`**, **`email`**, **`password`**, **`first_name`**, **`last_name`**, **`sexe`**, **`date_of_birth`**)
  * maladies(**`id_maladie`**, **`name_of_maladie`**)
  * cathegories(**`id_cathegory`**, **`name_of_cathegory`**)
  * patient_maladie(**`id_patient`**, **`id_maladie`**)
  * forum(**`id`**, **`Description`**, **`Post`**, **`patient`**, **`id_categorie`**, **`statut`**, **`type`**)
  * answers(**`id_consultation`**, **`id_doctor`**, **`answer`**)
  * admins(**`id_admin`**, **`email`**, **`password`**, **`first_name`**, **`last_name`**)
  * doctors(**`id_doctor`**, **`email`**, **`password`**, **`first_name`**, **`last_name`**, **`sexe`**, **`id_cathegory`**)

## Screenshots
### Home Page
![Example screenshot](Screenshots/home.png)
### Admin dashboard 
![Example screenshot](Screenshots/Dashboard.png)
### Blog page
![Example screenshot](Screenshots/Blog.png)
### Consultations page
![Example screenshot](Screenshots/consultations.png)
