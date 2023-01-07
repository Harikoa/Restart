var tables = document.querySelectorAll(".paginate")
var side = document.querySelectorAll(".side")
function paginateTable()
{
    var prevY=1
    var y =1
    for(var z = 0; z<tables.length;z++)
    {
    for(var x = 1;x<tables[z].rows.length;x++)
    {
        if(x%10==0)
            {
                y++
            }
        tables[z].rows[x].classList.add("x"+y)
    }
    change(prevY,z)
    for(var num = 1;prevY<=y;num++){
     
    side[z].insertAdjacentHTML("beforeend",'<li class="page-item"><button class="page-link" onclick="change(' + prevY + ','+ z+ ')">' + num + '</button></li>')
    prevY++
    }
    }//tables

}

function change(identifier,z)
{
    for(var x=1;x<tables[z].rows.length;x++)
        tables[z].rows[x].style.display="none"
    var show = document.querySelectorAll(".x"+identifier)
    console.log(".x"+identifier)
    show.forEach((row)=>{
        row.style.display="table-row"
    })

}