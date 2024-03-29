

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
       var ctr = data.length-1;
       data.forEach((doc)=>{
        console.log(doc)
        var classImp = "fa"
        if(doc.important)
        classImp="fas"
        journalContents.insertAdjacentHTML("afterbegin",
        "<div class='scroll-bar subPanel'><i onclick='toggle(" + ctr + ",\""+doc.id+"\")' class='fa-regular fa-star " + classImp + "'></i><h3><b>"  + doc.date + "</b></h3><p>Mood: " + doc.mood + "</p><p>Substance Frequency: " + doc.substanceFrequency + 
        "</p><p>Substance Intensity: " + doc.substanceIntensity + "<p>Substance Length: " + doc.substanceLength + "<p>Substance Number: " + doc.substanceNumber + 
        "</p><h4><b>Journal Contents</b></h4><p>" + doc.journalEntry + "</p></div>")
        ctr--;
       })
    })
    subPanel(0,"#")
}
async function toggle(index,id)
{
    var pid = getParameterByName("id")
    var star = document.querySelectorAll(".fa-regular")
    if(star[index].classList.contains("fa"))
    {
        star[index].classList.add("fas")
        star[index].classList.remove("fa")
    }
    else
    {
        star[index].classList.add("fa")
        star[index].classList.remove("fas")
    }
    fetch("/phy/important?jid="+id +"&pid="+pid)
    .then(async(res)=>{
        var data =await res.json()
        alert(data.msg)
    })
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
        "<div class='scroll-bar subTaskPanel'><h3><b>"  + doc.title + "</b></h3>" + "<h5><b>Deadline: " +  doc.taskDeadline + "</b></h5><p><b>Task Description: </b>" + doc.taskDescription + "</p> </div>" 
        )
        notCtr--
    }//notdone
    for(var doc of data.done)
    {
    taskDbutton.insertAdjacentHTML("afterbegin",
    "<li ><button class='subTaskDButton' onclick='subTaskPanel(" + ctr + ",false)' >"+ doc.title + "<br>" + doc.taskDate + "</button></li>"
    )
    taskDContent.insertAdjacentHTML("afterbegin",
    "<div class='scroll-bar subTaskDPanel'><h3><b>"  + doc.title + "</b></h3>" + "<p><b>Deadline: </b>" +  doc.taskDeadline + "</p><p><b>Task Description: </b>" + doc.taskDescription + "</p><h5><b>" + 
    "Date Accomplished: </b>" + doc.dateAccomplished + "</h5><h4><b>Patient's Reflection</b></h4><h5>" + doc.taskReflection + "</h5></div>" 
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
                '<div class="scroll-bar drugContents"><div class="sidebar"><h2><b>Due Date: ' + x.deadline + '</b></h2><h5><b>Date Assigned: ' + x.dateAssigned + '</b></h5></div></div>'
                )
                length--
            }
            for(var x of data.done)
            {
                complete.insertAdjacentHTML("afterbegin",
                "<li><button class='drugButton' onclick='drugSubPanel("+ length +")'>" + x.dateAssigned + "</button></li>"
                )        
                content.insertAdjacentHTML("afterbegin",
                '<div class="scroll-bar drugContents"><div class="sidebar"><h2><b>Due Date: </b> ' + x.deadline + '</h2><h5>Date Assigned: ' + x.dateAssigned + '</h5>'+ 
                '<h4><b>Patient\'s Drug Test Result</b></h4><img src ="'+ x.URL + '" width="500px" height="500px" ><form class="form-group formAlign" method="post" action="/phy/drugAssess?did='+x.id +'&id=' + id +'">' +
                '<h4 class="formAlign"><b>Test Evaluation</b></h4><h5><b>Current Evaluation: ' + x.assessment + '</b></h5><input type="radio" name="assessment" value="Positive" checked> Positive <input type="radio" name="assessment" value="Negative"> Negative</p><p><button class="btn btn-outline-success" type="submit">Submit</button></p></form></div></div>'
                )
                length--
            }

    })

    drugSubPanel(0)
}

async function getEvaluations()
{
    var id = getParameterByName("id")
    var pbutton = document.querySelector(".evalPendingButton")
    var pcontent = document.querySelector(".evalPendingContent")
    var dbutton = document.querySelector(".evalDoneButton")
    var dContent = document.querySelector(".evalDoneContent")
    var id =getParameterByName('id')
    await fetch("/phy/getEval?id="+id,{method:"POST"})
    .then(async(res)=>{
        var data = await res.json()
     
        var pending = data.notdone.length-1
        var done = data.done.length-1
        data.notdone.forEach((doc)=>{
            var monthly="Monthly Assessment"
            if(!doc.isMonthly)
            monthly = "Lowest Mood Assessment"
            pbutton.insertAdjacentHTML("afterbegin",
            "<li><button onclick='subAssessment("+pending + ",true)' class='pAssessBtn'>" + doc.dateAccomplished + "</button><li>"
            )
            pcontent.insertAdjacentHTML("afterbegin",
            "<div class='scroll-bar subEvalPPanel'><h3><b>"  + doc.dateAccomplished + "</b></h3>" + "<p><b>Type of Assessment: </b>" +  monthly + "</p><p><b>1)Little interest: </b>" + doc.phq1 + "</p><p>" +
            "<b>2)Feeling down: </b>" + doc.phq2 + "</p><p>" + "<b>3)Trouble Falling asleep: </b>" + doc.phq3 + "</p><p>" + "<b>4)Feeling Tired: </b>" + doc.phq4 + "</p><p>" + "<b>5)Poor appetite: </b>" + doc.phq5 + "</p><p>"+ 
            "<b>6)Feeling bad: </b>" + doc.phq6 + "</p><p>" + "<b>7)Trouble concentrating: </b>" + doc.phq7 + "</p><p>" + "<b>8)Moving or speaking slowly: </b>" + doc.phq8 + "</p><p>" + "<b>8)Thoughts that you would be better off dead: </b>" + doc.phq9 + "</p><p>" +
            "<b>Total: </b>" + doc.total + "</p><h4><b>Interpretation:</b></h4><form class='form-group formAlign' method='post' action='/phy/makeAssessment?id=" + id + "&aid=" + doc.id + "'><p><textarea class='taskDescription' required rows ='5' name='assessment'></textarea></p><button class='btn btn-outline-success' type='submit'>Submit</button></form></div>"
            )
            pending--
        })
        data.done.forEach((doc)=>{
            var monthly="Monthly Assessment"
            if(!doc.isMonthly)
            monthly = "Lowest Mood Assessment"
            dbutton.insertAdjacentHTML("afterbegin",
            "<li><button onclick='subAssessment("+done + ",false)' class='dAssessBtn'>" + doc.dateAccomplished + "</button><li>"
            )
            dContent.insertAdjacentHTML("afterbegin",
            "<div class='scroll-bar subEvalDPanel'><h3><b>"  + doc.dateAccomplished + "</b></h3>" + "<p><b>Type of Assessment: </b>" +  monthly + "</p><p><b>1)Little interest: </b>" + doc.phq1 + "</p><p>" +
            "<b>2)Feeling down: </b>" + doc.phq2 + "</p><p>" + "<b>3)Trouble Falling asleep: </b>" + doc.phq3 + "</p><p>" + "<b>4)Feeling Tired: </b>" + doc.phq4 + "</p><p>" + "<b>5)Poor appetite: </b>" + doc.phq5 + "</p><p>"+ 
            "<b>6)Feeling bad: </b>" + doc.phq6 + "</p><p>" + "<b>7)Trouble concentrating: </b>" + doc.phq7 + "</p><p>" + "<b>8)Moving or speaking slowly: </b>" + doc.phq8 + "</p><p>" + "<b>8)Thoughts that you would be better off dead: </b>" + doc.phq9 + "</p><p>" +
            "<b>Total: </b>" + doc.total + "</p><h4><b>Interpretation:</b></h4><p>" + doc.assessment + "</p></div>"
            ) 
            done--
        })
        subAssessment(0,true)
        subAssessment(0,false)
    })
}
getJournal()
getTasks()
getSGs()
getDrugTests()
getEvaluations()


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
    
     if(buttons.length>0)  
    {
        buttons.forEach(function(node){
            node.style.backgroundColor="";
            node.style.borderRadius="50px";
            node.style.padding="3px 20% 3px 20%"
            node.style.marginBottom="5px";
            node.style.fontSize="16px";
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

    function subAssessment(panelIndex, bool){
        if(bool)
        {
          
        var buttons = document.querySelectorAll(".pAssessBtn")
        var panels = document.querySelectorAll(".subEvalPPanel")
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
       
        var buttons = document.querySelectorAll(".dAssessBtn")
        var panels = document.querySelectorAll(".subEvalDPanel")
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
    }
        }
    
async function getData()
    {
        var id = getParameterByName('id')
        var journalSum= document.querySelector(".jSum")
        fetch("/phy/getJournal?id=" + id)
        .then(async(res)=>{
            var data= await res.json()
       
            for(var x of data.journals)
            {
                journalSum.insertAdjacentHTML("afterend",
                "<div class='jrnlDatas'><h3><b>" + x.date + "</b></h3><p>Mood: " + x.mood + "</p><p>Substance Frequency: " + x.substanceFrequency + 
                "</p><p>Substance Intensity: " + x.substanceIntensity + "<p>Substance Length: " + x.substanceLength + "<p>Substance Number: " + x.substanceNumber + 
                "</p></div>"
                
                )
            }
            
        })
        fetch("/phy/getData?id="+id,{
            method:"POST"
        }).then(async(res)=>{
            var data =await res.json()
           new Chart("moodChart",{
            type:"bar",
            data:{
            labels:data.MoodX,
            datasets:[{
                backgroundColor:"green",
                data:data.MoodY
            }]
            },  options:{ 
                legend:false,
                scales: {
                  yAxes: [{
                    ticks: {
                    beginAtZero:true,
                      stepSize: 1
                    }
                  }]
                }
              }
           })//mood Chart
           new Chart("urgeChart",{
            type:"bar",
            data:{
            labels:data.UrgeX,
            datasets:[{
                backgroundColor:"green",
                data:data.UrgeY
            }]
            },  options:{ 
                legend:false,
                scales:{
                    yAxes:[{
                       ticks:{
                        beginAtZero:true
                       }
                    }]}
                
              }
           })//Urge Chart
           var moodChart = document.getElementById("moodChart")
           var urgeChart = document.getElementById("urgeChart")
           for(var ctr=0;ctr<5;ctr++){
           moodChart.insertAdjacentHTML("afterend",
           "<p>Total " + data.MoodX[ctr] + ": " + data.MoodY[ctr] + "</p>"
           )
            if(ctr<4)
            {
            if(ctr<3)
            urgeChart.insertAdjacentHTML("afterend","<p>Average " + data.UrgeX[ctr] + ": " + data.UrgeY[ctr] + "</p>")
            }
            else
            urgeChart.insertAdjacentHTML("afterend","<p>Average Number" + ": " + data.UrgeNum+ "</p>")
           }
        })//mood and urge data

     fetch("/phy/phq9?id=" + id,{
            method:"POST"
         })
        .then(async(res)=>{
            var data =await res.json()
            var xlabel = ["Little Interest","Feeling Down","Trouble Falling asleep","Feeling Tired","Poor Appetite","Feeling Bad", "Trouble Concentrating","Moving or speaking slowly","Suicidal Thoughts"]
            var chart = document.getElementById("phq9Chart")
            new Chart("phq9Chart",{
                type:"bar",
                data:{
                labels:xlabel,
                datasets:[{
                    backgroundColor:"green",
                    data:data.phq9
                }]
                },  options:{ 
                    legend:false,
                    scales:{
                        yAxes:[{
                           ticks:{
                            beginAtZero:true
                           }
                        }]}
                    
                  }
               })
               for(var x =8;x>=0;x--)
               chart.insertAdjacentHTML("afterend",
               "<p>" + xlabel[x] + " : " + data.phq9[x] + "</p>"
               )
               chart.insertAdjacentHTML("afterend","<h3 class='mt-3'><b>Average Scores: </b></h3>")
        })
        
    }
getData()

async function exportData()
{
    var id = getParameterByName("id")
    var chart = await document.getElementById("moodChart").toDataURL()
    var uchart = await document.getElementById("urgeChart").toDataURL()
    var pchart = await document.getElementById("phq9Chart").toDataURL()
    const form = document.createElement('form')
    form.method="POST"
    form.action="/phy/exportData?id="+id
    chartField=document.createElement("input")
    chartField.name="moodChart"
    chartField.value=chart
    chartField.type="hidden"
    form.appendChild(chartField)
    chartField=document.createElement("input")
    chartField.name="urgeChart"
    chartField.value=uchart
    chartField.type="hidden"
    form.appendChild(chartField)
    chartField=document.createElement("input")
    chartField.name="phq9chart"
    chartField.value=pchart
    chartField.type="hidden"
    form.appendChild(chartField)
    document.body.appendChild(form)
    form.submit()
}

async function getImportant()
{
    var id = getParameterByName('id')
    const socket =io("https://restart-app.onrender.com")
    socket.emit("getImportant",{id:id})
    socket.on("importantJ",async(data)=>{
        var list = document.querySelector(".Important")
        var contentList = document.querySelector(".ImportantContent")
        list.innerHTML=""
        var ctr = data.important.length-1
        data.important.forEach((doc)=>{
            list.insertAdjacentHTML("afterbegin","<li ><button class='subImportantButton' onclick='subImportantPanel("+ ctr + ",\"#\")'>"+ doc.date + "</button></li>")
            var classImp = "fa"
            if(doc.important)
            classImp="fas"
            contentList.insertAdjacentHTML("afterbegin",
            "<div class='scroll-bar subImportantPanel'><i onclick='toggleImportant(" + ctr + ",\""+doc.id+"\")' class='fa-regular fa-star importantToggle " + classImp + "'></i><h3><b>"  + doc.date + "</b></h3><p>Mood: " + doc.mood + "</p><p>Substance Frequency: " + doc.substanceFrequency + 
            "</p><p>Substance Intensity: " + doc.substanceIntensity + "<p>Substance Length: " + doc.substanceLength + "<p>Substance Number: " + doc.substanceNumber + 
            "</p><h4><b>Journal Contents</b></h4><p>" + doc.journalEntry + "</p></div>")
            ctr--
        })
        subImportantPanel(0)    
    })
   
}

getImportant()
function subImportantPanel(panelIndex){
    var buttons = document.querySelectorAll(".subImportantButton")
    var panels = document.querySelectorAll(".subImportantPanel")
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

    async function toggleImportant(index,id)
{
    var pid = getParameterByName("id")
    var star = document.querySelectorAll(".importantToggle")
    if(star[index].classList.contains("fa"))
    {
        star[index].classList.add("fas")
        star[index].classList.remove("fa")
    }
    else
    {
        star[index].classList.add("fa")
        star[index].classList.remove("fas")
    }
    fetch("/phy/important?jid="+id +"&pid="+pid)
    .then(async(res)=>{
        var data =await res.json()
        alert(data.msg)
    })
}