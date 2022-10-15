'use strict'
const firebase = require("../config")
const acc = require("../model/Account")
const firestore = firebase.firestore()
const auth = firebase.auth()


const accConverter = {
    toFirestore: function(acc) {
        return {
            id : acc.id,
            email: acc.email,
            birthDay: acc.birthDay,
            connectedUser:acc.connectedUser,
            contact: acc.contact,
            fcm:acc.FCM,
            firstName:acc.firstName,
            lastAssessment: acc.lastAssessment,
            lastName: acc.lastName,
            lastSuspensionDay:acc.lastSuspensionDay,
            middleName:acc.middleName,
            nickname:acc.nickname,
            password:acc.password,
            role:acc.role,
            sex:acc.sex,
            substanceUsed:acc.substanceUsed,
            activated:acc.activated
            };
    },
    fromFirestore: function(snapshot, options){
        const data = snapshot.data(options);
        return new acc( data.id,data().email,
        data().password,
        data().firstName,
        data().lastName,
        data().middleName,
        data().nickname,
        data().role,
        data().sex,
        data().lastAssessment,
        data().fcm,
        data().connectedUser,
        data().substanceUsed,
        data().birthDay,
        data().lastSuspensionDay,
        data().activated,
        data().contact)}
};

const addAcc = async(req,res)=>
{
    const data = req.body;
    console.log(data)
    await auth.createUserWithEmailAndPassword(data.email,data.pw)
    .then((userCred)=>{
        var lastSus = new Date()
        lastSus.setDate(lastSus.getDate()-1)
        var account = new acc(
            userCred.user.uid,
            data.email,
            null,data.firstname,
            data.lastname,
            data.middlename,
            data.nickname,
            "patient",data.sex,new Date().toISOString().substring(0,10)
            ,null,null,data.substance,
            data.bday,
            lastSus,true,data.contact
        )
        console.log(account)
        firestore.collection("Accounts").doc(userCred.user.uid)
            .withConverter(accConverter)
            .set(account)
            .then((userAcc)=>{
                res.redirect('/admin?type=1')
            })
            .catch((e)=>{
                console.log(e.message)
            })
    })
    .catch((e)=>
    {
        res.redirect('/admin?type=email')
    })

}

const getAllAcc = async function(role){      
            const allAccs = []    
            await firestore.collection("Accounts").
            where('role','==', role)
            .get()            
            .then((data)=>{
            
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
                            doc.data().fcm,
                            doc.data().connectedUser,
                            doc.data().substanceUsed,
                            doc.data().birthDay,
                            doc.data().lastSuspensionDay,
                            doc.data().activated,
                            doc.data().contact
                        )
                        
                      allAccs.push(account)  
                    
                    })
                  
            }
        
        })
            .catch((e)=>{
                console.log(e.message)
            })
          return allAccs
            }
        
      
        



module.exports = {
    addAcc,
    getAllAcc
}