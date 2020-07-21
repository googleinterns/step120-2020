import React, { Component } from 'react';
import { Router, Link } from '@reach/router';

import Home from './Home/components/Home';
import MakeListingDisplay from './MakeListingDisplay/components/MakeListingDisplay';

import './App.css';

class App extends Component {
    render(){
        return (
            <div>
                <h1>Roomies</h1>

                <nav>
                    <Link to="/">Home</Link> {" "}
                    <Link to="/makeListing">Make a Listing</Link>
                </nav>

                <Router>
                    <Home path="/" />
                    <MakeListingDisplay path="/makeListing" />
                </Router>
            </div>
        );
    }
}

export default App;