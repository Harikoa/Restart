const express = require("express")
const cors = require('cors')
const app = express()
const fs=require("fs")
app.set('view engine', 'ejs');
const firebase = require("./config")
const adminrouter = require("./routes/accountRoutes")
const loginrouter = require("./routes/loginRoute")
const phyRouter = require("./routes/phyRoutes")
const https =require("https")
const cookieParser = require("cookie-parser")
var options = {
    key: fs.readFileSync('./localhost.decrypted.key'),
    cert: fs.readFileSync('./localhost.crt')
  }
const server = https.createServer(options,app)
app.use(cookieParser())
app.use('/public',express.static("public"))
app.use(express.urlencoded({extended:true}))
app.use(express.json())
app.use(cors())
app.use("/",loginrouter)
app.use("/admin",adminrouter)
app.use("/phy",phyRouter)

const io = require("socket.io")
const socket = io(server)
server.listen(1028,()=>console.log("Listening at port " + 1028))
socket.on("connection",sckt=>{
    sckt.on("get-messages",async(id)=>{
        const firestore=firebase.firestore()
        const auth = firebase.auth()
        const phyid = id.currentUser.substring(3)
        ptid = id.id
        console.log(id.id)
        console.log(id.currentUser.substring(3))
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
    
        await firebase.firestore().collection("Accounts").doc(id).collection("Journal")
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
