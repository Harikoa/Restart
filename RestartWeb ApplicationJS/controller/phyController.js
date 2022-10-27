const firebase = require("../config")
const firestore = firebase.firestore()
const auth = firebase.auth()

const getConnectedPatients = async(req,res)=>{
    const id =await auth.currentUser.uid
    var patients = []
    await firestore.collection("PhyLink").where("phy","==",id)
    .get()
    .then((snap)=>{
        snap.forEach((doc)=>{
            var data = doc.data()
            patients.push(data.patient)
        })
    })
    var json =[]
    var unconnect=[]
    await firestore.collection("Accounts").get()
    .then((snap)=>{
        snap.forEach((docs)=>{
            if(patients.includes(docs.id))
                json.push(docs.data())      
            else if(docs.data().role =="patient")
                unconnect.push(docs.data())
        })
    })
    .catch((e)=>{
        console.log(e.message)
    })
    
    res.json({connected:json,
    unconnected:unconnect})
}

const link = async(req,res)=>{
    var pid = req.query.id 
    var id = await auth.currentUser.uid
    var msg = ""
    await firestore.collection("PhyLink").add({
    patient:pid,
    phy:id
    })
    .then(()=>{
      msg="Success"  
    })
    .catch((e)=>{
        msg="Failed!"
        console.log(e.message)
    })
    res.json({msg:msg})
}

const getJournal = async(req,res)=>{
    var id = req.query.id
    var journals = []

    await firestore.collection("Accounts").doc(id).collection("Journal").get()
    .then(async(snap)=>{
        await snap.forEach((doc)=>{
            journals.push(doc.data())
          
        })
        res.json({journals:journals})    
    })
}

const createTask = async(req,res)=>{
    req.body.taskDate = new Date().toISOString().substring(0,10)
    req.body.complete=false
    await firestore.collection("Accounts").doc(req.query.id).collection("Task")
    .add(req.body)
    .then((snap)=>{
       
        res.redirect("/phy/managePatient?id=" + req.query.id + "&panel=1")
    })
    .catch((e)=>{
        console.log(e.message)
        res.redirect("/phy/managePatient?id=" + req.query.id + "&panel=1")
    })
}
const createDrugTest = async (req,res)=>{
    var id = req.query.id
    req.body.dateAssigned = new Date().toISOString().substring(0,10)
    await firestore.collection("Accounts").doc(id).collection("DrugTest")
}
module.exports ={
    getConnectedPatients,
    link,
    getJournal,
    createTask,
    createDrugTest
}