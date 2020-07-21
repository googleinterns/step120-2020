import React, { Component } from 'react';

import '../styles/ListingDisplay.css';

class MakeListingDisplay extends Component {
    render(){
        return (
            <div>
                <div id="header">
                    <h1>Post a Listing</h1>
                </div>
                <form action="/listings" id="listingForm" method="POST">
                    <p>Title:</p>
                    <input name="title" placeholder="Enter your title here" required/>

                    <p>Description:</p>
                    <input name="description" placeholder="Enter a description here" required/>

                    <p>Total number of rooms in apartment:</p>
                    <input type="number" id="numRooms" name="numRooms" min="1" required/>
                    
                    <p>Number of rooms for rent:</p>
                    <label for="numSingles"> Singles: </label>
                    <input type="number" id="numSingles" name="numSingles" min="0" value="0"
                    onClick="this.select()" onchange="calculateListingPrice()" required/>
                    
                    <p>Monthly Rent Per Single:</p>
                    <label for="singlePrice"> $ </label>
                    <input type="number" id="singlePrice" name="singlePrice" min="0" value="0"
                    step="0.01" onClick="this.select()" onchange="calculateListingPrice()" required/>
                    <label for="numShared"> Shared: </label>
                    <input type="number" id="numShared" name="numShared" min="0" value="0"
                    onClick="this.select()" onchange="calculateListingPrice()" required/>
                    
                    <p>Monthly Rent Per Shared Room:</p>
                    <label for="sharedPrice"> $ </label>
                    <input type="number" id="sharedPrice" name="sharedPrice" min="0" value="0"
                    step="0.01" onClick="this.select()" onchange="calculateListingPrice()" required/>
                    <div id="listingPriceDisplay"></div>
                    <input id="listingPrice" name="listingPrice" type="hidden"/>
                    
                    <p>Number of bathrooms:</p>
                    <input type="number" id="numBathrooms" name="numBathrooms" min="0" required/>
                    
                    <p>Lease type:</p>
                    <select name="leaseTypes" size="2" required>
                        <option value="YEAR_LONG">Year-long</option>
                        <option value="MONTH_TO_MONTH">Month-to-Month</option>
                    </select>
                    <label for="startDate">Lease Start Date:</label>
                    <input type="date" id="startDate" name="startDate" required/>
                    <label for="endDate">Lease End Date:</label>
                    <input type="date" id="endDate" name="endDate" required/>
                    <input type="submit" value="Post Listing"/>
                </form>
            </div>
        );
    }
}

export default MakeListingDisplay;