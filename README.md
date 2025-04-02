# Peach Interfaces: Fashion & Clothing Catalogue Project

## Overview
This project is focused on cataloging fashion and clothing items. The application features a graphical user interface (GUI) built using JavaSwing/JavaFX, with database management handled through SQL and Java.

## Features

### Admin Features
The catalogue provides the following core functionalities for administrators:  
- **Add items** to the catalogue.
- **Delete items** from the catalogue.
- **Edit items** in the catalogue.
- **Search for items** within the catalogue.
- **Apply filters** to refine search results.
- **Browse items** along with their images.
- **Login/Logout** of the system using a unique admin username and password.

### User Features
The catalogue provides the following core functionalities for users to access:  
- **Search for items** within the catalogue.
- **Apply filters** to refine search results.
- **Browse items** along with their images.
- **Favourite items** that the user prefers.
- **Fullscreen view and description** for better comprehension.
- **User authentication** to manage a personal account with a unique username and password.

These functionalities are implemented within a visually appealing and modernized GUI interface that includes at least five distinct fashion brands for a variety of different clothing categories. For more details on the brands, check out our [Figma](https://www.figma.com/design/UmCMeFAGiIxgV8TuBUHmbj/Peach-Interfaces?node-id=9-2&t=uSMTgTtrm3yoc0jZ-1) designs which include various catalogues featuring brands and the overall layout.

## How to Build and Run the Application

### Prerequisites
- Java Development Kit (JDK 23 or later): https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html.
- Apache Maven: https://maven.apache.org/install.html.
- Git: https://git-scm.com/downloads.

### Steps

1. **Open Terminal**:
  Open a local Command Prompt or Git Bash.

3. **Clone the Repository**:
  ```git clone https://github.com/Connor-Madden/PeachInterfaces.git```.

4. **Go to the Project Directory**:
  ```cd PeachInterfaces```.

5. **Build the Project With Maven**:
  ```mvn clean install```.

6. **Launch the Application**:
  ```mvn exec:java```.

## How to Use the Application 

Upon launching the application, you will first encounter the **Login Page**, where you can enter a username and password.  

- **Admin Credentials**:  
  - **Username**: `admin`.  
  - **Password**: `adminpass`.
  - **Login** to the admin account (**Green Button**)

 - **User Authentication**:
   - **Create Account** with a unique username and password (**Navy Blue Button**)
     - THE USERNAME AND PASSWORD MUST BE FOUR CHARACTERS LONG AT MINIMUM
   - **Remove Account** associated with a unique username and password (**Red Button**)
   - **Forgot Password** if you have forgotten the password associated to your username (**Mustard Yellow Button**)
   - **Continue as Guest** if you do not want to go through user authentication (**Grey Button**)
     - YOU WILL NOT HAVE ACCESS TO THE FAVOURITES FEATURE
 
To exit the application, simply click the **Exit** button (**Orange Button**).  

#### **Admin Privileges**  
When logged in as an admin, you have access to the following features:  
- **Add** a clothing item and its attributes (including images) to the catalogue (**Blue Button**).  
- **Edit** an existing clothing item and its attributes (including images) in the catalogue (**Yellow Button**).  
- **Remove** a clothing item from the catalogue (**Crimson Red Button**).   
- **Search** for items in the catalogue for better management.  
- **Filter** items in the catalogue to refine results.
- **Log Out** and return to the login page (**Bright Red Button**). 

#### **User Privileges**  
When logged in as a user, you can:  
- **Search** for clothing items to find what you like.  
- **Filter** items in the catalogue to narrow your selection.  
- **Browse** images of each item.  
- **Log Out** and return to the login page (**Bright Red Button**).

## How to Test the Application 

To switch to the testing branch and test the backend code (as the GUI is tested through regular use of the application), run the following command(s) in the project directory:  ```git checkout Backend-Iteration1``` or ```git checkout Backend-Iteration2```.

## Summary

All iterations have been successfully completed. Visit the **Project Board** to review the completed tasks and issues, or check the **Burn Down Chart** for a visual representation of our team's progress throughout the three iterations.

## Contributors
- Rayan Alam | Front-End Lead
- Connor Madden | Project Manager
- Adrian Ramirez | Back-End Lead
- Maryam Baz | Technical Lead
- Thaddeus Baturensky | Software Quality Lead
