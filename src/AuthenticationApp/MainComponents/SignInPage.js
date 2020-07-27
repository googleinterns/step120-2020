import React, {useState} from "react";
import {Link, navigate} from "@reach/router";
import { authentication } from "../firebase";
 
const SignInPage = () => {
  const [userPassword, setUserPassword] = useState('');
  const [userEmail, setUserEmail] = useState('');
  const [error, setError] = useState(null);
 
  const signInHandler = async (event,email,password) => {
    event.preventDefault();
    authentication.signInWithEmailAndPassword(email, password).catch(error => {
        setError("Error signing in with password and email!");
        console.error("Error signing in with password and email", error);
    });
    if (authentication.currentUser) {
        navigate("/");
    }
  };
  
  const onUserPasswordChangeHandler = event => {
        const {value} = event.currentTarget;
        setUserPassword(value);
    }
    const onUserEmailChangeHandler = event => {
        const {value} = event.currentTarget;
        setUserEmail(value);
    }
 
  return (
      <div className="SignIn">
        <h1 className="Title">Sign In </h1>
        <div className="SignIn-Area">
          {error !== null && <div>{error}</div>}
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
              onChange = {(event) => onUserEmailChangeHandler(event)} 
            />
            <label htmlFor="userPassword" className="block">
              Your Password:
            </label>
            <input
              type="password"
              className=""
              name="userPassword"
              value= {userPassword}
              placeholder="Your Password Here"
              onChange = {(event) => onUserPasswordChangeHandler(event)}
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
