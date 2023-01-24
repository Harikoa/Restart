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
            container.insertAdjacentHTML("afterbegin",'<div class="row justify-content-center"><div class="col-sm-12"><div class="formDesign"><h3><b>Name</b>:<a href="/admin/archiveGetFile?id=' + file + '">' + file.substring(0,file.indexOf("-")) + '</a></h3>'+
            '</div></div></div>')
        })
    })
}

getData()