function calculate_age(dob) { 
    var diff_ms = Date.now() - dob.getTime();
    var age_dt = new Date(diff_ms); 
  
    return Math.abs(age_dt.getUTCFullYear() - 1970);
}

async function table()
{
await fetch('/admin/getTable?role=patient')
.then(async (response)=>{
    var data = await response.json()
    
    
    data.accounts.forEach((acc)=>{
        for(const root of document.querySelectorAll(".tableRow"))
        {
            root.insertAdjacentHTML("afterend","<tr class='accRow'><td class='accounts'>" +
            acc.lastName +"</td><td class='accounts'>" + acc.firstName + "</td><td class='accounts'>" + 
            acc.middleName + "</td><td class='accounts'>" + acc.contact + "</td><td class='accounts'>" + acc.birthDay + "</td><td class='accounts'>"
            + acc.nickname + "</td><td class='accounts'>" + acc.substanceUsed + "</td><td class='accounts'>" + acc.email + "</td></tr>"
            ) 
        }
       
    })
    

    var table =document.querySelector(".table")
for(var x = 0;x<table.rows.length;x++)
{
    table.rows[x].onclick=function(){
        document.getElementById('Editemail').value = this.cells[7].innerHTML;
        document.getElementById('Editsubstance').value = this.cells[6].innerHTML;
        document.getElementById('Editnickname').value = this.cells[5].innerHTML;
        document.getElementById('Editbday').value = this.cells[4].innerHTML;
        document.getElementById('Editcontact').value = this.cells[3].innerHTML;
        document.getElementById('Editmname').value = this.cells[2].innerHTML;
        document.getElementById('Editfname').value = this.cells[1].innerHTML;
        document.getElementById('Editlname').value = this.cells[0].innerHTML;

    }
}

})
    
}
table()

