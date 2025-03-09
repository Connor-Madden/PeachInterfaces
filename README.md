# Peach Interfaces: Fashion & Clothing Catalogue Project

## Overview
This project is focused on cataloging fashion and clothing items. The application features a graphical user interface (GUI) built using JavaSwing/JavaFX, with database management handled through SQL and Java.

## Features
The catalogue currently supports the following core functionalities:
- **Add items** to the catalogue
- **Delete items** from the catalogue
- **Edit items** in the catalogue

These functionalities are implemented within a functional GUI interface that includes at least five distinct fashion brands for variety. For more details on the brands, check out our [Figma](https://www.figma.com/design/UmCMeFAGiIxgV8TuBUHmbj/Peach-Interfaces?node-id=9-2&t=uSMTgTtrm3yoc0jZ-1) designs which include various catalogues featuring brands and the overall layout.

## How-to-Run

### Prerequisites
- Java Development Kit (JDK 23 or later): https://www.oracle.com/java/technologies/javase/jdk23-archive-downloads.html
- Apache Maven: https://maven.apache.org/install.html
- Git: https://git-scm.com/downloads

### Steps

1. **Open Terminal**:
  Open Command Prompt or Git Bash.

3. **Clone the Repository**:
  ```git clone https://github.com/Connor-Madden/PeachInterfaces.git```

4. **Go to the Project Directory**:
  ```cd PeachInterfaces```

5. **Build the Project With Maven**:
  ```mvn clean install```

6. **Launch the Application**:
  ```mvn exec:java -Dexec.mainClass="csci2040u.catalogue.Fashion-Catalogue-Project"```

7. **Run the Application**:
   Once the application is running, you can start using it:
   - Click the blue button to add a clothing item and its attributes.
   - Click the yellow button to edit an existing clothing item in the catalogue.
   - Click the **crimson red** button to remove a clothing item.
   - Click the **bright red** button to exit the application.
     
## Upcoming Features

### Iteration 2:
- Upgrade the user interface for better usability and aesthetics (Todo)
- Display images of catalogue items (Todo)
- Implement search and filter functionality (In Progress)
- **Authentication system:** 
  - Log in (Todo)
  - Log out (Todo)

### Iteration 3:
- Further enhance the interface to make it visually appealing (Backlog)
- Add product details pages (Backlog)
- Implement "show similar items" feature (Backlog)

## Contributors
- Rayan Alam | Front-End Lead
- Connor Madden | Project Manager
- Adrian Ramirez | Back-End Lead
- Maryam Baz | Technical Lead
- Thaddeus Baturensky | Software Quality Lead


