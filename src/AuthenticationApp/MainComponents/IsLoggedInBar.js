import React from "react";
import { authentication } from "../firebase";

const IsLoggedInBar = () => {
    const fireUser = authentication.currentUser;
    return(
        <div>
            <div>
                <h2>{fireUser.displayName}</h2>
                <h3>{fireUser.email}</h3>
            </div>
            <button onClick = {() => {
                authentication.signOut();
            }}> Sign Out Here </button>
        </div>
    )
};
export default IsLoggedInBar;