import React, { useContext } from "react";
import { UserContext } from "../providers/UserProvider";
import { authentication } from "../firebase";
import { navigate } from "@reach/router";
 
const LoggedInPage = () => {
    const user = useContext(UserContext);
    const fireUser = authentication.currentUser;
    console.log(fireUser);
    console.log(user);
    return (
        <div>
            <div>
                <h2>{fireUser.displayName}</h2>
                <h3>{fireUser.email}</h3>
            </div>
            <button onClick = {() => {
                authentication.signOut();
                navigate('/')}}>Sign Out Here</button>
        </div>
        
 
    )
 
};
export default LoggedInPage;
