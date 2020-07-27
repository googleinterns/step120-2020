import React from "react";
import './App.css';
import SignInPage from "./AuthenticationApp/MainComponents/SignInPage";
import SignUpPage from "./AuthenticationApp/MainComponents/SignUpPage";
import { Router } from "@reach/router";
import UserWrappedHome from "./AuthenticationApp/MainComponents/UserWrappedHome";

/**Component class to render the whole comments page */
function App () {
    return (
        <Router>
            <UserWrappedHome path= "/" />
            <SignInPage path= "signIn" />
            <SignUpPage path= "signIn/signUp" />
        </Router>
    )
}

export default App;
