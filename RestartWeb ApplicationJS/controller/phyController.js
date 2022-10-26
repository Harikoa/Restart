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
module.exports ={
    getConnectedPatients,
    link
}