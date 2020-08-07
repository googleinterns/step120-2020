import React from 'react';
import { Router, Link } from '@reach/router';

import Home from './Home/components/Home';
import PostListingDisplay from './PostListingDisplay/components/PostListingDisplay';
import ViewListingDisplay from './ViewListingDisplay/components/ViewListingDisplay';

import './App.css';

/** Component class to render the whole comments page */
function App(props) {
    return (
        <div>
            <h1 className="site-header" >Roomies</h1>
            <nav>
                <Link to="/">
                    <button className="nav-button" type="button">
                        Home
                    </button>
                </Link>
                <Link to="/viewListing">
                    <button className="nav-button" type="button">
                        View a Listing
                    </button>
                </Link>
                <Link to="/postListing">
                    <button className="nav-button" type="button">
                        Post a Listing
                    </button>
                </Link>
            </nav>

            <Router>
                <Home path="/" />
                <ViewListingDisplay path="/viewListing" />
                <PostListingDisplay path="/postListing" />
            </Router>
        </div>
    );
}

export default App;