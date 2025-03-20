# Peach Interfaces: Fashion & Clothing Catalogue Project

## Overview
This project is focused on cataloging fashion and clothing items. The application features a graphical user interface (GUI) built using JavaSwing/JavaFX, with database management handled through SQL and Java.

## Features
The catalogue currently supports the following core functionalities for the Admin:
- **Add items** to the catalogue
- **Delete items** from the catalogue
- **Edit items** in the catalogue

The catalogue currently supports the following core functionalities for the User:
- **Search for items** within the catalogue
- **Apply filters** to refine search results
- **Browse items** along with their images

These functionalities are implemented within a visually upgraded GUI interface that includes at least five distinct fashion brands for variety with different clothing categories. For more details on the brands, check out our [Figma](https://www.figma.com/design/UmCMeFAGiIxgV8TuBUHmbj/Peach-Interfaces?node-id=9-2&t=uSMTgTtrm3yoc0jZ-1) designs which include various catalogues featuring brands and the overall layout.

## How-to-Run

### Prerequisites
- Java Development Kit (JDK 23 or later): https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html
- Apache Maven: https://maven.apache.org/install.html
- Git: https://git-scm.com/downloads

### Steps

1. **Open Terminal**:
  Open a local Command Prompt or Git Bash.

3. **Clone the Repository**:
  ```git clone https://github.com/Connor-Madden/PeachInterfaces.git```

4. **Go to the Project Directory**:
  ```cd PeachInterfaces```

5. **Build the Project With Maven**:
  ```mvn clean install```

6. **Launch the Application**:
  ```mvn exec:java```

7. **Run the Application**:
   
   Upon launching the application, you will first encounter the **Login Page**, where you can enter a username and password.

   **Admin Credentials**:  
   **Username**: `admin`  
   **Password**: `adminpass`  

   **User Credentials**:  
   **Username**: `user`  
   **Password**: `userpass`  

   To exit the application, simply click the **Exit** button.  

   As an admin, you have access to the following features:  
   **Add** a clothing item and its attributes to the catalogue (**Blue Button**).  
   **Edit** an existing clothing item in the catalogue (**Yellow Button**).  
   **Remove** a clothing item from the catalogue (**Crimson Red Button**).   
   **Search** for items in the catalogue for better management.  
   **Filter** items in the catalogue to refine results.
   **Log Out** and return to the login page (**Bright Red Button**). 

   As a user, you can:
   
   **Search** for clothing items to find what you like.  
   **Filter** items in the catalogue to narrow your selection.
   **Browse** images of each item.  
   **Log Out** and return to the login page (**Bright Red Button**).  

9. **Test the Application**:

   To switch to the testing branch and test the backend code, run the following command(s) in the project directory:  
   ```git checkout Backend-Iteration1``` or ```git checkout Backend-Iteration2```

## Upcoming Features

### Iteration 3:
- Further enhance the interface to make it visually appealing (Todo)
- Add product details pages (Todo)
- Implement the "show favourite items list" feature (Todo)

## Contributors
- Rayan Alam | Front-End Lead
- Connor Madden | Project Manager
- Adrian Ramirez | Back-End Lead
- Maryam Baz | Technical Lead
- Thaddeus Baturensky | Software Quality Lead
