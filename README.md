# Android App Development Assignment 2

Houses, developed in Android Studio, allows users to keep track of property prices in the housing market. 

Users can sign up, log in and post House listings for sale, along with a list date and the subsequent sold price and sold date. 
The data fields users can enter comprise the Address, List Price, List Date (via a Calendar date picker), Sold Price, Sold Date (via a Calendar date picker), No. of Bedrooms, No. of Bathrooms, a brief description, an image, a map marker (via google maps) which automatically adds the latitude and longitude for the location selected. The default location in the map is the users current location.  

A user can edit fields in a listing at any time and delete any listing of their choosing.
All the houses a users has posted are visible through google map markers in a Map of all Houses.

User authentication is through Firebase with Firebase datatbase also implemented.

This Repo is assignemnt two of android app development module. 
It continues from https://github.com/ADrohan/Houses2 (last commit 28th Nov 2021)
in addition to its precursor for assigment part 1 https://github.com/ADrohan/Houses2/commits/master

This version of the app has several reworked components that were present in the Assignemt 1 version, in addition to a number of new features and functionality.

New Features and functionality
* Register/Login 
* Logout
* Splashscreen
* Date Picker dialog to select list date 
* Date picker dialog to select sold date
* New maps activity showing all markers that have been added
* New extended card for maps activity
* Live location tracking
* MVP design pattern implemented


# Git Approach
Git flow was used which creates a master and develop branch by default. From the develop branch feature braches were created and merged back into the develop branch when completed. The devlop branch was finally merged to the master branch. Tags were created at various milestones throughout.

# UX/DX Approach
UX
* Custom colour theme used with consistant use of primary, secondary, accent colours 
* Splashscreen with customized Houses mipmap icon
* Android Date Picker (calendar) utilised

DX
* Model View Presenter (MVP) pattern is applied throughout with additional helpers packages

# Diagrams ( created with Code Iris Plugin )

![codeirisHouses](https://user-images.githubusercontent.com/30194159/187516968-e83f328e-a96c-4caf-859a-5ecf617f7b4b.png)

# Personal Statement
I certify this assignment is my work and was developed following a similar structure to the lecture and lab material from the HDip in Computer Science Course at SETU, along with additional reference guides as outlined below.

# References outside of course material
* https://codingzest.com/firebase-authentication-android/
* https://www.tutorialkart.com/kotlin-android/android-datepicker-kotlin-example/
* https://tutorialwing.com/android-datepicker-using-kotlin-example/
* http://www.codeplayon.com/2021/06/android-kotlin-splash-screen-example/
