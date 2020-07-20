import React, { Component } from 'react';
import PostCommentsDisplay from './PostCommentsDisplay/components/PostCommentsDisplay';
import firebase from 'firebase/app';
import 'firebase/database;';
import './App.css';

class App extends Component {
    constructor(props){
        super(props);

        const config ={
            
        }
    }

    render(){
        return (
            <PostCommentsDisplay />
        );
    }
}

export default App;
