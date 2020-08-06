import * as firebase from 'firebase';
import React from 'react';
import ReactDOM from 'react-dom';
import SignUpPage from '../../AuthenticationApp/MainComponents/SignUpPage';
import { shallow } from "enzyme";
import { mount } from "enzyme";
import { create } from "react-test-renderer";
import renderer from 'react-test-renderer'

import { configure } from "enzyme";
import Adapter from "enzyme-adapter-react-16";
configure({ adapter: new Adapter() });


describe('<SignUpPage />', () => {
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
            ReactDOM.render(<SignUpPage />, domDiv);
        });

        it("renders a snapshot of Component", () => {
            const snapshot = renderer.create(<SignUpPage />).toJSON();
            expect(snapshot).toMatchSnapshot();
        })

    })

    it("should update userPassword state", () => {
        wrapper = mount(<SignUpPage onUserPasswordChangeHandler={setState} />);
        
        wrapper.find('.userPassword').simulate('change');
        expect(setState).toBeTruthy();
    });

    it("should update userEmail state", () => {
        wrapper = mount(<SignUpPage onUserEmailChangeHandler={setState} />);
        
        wrapper.find('.userEmail').simulate('change');
        expect(setState).toBeTruthy();
    });

    it("should update displayName state", () => {
        wrapper = mount(<SignUpPage onDisplayNameChangeHandler={setState} />);
        
        wrapper.find('.displayName').simulate('change');
        expect(setState).toBeTruthy();
    });

    it("should update retypedPassword state", () => {
        wrapper = mount(<SignUpPage onRetypedPasswordChangeHandler={setState} />);
        
        wrapper.find('.retypedPassword').simulate('change');
        expect(setState).toBeTruthy();
    });
    
});
    

