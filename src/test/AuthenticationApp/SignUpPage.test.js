import React from "react";
import { shallow } from "enzyme";
import SignUpPage from "../AuthenticationApp/MainComponents/SignUpPage";
import { userEmail, displayName } from "../AuthenticationApp/MainComponents/SignUpPage";
import { userPassword, retypedPassword } from "../AuthenticationApp/MainComponents/SignUpPage";
import { error } from "../AuthenticationApp/MainComponents/SignUpPage";

import { configure } from "enzyme";
import Adapter from "enzyme-adapter-react-16";
configure({ adapter: new Adapter() });

const EMAIL = 'Example@edu.com';
const PASSWORD = '123Password';
const DISPLAY_NAME = 'Name';
const RETYPEDPASSWORD_CORRECT = '123Password';
const RETYPEDPASSWORD_WRONG = 'Password';

// jest.mock('firebaes/app', () => {
//     return {
//         auth: jest.fn().mockReturnThis(),
//         createUserWithEmailAndPassword: jest.fn(),
//     };
// });

// describe('Creates user', () => {
//     afterAll() => {
//         jest.resetAllMocks();
//     });
//     it('should pass without throwing error', async () => {
//         const actual = SignUpPage.createUser()
//     }
// }

describe("SignUp"), () => {
    let wrapper;
    let mockSignUp;

    beforeEach(() => {
        mockSignUp = jest.fn();
        wrapper = shallow( <SignUpPage />);
    });

    //Snapshot test
    it("should match with react snapshot", () => {
        expect(wrapper).toMatchSnapshot();
    });

    describe("handlePasswordChange", () => {
        it("should call setPassword", () => {
            const mockEvent = {
                target: {
                    name: "userPassword",
                    value: "test"
                }
            };

            const expected = {
                userPassword: "test"
            };

            wrappper.instance().onUserPasswordChangeHandler(mockEvent);

            expect(wrapper.userPassword).toEqual(expected);
            
        })
    })
}
