var list = document.querySelector(".msgList")

async function getList()
{
    await fetch("/phy/connected")
    .then(async(res)=>{
        var data = await res.json()
        data.connected.forEach((doc)=>{
            list.insertAdjacentHTML("afterbegin",
            "<li><a class='nav-link'href='/phy/message?id=" + doc.id + "'>" + doc.firstName + " " + doc.lastName +"</a></li>"
            )
        })
        
    })
}
getList()