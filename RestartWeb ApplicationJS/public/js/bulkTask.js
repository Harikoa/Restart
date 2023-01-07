var connected = document.querySelector(".connected")
var master = document.querySelector(".All")

async function getConnectedPatients()
{
    await fetch("/phy/connected").then(async(res)=>{
        var data = await res.json()
        for(var pt of data.connected)
        {
            connected.insertAdjacentHTML("afterend",
            "<tr><td><input type='checkbox'></td><td>" + pt.lastName + "</td><td>" + pt.firstName + "</td><td>" + pt.contact + 
            "</td><td>" + pt.nickname + "</td><td>" + pt.email + "</td></tr>"
            )
        }
        $('#bulkTable').DataTable();
    })
    paginateTable()
}


function goTo(id)
{
    window.location.href="/phy/managePatient?id=" +id
}
getConnectedPatients()

async function link(id)
{
    await fetch("/phy/link?id="+id)
    .then(async(res)=>{
        var data=await res.json()
        window.alert(data.msg)
        window.location.href="/phy/?panel=1"
    })
}

