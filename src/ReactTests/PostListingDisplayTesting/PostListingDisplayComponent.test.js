import React from 'react';
import ReactDOM from 'react-dom';
import PostListingDisplay from '../../PostListingDisplay/components/PostListingDisplay.jsx';
import {render, fireEvent, cleanup} from '@testing-library/react';

afterEach(cleanup)

//testing a controlled component form.
it('Inputing text updates the state', () => {
    const wrapper = render(<PostListingDisplay />);
    const titleInput = wrapper.getByLabelText('Title:');
    expect(titleInput.value).toBe('None');
    fireEvent.change(titleInput, { target: { value: 'Text' } });
    expect(titleInput.value).toEqual('Text');
})


it('Inputing room info (number and price) updates listingPrice', () => {
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