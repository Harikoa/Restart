const express = require("express")
const router = express.Router()
const firebase = require("../config")
const {getConnectedPatients,link}  = require("../controller/phyController")
router.get("/",(req,res)=>{
    if(firebase.auth().currentUser==null)
    {
        res.redirect('/')
    }
    else{
    res.render("../htmlFiles/PhysicianAllPatientList")
    }
})

router.get("/connected",getConnectedPatients)
router.get("/managePatient",(req,res)=>{
    res.render("../htmlFiles/PhysicianManagePatient")
})
router.get("/profile",(req,res)=>{
    res.render("../htmlFiles/PhysicianProfile")
})
router.get("/link",link)
module.exports=router