
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
    await fetch('/admin/alumnilink')
    .then(async (response)=>{
        var data = await response.json()
      for(const accs of data.accs)
    {
        table.insertAdjacentHTML('afterend',
        "<tr><td>" + accs.ptlname + "</td><td>" + accs.ptfname + "</td><td>" + accs.ptemail + "</td><td></td><td>" + 
        accs.allname + "</td><td>"  +  accs.alfname + "</td><td>"+accs.alemail + "</td><td><button class='deacBtn' onclick='unlink(\""+ accs.ptemail + "\",\"" + accs.alemail + 
        "\",1)'>Unlink</button></td></tr>" 
        )
      
    }
    })
}

async function updateLinkp2p()
{
    var table = document.querySelector(".linkp2p")
    await fetch('/admin/phylink')
    .then(async (response)=>{
        var data = await response.json()
      for(const accs of data.accs)
    {
        table.insertAdjacentHTML('afterend',
        "<tr><td>" + accs.ptlname + "</td><td>" + accs.ptfname + "</td><td>" + accs.ptemail + "</td><td></td><td>" + 
        accs.allname + "</td><td>"  +accs.alfname + "</td><td>"+  accs.alemail + "</td><td><button class='deacBtn' onclick='unlink(\""+ accs.ptemail + "\",\"" + accs.alemail + 
        "\",0)'>Unlink</button></td></tr>" 
        )
      
    }
    clickable()
    })
    paginateTable()
}
function clickable()
{
var tables = document.querySelectorAll(".table")
var phypt = document.getElementById('phypt')
var phyphy = document.getElementById('phyphy')
var alpt = document.getElementById('alpt')
var alal = document.getElementById('alal')
for(var x = 0; x<tables.length;x++)
{

for(var y =1;y<tables[x].rows.length;y++)
{
   
    if(x==0)
    tables[x].rows[y].onclick=function(){
        console.log(this)
        phypt.value =this.cells[2].innerHTML
    }
    if(x==1)
    tables[x].rows[y].onclick=function(){
        phyphy.value =this.cells[2].innerHTML
    }
    if(x==3)
    tables[x].rows[y].onclick=function(){
        alpt.value =this.cells[2].innerHTML
    }
    if(x==4)
    tables[x].rows[y].onclick=function(){
        alal.value =this.cells[2].innerHTML
    }
}
}
}

async function unlink(pt,someone,panel)
{
    await fetch("/admin/unlink?pt="+ pt + "&someone=" + someone)
    window.location.href="/admin/link?panel="+panel
}
updatePatient()
updatePhy()
updateAl()
updateLinkp2a()
updateLinkp2p()

