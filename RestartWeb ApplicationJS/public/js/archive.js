async function getData()
{
    fetch("/admin/archiveGetData",{
        method:"POST",
        headers:{"Content-Type":'application/json'}
    })
    .then(async(res)=>{
        var data = await res.json()
        var container = document.querySelector(".attatch")
        data.files.forEach((file)=>{
            container.insertAdjacentHTML("afterbegin",'<div class="row justify-content-center"><h3 style="text-align: center;"><a href="/admin/archiveGetFile?id=' + file + '">' + file.substring(0,file.indexOf("-")) + '</a></h3>'+
            '</div></div>')
        })
    })
}

getData()