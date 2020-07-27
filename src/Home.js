import React, { useContext } from 'react';
import PostCommentsDisplay from './PostCommentsDisplay/components/PostCommentsDisplay';
import { UserContext } from "./AuthenticationApp/Providers/UserProvider";
import IsLoggedInBar from "./AuthenticationApp/MainComponents/IsLoggedInBar";
import NotLoggedInBar from "./AuthenticationApp/MainComponents/NotLoggedInBar";

/**Component class to render the whole comments page */
function Home () {
    const user = useContext(UserContext);
    return (
        user ?
            [
                <IsLoggedInBar />,
                <PostCommentsDisplay />
            ]
        :
            [
                <NotLoggedInBar />,
                <PostCommentsDisplay />
            ]
        
            
    );
}

export default Home;
