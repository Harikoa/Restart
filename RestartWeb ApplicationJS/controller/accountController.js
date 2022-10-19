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
            null,true,data.contact
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
                        var sus = doc.data().lastSuspensionDay
                        if(sus!=null)
                        sus = sus.toDate().toISOString().substring(0,10)
                        else
                        sus = "never"
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
                            sus,
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
            }//getAllAcc

const editAcc = async (req,res)=>{
    const data = req.body
    var pw = data.pw
  
    await firestore.collection("Accounts").where('email',"==", data.email)
    .get()
    .then((dozc)=>{
        dozc.forEach(async (doc)=>{
            await doc.ref.update(data)
            .then((result)=>{
                res.redirect('/admin/?panel=1')
            })
            .catch((e)=>{
                console.log(e.message)
            })
        })
        
    })
    .catch((e)=>{
        console.log(e.message)
    })
 
}
        
const activate = async (bool,email)=>{
    if(bool=="true")
    bool = true
    else
    bool = false
    await firestore.collection("Accounts").where('email','==',email)
    .get()
    .then((doc)=>{
        doc.forEach(async(snap)=>{
            await snap.ref.update({
                activated:bool
            })  
        })
        return {msg:"Success"}
    })
    .catch((e)=>{
        console.log(e.message)
        return {msg:"Failed!"}
    })
}
       

    const suspend = async(req,res)=>{
        var data = req.body
        await firestore.collection('Accounts').where('email','==',data.email)
        .get()
        .then(async (snap)=>{
            snap.forEach(async(doc)=>{
               doc.ref.update({
                lastSuspensionDay: new Date(data.suspend)
            })
            })
            
        })
        res.redirect("/admin/?panel=3")
    }

    const signOut = async(req,res)=>{
        await auth.signOut()
        .then(()=>{
            res.redirect("/")
        })
    }


module.exports = {
    addAcc,
    getAllAcc,
    editAcc,
    activate,
    suspend,
    signOut
}