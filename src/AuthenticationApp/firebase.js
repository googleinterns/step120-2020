import firebase from "firebase/app";
import "firebase/auth";
import "firebase/firestore";
import "firebase/analytics";
import APIKey from "./APIKey";
 
const firebaseConfig = {
    apiKey: APIKey,
    authDomain: "step120-2020.firebaseapp.com",
    databaseURL: "https://step120-2020.firebaseio.com",
    projectId: "step120-2020",
    storageBucket: "step120-2020.appspot.com",
    messagingSenderId: "994372108603",
    appId: "1:994372108603:web:cb5d95fa2ec4d107b1b71e",
    measurementId: "G-C0ZHMWXRWH"
  };
 
  firebase.initializeApp(firebaseConfig);
  //firebase.analytics();
  export const generateUserDocument = async (user, furtherData) => {
      if(!user) return;
      
      //fill doc with user data
      
      return getUserDocument(user.uid);
  };
  const getUserDocument = async uid => {
      if (!uid) return null;
      try {
          const userDocument = await firestore.doc(`users/${uid}`).get();
          return {
              uid,
              ...userDocument.data()
          };
      } catch (error) {
          console.error("Error fetching user", error);
      }
  };
  export const authentication = firebase.auth();
  export const firestore = firebase.firestore();
