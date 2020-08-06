import React, { useContext } from 'react';
import { UserContext } from "./AuthenticationApp/Providers/UserProvider";
import IsLoggedInBar from "./AuthenticationApp/MainComponents/IsLoggedInBar";
import NotLoggedInBar from "./AuthenticationApp/MainComponents/NotLoggedInBar";
import ButtonBar from "./ButtonBar";

/** Component class to render the whole comments page */
function MainPage () {
    const user = useContext(UserContext);
    return (
        user ?
            [
                <IsLoggedInBar />,
                <ButtonBar />
            ]
        :
            [
                <NotLoggedInBar />,
                <ButtonBar />
            ]      
    );
}

export default MainPage;
