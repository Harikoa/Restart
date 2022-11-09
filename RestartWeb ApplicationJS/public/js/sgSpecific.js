function getParameterByName(name, url = window.location.href) {
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}
async function getContent()
{
    var id = getParameterByName('id')
    var sgid = getParameterByName('sgid')
    var commentHolder = document.querySelector(".commentHolder")
    var post = document.querySelectorAll(".postC")
    await fetch("/phy/specificPost?id="+id+"&sgid=" + sgid,{method:"POST"})
    .then(async(res)=>{
        var data = await res.json()
        console.log(data)
        post[0].insertAdjacentHTML("afterbegin",
        data.post.title + " by " + data.post.userNickname
        )
        post[1].insertAdjacentHTML("afterbegin",
        data.post.content
        )
        post[2].insertAdjacentHTML("afterbegin",data.post.date)

        for(var x of data.comments)
        {
            commentHolder.insertAdjacentHTML("afterbegin",
            '<div class="col-10"><h4><b>Comments:</b></h4><div class="commentBox"><h5><b>@' + x.nickName + '</b></h5>' +
            '<h6 class="SGMainComment">' + x.commentContent + '</h6><h6>Date posted: ' + x.date + '</h6></div></div>'
            )
        }
    })
    
}
document.getElementById("newPost").addEventListener('submit',async e=>{
    e.preventDefault()
    var id = getParameterByName('id')
    var sgid = getParameterByName('sgid')
    var commentContent = document.getElementById("textComment")
    await fetch("/phy/newComment?id="+id + "&sgid=" + sgid,{
        method:"POST",
        headers:{
            "Content-Type":"application/json"
        },
        body:JSON.stringify({
            commentContent:commentContent.value
        })
    })
    .then(async(snap)=>{
        var data = await snap.json()
        alert(data.msg)
        window.location.reload()
    })
    
})
getContent()