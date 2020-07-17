import React from "react";
import ReactDOM from "react-dom";
import { Router, Link } from "@reach/router";
import SignInPage from "./main-components/SignInPage";

const App = () => (
    <div>
        <Router> 
            <SignInPage path="/" />
        </Router>
    </div>
);

ReactDOM.render(<App />, document.getElementById("root"));