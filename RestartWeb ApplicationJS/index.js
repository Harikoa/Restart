const express = require("express")
const cors = require('cors')
var cron = require('node-cron');
const app = express()
var serviceAccount = require("./restart-aa1cc-firebase-adminsdk-i5j3x-740eaf6c71.json");
app.set('view engine', 'ejs');
const firebase = require("./config")
const admin= require('firebase-admin/app');
const { getAuth } = require('firebase-admin/auth');
admin.initializeApp({
    credential:admin.cert(serviceAccount)
  })
const adminrouter = require("./routes/accountRoutes")
const loginrouter = require("./routes/loginRoute")
const phyRouter = require("./routes/phyRoutes")
const http =require("http")
const cookieParser = require("cookie-parser")
const server = http.createServer(app)
const pdf = require("pdfkit")
const fs = require("fs")
app.use(cookieParser())
app.use('/public',express.static("public"))
app.use(express.urlencoded({extended:true}))
app.use(express.json())
app.use(cors())
app.use("/",loginrouter)
app.use("/admin",adminrouter)
app.use("/phy",phyRouter)

const io = require("socket.io");
const { moveDown } = require("pdfkit");

const socket = io(server)
const port =process.env.Port || 8080
server.listen(port,()=>console.log("Listening at port " + port))
socket.on("connection",sckt=>{
    sckt.on("get-messages",async(id)=>{
        const firestore=firebase.firestore()
        const auth = firebase.auth()
        const phyid = id.currentUser.substring(3)
        ptid = id.id
        
        await firestore.collection("Chat")
        .onSnapshot(async(snap)=>{
            var msgs=[]
             for(var doc of snap.docs)
                {
                    var data = doc.data()
                    if(data.receiverEmail==phyid && data.senderEmail==ptid)
                    {
                       data.date = data.date.toDate()
                        msgs.push(data)
                       
                    }
                    if(data.receiverEmail==ptid && data.senderEmail==phyid)
                    {
                        data.date = data.date.toDate()
                       msgs.push(data)
                     
                    }
                }
                
                msgs.sort((a,b)=>{
                    return new Date(b.date) - new Date(a.date)
                })
                msgs.forEach((sms)=>{
                    sms.date = sms.date.toLocaleString()
                    
                })
                console.log(msgs)
               sckt.emit("chat",{msgs:msgs})
        })
    })
    sckt.on('send-chat-message',async msg=>{
        const auth = firebase.auth()
        const phyid = msg.currentUser.substring(3)
        await firebase.firestore().collection('Chat').add({
             date:new Date(),
             msgContent:msg.message,
             senderEmail:phyid,
             receiverEmail:msg.id

        })
        sckt.emit("get-messages",msg.id)
    })
    sckt.on("getImportant",async(id)=>{
        await firebase.firestore().collection("Accounts").doc(id.id).collection("Journal")
        .where("important","==",true)
        .onSnapshot((snap)=>{
            var journals = []
            snap.forEach((doc)=>{
                var data = doc.data()
                data.id = doc.id
                journals.push(data)
            })
            sckt.emit("importantJ",{important:journals})
        }) 
    })
})

cron.schedule("*/3 * * * *",async()=>{
    var firestore = firebase.firestore()
    firebase.firestore().collection("Accounts").where("activated","==",false)
    .get()
    .then(async(res)=>{
        res.forEach(async(doc)=>{
            var data = await doc.data()
            var date = new Date(data.deactivateDate)
            var diff = new Date() - date
            var id = doc.id
            var flr = Math.floor(diff/(1000*60*60*24*365))
            if(flr>=1)
            {
                const doc = new pdf();
                doc.pipe(fs.createWriteStream("./archive/"+ data.firstName + " " + data.lastName + "-" + data.id + ".pdf"))
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
            .moveDown()
            .text("Date deactivated: " + data.deactivateDate)
            doc.addPage()
    doc.fontSize(20)
        .font('Helvetica-Bold')
        .text("Journal Summary").moveDown()
        var countJ = 0
        var vsad = 0
        var sad = 0
        var neutral = 0
        var happy = 0
        var vhappy = 0
        var subI = 0
        var subF = 0
        var subL = 0
        var subN = 0
        var important = []
        await firestore.collection("Accounts").doc(id).collection("Journal").get()
        .then((snap)=>{
            snap.forEach((docu)=>{
                var data = docu.data()
                countJ++
                if(data.important)
                {
                    important.push(data)
                }
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
                subI+=data.substanceIntensity
                subF+=data.substanceFrequency
                subL+=data.substanceLength
                subN+=data.substanceNumber
            })
        doc.fontSize(14)
            .font('Helvetica')
            .text("Total Journal logged: " + countJ)
            .moveDown()
            .text("Total Very Sad Days: " + vsad)
            .moveDown()
            .text("Total Sad Days: " + sad)
            .moveDown()
            .text("Total Neutral Days: " + neutral)
            .moveDown()
            .text("Total Happy Days: " + happy)
            .moveDown()
            .text("Total Very Happy Days: " + vhappy)
            .moveDown()
            .text("Average Substance Intensity: " + (subI/countJ).toFixed(2))
            .moveDown()
            .text("Average Substance Frequency: " + (subF/countJ).toFixed(2))
            .moveDown()
            .text("Average Substance Length: " + (subL/countJ).toFixed(2))
            .moveDown()
            .text("Average Substance Number: " + (subN/countJ).toFixed(2))
           
        })
        doc.addPage()
        .font('Helvetica-Bold')
        .fontSize(20)
        .text("PHQ 9 Summary").moveDown()
        var phq9=[0,0,0,0,0,0,0,0,0]
       await firestore.collection("Accounts").doc(id).collection("Assessment").get()
        .then((snap)=>{
            var ctr=0
            snap.forEach(async(doc)=>{
                
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
        doc.text("Average Scores",{align:"center"})
        .fontSize(14)
        .font("Helvetica")
        for(var x = 0;x<9;x++)
        {
            doc.text(xlabel[x] + " : " + phq9[x])
            .moveDown()
        }
        doc.addPage()
        .font('Helvetica-Bold')
        .fontSize(20)
        .text("Important Journals")
        .moveDown()
        .font("Helvetica")
        .fontSize(14)
        important.forEach((imp)=>{
            doc
            .font('Helvetica-Bold')
            .fontSize(17)
            doc.text(imp.date)
            .moveDown()
            .font("Helvetica")
            .fontSize(14)
            doc.text(imp.journalEntry)
            .moveDown()
            .text("Mood: " + imp.mood)
            .moveDown()
            .moveDown()
        })
        await firestore.collection("Accounts").doc(id).delete()
      await getAuth().deleteUser(id).then(()=>{
            console.log("YEHEY")
            
        })
       if(data.role=="patient")
       {
        await firestore.collection("PhyLink").where("patient","==",id)
        .get()
        .then((snap)=>{
            snap.forEach(async(dc)=>{
                await dc.ref.delete()
            })
        })
        .catch((e)=>{
            console.log(e)
        })
        await firestore.collection("AlumniLink").where("patient","==",id)
        .get()
        .then((snap)=>{
            snap.forEach(async(dc)=>{
                await dc.ref.delete()
            })
        })
        .catch((e)=>{
            console.log(e)
        })
       }//patient
       else if(data.role=="alumni")
       {
        await firestore.collection("AlumniLink").where("al","==",id)
        .get()
        .then((snap)=>{
            snap.forEach(async(dc)=>{
                await dc.ref.delete()
            })
        })
        .catch((e)=>{
            console.log(e)
        })
       }
       else if(data.role == "physician")
       {
        await firestore.collection("PhyLink").where("phy","==",id)
        .get()
        .then((snap)=>{
            snap.forEach(async(dc)=>{
                await dc.ref.delete()
            })
        })
        .catch((e)=>{
            console.log(e)
        })
       }
    console.log("SUCCESS")
  doc.end()
            }//if more than a year

           
        })
       
    })
  console.log("I RAN")
})