import React, { useContext } from 'react';
import { UserContext } from "./AuthenticationApp/Providers/UserProvider";
import IsLoggedInBar from "./AuthenticationApp/MainComponents/IsLoggedInBar";
import NotLoggedInBar from "./AuthenticationApp/MainComponents/NotLoggedInBar";
import ButtonBar from "./ButtonBar";
import Home from "./Home/components/Home"

/** Component class to render the whole comments page */
function MainPage () {
    const user = useContext(UserContext);
    return (
        user ?
            [
                <IsLoggedInBar />,
                <ButtonBar />,
                <Home />
            ]
        :
            [
                <NotLoggedInBar />,
                <ButtonBar />,
                <Home />
            ]      
    );
}

export default MainPage;
