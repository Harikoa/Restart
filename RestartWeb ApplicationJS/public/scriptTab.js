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

function calculate_age(dob) { 
    var diff_ms = Date.now() - dob.getTime();
    var age_dt = new Date(diff_ms); 
  
    return Math.abs(age_dt.getUTCFullYear() - 1970);
}

async function table()
{
await fetch('/admin/getTable?role=patient')
.then(async (response)=>{
    var data = await response.json()
    console.log(data.accounts)

    data.accounts.forEach((acc)=>{
        for(const root of document.querySelectorAll(".tableRow"))
        {
            root.insertAdjacentHTML("afterend","<tr class='accRow'><td class='accounts'>" +
            acc.lastName +"</td><td class='accounts'>" + acc.firstName + "</td><td class='accounts'>" + 
            acc.middleName + "</td><td class='accounts'>" + acc.contact + "</td><td class='accounts'>" + calculate_age(new Date(acc.birthDay)) + "</td><td class='accounts'>"
            + acc.email + "</td></tr>"
            )
        }
        console.log(acc)
    })
})
}
table()
