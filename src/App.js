import React, { Component } from 'react';
import PostCommentsDisplay from './PostCommentsDisplay/components/PostCommentsDisplay';
import './App.css';

/**Component class to render the whole comments page */
class App extends Component {
    render(){
        return (
            <PostCommentsDisplay />
        );
    }
}

export default App;
