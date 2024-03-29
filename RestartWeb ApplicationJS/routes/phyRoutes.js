const express = require("express")
const router = express.Router()
const firebase = require("../config")
const {getConnectedPatients,link,getJournal,createTask,createDrugTest,getTasks,getSGs,sgAction,getDrugTest,drugAssess,getMotiv,addQuote,deleteQuote,addActivity,getAct,deleteAct, getEval, makeAssessment, getMessages, getSGList, createSG, getSGMembers, getPosts, getContent, newComment, createPost, getData, getPHQ9,reportComment, reportPost, exportdata, important}  = require("../controller/phyController")
router.get("/",(req,res)=>{
    if(req.cookies.id==null)
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

router.get("/bulkTask",(req,res)=>{
    res.render("../htmlFiles/BulkTask")
})

router.post("/getMotiv",getMotiv)
router.post("/newQuote",addQuote)
router.post("/deleteQuote",deleteQuote)
router.post("/addActivity",addActivity)
router.post("/getAct",getAct)
router.post("/deleteAct",deleteAct)
router.post("/getEval",getEval)
router.post("/makeAssessment",makeAssessment)
router.get("/messageList",(req,res)=>{
    if(req.cookies.id==null)
    {
        res.redirect('/')
    }
    else
    res.render("../htmlFiles/PhyMsgList")
})
router.get("/message",getMessages)
router.get("/SGList",(req,res)=>{
    res.render("../htmlFiles/PhysicianSG")
})
router.post("/getSGList",getSGList)
router.post("/sgCreate",createSG)
router.get("/SG",async(req,res)=>{
    var id = req.query.id
    var title = ""
    await firebase.firestore().collection("Support Groups").doc(id).get()
    .then((snap)=>{
        title = snap.data().Title
    })
    res.render("../htmlFiles/PhySGMain",{Title:title})
})
router.post("/getMembers",getSGMembers)
router.post("/getPosts",getPosts)
router.get("/specificPost",(req,res)=>{
    res.render("../htmlFiles/PhySGSpecificPost")
})
router.post("/specificPost",getContent)
router.post("/newComment",newComment)
router.get("/create",async(req,res)=>{
    var id = req.query.sgid
    var title = ""
    await firebase.firestore().collection("Support Groups").doc(id).get()
    .then((snap)=>{
        title = snap.data().Title
    })
    res.render("../htmlFiles/createPostSG",{title:title})
})
router.post("/create",createPost)
router.post("/getData",getData)
router.post("/phq9",getPHQ9)
router.post("/reportComment",reportComment)
router.post("/reportPost",reportPost)
router.post("/exportData",exportdata) 
router.get("/important",important)
router.post("/bulkTask",(req,res)=>{
    
    firebase.firestore().collection("Accounts").where("id","in",req.body.checked)
    .get()
    .then((query)=>{
        query.forEach((doc)=>{
            doc.ref.collection("Task").add({
                "title":req.body.title,
                "taskDescription":req.body.desc,
                "taskDeadline":req.body.deadline,
                "complete":false,
                "taskDate":new Date().toISOString().substring(0,10)
            })
        })
        res.send("SUCCESS")
    })
    .catch((e)=>{
        console.log(e)
        res.send("FAILED")
    })
})
module.exports=router