var tabButtons = document.querySelectorAll(".tabContainer .buttonContainer button");
var tabPanels = document.querySelectorAll(".tabContainer .tabPanel");

function showPanel(panelIndex, colorCode){
    tabButtons.forEach(function(node){
        node.style.backgroundColor="";
        node.style.color="";

    });
    tabButtons[panelIndex].style.backgroundColor="seagreen";
    tabButtons[panelIndex].style.color="white";
    tabPanels.forEach(function(node) {
        node.style.display="none";

    });
    tabPanels[panelIndex].style.display="block";
    tabPanels[panelIndex].style.backgroundColor=colorCode;
}
showPanel(0, '#');



function getParameterByName(name, url = window.location.href) {
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

var panel = getParameterByName('panel');
if(panel!=null)
showPanel(panel,'#')

async function getJournal()
{
    var journaltab = document.querySelector(".journalClass")
    var journalContents = document.querySelector(".journalContent")
    var id = getParameterByName('id')
    await fetch("/phy/getJournal?id=" + id)
    .then(async(res)=>{
        var data = await res.json()
        data = data.journals
        var ctr = data.length-1;
       data.forEach((doc)=>{
        journaltab.insertAdjacentHTML("afterbegin",
        "<li ><button class='subButton' onclick='subPanel("+ ctr + ",\"#\")'>"+ doc.date + "</button></li>"
        ) 
        ctr--;
       })
       data.forEach((doc)=>{
        journalContents.insertAdjacentHTML("afterbegin",
        "<div class='scroll-bar subPanel'><h3><b>"  + doc.date + "</b></h3><p>Mood: " + doc.mood + "</p><p>Substance Frequency: " + doc.substanceFrequency + 
        "</p><p>Substance Intensity: " + doc.substanceIntensity + "<p>Substance Length: " + doc.substanceLength + "<p>Substance Number: " + doc.substanceNumber + 
        "</p><h4><b>Journal Contents</b></h4><p>" + doc.journalEntry + "</p></div>")
       })
    })
    subPanel(0,"#")
}

async function getTasks()
{
    var taskPbutton = document.querySelector(".taskPendingButton")
    var taskPContent = document.querySelector(".taskPendingContent")
    var taskDbutton = document.querySelector(".taskDoneButton")
    var taskDContent = document.querySelector(".taskDoneContent")

    var id = getParameterByName('id')
    await fetch("/phy/getTasks",{method:'POST', 
    headers: {
        'Content-Type': 'application/json'},
        body:JSON.stringify({"id":id})
    })
    .then(async(res)=>{
        var data = await res.json()
        var notCtr = data.notdone.length-1
        var ctr = data.done.length-1
        for(var doc of data.notdone)
        {
        taskPbutton.insertAdjacentHTML("afterbegin",
        "<li ><button class='subTaskButton' onclick='subTaskPanel(" + notCtr + ",true)'>"+ doc.title + "<br>" + doc.taskDate + "</button></li>"
        )
        taskPContent.insertAdjacentHTML("afterbegin",
        "<div class='scroll-bar subTaskPanel'><h3><b>"  + doc.title + "</b></h3>" + "<p>Deadline: " +  doc.taskDeadline + "</p><p>Task Description: " + doc.taskDescription + "</p> </div>" 
        )
        notCtr--
    }//notdone
    for(var doc of data.done)
    {
    taskDbutton.insertAdjacentHTML("afterbegin",
    "<li ><button class='subTaskDButton' onclick='subTaskPanel(" + ctr + ",false)' >"+ doc.title + "<br>" + doc.taskDate + "</button></li>"
    )
    taskDContent.insertAdjacentHTML("afterbegin",
    "<div class='scroll-bar subTaskDPanel'><h3><b>"  + doc.title + "</b></h3>" + "<p>Deadline: " +  doc.taskDeadline + "</p><p>Task Description: " + doc.taskDescription + "</p><p>" + 
    "Date Accomplished: " + doc.dateAccomplished + "</p><h4>Patient's Reflection<h4><p>" + doc.taskReflection + "</p></div>" 
    )
    ctr--
}//done
    })
    subTaskPanel(0,true)
    subTaskPanel(0,false)
}
async function getSGs()
{
    var id = getParameterByName('id')
    var current = document.querySelector(".currentSupport")
    var other = document.querySelector(".otherSupport")
    await fetch("/phy/getSGs",{method:"POST",
    headers:{"Content-Type":'application/json'},
    body:JSON.stringify({id:id})
}) .then(async(res)=>{
        var data = await res.json()
       
        for(var non of data.not)
        {
            other.insertAdjacentHTML("afterend",
            "<tr><td>" + non.Title + "</td><td>" + non.Description + "</td><td><button onclick='sgAction(true,\""+ non.id + "\")'class='actBtn'>Link</button></td></tr>"
            )
        }
        for(var yes of data.included)
        {
            current.insertAdjacentHTML("afterend",
            "<tr><td>" + yes.Title + "</td><td>" + yes.Description + "</td><td><button onclick='sgAction(false,\""+ yes.id + "\")'class='deacBtn'>Unlink</button></td></tr>"
            
            )
        }
})
}
async function getDrugTests()
{
    var id = getParameterByName('id')
    var pending = document.querySelector(".pendingDrug")
    var complete = document.querySelector(".completeDrug")
    var content = document.querySelector(".drugContent")
    await fetch("/phy/getDrugTest",{
        method:"POST",
        headers: {
            'Content-Type': 'application/json'},
        body:JSON.stringify({id:id})
    }).then(async (res)=>{
            var data = await res.json()
            var length = data.notdone.length + data.done.length -1
        
            for(var x of data.notdone)
            {
                pending.insertAdjacentHTML("afterbegin",
                "<li><button class='drugButton' onclick='drugSubPanel(" + length + ")'>" + x.dateAssigned + "</button></li>"
                )
                content.insertAdjacentHTML("afterbegin",
                '<div class="scroll-bar drugContents"><div class="sidebar"><h2><b>Due Date</b> ' + x.deadline + '</h2><p>Date Assigned:' + x.dateAssigned + '</p></div></div>'
                )
                length--
            }
            for(var x of data.done)
            {
                complete.insertAdjacentHTML("afterbegin",
                "<li><button class='drugButton' onclick='drugSubPanel("+ length +")'>" + x.dateAssigned + "</button></li>"
                )        
                content.insertAdjacentHTML("afterbegin",
                '<div class="scroll-bar drugContents"><div class="sidebar"><h2><b>Due Date</b> ' + x.deadline + '</h2><p>Date Assigned:' + x.dateAssigned + '</p>'+ 
                '<h4><b>Patient\'s Drug Test Result</b></h4><img src ="'+ x.URL + '" width="500px" height="500px" ><form class="form-group formAlign" method="post" action="/phy/drugAssess?did='+x.id +'&id=' + id +'">' +
                '<h4 class="formAlign"><b>Test Evaluation</b></h4><p><b>Current Evaluation: ' + x.assessment + '</b></p><input type="radio" name="assessment" value="Positive" checked> Positive <input type="radio" name="assessment" value="Negative"> Negative</p><p><button class="btn btn-outline-success" type="submit">Submit</button></p></form></div></div>'
                )
                length--
            }

    })

    drugSubPanel(0)
}

getJournal()
getTasks()
getSGs()
getDrugTests()



function subPanel(panelIndex, colorCode){
    var buttons = document.querySelectorAll(".subButton")
    var panels = document.querySelectorAll(".subPanel")
     if(buttons.length>0)  
    {
        buttons.forEach(function(node){
            node.style.backgroundColor="";
            node.style.borderRadius="50px";
            node.style.paddingLeft="25%";
            node.style.paddingRight="25%";
            node.style.paddingTop="3%";
            node.style.paddingBottom="3%";
            node.style.marginBottom="5px";
            node.style.fontSize="20px";
            node.style.color="";
        });
        buttons[panelIndex].style.backgroundColor="forestgreen";
        buttons[panelIndex].style.color="white";
        panels.forEach(function(node) {
            node.style.display="none";
    
        });
        panels[panelIndex].style.display="block";
        panels[panelIndex].style.backgroundColor=colorCode;   
    }
    }
    function subTaskPanel(panelIndex, bool){
        if(bool)
        {
          
        var buttons = document.querySelectorAll(".subTaskButton")
        var panels = document.querySelectorAll(".subTaskPanel")
         if(buttons.length>0)  
        {
            buttons.forEach(function(node){
                node.style.backgroundColor="";
                node.style.borderRadius="20px";
                node.style.padding="3px 20% 3px 20%"
                node.style.marginBottom="5px";
                node.style.fontSize="15px";
                node.style.color="";
            });
            buttons[panelIndex].style.backgroundColor="forestgreen";
            buttons[panelIndex].style.color="white";
            panels.forEach(function(node) {
                node.style.display="none";
        
            });
            panels[panelIndex].style.display="block";
            panels[panelIndex].style.backgroundColor="#";   
        }
    }else
    {
       
        var buttons = document.querySelectorAll(".subTaskDButton")
        var panels = document.querySelectorAll(".subTaskDPanel")
        if(buttons.length>0)  
        {
            buttons.forEach(function(node){
                node.style.backgroundColor="";
                node.style.borderRadius="50px";
                node.style.paddingLeft="25%";
                node.style.paddingRight="25%";
                node.style.paddingTop="3%";
                node.style.paddingBottom="3%";
                node.style.marginBottom="5px";
                node.style.fontSize="20px";
                node.style.color="";
            });
            buttons[panelIndex].style.backgroundColor="forestgreen";
            buttons[panelIndex].style.color="white";
            panels.forEach(function(node) {
                node.style.display="none";
        
            });
            panels[panelIndex].style.display="block";
            panels[panelIndex].style.backgroundColor="#";   
        }
    }
        }

async function sgAction(bool,id)
{
    var patientid = getParameterByName('id')
    await fetch("/phy/sgAction",{
        method:"POST",
        headers:{"Content-Type":'application/json'},
        body:JSON.stringify({id:id,bool:bool,pid:patientid})
    })
    .then(async(res)=>{
        var result = await res.json()
        alert(result.msg)
        window.location.href="/phy/managePatient?id=" + patientid + "&panel=2"
    })
}
function drugSubPanel(panelIndex){
    
    var buttons = document.querySelectorAll(".drugButton")
    var panels = document.querySelectorAll(".drugContents")
    console.log(buttons.length)
     if(buttons.length>0)  
    {
        buttons.forEach(function(node){
            node.style.backgroundColor="";
            node.style.borderRadius="50px";
            node.style.paddingLeft="25%";
            node.style.paddingRight="25%";
            node.style.paddingTop="3%";
            node.style.paddingBottom="3%";
            node.style.marginBottom="5px";
            node.style.fontSize="20px";
            node.style.color="";
        });
        panels.forEach(function(node) {
            node.style.display="none";
    
        });
        if(buttons[0].closest(".pendingDrug")==null)
        {
        buttons[panelIndex].style.backgroundColor="forestgreen";
        buttons[panelIndex].style.color="white";
        panels[panelIndex].style.display="block";
        panels[panelIndex].style.backgroundColor="#";   
    }
    else{
        if(panelIndex==buttons.length-1)
        {
            buttons[0].style.backgroundColor="forestgreen";
            buttons[0].style.color="white";
            panels[panelIndex].style.display="block";
            panels[panelIndex].style.backgroundColor="#";   
        }
        else{
        buttons[panelIndex+1].style.backgroundColor="forestgreen";
        buttons[panelIndex+1].style.color="white";
        panels[panelIndex].style.display="block";
        panels[panelIndex].style.backgroundColor="#";   
        }
    }
    }
    }
    