async function updatePatient()
{
    var patient = document.querySelectorAll(".patient")
    await fetch('/admin/getTable?role=patient')
    .then(async (response)=>{
        var data = await response.json()
        for(const pat of patient)
        {
            data.accounts.forEach((acc)=>{
                pat.insertAdjacentHTML('afterend',
                "<tr><td>"+ acc.lastName + "</td><td>"+ acc.firstName + "</td><td>" + acc.email + "</td></tr>" )
            })
        }
    })
}

async function updateAl()
{
    var alumni = document.querySelector('.alumni')
    await fetch('/admin/getTable?role=alumni')
    .then(async (response)=>{
        var data = await response.json()
        data.accounts.forEach((acc)=>{
        alumni.insertAdjacentHTML('afterend',
        "<tr><td>"+ acc.lastName + "</td><td>"+ acc.firstName + "</td><td>" + acc.email + "</td></tr>" )
        })
    })
}
async function updatePhy()
{ 
    var phy = document.querySelector('.phy')
    await fetch('/admin/getTable?role=physician')
    .then(async (response)=>{
        var data = await response.json()
        
        data.accounts.forEach((acc)=>{
            phy.insertAdjacentHTML('afterend',
            "<tr><td>"+ acc.lastName + "</td><td>"+ acc.firstName + "</td><td>" + acc.email + "</td></tr>" )
            })
    })
    .catch((e)=>{
        console.log(e.message)
    })
}

async function updateLinkp2a()
{
    var table = document.querySelector(".linkp2a")
    var pt
    var al
    await fetch('/admin/getTable?role=patient')
    .then(async (response)=>{
        var data = await response.json()
        
        pt = data
    })
    await fetch('/admin/getTable?role=alumni')
    .then(async (response)=>{
        var data = await response.json()
        
        al = data
    })
}

updatePatient()
updatePhy()
updateAl()
updateLinkp2a()
