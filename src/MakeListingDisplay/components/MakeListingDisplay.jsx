import React, { Component } from 'react';

import '../styles/MakeListingDisplay.css';

class MakeListingDisplay extends Component {
    constructor(props){
        super(props);

        this.state = {
            title: '',
            description: '',
            totalNumOfRooms: '',
            numOfRoomsForRentSingles: '',
            singleRoomPrice: '',
            numOfRoomsForRentShared: '',
            sharedRoomPrice: '',
            numOfBathrooms: '',
            leaseType: '',
            startDate: '',
            endDate: ''
        };

        this.handleSubmit = this.handleSubmit.bind(this);
        this.handleChange = this.handleChange.bind(this);
    }

    handleChange(event) {
        const target = event.target;
        this.setState({
            [target.name]: target.value
        });
    }

    handleSubmit(event) {
        alert("GOT IT!")
        console.log(this.state);
        event.preventDefault();
    }

    render(){
        return (
            <div className="make-a-listing-body">
                <div id="header">
                    <h1>Post a Listing</h1>
                </div>
                <form onSubmit={this.handleSubmit}>
                    <p>Title:</p>
                    <input type="text" name="title" value={this.state.title} 
                    placeholder="Enter a title here" onChange={this.handleChange} required/>

                    <p>Description:</p>
                    <input type="text" name="description" value={this.state.description} 
                    placeholder="Enter a description here" onChange={this.handleChange} required/>
                    
                    <p>Total number of rooms in apartment:</p>
                    <input type="number" name="totalNumOfRooms"  
                    min="1" onChange={this.handleChange} required/>

                    <p>Number of rooms for rent:</p>
                    <label> Singles: </label>
                    <input type="number" name="numOfRoomsForRentSingles" min="0" 
                    onChange={this.handleChange} required/>
                    <p>Monthly Rent Per Single:</p>
                    <label> $ </label>
                    <input type="number" name="singleRoomPrice" min="0"
                    step="0.5" onChange={this.handleChange} required/>
                    <br></br>

                    <label> Shared: </label>
                    <input type="number" name="numOfRoomsForRentShared" min="0"
                    onChange={this.handleChange} required/>
                    <p>Monthly Rent Per Shared Room:</p>
                    <label> $ </label>
                    <input type="number" name="sharedRoomPrice" min="0"
                    step="0.01" onChange={this.handleChange} required/>

                    <p>Number of bathrooms:</p>
                    <input type="number" name="numOfBathrooms" min="0" onChange={this.handleChange} required/>

                    <p>Lease type:</p>
                    <select name="leaseType" size="2" onChange={this.handleChange} required>
                        <option value="YEAR_LONG">Year-long</option>
                        <option value="MONTH_TO_MONTH">Month-to-Month</option>
                    </select>
                    <label>Lease Start Date:</label>
                    <input type="date" name="startDate" onChange={this.handleChange} required/>
                    <label>Lease End Date:</label>
                    <input type="date" name="endDate" onChange={this.handleChange} required/>
                    
                    <input type="submit" value="Post Listing"/>
                </form>
            </div>
        );
    }
}

export default MakeListingDisplay;

/*


                    
   
    

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
*/