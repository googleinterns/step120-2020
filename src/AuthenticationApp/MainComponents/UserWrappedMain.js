import React from "react";
import UserProvider from "../Providers/UserProvider";
import MainPage from "../../MainPage";
function UserWrappedMain() {
  return (
    <UserProvider>
      <MainPage />
    </UserProvider>
  );
}
export default UserWrappedMain;
