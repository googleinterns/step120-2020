import React, { useContext } from 'react';
import { UserContext } from "./AuthenticationApp/Providers/UserProvider";
import IsLoggedInBar from "./AuthenticationApp/MainComponents/IsLoggedInBar";
import NotLoggedInBar from "./AuthenticationApp/MainComponents/NotLoggedInBar";
import { Link } from "@reach/router";

/**Component class to render the whole comments page */
function MainPage () {
    const user = useContext(UserContext);
    return (
        user ?
            [
                <IsLoggedInBar />,
                <div>
                    <h1>Roomies</h1>

                    <nav>
                        <Link to="home">
                        <button type="button">
                            Home
                        </button>
                        </Link>
                        <Link to="makeListing">
                        <button type="button">
                            Make a Listing
                        </button>
                        </Link>
                        <Link to="postComment" >
                        <button type="button">
                            Post a comment
                        </button>
                        </Link>
                    </nav>
                </div>
            ]
        :
            [
                <NotLoggedInBar />,
                <div>
                    <h1>Roomies</h1>

                    <nav>
                        <Link to="/home">
                        <button type="button">
                            Home
                        </button>
                        </Link>
                        <Link to="/makeListing">
                        <button type="button">
                            Make a Listing
                        </button>
                        </Link>
                        <Link to="/postComment" >
                        <button type="button">
                            Post a comment
                        </button>
                        </Link>
                    </nav>
                </div>
            ]
        
            
    );
}

export default MainPage;
