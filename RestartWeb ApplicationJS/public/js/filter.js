function search(x)
{
var allTables = document.querySelectorAll(".table")

    var fname = document.getElementById('fname' + x).value.toUpperCase().trim()
    var lname = document.getElementById('lname' + x).value.toUpperCase().trim()

    var table = allTables[x]

    for(var ctr = 0; ctr<table.rows.length;ctr++)
    {
        var tblLN = table.rows[ctr].cells[0].innerHTML.toUpperCase().trim()
        var tblFN = table.rows[ctr].cells[1].innerHTML.toUpperCase().trim()

        if(tblFN.indexOf(fname)>-1 && tblLN.indexOf(lname)>-1)
            table.rows[ctr].style.display=""
        else
            table.rows[ctr].style.display="none"
        
        
    }
    

}



