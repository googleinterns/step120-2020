import React from 'react';

import '../styles/Home.css'

/** Functional component that displays the home screen of Roomies. Tells the user to visit one of the other pages */
function Home() {
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

export default Home;