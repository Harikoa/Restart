var fname = document.getElementById('fname')
var mname = document.getElementById('mname')
var lname = document.getElementById('lname')
var nickname = document.getElementById('nickname')
var bday = document.getElementById('birthday')
var sex = document.getElementById('sex')
var email = document.getElementById('email')
var contact = document.getElementById('contact')

async function updateInfo()
{
    await fetch('/admin/fetchProfile')
    .then(async (res)=>{
        var data =await res.json()
        fname.innerHTML = "First Name: " + data.firstName
        mname.innerHTML = "Middle Name: " + data.middleName
        lname.innerHTML = "Last Name: " + data.lastName
        nickname.innerHTML = "Nickname: " + data.nickname
        bday.innerHTML = "Birthday: " + data.lastName
        sex.innerHTML = "Sex: " + data.sex
        email.innerHTML = "Email: " + data.email
        contact.innerHTML = "Contact: " + data.contact
    })
}
updateInfo()