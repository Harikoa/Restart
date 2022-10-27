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
        "</p><h3><b>Journal Contents</b></h3><p>" + doc.journalEntry + "</p></div>")
       })
    })
    subPanel(0,"#")
}
getJournal()

function subPanel(panelIndex, colorCode){
var buttons = document.querySelectorAll(".subButton")
var panels = document.querySelectorAll(".subPanel")
    
    buttons.forEach(function(node){
        node.style.backgroundColor="";
        node.style.color="";
    });
    buttons[panelIndex].style.backgroundColor="seagreen";
    buttons[panelIndex].style.color="white";
    panels.forEach(function(node) {
        node.style.display="none";

    });
    panels[panelIndex].style.display="block";
    panels[panelIndex].style.backgroundColor=colorCode;   
}


