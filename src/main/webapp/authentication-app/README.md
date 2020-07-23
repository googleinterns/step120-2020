## Authentication

This portion of the project deals with Authentication of users.
Specifically the Sign In and Sign Out Pages, but there are several
other helper components that are used in this portion of the project.

## Application

The Application component checks to see if the user is already signed in.
If they are signed in, the LoggedInPage is loaded to show the user their 
information. Later this will be changed to instead sending that user inforomation
back to the main page to be displayed. If the user is not signed in, they are taken
to the sign in page. 

## SignInPage 

The Sign In page takes two main fields, the users email and password. 
It uses this information to get the user from firestore and sign them in.
Once they are signed in, all their information can be displayed, such as
email and display name. There is a link under sign in for people who do not
have an account to sign into. 

##SignUpPage
The Sign Up page takes the users display name, email, and password and stores 
them in firestore after the user clicks the Sign Up button. This automatically 
signs the user in, but there is also a Sign In link at the bottom in case the user
accidentally selected the Sign Up page. 
