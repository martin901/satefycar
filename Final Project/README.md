# Safety Car

Safety car is a web application for calculating and managing car insurance policies.

The project has 3 levels of accessibility:
### Public

All visitors can calculate offers using the insurance calculator based on different criterias, such as:
* Car Make
* Car Model
* Cubic Capacity
* First registration date
* Whether or not they have had an accident during the previous year
* Driver Age

### Private

Users can not only calculate offers, but also request policies based on the offer they've calculated using the insurance calculator by adding:
* Effective date of the policy
* Vehicle registration card image

They can also view all of their requests and cancel the ones that are still pending, or print the approved/declined.
They can view and edit their profile info.

### Admin

Admins can view all requests and filter them by:
* Username (requester)
* Date of request
* Request status (Pending, Approved, Declined, Canceled, Expired)

They can approve/decline requests, update the multiciteria table used for calculating the offers.


## Getting Started
These instructions will get you a copy of the project up and running on your local machine for development and testing purposes.
### Prerequisites

* Java 1.8
* MariaDB

### Installing

* Download the project
* Run the SQL script provided in the db folder
* Run the application with the following command

If everything was done correctly you should see the application running on this url: http://localhost:8080

* You have one admin user:
```
username: admin45
password: p@ssword
```
* Log in with admin user and go to http://localhost:8080/admin/upload 
* Upload the data in exceldatabase.xlsx 
* This is the initial data to be able to calculate different offers.

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot) - The web framework used
* [Gradle](https://gradle.org/) - Build Tool

## Screenshots

[**Overview Video**](https://youtu.be/3yPF671CUtE)

### Project Structure

![Project Structure](https://i.ibb.co/88n05nN/project-structure.png)

### Main Page

![Main Page](https://i.ibb.co/J3KrFkZ/mainpage.png)

* Main Page responsive (all pages are responsive)

![Main Page Responsive](https://i.ibb.co/KsPQGfX/respnsive-main.png)

### Policy Calculator

![Policy Calculator Page](https://i.ibb.co/LCHShXR/policy-calc.png)

### Offer Page

![Offer Page](https://i.ibb.co/VTMRYj1/offer.png)

### Login Page

![Login Page](https://i.ibb.co/SNRps5c/login.png)

### Registration Page

![Registration Page](https://i.ibb.co/SQkx2b2/registration.png)

### User profile Page

![User Profile Page](https://i.ibb.co/C16SSkm/profile.png)

### User 'My Requests' page

![User Requests Page](https://i.ibb.co/C0TDgZV/myrequests.png)

### Admin 'All Requests' Page

![Admin All Requests Page](https://i.ibb.co/J7w9w0J/admin-req.png)

### Admin Upload Excel Table Page

![Admin Upload Table Page](https://i.ibb.co/JsXxM7Q/admin-upload.png)

## Developers

* [**Daniel Galabov**](https://gitlab.com/danielgylybov)
* [**Nikolay Georgiev**](https://gitlab.com/nikolayg073)
