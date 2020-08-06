import React from "react";
import { Link } from "@reach/router";

const ButtonBar = () => {
    return (
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
    )
}
export default ButtonBar;