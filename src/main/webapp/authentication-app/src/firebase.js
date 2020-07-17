import firebase from "firebase/app";
import "firebase/auth";
import "firebase/firestore";

const firebaseConfig = {
    apiKey: "AIzaSyAjse2YjCJHUUe5w1i7I9jkjH8cqqRcAIU",
    authDomain: "step120-2020.firebaseapp.com",
    databaseURL: "https://step120-2020.firebaseio.com",
    projectId: "step120-2020",
    storageBucket: "step120-2020.appspot.com",
    messagingSenderId: "994372108603",
    appId: "1:994372108603:web:cb5d95fa2ec4d107b1b71e",
    measurementId: "G-C0ZHMWXRWH"
  };

  firebase.initializeApp(firebaseConfig);
  firebase.analytics();
  export const authentication = firebase.auth();
  export const firestore = firebase.firestore();