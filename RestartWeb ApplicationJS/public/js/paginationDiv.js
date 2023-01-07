
function paginateTableDiv()
{
    var tables = document.querySelectorAll(".paginateDiv")
var side = document.querySelectorAll(".sideDiv")
    var prevY=1
    var y =1
    for(var z = 0; z<tables.length;z++)
    {
    for(var x = 0;x<tables[z].getElementsByClassName("newPosts").length;x++)
    {
        if(x%5==0&&x>1)
            {
                y++
            }
        tables[z].getElementsByClassName("newPosts")[x].classList.add("xD"+y)
    }
   
    changeDiv(prevY,z)
    for(var num = 1;prevY<=y;num++){
     
    side[z].insertAdjacentHTML("beforeend",'<li class="page-item"><button class="page-link" onclick="changeDiv(' + prevY + ','+ z+ ')">' + num + '</button></li>')
    prevY++
    }
    y++
    }//tables

}

function changeDiv(identifier,z)
{
    var tables = document.querySelectorAll(".paginateDiv")
var side = document.querySelectorAll(".sideDiv")
    for(var x=0;x<tables[z].getElementsByClassName("newPosts").length;x++)
        tables[z].getElementsByClassName("newPosts")[x].style.display="none"
    var show = document.querySelectorAll(".xD"+identifier)
    show.forEach((row)=>{
        row.style.display="block"
    })

}