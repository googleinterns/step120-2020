import React from 'react';
import { Router, Link } from '@reach/router';

import Home from './Home/components/Home';
import PostListingDisplay from './PostListingDisplay/components/PostListingDisplay';

import './App.css';

/** Component class to render the whole comments page */
function App(props) {
    return (
        <div>
            <h1>Roomies</h1>
            <nav>
                <Link to="/">
                    <button type="button">
                        Home
                    </button>
                </Link>
                <Link to="/postListing">
                    <button type="button">
                        Post a Listing
                    </button>
                </Link>
            </nav>

            <Router>
                <Home path="/" />
                <PostListingDisplay path="/postListing" />
            </Router>
        </div>
    );
}

export default App;