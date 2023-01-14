const e = require("express")
const firebase = require("../config")
const firestore = firebase.firestore()
const auth = firebase.auth()
const chartJS = require("chart.js")
const pdfdoc = require("pdfkit")
const fs=require("fs")
const getConnectedPatients = async(req,res)=>{
    const id = req.cookies.id
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
    .then(async(snap)=>{
        for(var x =0; x<snap.docs.length;x++)
        {
            var docs = await snap.docs[x].data()
            if(patients.includes(docs.id)){
                
            await firestore.collection("Accounts").doc(docs.id).collection("Journal").orderBy("date","desc").limit(1)
            .get()
            .then(async(qry)=>{
                if(qry.docs[0]!=null)
                    {
                       var data = await qry.docs[0].data()
                       var date = new Date(data.date)
                       var diff = new Date() - date
                       var days = Math.ceil(diff/(1000*60*60*24)) -1
                       if(days>3)
                        docs.journal = '<span type="" class="" data-bs-toggle="tooltip" data-bs-placement="top" title="This patient is non compliant"><span><i class="fa-solid fa-triangle-exclamation"></i></span></span>'
                        else
                        docs.journal = ''
                    }
                    else
                        docs.journal = '<span type="" class="" data-bs-toggle="tooltip" data-bs-placement="top" title="This patient is non compliant"><span><i class="fa-solid fa-triangle-exclamation"></i></span></span>'
                
            })
            json.push(docs)      
            }
          else if(docs.role =="patient")
            unconnect.push(docs)
        }
        
        
        
    })
    .catch((e)=>{
        console.log(e.message)
    })
    
    res.json({connected:json,
    unconnected:unconnect})
}

const link = async(req,res)=>{
    var pid = req.query.id 
    var id = req.cookies.id
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
            var data = doc.data()
            data.id=doc.id
            journals.push(data)
          
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
    if(req.cookies.id==null)
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
    if(req.cookies.id==null)
    {
        res.redirect("/")
    }
    else{
    var data = req.body
    data.Members = []
    data.creatorEmail = req.cookies.id
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
            data.id=doc.id
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
    data.userID= req.cookies.id
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

const createPost = async(req,res)=>{
    var data= req.body
    data.userID = req.cookies.id
    data.datePosted = new Date()
    data.reported = false
    data.resolved = false
    data.sgid = req.query.sgid
    await firestore.collection("Accounts").doc(data.userID).get()
    .then((snap)=>{
        data.userNickname=snap.data().nickname
    })
    await firestore.collection("Support Groups").doc(req.query.sgid).collection("Post")
    .add(data)
    .then(()=>{
        res.json({bool:true})
    })
    .catch((e)=>{
        console.log(e.message)
        res.json({bool:false})
    })
}

const getData= async(req,res)=>{
    var id =req.query.id
    var vsad=0
    var sad =0
    var neutral =0
    var happy =0
    var vhappy=0
    var totalJournals =0
    var intensity=0
    var freq=0
    var length =0
    var number=0
    await firestore.collection("Accounts").doc(id).collection("Journal").get()
    .then((snap)=>{
        snap.forEach((doc)=>{
            var data = doc.data()
            if(data.mood=='Very Sad')
            vsad++
            if(data.mood=='Sad')
            sad++
            if(data.mood=='Neutral')
            neutral++
            if(data.mood=='Happy')
            happy++
            if(data.mood=='Very Happy')
            vhappy++
            intensity+=data.substanceIntensity
            freq+=data.substanceFrequency
            length+=data.substanceLength
            number+=data.substanceNumber
            totalJournals++
        })
    })
    intensity=(intensity/totalJournals).toFixed(2)
    freq=(freq/totalJournals).toFixed(2)
    length=(length/totalJournals).toFixed(2)
    number=(number/totalJournals).toFixed(2)
    var MoodxValues=["Very Sad","Sad","Neutral","Happy","Very Happy"]
    var MoodyValues=[vsad,sad,neutral,happy,vhappy]
    var UrgexValues = ["Intensity","Frequency","Length"]
    var UrgeyValues = [intensity,freq,length]

    res.json({
        MoodX:MoodxValues,
        MoodY:MoodyValues,
        UrgeX:UrgexValues,
        UrgeY:UrgeyValues,
        UrgeNum:number
    })
}
const getPHQ9 = async(req,res)=>{
    var phq9=[0,0,0,0,0,0,0,0,0]

    await firestore.collection("Accounts").doc(req.query.id).collection("Assessment")
    .get()
    .then((snap)=>{
        var ctr=0
        snap.forEach((doc)=>{
            var data = doc.data()
            phq9[0]+=data.phq1
            phq9[1]+=data.phq2
            phq9[2]+=data.phq3
            phq9[3]+=data.phq4
            phq9[4]+=data.phq5
            phq9[5]+=data.phq6
            phq9[6]+=data.phq7
            phq9[7]+=data.phq8
            phq9[8]+=data.phq9 
            ctr++
        })
    if(ctr>0)
      for(var x = 0;x<9;x++)
      {  
        phq9[x] = phq9[x]/ctr.toFixed(2)
      }     
    })
    res.json({phq9:phq9})
}
const reportComment = async(req,res)=>{
    var data = req.body
    firestore.collection("Support Groups").doc(data.sgid).collection("Post").doc(data.id).collection("Comments").doc(data.commentId)
    .update({
        reported:true,
        resolved:false
    })
    .then(()=>{
        res.json({msg:"Comment Reported"})
    })
    .catch(()=>{
        res.json({msg:"Error! Try Again"})
    })
}
const reportPost = (req,res)=>{
    var data =req.body
    firestore.collection("Support Groups").doc(data.sgid).collection("Post").doc(data.id)
    .update({
        reported:true,
        resolved:false
    })
    .then(()=>{
        res.json({msg:"Post Reported!",bool:true})
    })
    .catch(()=>{
        res.json({msg:"Error! Try again!",bool:false})
    })
}
const exportdata= async(req,res)=>{
    var id = req.query.id
    var doc = new pdfdoc()
    res.setHeader('Content-Type','application/pdf')
    doc.pipe(res)
    await firestore.collection("Accounts").doc(id)
    .get()
    .then((snap)=>{
        var data= snap.data()
        doc.fontSize(20)
            .text(data.firstName + " " + data.middleName + " " + data.lastName) 
            .fontSize(12)
            .moveDown()
            .text("Contact: " + data.contact)
            .moveDown()
            .text("Substance Used: " + data.substanceUsed)
            .moveDown()
            .text("Birthday: " + data.birthDay)
            .moveDown()
            .text("Last Assessment: " + data.lastAssessment)
            .moveDown()
            .text("Email: " + data.email)
            .moveDown().moveDown()
    })
    doc.fontSize(20)
        .font('Helvetica-Bold')
        .text("Journal Summary").moveDown()
        
    await firestore.collection("Accounts").doc(id).collection("Journal").get()
    .then((snap)=>{
        snap.forEach((docu)=>{
            var data = docu.data()
            doc.fontSize(14)
            doc.font('Helvetica-Bold')
                .text(data.date)
                .moveDown()
                .font('Helvetica')
                .text("Mood: " + data.mood)
                .moveDown()
                .text("Intensity of Craving: " + data.substanceIntensity)
                .moveDown()
                .text("Frequency of Craving: " + data.substanceFrequency)
                .moveDown()
                .text("Length of Craving: " + data.substanceLength)
                .moveDown()
                .text("Number of occurence of Craving: " + data.substanceNumber)
                .moveDown()
        })
    })
    var vsad=0
    var sad =0
    var neutral =0
    var happy =0
    var vhappy=0
    var totalJournals =0
    var intensity=0
    var freq=0
    var length =0
    var number=0
    await firestore.collection("Accounts").doc(id).collection("Journal").get()
    .then((snap)=>{
        snap.forEach((doc)=>{
            var data = doc.data()
            if(data.mood=='Very Sad')
            vsad++
            if(data.mood=='Sad')
            sad++
            if(data.mood=='Neutral')
            neutral++
            if(data.mood=='Happy')
            happy++
            if(data.mood=='Very Happy')
            vhappy++
            intensity+=data.substanceIntensity
            freq+=data.substanceFrequency
            length+=data.substanceLength
            number+=data.substanceNumber
            totalJournals++
        })
    })
    intensity=(intensity/totalJournals).toFixed(2)
    freq=(freq/totalJournals).toFixed(2)
    length=(length/totalJournals).toFixed(2)
    number=(number/totalJournals).toFixed(2)
    var MoodxValues=["Very Sad","Sad","Neutral","Happy","Very Happy"]
    var MoodyValues=[vsad,sad,neutral,happy,vhappy]
    var UrgexValues = ["Intensity","Frequency","Length"]
    var UrgeyValues = [intensity,freq,length]
    var data = req.body
    doc.addPage() 
    try{
        doc.fontSize(20)
        doc.font('Helvetica-Bold')
        .text("Mood Data")
        .moveDown()
        doc.image(data.moodChart,{fit:[500,500]})
        doc.moveDown()
        for(var x=0;x<5;x++)
        {
            doc.font("Helvetica")
            .fontSize(14)
            .text("Total " + MoodxValues[x] + " Days: " + MoodyValues[x])
            .moveDown()
        }
        doc.addPage()
        doc.font('Helvetica-Bold')
        .text("Substance Craving  Data")
        .moveDown()
        doc.image(data.urgeChart,{fit:[500,500]})
        doc.moveDown()
        for(var x=0;x<3;x++)
        {
            doc.font("Helvetica")
            .fontSize(14)
           .text("Average " + UrgexValues[x] + " of Substance Craving: " + UrgeyValues[x])
            .moveDown()
        }
        doc.text("Average Number of times of Substance Craving: " + number)
        doc.addPage()
        var phq9=[0,0,0,0,0,0,0,0,0]
        
        await firestore.collection("Accounts").doc(req.query.id).collection("Assessment")
        .get()
        .then((snap)=>{
            var ctr=0
            snap.forEach((doc)=>{
                var data = doc.data()
                phq9[0]+=data.phq1
                phq9[1]+=data.phq2
                phq9[2]+=data.phq3
                phq9[3]+=data.phq4
                phq9[4]+=data.phq5
                phq9[5]+=data.phq6
                phq9[6]+=data.phq7
                phq9[7]+=data.phq8
                phq9[8]+=data.phq9 
                ctr++
            })
        if(ctr>0)
          for(var x = 0;x<9;x++)
          {  
            phq9[x] = phq9[x]/ctr.toFixed(2)
          }     
        })
        var xlabel = ["Little Interest","Feeling Down","Trouble Falling asleep","Feeling Tired","Poor Appetite","Feeling Bad", "Trouble Concentrating","Moving or speaking slowly","Suicidal Thoughts"]
        doc.fontSize(20)
        doc.font('Helvetica-Bold')
        .text("PHQ9 Data")
        .moveDown()
        .image(data.phq9chart,{
            fit:[500,500]
        })
        .moveDown()
        doc.fontSize(17)
        doc.text("Average Scores",{align:"center"})
        .moveDown()
        .font("Helvetica")
        .fontSize("14")
        for(var x = 0;x<9;x++)
        {
            doc.text(xlabel[x] + " : " + phq9[x])
            .moveDown()
        }
    }
    catch(e){
        console.log(e)
    }
  
    doc.end()
}
const important = async(req,res)=>{
    var data = req.query
    var important = false
    await firestore.collection("Accounts").doc(data.pid).collection("Journal").doc(data.jid)
    .get()
    .then((snap)=>{
        important = snap.data().important
    })
    await firestore.collection("Accounts").doc(data.pid).collection("Journal").doc(data.jid)
    .update({
        important:!important
    }).then(()=>{
        res.json({msg:"SUCCESS"})
    })
    .catch(()=>{
        res.json({msg:"FAILED"})
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
    newComment,
    createPost,
    getData,
    getPHQ9,
    reportComment,
    reportPost,
    exportdata,
    important
}