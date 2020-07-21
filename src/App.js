import React, { Component } from 'react';
import { BrowserRouter, Route, Switch } from 'react-router-dom';

import Home from './Home/components/Home';
import MakeListingDisplay from './MakeListingDisplay/components/MakeListingDisplay';
import Navigation from './Navigation';

import './App.css';

class App extends Component {
    render(){
        return (

                <BrowserRouter>
                    <Navigation />
                    <Switch>
                        <Route path="/" component={Home} exact/>
                        <Route path="/makeListing" component={MakeListingDisplay}/>
                    </Switch>
                </BrowserRouter>

        );
    }
}

export default App;