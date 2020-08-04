import React from 'react';

import PostCommentsDisplay from '../../PostCommentsDisplay/components/PostCommentsDisplay.jsx';

import '../styles/Listing.css';

/** Functional component that creates a listing. Takes in listingInfo as a prop 
 * (which holds the title, description, listingPrice, etc.) to fill in the listing
 */
function Listing(props) {
    const { 
        title, 
        description, 
        listingPrice, 
        numRooms, 
        numSingles, 
        numShared, 
        singlePrice, 
        sharedPrice, 
        leaseType, 
        startDate, 
        endDate, 
        numBathrooms 
    } = props.listingInfo;

    const roomInfo = getRoominfo(numSingles, numShared, singlePrice, sharedPrice);
    const leaseTypeText = (leaseType === "YEAR_LONG") ? "Yearly" : "Monthly";
    return (
        <div className="listing-box">
            <h1 className="title">{title}</h1>
            <p className="description">{description}</p>
            <p className="listingPrice">Listing Price: {listingPrice}</p>
            <p className="leaseType">Lease Type: {leaseTypeText} Lease</p>
            <div className="avaiability-container">
                <h3>Availability</h3>
                <p>Start: {startDate}</p>
                <p>End: {endDate}</p>
            </div>
            <div className="rooms-info-containter">
                <h3>Room(s) Information:</h3>
                <p>Total number of room(s): {numRooms}</p>
                { roomInfo }
                <p>Total number of bathroom(s): {numBathrooms}</p>
            </div>
            <PostCommentsDisplay/>
        </div>
    );
}

function getRoominfo(numSingles, numShared, singlePrice, sharedPrice){
    if((numSingles === '0' || singlePrice === 'USD 0') && (numShared === '0' || sharedPrice === 'USD 0')){
        return "No rooms available";
    }
    else if(numSingles === '0' || singlePrice === 'USD 0'){
        return numShared + " shared room(s) for " + sharedPrice;
    }
    else if(numShared === '0' || sharedPrice === 'USD 0'){
        return numSingles + " single room(s) for " + singlePrice;
    }
    else {
        return numSingles + " single room(s) for " + singlePrice + " and " + numShared + " shared room(s) for " + sharedPrice;;
    }
}

export default Listing;