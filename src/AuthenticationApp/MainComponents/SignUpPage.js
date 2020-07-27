import React, { useState } from "react";
import { Link, navigate } from "@reach/router";
import { authentication, generateUserDocument } from "../firebase";
 
const SignUpPage = () => {
    const [userEmail, setUserEmail] = useState("");
    const [userPassword, setUserPassword] = useState("");
    const [retypedPassword, setRetypedPassword] = useState("");
    const [displayName, setDisplayName] = useState("");
    const [error, setError] = useState(null);
 
    const createUser = async (
        event, 
        userEmail, 
        userPassword, 
        displayName,
        retypedPassword
        ) => {
            event.preventDefault();
            
            try {
                if (retypedPassword !== userPassword) {
                    throw new SyntaxError("Your passwords don't match");
                } else {  
                    const {user} = await authentication.createUserWithEmailAndPassword(
                        userEmail, 
                        userPassword
                        );
                    user.updateProfile({
                        displayName: displayName
                    }).then(() => {
                        console.log("update successful");
                    })
                    generateUserDocument(user);
                }
            } catch(error) {
                setError('Error signing up with email and password, ' + error.message);
            }
 
            setUserEmail("");
            setUserPassword("");
            setDisplayName("");
            setRetypedPassword("");
            if (authentication.currentUser) {
                navigate("/");
            }
        };
 
    const onUserPasswordChangeHandler = event => {
        const {value} = event.currentTarget;
        setUserPassword(value);
    }
    const onDisplayNameChangeHandler = event => {
        const {value} = event.currentTarget;
        setDisplayName(value);
    }
    const onUserEmailChangeHandler = event => {
        const {value} = event.currentTarget;
        setUserEmail(value);
    }
    const onRetypedPasswordChangeHandler = event => {
        const {value} = event.currentTarget;
        setRetypedPassword(value);
    }
 
    return (
        <div className="SignUp">
        <h1 className="Title">Sign Up </h1>
        <div className="SignUp-Area">
          {error !== null && <div>{error}</div>}
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
              onChange={(event) => onDisplayNameChangeHandler(event)}
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
              onChange = {(event) => onUserEmailChangeHandler(event)} 
            />
            <label htmlFor="userPassword" className="block">
              Make a Strong Password:
            </label>
            <input
              type="password"
              className=""
              name="userPassword"
              value= {userPassword}
              placeholder="Your Password Here"
              onChange = {(event) => onUserPasswordChangeHandler(event)}
            />
            <label htmlFor="retypedPassword" className="block">
              Retype your Password:
            </label>
            <input
              type="password"
              className=""
              name="retypedPassword"
              value= {retypedPassword}
              placeholder="Type your password again"
              onChange = {(event) => onRetypedPasswordChangeHandler(event)}
            />
            <button 
              className="signUpbutton"
              onClick= { event => {createUser(
                  event, 
                  userEmail, 
                  userPassword, 
                  displayName,
                  retypedPassword
                  )}}>
              Click to Sign Up
            </button>
          </form>
          <p className="">or</p>
          <button> Sign in with Google </button>
          <p className="">
            Already have an Account? Sign In! {" "}
            <Link to = "/signIn" className="signin-link">
              Sign In 
            </Link>
          </p>
        </div>
      </div>
    );
}
export default SignUpPage;
