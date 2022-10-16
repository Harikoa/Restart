const express = require("express")
const router = express.Router()
const {addAcc,getAllAcc} = require("../controller/accountController")
const firebase = require('../config.js')

router.get("/",(req,res)=>{
   
    var type= req.query.type
    var msg =''
    if(type=='1')
    msg = "Success!"
    else if (type=='email')
    msg = "Email is already in use!"
    res.render("../htmlFiles/AdminManagePatient",{msg})
    
})
router.get("/getTable",async (req,res)=>{
    var role = req.query.role
    res.json({accounts:await getAllAcc(role)})
})
router.post("/create",addAcc)
module.exports = router