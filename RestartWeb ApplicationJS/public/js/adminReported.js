
var start = document.querySelectorAll(".sgrep")
async function getResolved()
{

        var ctr = 0
        for(var st of start)
        {
            if(ctr==0)
            {
                await fetch("/admin/sg/resolved")
                .then(async(res)=>{
                    var data =await res.json()
                for(var doc of data.comments)
                {
                    st.insertAdjacentHTML("afterbegin",
                    "      <div class='col-xl-8 rowDesign'><h5><b>Comment by " + doc.nickName + "</b></h5><p>" +
                     doc.commentContent+"</p><button onclick='action(\"" + doc.sgid + "\",\""+ doc.postID + "\",\""+ doc.id + "\",\"remove\")'"+
                     "class='btn btnRev btn-info'>Remove</button><button onclick='action(\"" + doc.sgid + "\",\""+ doc.postID + "\",\""+ doc.id + "\",\"ignore\")'"+ 
                     "class='btn btnIgn btn-warning'>Restore</button></div>")
                     
                }
                for(var doc of data.posts)
                {
            st.insertAdjacentHTML("afterbegin",
            "      <div class='col-xl-8 rowDesign'><h5><b>" + doc.title +  " by " + doc.userNickname + "</b></h5><p>" +
             doc.content+"</p><button onclick='actionPost(\"" + doc.sgid + "\",\""+ doc.id + "\",\"remove\")'"+
             "class='btn btnRev btn-info'>Remove</button><button onclick='actionPost(\"" + doc.sgid + "\",\""+ doc.id + "\",\"ignore\")'"+ 
             "class='btn btnIgn btn-warning'>Restore</button></div>")
                }
            })
            }
            else if(ctr==1)
            {
                await fetch("/admin/sg/unresolved")
                .then(async(res)=>{
                    var data =await res.json()
                for(var doc of data.comments)
                {
                    st.insertAdjacentHTML("afterbegin",
                    "      <div class='col-xl-8 rowDesign'><h5><b>Comment by " + doc.nickName + "</b></h5><p>" +
                     doc.commentContent +"<h4><b>Status: " +doc.reported +  "</b></h4>")
                     
                }
                for(var doc of data.posts)
                {
            st.insertAdjacentHTML("afterbegin",
            "      <div class='col-xl-8 rowDesign'><h5><b>" + doc.title +  " by " + doc.userNickname + "</b></h5><p>" +
             doc.content +"<h4><b>Status: " +doc.reported +  "</b></h4>")
                }
            })
            }
            ctr++
        }
 
}
getResolved()

async function action(sgid,postid,commentid,action)
{
 
await fetch("/admin/sg/action?sgid="+sgid + "&postid=" + postid + "&commentid="+commentid +
    "&action=" + action + "&type=comment")
    .then(async(res)=>{
        var data = await res.json()
       window.location.reload()
       window.alert(data.message)
    })
}

async function actionPost(sgid,postid,action)
{
 
await fetch("/admin/sg/action?sgid="+sgid + "&postid=" + postid +
    "&action=" + action + "&type=post")
    .then(async(res)=>{
        var data = await res.json()
       window.location.reload()
       window.alert(data.message)
    })
}

