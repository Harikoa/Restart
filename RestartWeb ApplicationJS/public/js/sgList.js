var ul = document.querySelector('.sgUL')

function getList()
{
    fetch("/phy/getSGList",{
        method:"POST"
    })
    .then(async(res)=>{
        var data = await res.json()
        console.log(data)
        for(var x of data.list)
        {
           ul.insertAdjacentHTML("afterbegin",
           "<li><a class='nav-link' href='/phy/sg?id=" + x.id + "'>" + x.Title + "</a></li>"
           ) 
        }
    })
}
getList()