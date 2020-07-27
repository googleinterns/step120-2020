import React from 'react';
import { Router } from '@reach/router';
import Home from './Home/components/Home';
import MakeListingDisplay from './MakeListingDisplay/components/MakeListingDisplay';
import './App.css';
import SignInPage from "./AuthenticationApp/MainComponents/SignInPage";
import SignUpPage from "./AuthenticationApp/MainComponents/SignUpPage";
import UserWrappedMain from "./AuthenticationApp/MainComponents/UserWrappedMain";
import PostCommentsDisplay from './PostCommentsDisplay/components/PostCommentsDisplay';

/**Component class to render the whole comments page */
function App () {
    return (
        <Router>
            <UserWrappedMain path= "/" />
            <SignInPage path= "signIn" />
            <SignUpPage path= "signIn/signUp" />
            <MakeListingDisplay path="makeListing" />
            <PostCommentsDisplay path= "postComment" />
            <Home path= "home" />
        </Router>
    )
}

export default App;