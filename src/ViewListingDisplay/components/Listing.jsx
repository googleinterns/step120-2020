import React from 'react';

import PostCommentsDisplay from '../../PostCommentsDisplay/components/PostCommentsDisplay.jsx';

import '../styles/Listing.css';

/** Component class that displays the input fields for a user to post a listing */
function Listing(props) {
    const { title, description, listingPrice, numRooms, numSingles, numShared, singlePrice, sharedPrice } = props.listingInfo;
    const roomInfo = getRoominfo(numSingles, numShared, singlePrice, sharedPrice);
    return (
        <div className="listing-box">
            <h1 className="title">{title}</h1>
            <p className="description">{description}</p>
            <p className="listingPrice">Listing Price: {listingPrice}</p>
            <div className="rooms-info">
                <h2>Rooms Information:</h2>
                <p>Total number of rooms: {numRooms}</p>
                { roomInfo }
            </div>
            <PostCommentsDisplay/>
        </div>
    );
}

function getRoominfo(numSingles, numShared, singlePrice, sharedPrice){
    if((numSingles === '0' || numShared === 'USD 0') && (singlePrice === '0' || sharedPrice === 'USD 0')){
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

/*
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
*/