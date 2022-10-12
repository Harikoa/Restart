const express = require("express")
const cors = require('cors')
const app = express()
const firebase = require("./config")
app.use(express.json())
app.use(cors())
app.get("/account",async(req,res)=>{
    const data = await firebase.collection("Accounts").get();
    if(data!=null)
    {
     res.send(data)
    }
})
app.listen(8080,()=>console.log("Listening at port 8080"))
