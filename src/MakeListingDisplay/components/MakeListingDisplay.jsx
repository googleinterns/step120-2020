import React, { Component } from 'react';

import InputField from './InputField';

import '../styles/MakeListingDisplay.css';

/** Component class that displays the input fields for a user to post a listing*/
class MakeListingDisplay extends Component {
    render(){
        return (
            <div>
                <div id="header">
                    <h1>Post a Listing</h1>
                </div>
                <form action="/listings" id="listingForm" method="POST">
                    <InputField fieldHeader="Title:" fieldName="title" fieldType="text" />
                    <InputField fieldHeader="Description:" fieldName="description" fieldType="text" />
                    <InputField fieldHeader="Total number of rooms in apartment:" fieldName="numRooms" fieldType="number" />
                    
                    <p>Number of rooms for rent:</p>
                    <div id="singleInfo">
                        <InputField fieldHeader="Singles:" fieldName="numSingles" fieldType="number"/>
                        <InputField fieldHeader="Monthly Rent Per Single:" fieldName="singlePrice" fieldType="number" fieldStep="0.01"/>
                    </div>
                    <div id="sharedInfo">
                        <InputField fieldHeader="Shared:" fieldName="numShared" fieldType="number" />
                        <InputField fieldHeader="Monthly Rent Per Shared Room:" fieldName="sharedPrice" fieldType="number" fieldStep="0.01"/>
                    </div>
                    <div id="listingPriceDisplay"></div>

                    <InputField fieldHeader="" fieldName="listingPrice" fieldType="hidden" />
                    <InputField fieldHeader="Number of bathrooms:" fieldName="numBathrooms" fieldType="number" />
                    
                    <p>Lease type:</p>
                    <select name="leaseTypes" size="2" required>
                        <option value="YEAR_LONG">Year-long</option>
                        <option value="MONTH_TO_MONTH">Month-to-Month</option>
                    </select>
                    <InputField fieldHeader="Lease Start Date:" fieldName="startDate" fieldType="date" />
                    <InputField fieldHeader="Lease End Date:" fieldName="endDate" fieldType="date" />
                    <input type="submit" value="Post Listing"/>
                </form>
            </div>
        );
    }
}

export default MakeListingDisplay;