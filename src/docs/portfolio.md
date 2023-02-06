# Portfolio A


## Overview

We are hoping to create a piece of software to speed up the process of transcribing notebooks in Bristol Museum; these notebooks contain important data on species, some of which are now extinct. Currently, this takes a long time as a transcriber must learn the handwriting of each individual collector and manually transcribe the text into a digital format.

Our MVP would be a tool that allows the user to select a section from a scan of a notebook page and transcribe the text. However, we are hoping to incorporate machine learning to automatically transcribe the text. These notebooks are a good candidate for machine learning as much of the handwriting is very consistent, and there are extensive databases of handwritten letters which we can use as training data. We would also have functionality to allow the user to make corrections to the machine-transcribed text.

This will be incorporated into a web application, and it will be an open source solution.


## Requirements


### Stakeholders



*   **Museum curator** - The curators at Bristol Museum and other museums around the world, who manage the vast amounts of artifacts in their stores, want to preserve the information that comes with these objects. In this case, they want to digitally transcribe handwritten notes so that they can be easily accessed by others. 
*   **Transcriber** - For handwritten notes to be digitised, people traditionally need to read the handwriting physically and then type the text as accurately as possible. We aim to aid these transcribers by providing a package for them which automatically recognises and transcribes as much text as possible, then giving them the opportunity to make edits/finalise the text.
*   **Systems manager** - To manage the systems controlling the product, it may be necessary for someone to have administrative access to the system. This person would be someone at the museum with technical knowledge who interacts with the system to maintain it.
*   **Researchers** - People who analyse and identify information from written notes will be affected by this project. In our specific case, these people will be those interested in biological data that has been recorded firsthand. After the text has been transcribed, it should be able to be easily shared for these researchers to access remotely, e.g., the National Biodiversity Network Atlas. Any handwritten notes could be made widely available to researchers of varying professions using this program, greatly increasing the availability of rarely seen information.
*   **Historians** - Historians in the future may want to look back on information stored on handwritten notes. Even if handwritten notes are kept pristine by museums and collectors, it will still be useful to have a digital version created to reduce the decay caused by repeated disturbances of the artifacts. Thus our project can essentially help preserve the information and the original material it is sourced from.
*   **Anyone with internet access** - There is potential that anybody with internet access would want to use this software, if they want to transcribe their own handwritten notes. This software itself is not limited to the region of manuscripts, it could also be used for recognising hand-written text written on a tablet. With the increase in paperless work, the software could be useful in office scenarios. 


### User stories

**Museum curator**




*   As a museum curator, I want to use software to upload images of handwritten documents so that they can be transcribed.
*   As a museum curator, I want to be able to export the transcribed document to a variety of formats so that I can share the information in different ways.
*   As a museum curator, I want to be able to look at text statistics of the document to see how often certain data is mentioned.

**Transcriber**



*   As a transcriber, I want to use machine learning to transcribe handwritten text so that the process can be made faster.
*   As a transcriber, I want to edit the output text of the program so that any errors can be corrected or important details added.
*   As a transcriber, I want to save my progress and be able to continue later so that I can work on the transcription over a period of time.

**Systems manager**



*   As a systems manager, I want to be able to use handwritten text to train the neural network so that it becomes more accurate at recognising handwriting.


### Core flows

As a **transcriber**, I want to **use machine learning to transcribe handwritten text** so that **the process can be made faster**. 

Basic flow:



1. Login to webapp with credentials
2. Press button to create new transcription
3. Select image to transcribe
4. Navigate to and press “Automatic Transcription” button
5. Wait as transcribed text is prepared
6. Review text

Alternative flow:



1. Find produced text unsatisfactory
2. Navigate to and press “Automatic Transcription” button again
3. Wait as transcribed text is prepared 
4. Review to see if text is more accurate

Exceptional flow:



1. Produced text is still unsatisfactory
2. Delete text in textbox
3. Transcribe manually 

	

As a **transcriber**, I want to **edit the output text of the program** so that **any errors can be corrected or important details added**.

Basic flow:



1. Follow sequence of steps from the above user story
2. Identify text that needs to be changed
3. Navigate to textbox
4. Edit text to change errors with keyboard input

As a **transcriber**, I want to **save my progress and be able to continue later** so that **I can work on the transcription over a period of time**.

Basic flow:



1. Navigate to and press “Save As” button
2. Name document
3. Fill in optional document metadata
4. Press save
5. Close webapp

Alternative flow:



1. Navigate to and press “Save” button
2. Wait as webapp saves progress
3. Close webapp

Exceptional flow:



1. Login to webapp with credentials
2. Navigate to list of saved documents
3. Click on intended document
4. Wait as webapp loads your progress
5. Continue transcription


### Atomic implementation requirements

The following is a breakdown of the steps and requirements for a transcriber aiming to use machine learning to aid in transcribing.



*   Upon launching the webapp the transcriber is greeted with a login page where they will enter their credentials
*   Once logged in they will be directed to a home page displaying pre existing saved transcriptions
*   Here they will have the option to:
    *   Upload - select pictures of handwritten text to send to the server
    *   Download - select transcriptions to download to their local machine
    *   Create new transcription - start transcribing a new handwritten document or redo an existing one
    *   Load existing transcription - load progress from an existing transcription from the server
    *   Change settings - enter a settings menu where they can alter certain properties of the webapp
*   The transcriber will select “Create new transcription”, taking them to an editing page
*   The editing page will have:
    *   Buttons along the top with upload/download functionality 
    *   Automatic Transcription - a button to start the machine learning transcription
    *   Photo area - a section to select the image to transcribe and view said image
    *   Text area - a section for the transcribed text to be stored
    *   Focus swap - the ability to swap which “area” is on the left/right
*   The transcriber will select the image they require to be placed in the photo area
*   Once the image loads, they will select the “Automatic Transcription” button and wait for the webapp to return the recognised text
    *   Here we could have a progress bar
*   Upon completion, the transcribed text will be placed in the text box where the transcriber can review it

		


## Personal Data, Privacy, Security and Ethics Management

Ethics approval was applied for on 11/11/20.

Our project will likely feature a login system. This will allow our client to have multiple users working on different pages at a time. To facilitate this, we have decided to require either an email or username along with a password to authenticate the users that will be logging into the website to access the database.

To store the passwords of users securely, we will use a hashing algorithm (a one-way algorithm that produces a unique result for each input value) and throw away the original password. This hashed value will be stored in the database, and when a user logs in the password they submit will be hashed before being sent to the server and compared to this value.

We will likely store the emails/usernames in either plain text using asymmetric encryption. These will need to be retrieved occasionally in case a password needs to be reset, so we cannot discard their meaning with a hashing algorithm.

When logging in, the submitted username and password will be encrypted using asymmetric encryption – they will be encrypted by the client with a public key and then decrypted on the server side with the private key. This will mean the login information is secured when travelling across the Internet.

If we need to implement cookies into our web app, we will ensure we comply with the cookie law by asking for permission from the users before allowing them to enter the site. This will be done by creating an overlay in HTML which prevents the user from interacting with the website, which will remain until they choose to accept the necessary cookies and the optional cookies they are happy with keeping active (if applicable).

When interacting with the database through the web app, we will use asymmetric encryption methods to secure the data that is passed between clients and the server. While the only personal data that will be transferred is during the login process, we will keep the connection secure in this way throughout to ensure privacy and to minimise the risk of messages being intercepted. 


## Architecture


### Drivers



*   The client requires the system to be available as a web application.
*   The system must be able to store the transcriptions (whether from a transcriber or from the deep learning library) in a database and provide them on demand, so that they can be improved iteratively.
*   Transcribers should be able to save their progress on a transcription and return to it later.
*   User login data must be hashed before being stored.
*   Transcribers and museum curators should be able to download a copy of the transcription for a certain page.
*   Museum curators must be able to upload an image of a handwritten document so that it can be transcribed.
*   The client is hoping for part of the transcription process to be automated by machine learning.


### Diagram



![image](archdiagram.jpg)



## Development Testing

During the first stage of our development, we mainly do the interface testing, our strategy is implementing the several mocked examples on the programme based on different situations.  

The focus of our current tests is the controllers, we would do the interface testing to guarantee the stability and correctness when the project runs. More than that, the interface testing could show if we are using the right logic and building our project as the indicated architecture, in which we could fix the errors in the early stage.

The detailed strategy for interface testing is shown in the below table. 

|                                            | Number of cases to test | Approach for testing | Priority    |
| -----------                                | -----------             | -----------          | ----------- |
| Login/Interceptors                         | 4                       | Mocked examples      | 2           |
| Communication with the database            | 2                       | Mocked examples      | 1           |
| Interface transaction during the programme | 2-3                     | Mocked examples      | 2           |


For testing in the login interface, in the case of different combinations of username and password, we would give three different interactions, successful login and the unsuccessful case with either wrong password or unregistered username. Meanwhile, we would use login interceptors to prevent users from snooping the transcribed documents, which means the user would only use the web as a one-time translator without logging. All these would be tested with mocked examples, that we would simulate the rare situations to observe how the programme would react.

In the consequence, we would test the communication between the database and the programme, with the built controllers we would use mocked examples to test if the programme would update data into the database, for example, if we would add new users into the user information table when the registration form is submitted on the website. We might use mocked examples to check if we’re using the well-formed SQL code or correct transfer between the interfaces. As a result, in general, we would test all the actions related to CRUD. 

The interface transaction testing part would test the whole running flow of our project. After testing the above situation, we would use two to three mocked examples to simulate real users for our current programme, which could give us clarification on whether the logic is workable.


## Release Testing

Before our web app release, we would do this release testing to maintain the stability of our app and provide a good user experience. As a result, beyond the essential operation, we would focus on how our app has reached the releasing standards.

In this way, our strategy for testing this will mainly involve the manual test. Which means that, after reaching a certain stage of our development, one of our team members would play as a user, or even, we would invite our friends as users to gain real experience. 

For example, as a museum curator, I only know the basic work-related operations on computers, however, I need to use this unfamiliar application in my research. In this way, I hope I could use the tutorial to teach myself and get the result successfully in the future.

|                               | Number of cases to test | Approach for testing         | Priority    |
| -----------                   | -----------             | -----------                  | ----------- |
| User experience               | As much as possible     | Manual tests                 | 2           |
| Working flow for this project | 1-2                     | Manual tests & pre-set tests | 1           |

The above table shows the details for testing in this stage. 

To ensure the reliability of our website, like in the development testing, we would finish manual tests combined with some pre-set tests inside the project. This could provide us with a clean environment for further tests. As a result, in total, there would be one or two tests in this section.

Later, as has been explained before, we could use manual tests to gain the outcome of user experience. One of our team members in the testing department would first use this application. With the provided feedback, the development department could improve this project. This two steps inside the user experience test provide us with reliable pieces of advice towards the project. Also, to perfect our project, we might invite our friends to use the beta version to obtain suggestions from different aspects.
