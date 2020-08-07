import React, { Component } from 'react';

import Listing from './Listing.jsx';

/** Component class that displays the listings found in the datastore. 
 * This class utilizes a helper component called "Listing" that renders each individual listing 
 */
class ViewListingDisplay extends Component {
    constructor(props){
        super(props);

        this.state = {
            listings:[]
        };

        this.fetchListings = this.fetchListings.bind(this);
        this.addListing = this.addListing.bind(this);
    }

    fetchListings(){
        fetch('/listings')
            .then(response => response.json())
            .then((userListings) => {
                userListings.forEach((userListing) => {
                    this.addListing(userListing);
                });
            });
    }

    addListing(listing){
        const newState = Object.assign({}, this.state);
        newState.listings.push(listing);
        this.setState(newState);
    }

    componentDidMount() {
        this.fetchListings();
    }

    render(){
        return (
            <div>
                { 
                    this.state.listings.map((listing, i) => {
                        return (
                            <Listing key={i} listingInfo={listing}/>
                        )
                    })
                }
            </div>
        );
    }
}

export default ViewListingDisplay;