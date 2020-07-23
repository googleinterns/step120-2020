import React, {useState} from "react";
import {Link} from "@reach/router";

const SignInPage = () => {
  const [userPassword, setUserPassword] = useState('');
  const [userEmail, setUserEmail] = useState('');
  const [error, setError] = useState(null);

  const signInHandler = (event,email,password) => {
    event.preventDefault();
  };

  const onChangeHandler = (event) => {
      const {name, value} = event.currentTarget;
      {/* gets the . part of the address to check if its edu */}
      const emailEnd = name.substring(name.length - 4, name.length);
      
      {/*
        TODO redirect the user to an error page
        that tells them that their email addresses
        is invalid and must be a .edu address 
        */}
      if(name === 'userEmail') {
        setUserEmail(value);
      }
      else if(name === 'userPassword') {
        setUserPassword(value);
      }
  };

  return (
      <div className="SignIn">
        <h1 className="Title">Sign In </h1>
        <div className="SignIn-Area">
          <form className="SignIn-form">
            <label htmlFor="userEmail" className="block">
              Your Email:
            </label>
            <input
              type="userEmail"
              className=""
              name="userEmail"
              value= {userEmail}
              placeholder= "E.x: JohnAppleseed@gmail.com"
              id="userEmail"
              onChange = {(event) => onChangeHandler(event)} 
            />
            <label htmlFor="userPassword" className="block">
              Make a Strong Password:
            </label>
            <input
              type="userPassword"
              className=""
              name="userPassword"
              value= {userPassword}
              placeholder="Your Password Here"
              onChange = {(event) => onChangeHandler(event)}
            />
            <button 
              className="SignInButton"
              onClick= {(event) => {signInHandler(event, userEmail, userPassword)}}>
              Click to Sign in
            </button>
          </form>
          <p className="">or</p>
          <button> Sign in with Google </button>
          <p className="">
            Don't have an account {" "}
            <Link to = "signUp" className="signup-link">
              Sign up 
            </Link>{" "}
            <br /> {" "}
            <Link to = "passwordReset" className="password-reset-link">
              Forgot your password? Reset it here
            </Link>
          </p>
        </div>
      </div>

        
  );
}
export default SignInPage;
