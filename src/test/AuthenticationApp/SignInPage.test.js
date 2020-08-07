import * as firebase from 'firebase';
import React from 'react';
import ReactDOM from 'react-dom';
import SignInPage from '../../AuthenticationApp/MainComponents/SignInPage';
import { shallow } from "enzyme";
import { mount } from "enzyme";
import { create } from "react-test-renderer";
import renderer from 'react-test-renderer'

import { configure } from "enzyme";
import Adapter from "enzyme-adapter-react-16";
configure({ adapter: new Adapter() });


describe('<SignInPage />', () => {
    let wrapper;
    const setState = jest.fn();
    const useStateSpy = jest.spyOn(React, 'useState');
    useStateSpy.mockImplementation((init) => [init, setState]);

    afterEach(() => {
        jest.clearAllMocks();
    });

    describe("Snapshot Test" , () => {
        it("should render without crashing", () => {
            const domDiv = document.createElement('div');
            ReactDOM.render(<SignInPage />, domDiv);
        });

        it("renders a snapshot of Component", () => {
            const snapshot = renderer.create(<SignInPage />).toJSON();
            expect(snapshot).toMatchSnapshot();
        })

    })

    it("should update userPassword state", () => {
        wrapper = mount(<SignInPage onUserPasswordChangeHandler={setState} />);
        
        wrapper.find('.userPassword').simulate('change');
        expect(setState).toBeTruthy();
    });

    it("should update userEmail state", () => {
        wrapper = mount(<SignInPage onUserEmailChangeHandler={setState} />);
        
        wrapper.find('.userEmail').simulate('change');
        expect(setState).toBeTruthy();
    });
    
});