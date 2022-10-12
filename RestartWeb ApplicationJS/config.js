const firebase = require("firebase")
const firebaseConfig = {
    apiKey: "AIzaSyDLVanW64wTt__5xdCnM-hhhufGLZJHtOM",
    authDomain: "restart-aa1cc.firebaseapp.com",
    projectId: "restart-aa1cc",
    storageBucket: "restart-aa1cc.appspot.com",
    messagingSenderId: "661573872148",
    appId: "1:661573872148:web:134335c72cdb7d2e6ad501",
    measurementId: "G-NT125R9BF6"
  };
  firebase.initializeApp(firebaseConfig);
  const db = firebase.firestore()

  module.exports = db;