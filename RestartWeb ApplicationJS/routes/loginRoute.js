const express = require("express")
const router = express.Router()
const firebase  = require("../config")
const auth = firebase.auth()
const firestore = firebase.firestore()
router.get("/",(req,res)=>
{
    res.render("../htmlFiles/login.ejs")

})

router.post("/",(req,res)=>{
    console.log("HELLOW")
   
    
    const email = req.body.email
    const pw = req.body.pw
    auth.signInWithEmailAndPassword(email, pw).then((user)=>{
        
        console.log("Successful Login!")
    })
    .catch((e)=>{
        console.log(e.message)
        res.render("../htmlFiles/login.ejs",{error:"Invalid Credentials"})
    });
 
})
module.exports = router;