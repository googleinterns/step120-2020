import React, { Component } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import Home from './Home/components/Home';
import ListingDisplay from './ListingDisplay/components/ListingDisplay';
import Navigation from './Navigation';

import './App.css';

class App extends Component {
    render(){
        return (

                <BrowserRouter>
                    <Navigation />
                    <Switch>
                        <Route path="/" component={Home} exact/>
                        <Route path="/listing" component={ListingDisplay}/>
                    </Switch>
                </BrowserRouter>

        );
    }
}

export default App;