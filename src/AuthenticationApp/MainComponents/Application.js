import React, { useContext } from "react";
import { Router } from "@reach/router";
import SignInPage from "./SignInPage";
import SignUpPage from "./SignUpPage";
import { UserContext } from "../Providers/UserProvider";
import { authentication } from "../firebase";
import App from "../../App";
 
 
 
function Application () {
    const user = useContext(UserContext);
    console.log(user);
    return(
        user ?
        <App />
    :
        <Router> 
            <SignInPage path="/" />
            <SignUpPage path="signUp" />
        </Router>
 
    );
}
export default Application;
