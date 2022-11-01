async function getMotivQuotes()
{
    var table = document.querySelector(".motivQuote")
    await fetch("/phy/getMotiv",{
        method:"POST"
    })
    .then(async(res)=>{
        var data = await res.json()
        console.log(data)
        data.quotes.forEach((doc)=>{
            table.insertAdjacentHTML('afterend',
            "<tr><td>" + doc.author + "</td><td>" + doc.body + "</td><td><button onclick='deleteQuote(\"" + doc.id + "\")' class='deacBtn'>Delete</button></tr>")
        })
    })
}
async function deleteQuote(id)
{
await fetch("/phy/deleteQuote?id="+id,{method:"POST"})
.then(async(res)=>{
    var data =await res.json()
    alert(data.msg)
    window.location.href="/phy/selfHelp"
})
}
getMotivQuotes()