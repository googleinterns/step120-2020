import React, { useContext } from "react";
import { Router } from "@reach/router";
import SignInPage from "./SignInPage";
import SignUpPage from "./SignUpPage";
import { UserContext } from "../providers/UserProvider";
import LoggedInPage from "./LoggedInPage";
import { authentication } from "../firebase"
 
 
 
function Application () {
    const user = useContext(UserContext);
    console.log(user);
    return(
        user ?
        <LoggedInPage />
    :
        <Router> 
            <SignInPage path="/" />
            <SignUpPage path="signUp" />
            <LoggedInPage path="loggedInPage" />
        </Router>
 
    );
}
export default Application;
