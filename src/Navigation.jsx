import React from 'react';
 
import { NavLink } from 'react-router-dom';
 
const Navigation = () => {
    return (
       <div>
          <NavLink to="/">Home</NavLink>
          <NavLink to="/makeListing">Make a Listing</NavLink>
       </div>
    );
}
 
export default Navigation;