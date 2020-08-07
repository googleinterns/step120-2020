import React from 'react';
import { Router } from '@reach/router';
import PostListingDisplay from './PostListingDisplay/components/PostListingDisplay';
import ViewListingDisplay from './ViewListingDisplay/components/ViewListingDisplay';
import './App.css';
import SignInPage from "./AuthenticationApp/MainComponents/SignInPage";
import SignUpPage from "./AuthenticationApp/MainComponents/SignUpPage";
import UserWrappedMain from "./AuthenticationApp/MainComponents/UserWrappedMain";

/** Component class to render the whole comments page */
function App () {
    return (
        <Router>
            <UserWrappedMain path= "/" />
            <SignInPage path= "signIn" />
            <SignUpPage path= "signIn/signUp" />
            <ViewListingDisplay path= "viewListing" />
            <PostListingDisplay path= "postListing" />
        </Router>
    )
}

export default App;