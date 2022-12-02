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
    await auth.createUserWithEmailAndPassword(data.email,data.pw)
    .then((userCred)=>{
        
        var account = new acc(
            userCred.user.uid,
            data.email,
            null,data.firstname,
            data.lastname,
            data.middlename,
            data.nickname,
            req.query.role,data.sex,new Date().toISOString().substring(0,10)
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
        
const activate = async (req,res)=>{
    var bool = req.query.bool
    var email = req.query.email
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
       res.json({msg:"SUCCESS"})
    })
    .catch((e)=>{
        console.log(e.message)
        res.json({msg:"FAILED"})
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
        res.clearCookie("id")
        res.redirect("/")
    
    }

    const profile = async(req,res)=>{
        var id = req.cookies.id
        await firestore.collection("Accounts").doc(id)
        .get()
        .then(async (doc)=>{
            var data = doc.data()
            await res.json(data)
        })
    }

    const link = async (req,res)=>
    {
        var to = req.query.to
        var data = req.body
        var pid;
        var sid;
        var patient = data.patient
        var someone = data.someone
        console.log(patient.length)
        if(patient.length==0 || someone.length==0)
        {
            res.redirect("/admin/link") 
            console.log("RAWR")
        }
        else{
        await firestore.collection("Accounts").get()
        .then((snap)=>{
            snap.forEach((doc)=>{
                if(doc.data().email==patient)
                    pid = doc.data().id
                else if(doc.data().email==someone)
                    sid = doc.data().id
                
            })
        })
      
       if(to=="phy")
       {
        await firestore.collection("PhyLink")
        .where('patient','==',pid)
        .where('phy','==',sid)
        .get()
        .then(async(snap)=>{
            if(snap.empty)
            {
                await firestore.collection("PhyLink").add({
                    patient:pid,
                    phy:sid
                })
                res.redirect("/admin/link?panel=0&res=1")
            }
            else{
                res.redirect("/admin/link?panel=0&res=-1")
            }
        })
       }
       else{
        await firestore.collection("AlumniLink")
        .where('patient','==',pid)
        .where('al','==',sid)
        .get()
        .then(async(snap)=>{
            if(snap.empty)
            {
                await firestore.collection("AlumniLink").add({
                    patient:pid,
                    al:sid
                })
                res.redirect("/admin/link?panel=1&res=1")
            }
            else{
                res.redirect("/admin/link?panel=1&res=-1")
            }
        })
       }
    }
}
    const getAlumniLinked = async(req,res)=>{
        var linked=[]
        await firestore.collection("AlumniLink").get()
        .then(async (snap)=>{
            for(const doc of snap.docs)
        {
                var data = doc.data()            
             await firestore.collection("Accounts").doc(data.patient)
                .get()
                .then((pt)=>{
                    data.ptfname = pt.data().firstName
                    data.ptlname = pt.data().lastName
                    data.ptemail=pt.data().email
                })
                
              await firestore.collection("Accounts").doc(data.al)
                .get()
                .then((al)=>{
                    data.alfname = al.data().firstName
                    data.allname = al.data().lastName
                    data.alemail=al.data().email
                })
               
                linked.push(data)
            }
        })
        linked.sort((a,b)=>{
            a=a.ptlname 
            b=b.ptlname
            return (a > b) ? -1 : (a < b) ? 1 : 0;
        })
        await res.json({accs:linked})
       }

       const getPhyLinked = async(req,res)=>{
        var linked=[]
        await firestore.collection("PhyLink").get()
        .then(async (snap)=>{
            for(const doc of snap.docs)
        {
                var data = doc.data()            
             await firestore.collection("Accounts").doc(data.patient)
                .get()
                .then((pt)=>{
                    data.ptfname = pt.data().firstName
                    data.ptlname = pt.data().lastName
                    data.ptemail=pt.data().email
                })
                
              await firestore.collection("Accounts").doc(data.phy)
                .get()
                .then((al)=>{
                    data.alfname = al.data().firstName
                    data.allname = al.data().lastName
                    data.alemail=al.data().email
                })
                
                linked.push(data)
            }
        })
        linked.sort((a,b)=>{
            a=a.ptlname 
            b=b.ptlname
            return (a > b) ? -1 : (a < b) ? 1 : 0;
        })
        await res.json({accs:linked})
       }

       const unlink= async(req,res)=>
       {
            var pt = req.query.pt
            var some = req.query.someone
            var role = "physician"
        await firestore.collection("Accounts").get()
        .then((snap)=>{
            for(const docs of snap.docs)
            {
                var data = docs.data()
                if(data.email==pt)
                {
                pt=data.id
                }
                if(data.email==some)
                {
                some=data.id
                if(data.role=="alumni")
                role ='alumni'
                }
            }
        })
  
        if(role=="physician")
        {
            await firestore.collection("PhyLink").where("patient","==",pt)
            .where("phy","==",some)
            .get()
            .then(async (snap)=>{
                for(const sn of snap.docs)
                {
                    await sn.ref.delete()
                }
            })
            .catch((e)=>{
                console.log(e.message)
            })
            
        }
        else{
            await firestore.collection("AlumniLink").where("patient","==",pt)
            .where("al","==",some)
            .get()
            .then(async(snap)=>{
                for(const sn of snap.docs)
                {
                    await sn.ref.delete()
                }
            })
            .catch((e)=>{
                console.log(e.message)
            })
        }
        res.send("")
       }

       const getResolved = async(req,res)=>{
        var posts = []
        var comments = []
        await firestore.collection("Support Groups").get()
        .then(async(snap)=>{
            for(var docz of snap.docs)
            {
                await docz.ref.collection("Post")
                .get()
                .then(async(snaps)=>{
                    
                    for (var docs of snaps.docs)
                    {
                        if(docs.data().reported==true&& docs.data().resolved==false)
                        {
                            var post= docs.data()
                            post.id=docs.id
                        posts.push(post)
                        }
                        await docs.ref.collection("Comments").where("reported","==",true)
                        .where("resolved","==",false)
                        .get()
                        .then(async(snap)=>{
                            for(var doc of snap.docs)
                            {
                                var comm = doc.data()
                                comm.postID = docs.id
                                comm.sgid=docs.data().sgid
                                comm.id =doc.id
                               comments.push(comm)
                            }
                        })
                        
                    }
                })
            }
          
        })
       res.json({posts: posts,comments:comments})
       }


       const resolve = async(req,res)=>{
        var data = req.query
        var rep = true
        if(data.action=="ignore")
            rep=false
        
        console.log(data)
        if(data.type=="comment")
        {
           await firestore.collection("Support Groups").doc(data.sgid).collection("Post").doc(data.postid)
           .collection("Comments").doc(data.commentid.trim()).update({
            resolved:true,
            reported:rep
           })
           .then(async (snap)=>{
            await res.json({message:"SUCCESS!"})
           })
           .catch(async(e)=>{
            await res.json({message:"Failed!"})
            console.log(e.message)
           })
        }
        else if(data.type=="post")
        {
            console.log(data.postid)
            await firestore.collection("Support Groups").doc(data.sgid).collection("Post").doc(data.postid).update({
             resolved:true,
             reported:rep
            })
            .then(async (snap)=>{
             await res.json({message:"SUCCESS!"})
            })
            .catch(async(e)=>{
             await res.json({message:"Failed!"})
             console.log(e.message)
            })
        }
       
       }

       const getUnresolved = async(req,res)=>{
        var posts = []
        var comments = []
        await firestore.collection("Support Groups").get()
        .then(async(snap)=>{
            for(var docz of snap.docs)
            {
                await docz.ref.collection("Post")
                .get()
                .then(async(snaps)=>{
                    
                    for (var docs of snaps.docs)
                    {
                        if(docs.data().resolved==true)
                        {
                            var post= docs.data()
                            post.id=docs.id
                        posts.push(post)
                        }
                        await docs.ref.collection("Comments")
                        .where("resolved","==",true)
                        .get()
                        .then(async(snap)=>{
                            for(var doc of snap.docs)
                            {
                                var comm = doc.data()
                                comm.postID = docs.id
                                comm.sgid=docs.data().sgid
                                comm.id =doc.id
                               comments.push(comm)
                            }
                        })
                        
                    }
                })
            }
          
        })
       res.json({posts: posts,comments:comments})
       }

    const getDeactivateReq = async(req,res)=>{
        var accountLists =[]
        var patientlist = []
        var alumnilist = []
        await firestore.collection("Accounts").get()
        .then((snap)=>{
            snap.forEach((doc)=>{
                var data = doc.data()
                data.id = doc.id
                accountLists.push(data)
            })
        })
        await firestore.collection("AccountDeactivation")
        .where("finished","==",false)
        .get()
        .then((snap)=>{
                snap.forEach((doc)=>{
                    var data = doc.data()
                    accountLists.forEach((xd)=>{
                        if(data.userID == xd.id)
                        {
                           data.nickname =  xd.nickname
                           data.firstName = xd.firstName
                           data.lastName = xd.lastName
                        }
                    })
                    data.id = doc.id
                    if(data.role=="patient")
                    patientlist.push(data)
                    else
                    alumnilist.push(data)
                })
        })
        res.json({patient:patientlist,alumni:alumnilist})
    }
    const activateReq = async (req,res)=>{
        var bool = req.query.bool
        var email = req.query.email
        var id = req.query.id
        console.log("HELLO")
        console.log(req.query)
        if(bool=="true")
        bool = false
        else
        bool = true
        await firestore.collection("AccountDeactivation").doc(id)
        .update({finished:true})
        await firestore.collection("Accounts").doc(email).update({
            activated:bool
        })
        .then(()=>{
            
           res.json({msg:"SUCCESS"})
        })
        .catch((e)=>{
            console.log(e.message)
            res.json({msg:"FAILED"})
        })
    }
module.exports = {
    addAcc,
    getAllAcc,
    editAcc,
    activate,
    suspend,
    signOut,
    profile,
    link,
    getAlumniLinked,
    getPhyLinked,
    unlink,
    getResolved,
    resolve,
    getUnresolved,
    getDeactivateReq,
    activateReq
}