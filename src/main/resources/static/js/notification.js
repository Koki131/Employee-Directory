let timeoutId;
async function onNotify(user) {


    const stompClient = await connectStompClient();


    stompClient.subscribe(`/user/${user}/notify`, function(message) {

        const response = JSON.parse(message.body);

        displayNotification(response);

    });

}

function displayNotification(response) {

    const toast = document.querySelector(".toast");
    const toastBody = document.querySelector(".toast-body");
    const title = document.querySelector(".me-auto");
    const time = document.querySelector("#time-sent");
    const btn = toast.querySelector(".btn-close");

    const date = new Date(response.timestamp);

    toast.style.display = "block";
    title.textContent = response.sender.userName;
    toastBody.textContent = response.content;
    time.textContent = date.getHours() + ":" + date.getMinutes();

    btn.addEventListener("click", function() {
        toast.style.display = "none";
    });

    if (timeoutId) clearTimeout(timeoutId);

    timeoutId = setTimeout(function() {
        toast.style.display = "none";
    }, 5000);

}