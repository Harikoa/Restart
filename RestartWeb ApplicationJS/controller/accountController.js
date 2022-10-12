'use strict'
const firebase = require("../config")
const acc = require("../model/Account")
const firestore = firebase.firestore()

const addAcc = async(req,res)=>
{
    const data = req.body;
    await firestore.collection("Accounts").add(data)
    res.send("test")
}

const getAllAcc = async(req,res) =>{

        try{           
            const data =await firestore.collection("Accounts").get()
            const allAccs = []
            if(!data.empty)
            {
                data.forEach((doc)=>{
                    const account = new acc(
                        doc.id,
                        doc.data().email,
                        doc.data().password,
                        doc.data().firstName,
                        doc.data().lastName,
                        doc.data().middleName,
                        doc.data().nickname,
                        doc.data().role,
                        doc.data().sex,
                        doc.data().lastAssessment,
                        doc.data().FCM,
                        doc.data().connectedUser,
                        doc.data().substanceUsed,
                        doc.data().birthDay,
                        doc.data().lastSuspensionDay,
                        doc.data().activated,
                        doc.data().important
                    )
                  allAccs.push(account)  
                })
                res.status(200).send(allAccs)
            }
        }
        catch(exception)
        {
            res.status(500).send(exception.message)
        }
        
}
module.exports = {
    addAcc,
    getAllAcc
}