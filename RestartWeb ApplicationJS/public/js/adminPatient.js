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
    console.log(data.accounts)

    data.accounts.forEach((acc)=>{
        for(const root of document.querySelectorAll(".tableRow"))
        {
            root.insertAdjacentHTML("afterend","<tr class='accRow'><td class='accounts'>" +
            acc.lastName +"</td><td class='accounts'>" + acc.firstName + "</td><td class='accounts'>" + 
            acc.middleName + "</td><td class='accounts'>" + acc.contact + "</td><td class='accounts'>" + acc.birthDay + "</td><td class='accounts'>"
            + acc.nickname + "</td><td class='accounts'>" + acc.substanceUsed + "</td><td class='accounts'>" + acc.email + "</td></tr>"
            )
        }
        console.log(acc)
    })
})
}
table()

