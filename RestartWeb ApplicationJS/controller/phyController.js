const e = require("express")
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
    req.body.completion=false
    await firestore.collection("Accounts").doc(id).collection("DrugTest").where('completion',"==",false)
    .get()
    .then(async(snap)=>{
            if(snap.empty)
            {
                await firestore.collection("Accounts").doc(id).collection("DrugTest").add(req.body)
                res.send("<script>window.location.href='/phy/managePatient?id="+ id + "&panel=3';alert('Success!');</script>")
            }
            else{
                res.send("<script>window.location.href='/phy/managePatient?id="+ id + "&panel=3';alert('Already has a pending drug test request');</script>")
            }
    })
}
const getTasks = async (req,res)=>{
    var id = req.body.id
    var done = []
    var notdone=[]
    await firestore.collection("Accounts").doc(id).collection("Task")
    .get()
    .then(async(snap)=>{
        await snap.forEach((doc)=>{
            var data = doc.data()
            if(data.complete==true)
                done.push(data)
            else
                notdone.push(data)
        })
        res.json({done:done,notdone:notdone})
    })

}

const getSGs = async(req,res)=>{
    var id = req.body.id
    var included = []
    var notIncluded=[]
    await firestore.collection("Support Groups").get()
    .then((snap)=>{
        snap.forEach((doc)=>{
            var data = doc.data()
            data.id = doc.id
            var members = data.Members
            if(members.includes(id))
                included.push(data)
            else
                notIncluded.push(data)
        })
    })
    res.json({included:included,
        not:notIncluded
    })
}
const sgAction = async(req,res)=>{
    var id =req.body.id
    var link =req.body.bool
    var pid = req.body.pid
    if(!link){
        await firestore.collection("Support Groups").doc(id).get()
        .then(async(snap)=>{
            var data = snap.data()
            var members = data.Members
            var index = members.indexOf(pid)
            members.splice(index,1)
            console.log("HELLO")
            await firestore.collection('Support Groups').doc(id).update({Members:members})
            res.json({msg:"Success"})
        })
        .catch((e)=>{
            res.json({msg:"FAILED"})
        })
    }
    else{
        await firestore.collection("Support Groups").doc(id).get()
        .then(async(snap)=>{
            var data= snap.data()
            var members = data.Members
            members.push(pid)
            await firestore.collection("Support Groups").doc(id).update({Members:members})
            res.json({msg:"Success"})
        })
        .catch((e)=>{
            res.json({msg:"FAILED"})
        })
    }
}
const getDrugTest = async(req,res)=>{
    var id = req.body.id
    var done = []
    var notdone=[]
    await firestore.collection("Accounts").doc(id).collection("DrugTest").get()
    .then((snap)=>{
        snap.forEach((doc)=>{
            var data = doc.data()
            data.id = doc.id
            if(data.assessment==null)
                data.assessment = 'None'
            if(data.completion==true)
                done.push(data)
            else
                notdone.push(data)
        })
    })
    res.json({done:done,notdone:notdone})
}

const drugAssess = async(req,res)=>{
    var pid = req.query.id
    var did = req.query.did
    var assess = req.body.assessment
    await firestore.collection("Accounts").doc(pid).collection("DrugTest").doc(did).update({
        assessment:assess
    })
    res.send("<script>window.location.href='/phy/managePatient?id="+ pid + "&panel=3';alert('Success!');</script>")
}

const getMotiv = async(req,res)=>{
    var motiv = []
    await firestore.collection("MotivQuotes").get()
    .then((snap)=>{
        snap.forEach((doc)=>{
            var data = doc.data()
            data.id = doc.id
            motiv.push(data)
        })
    })
    res.json({quotes:motiv})
}
const addQuote = async(req,res)=>{

    var data = req.body
    var time = new Date()
    await firestore.collection("MotivQuotes").add({
        author:data.author,
        body:data.body,
        timeAccessed:time
    })
    res.redirect("/phy/selfHelp?panel=0")
}

const deleteQuote=async(req,res)=>{
    var id = req.query.id
    await firestore.collection("MotivQuotes").doc(id).delete()
    .then(()=>{
        res.json({msg:"SUCCESS"})
    })
    .catch((e)=>{
        console.log(e.message)
        res.json({msg:"FAILED"})
    })
}
const addActivity = async(req,res)=>{
    await firestore.collection("SuggestedActivities").add(req.body)
    res.redirect("/phy/selfHelp?panel=1")
}
const getAct = async(req,res)=>{
    activities=[]
    await firestore.collection("SuggestedActivities").get()
    .then((snap)=>{
        snap.forEach((doc)=>{
            var data = doc.data()
            data.id = doc.id
            activities.push(data)
        })
    })
    res.json({act:activities})
}

const deleteAct=async(req,res)=>{
    var id = req.query.id
    await firestore.collection("SuggestedActivities").doc(id).delete()
    .then(()=>{
        res.json({msg:"SUCCESS"})
    })
    .catch((e)=>{
        console.log(e.message)
        res.json({msg:"FAILED"})
    })
}
const getEval = async (req,res)=>{
    var id = req.query.id
    var evalDone = []
    var eval=[]
    await firestore.collection("Accounts").doc(id).collection("Assessment").get()
    .then((snap)=>{
            snap.forEach((doc)=>{
                var data = doc.data()
                data.id=doc.id
                if(data.isInterpreted==false)
                    eval.push(data)
                else
                evalDone.push(data)
            })
    })
    res.json({done:evalDone,notdone:eval})
}
const makeAssessment = async(req,res)=>{
    var id = req.query.id
    var aid = req.query.aid
    var data = req.body
    data.isInterpreted = true
    await firestore.collection("Accounts").doc(id).collection("Assessment").doc(aid)
            .update(data)
    res.redirect("/phy/managePatient?id="+id + "&panel=5")
}
const getMessages = async(req,res)=>{
    var name = ""
    if(firebase.auth().currentUser==null)
    {
   
        res.redirect('/')
    }
    else{
    await firebase.firestore().collection("Accounts").doc(req.query.id).get()
    .then((snap)=>{
        var data = snap.data()
        name = data.firstName + " " + data.lastName
    })
    res.render("../htmlFiles/PhySpecificMsg",{name:name})

}
}
const getSGList = async (req,res)=>{
    var sglists = []
    await firestore.collection("Support Groups").get()
    .then((snap)=>{
        snap.forEach((doc)=>{
            var data = doc.data()
            data.id=doc.id
            sglists.push(data)
        })
    })
    res.json({list:sglists})
}
const createSG = async(req,res)=>{
    if(auth.currentUser==null)
    {
        res.redirect("/")
    }
    else{
    var data = req.body
    data.Members = []
    data.creatorEmail = auth.currentUser.uid
    data.dateCreated = new Date()
    var id 
    await firestore.collection("Support Groups").add(data)
    .then((doc)=>{
       id = doc.id
    })
    res.redirect("/phy/sg?panel=1&id=" + id)
    }

}
const getSGMembers = async(req,res)=>{
    var members = []
    var mem = []
    var non = []
    var id = req.query.id
    await firestore.collection("Support Groups").doc(id).get()
    .then((snap)=>{

       members = snap.data().Members
    })
    await firestore.collection("Accounts").get()
    .then((snap)=>{
        snap.forEach((doc)=>{
            var data=doc.data()
            if(data.role=="patient"|| data.role=="alumni")
            {
            if(members.includes(data.id))
                mem.push(data)
            else
                non.push(data)
            }
        })
    })
    res.json({mem:mem,non:non})
}
const getPosts = async(req,res)=>{
    var id = req.query.id
    var posts=[]
    await firestore.collection("Support Groups").doc(id).collection("Post").where("reported","==",false).get()
    .then((snap)=>{
        snap.forEach((doc)=>{
            var data = doc.data()
            data.id=doc.id
            data.date = data.datePosted.toDate().toLocaleString()
            posts.push(data)
        })
    })
    posts.sort((a,b)=>{
        return new Date(a.date) - new Date(b.date)
    })
    res.json({posts:posts})
}

const getContent = async(req,res)=>{

    var data = req.query
    var post
    var comments=[]
    await firestore.collection("Support Groups").doc(data.sgid).collection("Post").doc(data.id)
    .get()
    .then((snap)=>{
        var data = snap.data()
        data.date = data.datePosted.toDate().toLocaleString()
        post = data
    })
    await firestore.collection("Support Groups").doc(data.sgid).collection("Post").doc(data.id).collection("Comments")
    .where('reported',"==",false)
    .get()
    .then((snap)=>{
        snap.forEach((doc)=>{
            var data = doc.data()
            data.date = data.datePosted.toDate().toLocaleString()
            comments.push(data)
        })
    })
    comments.sort((a,b)=>{
        return new Date(a.date) - new Date(b.date)
    })
    res.json({post:post,comments:comments})
}

const newComment = async(req,res)=>{
    var query = req.query
    var data=req.body
    data.datePosted=new Date()
    data.userID= auth.currentUser.uid
    await firestore.collection("Accounts").doc(data.userID).get()
    .then((snap)=>{
        data.nickName = snap.data().nickname
    })
    data.reported=false
    data.resolved=false
 
    await firestore.collection("Support Groups").doc(query.sgid).collection("Post").doc(query.id)
    .collection("Comments")
    .add(data)
    .then(()=>{
        res.json({msg:"SUCCESS"})
    })
    .catch(()=>{

        res.json({msg:"FAILED!"})
    })
    
}
module.exports ={
    getConnectedPatients,
    link,
    getJournal,
    createTask,
    createDrugTest,
    getTasks,
    getSGs,
    sgAction,
    getDrugTest,
    drugAssess,
    getMotiv,
    addQuote,
    deleteQuote,
    addActivity,
    getAct,
    deleteAct,
    getEval,
    makeAssessment,
    getMessages,
    getSGList,
    createSG,
    getSGMembers,
    getPosts,
    getContent,
    newComment
    
}