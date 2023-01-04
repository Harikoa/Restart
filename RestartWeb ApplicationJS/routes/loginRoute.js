const express = require("express")
const router = express.Router()
const firebase  = require("../config")
const auth = firebase.auth()
const firestore = firebase.firestore()
const mailer = require("nodemailer")
router.get("/",(req,res)=>
{
    const query = req.query.type
    let msg = ''
    
    if(req.cookies.id==null){
    if(query=='acc')
    msg = "Account can't be logged in here"
    else if(query =='inv')
    msg = "Invalid Credentials"
    res.render("../htmlFiles/login.ejs",{error:msg})
   
    }
    else{
        console.log("RAWR")
        firestore.collection("Accounts").doc(req.cookies.id).get()
        .then((query)=>{
            const data = query.data()
            if(data.role=="admin")
            {
                res.redirect('/admin')
            }
            else if(data.role=="physician")
            {
                res.redirect('/phy')
            }});
    }
})

router.post("/",(req,res)=>{
    
    const email = req.body.email
    const pw = req.body.pw
    auth.signInWithEmailAndPassword(email, pw).then((user)=>{
        firestore.collection("AccTries").doc(email).delete()
        .then(()=>{
            console.log("Prev Tries deleted Successfully!")
        })
        firestore.collection("Accounts").doc(user.user.uid).get()
        .then((query)=>{
            const data = query.data()
            if(data.role=="admin")
            {
                res.setHeader("set-cookie","id="+data.id)
                res.redirect('/admin')
            }
            else if(data.role=="physician")
            {
                res.setHeader("set-cookie","id="+data.id)
                res.redirect('/phy')
            }
            else{
                auth.signOut()
                .then(()=>
                {
                    res.redirect('/?type=acc')
                })
            }
        })
        console.log("Successful Login!")
    })
    .catch((e)=>{
        console.log(e.code)
        if(e.code=="auth/wrong-password")
        {
            firestore.collection("AccTries").doc(email).get()
            .then((query)=>{
                if(query.exists)
               {
                var tries = query.data().numTries
                tries++
                query.ref.update({
                    numTries:tries
                })
                if(tries>=3)
                    {
                        var transport = mailer.createTransport({
                            service:"gmail",
                            auth:{
                                user:"restart.meyvn@gmail.com",
                                pass:"iherharqhocouqkz"
                            }
                        });
                        var content = {
                            from: 'restart.meyvn@gmail.com',
                            to: email,
                            subject: 'Someone tried to access your account!',
                            text: "Someone has tried to access your Restart Account! They have tried " + tries + " times!"
                        }
                        transport.sendMail(content,(e,i)=>{
                            if(e){
                                console.log(e)
                            }
                            else{
                                console.log('Email sent: ' + i.response);
                            }
                        })
                    }
              
            
                
               }
               else
               {
                firestore.collection("AccTries").doc(email).set({
                    numTries:1
                })
               }
            })
        }
     res.redirect('/?type=inv')
    });
 
})

router.get("/forget",(req,res)=>{
    res.render("../htmlFiles/forgotPassEmail")
})
router.post("/forgetSubmit",async(req,res)=>{
    await auth.sendPasswordResetEmail(req.body.email)
    .then(()=>{
        res.send("<script>alert('Password Reset email sent');window.location.href='/'</script>")
    })
    .catch(()=>{
        res.send("<script>alert('User not found!');window.location.href='/forget'</script>")
    })
    
})
router.get("/loaderio-322c621dc0ff9caa1e5a59123b757b8e",(req,res)=>{
    res.send("loaderio-322c621dc0ff9caa1e5a59123b757b8e")
})
module.exports = router;