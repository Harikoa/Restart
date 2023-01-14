var connected = document.querySelector(".connected")
var master = document.querySelector(".All")

async function getConnectedPatients()
{
    await fetch("/phy/connected").then(async(res)=>{
        var data = await res.json()
        console.log(data)
        for(var pt of data.connected)
        {
            connected.insertAdjacentHTML("afterend",
            "<tr><td><input class='check' value='"+ pt.id + "' type='checkbox'></td><td>" + pt.lastName + "</td><td>" + pt.firstName + "</td><td>" + pt.contact + 
            "</td><td>" + pt.nickname + "</td><td>" + pt.email + "</td></tr>"
            )
        }
      
    })
    paginateTable()
}
getConnectedPatients()
async function submit()
{
    var dl = document.getElementById("deadline").value
    var title = document.getElementById("title").value
    var desc = document.getElementById("description").value
    if(dl.length==0 || title.length==0|| desc.length==0)
    {
        alert("Please fill all the information regarding the tasks")
        return
    }
    var checked = []
    var all =document.querySelectorAll(".check")
    all.forEach((row)=>{
        if(row.checked)
            checked.push(row.value)
    })
    if(checked.length==0)
        {
            alert("Please select atleast one patient")
            return
        }
    fetch("/phy/bulkTask",{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify({
            "deadline":dl,
            "title":title,
            "desc":desc,
            "checked":checked
        })
    })
    .then(async(res)=>{
    var data = await res.text()
    alert(data)
    window.location.reload()
    })
}


