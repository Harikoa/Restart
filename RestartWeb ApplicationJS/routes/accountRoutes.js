const express = require("express")
const router = express.Router()
const {addAcc,getAllAcc} = require("../controller/accountController")

router.get("/",(req,res)=>{
    res.render("../htmlFiles/AdminManagePatient")
})

module.exports = router