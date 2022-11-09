var mem = document.querySelector(".mem")
var non = document.querySelector(".non")


function getParameterByName(name, url = window.location.href) {
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

async function getMembers()
{
    var id =getParameterByName('id')
    fetch("/phy/getMembers?id="+id,{
        method:"POST"
    })
    .then(async(res)=>{
        var data = await res.json()
        for(var x of data.mem)
        {
            mem.insertAdjacentHTML('afterend',
            "<tr><td>" + x.lastName + "</td><td>" + x.firstName + "</td><td>" + x.nickname + "</td><td>"  + x.email + "</td><td><button onclick=\"link(\'" + x.id + "\',false)\" class='deacBtn'>Unlink</button></td></tr>"
            )
        }
        for(var x of data.non)
        {
            non.insertAdjacentHTML('afterend',
            "<tr><td>" + x.lastName + "</td><td>" + x.firstName + "</td><td>" + x.nickname + "</td><td>"  + x.email + "</td><td><button onclick=\"link(\'" + x.id + "\',true)\" class='actBtn'>Link</button></td></tr>"
            )
        }
    })
}

async function link(id,bool)
{
    var sgid = getParameterByName('id')
    console.log(typeof bool)
   fetch("/phy/sgAction",{
    method:"POST",
    headers:{
        'Content-Type': 'application/json'
    },
    body: JSON.stringify({id:sgid,pid:id,bool:bool})
   })
   .then(async(res)=>{
    var msg = await res.json()
    alert(msg.msg)
    window.location.href="/phy/sg?id=" + sgid + "&panel=1"
   })
}

async function getPosts()
{
    var post = document.querySelector(".post")
    var sgid = getParameterByName('id')
    await fetch("/phy/getPosts?id="+sgid,{
        method:"POST"
    })
    .then(async(res)=>{
        var data= await res.json()
        console.log(data)
        for(var x of data.posts){
            post.insertAdjacentHTML("afterbegin",
            '<div class="formDesign formAlign newPosts" onclick="goTo(\'' + x.id + '\',\'' + x.sgid +'\')"><h3 style="text-align: center;"><b>' + x.title + '</b></h3>'+ 
            '<div class="SGMainPost"><h4>By ' + x.userNickname + '</h4><h4>Posted on: ' + x.date +'</div></div>'
            )
        }
    })
}
function goTo(id,sgid)
{
window.location.href="/phy/specificPost?id="+id+"&sgid="+sgid
}
getPosts()
getMembers()