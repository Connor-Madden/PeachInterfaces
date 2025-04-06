# Peach Interfaces: Fashion & Clothing Catalogue Project

## 📋 Overview
This project is focused on cataloging fashion and clothing items. The application features a graphical user interface (GUI) built using JavaSwing/JavaFX, with database management handled through SQL and Java.

## 🔑 Features
 
### 👨‍💼 Admin Features
The catalogue provides the following core functionalities for administrators:  
- **Add items** to the catalogue.
- **Delete items** from the catalogue.
- **Edit items** in the catalogue.
- **Search for items** within the catalogue.
- **Apply filters** to refine search results.
- **Browse items** along with their images.
- **Login/Logout** of the system using a special admin username and password.

### 👤 User Features
The catalogue provides the following core functionalities for users to access:  
- **Search for items** within the catalogue.
- **Apply filters** to refine search results.
- **Browse items** along with their images.
- **Favourite items** that the user prefers.
- **Fullscreen view and description** of items for better comprehension.
- **User authentication** to manage a personal account with a unique username and password.

These functionalities are implemented within a visually appealing and modernized GUI interface that includes at least five distinct fashion brands for a variety of different clothing categories. For more details on the brands, check out our [Figma](https://www.figma.com/design/UmCMeFAGiIxgV8TuBUHmbj/Peach-Interfaces?node-id=9-2&t=uSMTgTtrm3yoc0jZ-1) designs which include various catalogues featuring brands and the overall layout.

## 🛠️ How to Build and Run the Application

### ✅ Prerequisites
- Java Development Kit (JDK 11 or later): [Download JDK Version 11+](https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html).
- Apache Maven: [Download Apache Maven Project Tool](https://maven.apache.org/install.html).
- Git: [Download Git Version Control System](https://git-scm.com/downloads).

### ⚙️ Steps

1. **Open Terminal**:
  Open a local Command Prompt or Git Bash.

3. **Clone the Repository**:
  ```git clone https://github.com/Connor-Madden/PeachInterfaces.git```.

4. **Go to the Project Directory**:
  ```cd PeachInterfaces```.

5. **Build the Packaged Project With Maven**:
  ```mvn clean package```.

6. The **.jar** executable file with all dependencies included will be located at ```target/fashion-catalogue-jar-with-dependencies.jar```

7. **Execute the Application**:
  ```java -jar target/fashion-catalogue-jar-with-dependencies.jar```.

### 👨🏼‍💻 Troubleshooting

| Issue | Solution |
|-------|----------|
| **"mvn: command not found"** | Make sure Maven is installed and added to your PATH. Verify with `mvn -v`. |
| **"java: command not found"** | Ensure Java is installed and the `java` command is available. Try `java -version`. |
| **Build fails due to missing dependencies** | Run `mvn clean install` to force dependency download. |
| **App doesn’t start after running the JAR** | Check that `LogInPanel.java` is the correct main class. Also check for any console error logs. |
| **UI doesn't display correctly or crashes** | Ensure you are using Java 11+ and your system supports Java Swing GUI apps. |
| **Database connection issues** | Ensure `fashionDb.db` (or your SQLite file) exists in the correct path, or modify the path in the code. |

## 📝 How to Use the Application 

Upon launching the application, you will first encounter the **Login Page**, where you can enter a username and password.  

- **Admin Credentials**:  
  - **Username**: `admin`.  
  - **Password**: `adminpass`.
  - **Login** to the admin account (**Green Button**).

 - **User Authentication**:
   - **Create Account** with a unique username and password (**Navy Blue Button**).
     - *The username and password must be four characters long at minimum*.
   - **Remove Account** associated with a unique username and password (**Red Button**).
   - **Forgot Password** if you have forgotten the password associated with your username (**Mustard Yellow Button**).
     - *You must know your username in order to retrieve your password*.
   - **Continue as Guest** if you do not want to go through user authentication (**Grey Button**).
     - *You will not have access to the favourites feature as a guest*.
   - **Login** to your user account (**Green Button**).
 
To exit the application, simply click the **Exit** button (**Orange Button**).  

### 🧑‍💼 Admin Privileges
When logged in as an admin, you have access to the following features:  
- **Add** a clothing item and its attributes to the catalogue (**Blue Button**).
  - *All added images must first be saved in a designated file directory that you have recorded or remembered*.
- **Edit** an existing clothing item and its attributes in the catalogue (**Yellow Button**).
  - *All newly added images must first be saved in a designated file directory that you have recorded or remembered*. 
- **Remove** a clothing item and its attributes from the catalogue (**Crimson Red Button**).   
- **Search** for items in the catalogue for better management.
  - *As an admin, you can search for items using their ID numbers*.
- **Filter** items in the catalogue to refine results.
- **Log Out** and return to the login page (**Bright Red Button**). 

### 👤 User Privileges
When logged in as a user, you have access to the following features:   
- **Search** for clothing items to find what you like.  
- **Filter** items in the catalogue to narrow your selection.  
- **Browse and Scroll** images and brief descriptions of each item.
- **Click** on an image to view it in fullscreen with a detailed description in separate tabs.  
- **Favourite** items by clicking the heart icon in the top corner of each item.
- **View Favourites** to see a list of all the items you've favourited (**Pink Button**). 
- **Log Out** and return to the login page (**Bright Red Button**).

### 🎥 Functionality Demonstration Video
If you're still unsure how to use the fashion catalogue, check out the demo video here: [Peach Interfaces Demo Video](https://youtu.be/fT7_Kj6ps-M).

## 🧪 How to Test the Application 

To switch to the testing branch and test the backend code (as the GUI is tested through regular use of the application), run the following command(s) in the project directory:  ```git checkout Backend-Iteration1``` or ```git checkout Backend-Iteration2```. Additionally, the ```main``` branch contains all our **Unit Tests**, **System Tests**, and **Integration Tests** implemented using **JUnit**.  

## 📊 Summary

All iterations have been successfully completed. Visit the **Project Board** to review the completed tasks and issues, or check the **Burn Down Chart** for a visual representation of our team's progress throughout the three iterations.

## 👥 Contributors
- Rayan Alam | Front-End Lead
- Connor Madden | Project Manager
- Adrian Ramirez | Back-End Lead
- Maryam Baz | Technical Lead
- Thaddeus Baturensky | Software Quality Lead
