const socket = new SockJS('/chat-websocket');
const stompClient = Stomp.over(socket);
stompClient.debug = () => {}
function connectStompClient() {
    return new Promise((resolve, reject) => {
        stompClient.connect({}, function() {
            console.log('Connected to WebSocket');
            resolve(stompClient);
        }, function(error) {
            console.error('Error connecting to WebSocket:', error);
            reject(error);
        });
    });
}