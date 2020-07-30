import React from 'react';
import ReactDOM from 'react-dom';
import PostListingDisplay from '../../../PostListingDisplay/components/PostListingDisplay.jsx';
import {render, fireEvent, cleanup} from '@testing-library/react';

afterEach(cleanup)

it('Inputing Title text updates the state', () => {
    const wrapper = render(<PostListingDisplay />);
    const titleInput = wrapper.getByLabelText('Title:');
    expect(titleInput.value).toBe('None');
    fireEvent.change(titleInput, { target: { value: 'Text' } });
    expect(titleInput.value).toEqual('Text');
})

it('Inputing Description text updates the state', () => {
    const wrapper = render(<PostListingDisplay />);
    const descriptionInput = wrapper.getByLabelText('Description:');
    expect(descriptionInput.value).toBe('None');
    fireEvent.change(descriptionInput, { target: { value: 'Text' } });
    expect(descriptionInput.value).toEqual('Text');
})

it('Inputing Number of Rooms text updates the state', () => {
    const wrapper = render(<PostListingDisplay />);
    const numRoomsInput = wrapper.getByLabelText('Total number of rooms in apartment:');
    expect(numRoomsInput.value).toBe('0');
    fireEvent.change(numRoomsInput, { target: { value: '2' } });
    expect(numRoomsInput.value).toEqual('2');
})

it('Inputing all room info (number and price) updates listingPrice', () => {
    const wrapper = render(<PostListingDisplay />);
    const listingPriceValue = wrapper.getByLabelText('');
    const numSinglesInput = wrapper.getByLabelText('Singles:');
    const singlePriceInput = wrapper.getByLabelText('Monthly Rent Per Single:');
    const numSharedInput = wrapper.getByLabelText('Shared:');
    const sharedPriceInput = wrapper.getByLabelText('Monthly Rent Per Shared Room:');
    fireEvent.change(numSinglesInput, { target: { value: '1' } });
    fireEvent.change(singlePriceInput, { target: { value: '1000' } });
    fireEvent.change(numSharedInput, { target: { value: '2' } });
    fireEvent.change(sharedPriceInput, { target: { value: '1500' } });
    expect(listingPriceValue.value).toEqual('4000');
})

it('Inputing singles info updates listingPrice', () => {
    const wrapper = render(<PostListingDisplay />);
    const listingPriceValue = wrapper.getByLabelText('');
    const numSinglesInput = wrapper.getByLabelText('Singles:');
    const singlePriceInput = wrapper.getByLabelText('Monthly Rent Per Single:');
    fireEvent.change(numSinglesInput, { target: { value: '1' } });
    fireEvent.change(singlePriceInput, { target: { value: '1000' } });
    expect(listingPriceValue.value).toEqual('1000');
})

it('Inputing shared info updates listingPrice', () => {
    const wrapper = render(<PostListingDisplay />);
    const listingPriceValue = wrapper.getByLabelText('');
    const numSharedInput = wrapper.getByLabelText('Shared:');
    const sharedPriceInput = wrapper.getByLabelText('Monthly Rent Per Shared Room:');
    fireEvent.change(numSharedInput, { target: { value: '2' } });
    fireEvent.change(sharedPriceInput, { target: { value: '1500' } });
    expect(listingPriceValue.value).toEqual('3000');
})

it('Inputing 0 updates listingPrice', () => {
    const wrapper = render(<PostListingDisplay />);
    const listingPriceValue = wrapper.getByLabelText('');
    const numSinglesInput = wrapper.getByLabelText('Singles:');
    const singlePriceInput = wrapper.getByLabelText('Monthly Rent Per Single:');
    fireEvent.change(numSinglesInput, { target: { value: '0' } });
    fireEvent.change(singlePriceInput, { target: { value: '1000' } });
    expect(listingPriceValue.value).toEqual('0');
})