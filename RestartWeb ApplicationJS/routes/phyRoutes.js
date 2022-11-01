const express = require("express")
const router = express.Router()
const firebase = require("../config")
const {getConnectedPatients,link,getJournal,createTask,createDrugTest,getTasks,getSGs,sgAction,getDrugTest,drugAssess,getMotiv,addQuote,deleteQuote}  = require("../controller/phyController")
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
router.get("/managePatient",async(req,res)=>{
   
    var id = req.query.id
    if(id!=null){
    await firebase.firestore().collection("Accounts").doc(req.query.id)
    .get()
    .then((snap)=>{
        
       var name = snap.data().firstName + " " + snap.data().lastName
       res.render("../htmlFiles/PhysicianManagePatient",{whole:name,id:id})
    })
   
    }
    else
    res.redirect("/phy")
})
router.get("/profile",(req,res)=>{
    res.render("../htmlFiles/PhysicianProfile")
})
router.post("/getSGs",getSGs)
router.get("/getJournal",getJournal)
router.get("/link",link)
router.post("/createTask",createTask)
router.post("/getDrugTest",getDrugTest)
router.post("/createDrugTest",createDrugTest)
router.post("/getTasks",getTasks)
router.post("/sgAction",sgAction)
router.post("/drugAssess",drugAssess)
router.get("/selfHelp",(req,res)=>{
    res.render("../htmlFiles/PhysicianSelfHelp")
})
router.post("/getMotiv",getMotiv)
router.post("/newQuote",addQuote)
router.post("/deleteQuote",deleteQuote)

module.exports=router