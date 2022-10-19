const express = require("express")
const router = express.Router()
const {addAcc,getAllAcc,editAcc,activate,suspend,signOut} = require("../controller/accountController")
const firebase = require('../config.js')

router.get("/",(req,res)=>{
    if(firebase.auth().currentUser==null)
    {
        res.redirect('/')
    }
    else
    {
    var type= req.query.type
    var msg =''
    if(type=='1')
    msg = "Success!"
    else if (type=='email')
    msg = "Email is already in use!"
    res.render("../htmlFiles/AdminManagePatient",{msg})
    }
})
router.get("/getTable",async (req,res)=>{
    var role = req.query.role
    res.json({accounts:await getAllAcc(role)})
})

router.get("/activate",async(req,res)=>{
    var bool = req.query.bool
    var email = req.query.email
    console.log("HELLO")
    await activate(bool,email)

})
router.get("/signOut",signOut)
router.post("/suspend",suspend)
router.post("/create",addAcc)
router.post('/edit',editAcc)

module.exports = router