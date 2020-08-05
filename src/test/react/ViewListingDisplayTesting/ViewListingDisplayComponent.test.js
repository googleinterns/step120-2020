import React from 'react';
import ReactDOM from 'react-dom';
import ViewListingDisplay from '../../../ViewListingDisplay/components/ViewListingDisplay.jsx';
import Listing from '../../../ViewListingDisplay/components/Listing.jsx';
import {render, fireEvent, cleanup} from '@testing-library/react';

afterEach(cleanup)

/** Tests for the getRoomInfo function */
it('Providing all room info produces correct output', () => {
    const testListing = {
        numSingles: "1", 
        numShared: "1", 
        singlePrice: "USD 1000", 
        sharedPrice: "USD 1000", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('1 single room(s) for USD 1000 and 1 shared room(s) for USD 1000');
})

it('Providing just single room info produces correct output', () => {
    const testListing = {
        numSingles: "1", 
        numShared: "0", 
        singlePrice: "USD 1000", 
        sharedPrice: "USD 0", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('1 single room(s) for USD 1000');
})

it('Providing no numShared info produces correct output', () => {
    const testListing = {
        numSingles: "1", 
        numShared: "0", 
        singlePrice: "USD 1000", 
        sharedPrice: "USD 1000", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('1 single room(s) for USD 1000');
})

it('Providing no sharedPrice info produces correct output', () => {
    const testListing = {
        numSingles: "1", 
        numShared: "1", 
        singlePrice: "USD 1000", 
        sharedPrice: "USD 0", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('1 single room(s) for USD 1000');
})

it('Providing just shared room info produces correct output', () => {
    const testListing = {
        numSingles: "0", 
        numShared: "1", 
        singlePrice: "USD 0", 
        sharedPrice: "USD 1000", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('1 shared room(s) for USD 1000');
})

it('Providing no numSingles info produces correct output', () => {
    const testListing = {
        numSingles: "0", 
        numShared: "1", 
        singlePrice: "USD 1000", 
        sharedPrice: "USD 1000", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('1 shared room(s) for USD 1000');
})

it('Providing no singlePrice info produces correct output', () => {
    const testListing = {
        numSingles: "1", 
        numShared: "1", 
        singlePrice: "USD 0", 
        sharedPrice: "USD 1000", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('1 shared room(s) for USD 1000');
})

it('Providing no room info produces correct output', () => {
    const testListing = {
        numSingles: "0", 
        numShared: "0", 
        singlePrice: "USD 0", 
        sharedPrice: "USD 0", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('No rooms available');
})

it('Providing not enough room info produces correct output', () => {
    const testListing = {
        numSingles: "1", 
        numShared: "0", 
        singlePrice: "USD 0", 
        sharedPrice: "USD 0", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('No rooms available');
})

it('Providing not enough room info produces correct output', () => {
    const testListing = {
        numSingles: "0", 
        numShared: "0", 
        singlePrice: "USD 0", 
        sharedPrice: "USD 0", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('No rooms available');
})

it('Providing not enough room info produces correct output', () => {
    const testListing = {
        numSingles: "0", 
        numShared: "1", 
        singlePrice: "USD 0", 
        sharedPrice: "USD 0", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('No rooms available');
})

it('Providing not enough room info produces correct output', () => {
    const testListing = {
        numSingles: "0", 
        numShared: "0", 
        singlePrice: "USD 1000", 
        sharedPrice: "USD 1000", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('No rooms available');
})

it('Providing not enough room info produces correct output', () => {
    const testListing = {
        numSingles: "0", 
        numShared: "1", 
        singlePrice: "USD 1000", 
        sharedPrice: "USD 0", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('No rooms available');
})

it('Providing not enough room info produces correct output', () => {
    const testListing = {
        numSingles: "1", 
        numShared: "0", 
        singlePrice: "USD 0", 
        sharedPrice: "USD 1000", 
    }
    const wrapper = render(<Listing listingInfo={testListing}/>);
    const roomInfo = wrapper.getByRole('roomInfo');
    expect(roomInfo.textContent).toEqual('No rooms available');
})