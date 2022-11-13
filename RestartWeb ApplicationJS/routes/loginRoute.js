const express = require("express")
const router = express.Router()
const firebase  = require("../config")
const auth = firebase.auth()
const firestore = firebase.firestore()
router.get("/",(req,res)=>
{
    const query = req.query.type
    let msg = ''
    if(auth.currentUser==null){
    if(query=='acc')
    msg = "Account can't be logged in here"
    else if(query =='inv')
    msg = "Invalid Credentials"
    res.render("../htmlFiles/login.ejs",{error:msg})
    }
    else{
        var user = auth.currentUser
        console.log(user)
        firestore.collection("Accounts").doc(user.uid).get()
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
        firestore.collection("Accounts").doc(user.user.uid).get()
        .then((query)=>{
            const data = query.data()
            if(data.role=="admin")
            {
                res.redirect('/admin')
            }
            else if(data.role=="physician")
            {
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
        console.log(e.message)
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
    
})
module.exports = router;