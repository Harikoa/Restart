var msg = document.querySelector(".msg-page")
function getParameterByName(name, url = window.location.href) {
    name = name.replace(/[\[\]]/g, '\\$&');
    var regex = new RegExp('[?&]' + name + '(=([^&#]*)|&|#|$)'),
        results = regex.exec(url);
    if (!results) return null;
    if (!results[2]) return '';
    return decodeURIComponent(results[2].replace(/\+/g, ' '));
}

async function getMessages()
{
   
    var id = getParameterByName("id")
    const socket =io("https://localhost:1028")
    socket.emit("get-messages",{id:id,
        currentUser:document.cookie
    })
    socket.on('chat',data=>{
        msg.innerHTML=""
        var chat = data.msgs
        chat.forEach((doc)=>{
            if(doc.senderEmail==id)
                {
                    msg.insertAdjacentHTML("afterbegin",
                '<div class="received-chats"><div class="received-chats-img">' +
                '<img src="../public/messageicon.png"></div><div class="received-msg">' + 
                '<div class="received-msg-inbox"><p>' + doc.msgContent + '</p><span class="time">' + doc.date + '</span></div></div></div>'
                    )
                }
            else{
                    msg.insertAdjacentHTML("afterbegin",
                    '<div class="outgoing-chats"><div class="outgoing-chats-msg"><p>'+ doc.msgContent + '</p><span class="time">'+ doc.date + '</span></div></div>'
                    )
            }
        })
        msg.scrollTop=msg.scrollHeight
    })
    
  
}
function send()
{
    const id = getParameterByName("id")
    const socket =io("https://localhost:1028")
    var inputBox = document.getElementById("inputChat")
    var message = inputBox.value
    inputBox.value=""
    socket.emit('send-chat-message',{
        message:message,
        id:id,
        currentUser:document.cookie
    })
    
}
getMessages()