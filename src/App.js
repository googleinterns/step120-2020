import React from 'react';
import { Router, Link } from '@reach/router';

import Home from './Home/components/Home';
import MakeListingDisplay from './MakeListingDisplay/components/MakeListingDisplay';

import './App.css';

/**Component class to render the whole comments page */
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
                <Link to="/makeListing">
                    <button type="button">
                        Make a Listing
                    </button>
                </Link>
            </nav>

            <Router>
                <Home path="/" />
                <MakeListingDisplay path="/makeListing" />
            </Router>
        </div>
    );
}

export default App;