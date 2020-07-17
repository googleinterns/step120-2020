import React, { useState } from "react";
import { Link } from "@reach/router";

const SignUpPage = () => {
    const [userEmail, setUserEmail] = useState("");
    const [userPassword, setUserPassword] = useState("");
    const [displayName, setDisplayName] = useState("");
    const [error, setError] = useState(null);

    const createUser = (event, userEmail, userPassword) => {
        event.preventDefault();
        setUserEmail("");
        setUserPassword("");
        setDisplayName("");
    };

    const onChangehandler = event => {
        const { name, value } = event.currentTarget;
        if( name === userPassword) {
            setUserPassword(value);
        }
        else if (name === displayName) {
            setDisplayName(value);
        }
        else if (name === userEmail) {
            setUserEmail(value);
        }
    };

    return (
        <div className="SignUp">
        <h1 className="Title">Sign Up </h1>
        <div className="SignUp-Area">
          <form className="SignUp-form">
            <label htmlFor="displayName" className="block">
              Choose your Display Name:
            </label>
            <input
              type="text"
              className=""
              name="displayName"
              value={displayName}
              placeholder= "E.x: AwesomeDude123"
              id="displayName"
              onChange={event => onChangeHandler(event)}
            />
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
              className="signUpbutton"
              onClick= { event => {createUser(event, userEmail, userPassword)}}>
              Click to Sign Up
            </button>
          </form>
          <p className="">or</p>
          <button> Sign in with Google </button>
          <p className="">
            Already have an Account? Sign In! {" "}
            <Link to = "/" className="signin-link">
              Sign up 
            </Link>{" "}
          </p>
        </div>
      </div>
    )
}
export default SignUpPage;

