import React, { Component } from 'react';

import InputField from './InputField';

import '../styles/PostListingDisplay.css';

/**Component class that displays the input fields for a user to post a listing */
class PostListingDisplay extends Component {
    constructor(props){
        super(props);

        this.state = {
            userInput:{
                title: '',
                description: '',
                numRooms: '',
                numSingles: '',
                singlePrice: '',
                numShared: '',
                sharedPrice: '',
                numBathrooms: '',
                leaseTypes: '',
                startDate: '',
                endDate: '',
                listingPrice: ''
            }
        };

        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        let value = event.target.value;
        let name = event.target.name;
        this.setState( prevState => {
            return { 
                userInput : {
                    ...prevState.userInput, [name]: value
                }
            }
        }
        )
    }

    render(){
        return (
            <div id="make-a-listing-body">
                <div id="header">
                    <h1>Post a Listing</h1>
                </div>

                <form action="/listings" method="POST">
                    <InputField fieldHeader="Title:" fieldName="title" fieldType="text" onChange={this.handleChange} />
                    <InputField fieldHeader="Description:" fieldName="description" fieldType="text" onChange={this.handleChange} />
                    <InputField fieldHeader="Total number of rooms in apartment:" fieldName="numRooms" fieldType="number" onChange={this.handleChange} />

                    <p>Number of rooms for rent:</p>
                    <div id="singleInfo">
                        <InputField fieldHeader="Singles:" fieldName="numSingles" fieldType="number" onChange={this.handleChange} />
                        <InputField fieldHeader="Monthly Rent Per Single:" fieldName="singlePrice" fieldType="number" onChange={this.handleChange} />
                    </div>
                    <div id="sharedInfo">
                        <InputField fieldHeader="Shared:" fieldName="numShared" fieldType="number" onChange={this.handleChange} />
                        <InputField fieldHeader="Monthly Rent Per Shared Room:" fieldName="sharedPrice" fieldType="number" onChange={this.handleChange} />
                    </div>
                    <div id="listingPriceDisplay"></div>
                  {/**TODO: Joey: Implement price calculation for listing price so a user doesn't have to input it in this line below. */}
                    <InputField fieldHeader="Listing Price:" fieldName="listingPrice" fieldType="number" onChange={this.handleChange} />

                    <InputField fieldHeader="Number of bathrooms:" fieldName="numBathrooms" fieldType="number" onChange={this.handleChange} />

                    <p>Lease type:</p>
                    <select name="leaseTypes" size="2" onChange={this.handleChange} required>
                        <option value="YEAR_LONG">Year-long</option>
                        <option value="MONTH_TO_MONTH">Month-to-Month</option>
                    </select>
                    <InputField fieldHeader="Lease Start Date:" fieldName="startDate" fieldType="date" onChange={this.handleChange} />
                    <InputField fieldHeader="Lease End Date:" fieldName="endDate" fieldType="date" onChange={this.handleChange} />
                    <input id="post-listing-button" type="submit" value="Post Listing"/>
                </form>
            </div>
        );
    }
}

export default PostListingDisplay;