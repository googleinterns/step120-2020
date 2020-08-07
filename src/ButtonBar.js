import React from "react";
import { Link } from "@reach/router";

const ButtonBar = () => {
    return (
        <div>
            <h1 className="site-header" >Roomies</h1>
            <nav>
                <Link to="/">
                    <button className="nav-button" type="button">
                        Home
                    </button>
                </Link>
                <Link to="viewListing">
                    <button className="nav-button" type="button">
                        View a Listing
                    </button>
                </Link>
                <Link to="postListing">
                    <button className="nav-button" type="button">
                        Post a Listing
                    </button>
                </Link>
            </nav>
        </div>
    )
}
export default ButtonBar;