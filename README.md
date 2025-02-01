# Lab 2 - Build Tools

CSCI 2020U: Software Systems Development and Integration

## Overview
In this lab, you will download and set up IntelliJ IDEA. You’ll build a project directory, along with some Java code, and create a Maven build. You’ll also
add this directory to the local and remote Git repositories to reinforce proper version control practices.
The environment can be replicated as a base for your project and assignments.

## Prerequisites
- For this lab and subsequent labs, you will need IntelliJ IDEA. You can download the community version [here](https://www.jetbrains.com/idea/download/) (scroll down). You can also obtain the ultimate version at an educational discount, details of which can be found [here](https://www.jetbrains.com/community/education/#students).
- We will be using Maven as our build tool, which should be installed by default by IntelliJ. If not, navigate to `File -> Settings -> Plugins -> Marketplace`. Search and install `Maven` and `Maven Extension`. You will see the installed plugins in the `Installed` tab.
- Next, navigate to `Settings -> Build, Execution, Development -> Maven -> Runner` and enable `Delegate IDE build/run action to Maven`
- To set up the project's JDK, navigate to `Project Structure -> Project -> SDK` and download the latest version (version 23).

>If you encounter any difficulties, contact your Teaching Assistant for assistance in resolving issues.

## Tasks
1. To get started with this lab, you must `clone` the repository to your local machine either in [IntelliJ](https://www.jetbrains.com/help/idea/set-up-a-git-repository.html#clone-repo) or by the terminal.
2. Make sure you have the Maven project is set up. Right-click `pom.xml` and click on `Add as Maven Project`.
3. Edit `pom.xml` to have these tags:
   - `<groupId>`:`csci2020u`
   - `<artifactId>`:`lab02`
   - `<version>`:`1.0`
   - `<name>`:`CSCI 2020U Lab 2`
4. You will create a simple HelloWorld, where the application will print out `Hello World!` (check the spelling to pass the auto grader). You will put your code in `src/main/java/HelloWorld.java`, already created for you. Take note of the directory structure, as it is the convention and can be used as a base for your projects and assignments.
5. You can run by clicking on the green arrow at the top.
   <details>
      <summary>Click here for the expected output using Maven</summary>
   
      ```
      [INFO] Scanning for projects...
      [INFO]
      [INFO] --------------------------< csci2020u:lab02 >---------------------------
      [INFO] Building CSCI 2020U Lab 2 1.0
      [INFO]   from pom.xml
      [INFO] --------------------------------[ jar ]---------------------------------
      [INFO]
      [INFO] --- exec:3.5.0:exec (default-cli) @ lab02 ---
      Hello World!
      [INFO] ------------------------------------------------------------------------
      [INFO] BUILD SUCCESS
      [INFO] ------------------------------------------------------------------------
      [INFO] Total time:  1.159 s
      [INFO] Finished at: 2024-12-18T23:40:23-05:00
      [INFO] ------------------------------------------------------------------------
      ```
   </details>
6. Commit and push to your local and remote repositories.

>Do **NOT** edit anything in the test folder and for future labs

## How to Submit

### In session

(Preferably)

- Show your local and remote repositories to the TA to prove that you have finished this lab.

### After lab hours

(1 week to submit - before your next lab session)

- Link to your GitHub repository on Canvas
- Screenshots of the command line terminal
- Add screenshots to `README.md`

The TA can provide oral feedback if you do not receive full marks for any lab assignment, but it is most
appropriate to ask the TA for this feedback in a timely fashion (i.e. ask now, not at the end of the term).

See command line screenshot in images file