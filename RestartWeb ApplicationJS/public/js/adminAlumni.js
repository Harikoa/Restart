

function calculate_age(dob) { 
    var diff_ms = Date.now() - dob.getTime();
    var age_dt = new Date(diff_ms); 
  
    return Math.abs(age_dt.getUTCFullYear() - 1970);
}

async function table()
{
await fetch('/admin/getTable?role=alumni')
.then(async (response)=>{
    var data = await response.json()
        var ctr = 0
        for(const root of document.querySelectorAll(".tableRow"))
        {
            if(ctr==0)
            {
            data.accounts.forEach((acc)=>{
            root.insertAdjacentHTML("afterend","<tr class='accRow'><td class='accounts'>" +
            acc.lastName +"</td><td class='accounts'>" + acc.firstName + "</td><td class='accounts'>" + 
            acc.middleName + "</td><td class='accounts'>" + acc.contact + "</td><td class='accounts'>" + acc.birthDay + "</td><td class='accounts'>"
            + acc.nickname + "</td><td class='accounts'>" + acc.email + "</td></tr>"
            ) })
            }
            else if(ctr==1)
            {
                
                data.accounts.forEach((acc)=>{
                    var checked = ""
                    if(acc.activated)
                    checked = "checked"
                    root.insertAdjacentHTML("afterend","<tr class='accRow'><td class='accounts'>" +
                    acc.lastName +"</td><td class='accounts'>" + acc.firstName + "</td><td class='accounts'>" + 
                    acc.middleName + "</td><td class='accounts'>"+ acc.nickname + "</td><td class='accounts'>" + acc.email +  "</td><td class='accounts'><label class='switch'><input onclick=\"activate(this,'" + acc.email + "')\"type='checkbox'" + checked + "><span class='slider round'></span></label>" +
                    "</td></tr>"
                    ) 
                  
                })
            }
            else if(ctr==2)
            {
                data.accounts.forEach((acc)=>{
                    root.insertAdjacentHTML("afterend","<tr class='accRow'><td class='accounts'>" +
                    acc.lastName +"</td><td class='accounts'>" + acc.firstName + "</td><td class='accounts'>" + 
                    acc.middleName + "</td><td class='accounts'>"+ acc.nickname + "</td><td class='accounts'>" + acc.email + "</td><td class='accounts'>" + acc.lastSuspensionDay + "</td></tr>"
                    ) 
            
                })
            }
            ctr++
       
    }
    paginateTable()
})
    
var table =document.querySelector(".table")
for(var x = 0;x<table.rows.length;x++)
{
    table.rows[x].onclick=function(){
        document.getElementById('Editemail').value = this.cells[6].innerHTML;
        document.getElementById('Editnickname').value = this.cells[5].innerHTML;
        document.getElementById('Editbday').value = this.cells[4].innerHTML;
        document.getElementById('Editcontact').value = this.cells[3].innerHTML;
        document.getElementById('Editmname').value = this.cells[2].innerHTML;
        document.getElementById('Editfname').value = this.cells[1].innerHTML;
        document.getElementById('Editlname').value = this.cells[0].innerHTML;

    }
}

var suspend =document.querySelector(".suspend")
for(var x = 0;x<suspend.rows.length;x++)
{
    console.log("hello")
    suspend.rows[x].onclick=function(){
        document.getElementById('Suspendemail').value = this.cells[4].innerHTML;
       
    }
}

}

async function activate(bool,email)
{
    fetch("/admin/activate?bool=" + bool.checked + "&email=" + email)
    .then(async(response)=>{
        var data =await response.json()
        alert(data.msg)
    })
 
}

function getDeacRequest()
{
    var container = document.querySelector(".deacRepeat")
    fetch("/admin/deacRequest")
    .then(async(res)=>{
        var data = await res.json()
        console.log(data)
        data.alumni.forEach((doc)=>{
            container.insertAdjacentHTML("afterbegin",
            '<div class="formDesign"><h5>' + doc.firstName + " " + doc.lastName + " a.k.a " + doc.nickname + '</h5><button onclick="activateReq(true,\'' + doc.userID + '\',\'' + 
            doc.id + '\')" class="btn btn-primary" type="">Deactivate</button><button onclick="activateReq(false,\'' + doc.userID + '\',\'' + doc.id + '\')" class="btn btn-warning" type="">Ignore</button></div>'
            )
        })
    
    })
}
async function activateReq(bool,email,id)
{
    
    fetch("/admin/activateReq?bool=" + bool + "&email=" + email + "&id=" + id)
    .then(async(response)=>{
        var data = await response.json()
        console.log(data)
        alert(data.msg)
        window.location.href="/admin/alumni?panel=4"
    })
 
}

getDeacRequest()
table()

