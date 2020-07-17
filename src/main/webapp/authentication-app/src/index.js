import React from "react";
import ReactDOM from "react-dom";
import { Router, Link } from "@reach/router";
import SignInPage from "./main-components/SignInPage";
import SignUpPage from "./main-components/SignUpPage";

const App = () => (
    <div>
        <Router> 
            <SignInPage path="/" />
            <SignUpPage path="signUp" />
        </Router>
    </div>
);

ReactDOM.render(<App />, document.getElementById("root"));