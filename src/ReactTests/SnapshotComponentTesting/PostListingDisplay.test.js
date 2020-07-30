import React from 'react';
import renderer from 'react-test-renderer';
import PostListingDisplay from '../../PostListingDisplay/components/PostListingDisplay.jsx';
import InputField from '../../PostListingDisplay/components/InputField.jsx';

test('PostListingDisplay renders correctly', () => {
  const tree = renderer
    .create(<PostListingDisplay />)
    .toJSON();
  expect(tree).toMatchSnapshot();
});

test('Blank InputField renders correctly', () => {
  const tree = renderer
    .create(<InputField />)
    .toJSON();
  expect(tree).toMatchSnapshot();
});

test('InputField with just one of each prop renders correctly', () => {
  const headerComponent = renderer.create(<InputField fieldHeader="Header"/>);
  let tree = headerComponent.toJSON();
  expect(tree).toMatchSnapshot();

  const nameComponent = renderer.create(<InputField fieldName="name"/>);
  tree = nameComponent.toJSON();
  expect(tree).toMatchSnapshot();

  const typeComponent = renderer.create(<InputField fieldType="text"/>);
  tree = typeComponent.toJSON();
  expect(tree).toMatchSnapshot();
});

test('InputField renders correctly', () => {
  const tree = renderer
    .create(<InputField fieldHeader="Header" fieldName="name" fieldType="text"/>)
    .toJSON();
  expect(tree).toMatchSnapshot();
});