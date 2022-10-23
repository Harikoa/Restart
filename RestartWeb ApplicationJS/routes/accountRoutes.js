const express = require("express")
const router = express.Router()
const {addAcc,getAllAcc,editAcc,activate,suspend,signOut,profile,link,getAlumniLinked,getPhyLinked} = require("../controller/accountController")
const {addAccPhy,getAllAccPhy,editAccPhy,activatePhy,suspendPhy} = require("../controller/adminControllerPhysician")
const {addAccAl,getAllAccAl,editAccAl,activateAl,suspendAl} = require("../controller/adminControllerAlumni")
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

router.get("/physician/",(req,res)=>{
    if(firebase.auth().currentUser==null)
    {
        res.redirect('/')
    }
    else
    {
    res.render('../htmlFiles/AdminManagePhysician')
    }
})
router.post("/physician/suspend",suspendPhy)
router.post("/physician/create",addAccPhy)
router.post('/physician/edit',editAccPhy)

router.get("/alumni/",(req,res)=>{
    if(firebase.auth().currentUser==null)
    {
        res.redirect('/')
    }
    else
    {
    res.render('../htmlFiles/AdminManageAlumni')
    }
})
router.post("/alumni/suspend",suspendAl)
router.post("/alumni/create",addAccAl)
router.post('/alumni/edit',editAccAl)

router.get("/profile",(req,res)=>{
    if(firebase.auth().currentUser==null)
    res.redirect('/')
    else
    {
    res.render('../htmlFiles/AdminProfile')
    }
})
router.get('/link',(req,res)=>{
    var r = req.query.res  
    var panel = req.query.panel
    var almsg = ''
    var phymsg = ''
    if(panel==0)
        {
            if(r==-1)
                phymsg="Accounts are already connected!"
            else
                phymsg="Accounts successfully connected"
        }
    else{
        if(r==-1)
        almsg="Accounts are already connected!"
    else
        almsg="Accounts successfully connected"
    }
    res.render("../htmlFiles/AdminLinkPatient",{
        almsg:almsg,
        phymsg:phymsg
    })
})
router.get('/fetchProfile',profile)
router.post('/link',link)
router.get('/alumnilink',getAlumniLinked)
router.get('/phylink',getPhyLinked)
module.exports = router