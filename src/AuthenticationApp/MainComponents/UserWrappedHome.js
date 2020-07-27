import React from "react";
import UserProvider from "../Providers/UserProvider";
import Home from "../../Home";
function UserWrappedHome() {
  return (
    <UserProvider>
      <Home />
    </UserProvider>
  );
}
export default UserWrappedHome;
