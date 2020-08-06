import React from "react";
import { navigate } from "@reach/router";

function NotLoggedInBar() {
  return (
    <div>
        <button onClick ={() => {navigate("signIn")}}>Login</button>
    </div>
  );
}
export default NotLoggedInBar;
