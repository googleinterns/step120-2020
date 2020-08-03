import React, { Component } from 'react';

import '../styles/Home.css'

/** Dummy component for now. Will be the place where current listing are shown that users can
 filter through */
class Home extends Component {
    render(){
        return (
            <div className="home-body">
                <h2>Welcome to Roomies!</h2>
                <p>To continue: </p>
                <ul>
                    <li>press the "View a Listing" button to find listings made by other users</li>
                    <li>press the "Post a Listing" button to fill out a form to make your own listing</li> 
                </ul>
            </div>
        );
    }
}

export default Home;